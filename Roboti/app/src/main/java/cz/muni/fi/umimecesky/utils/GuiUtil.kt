package cz.muni.fi.umimecesky.utils

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import cn.refactor.lib.colordialog.PromptDialog

/**
 * User interface static methods.
 */

object GuiUtil {

    /**
     * When activity is in immersive fullscreen mode, f.e. navigation bar is hidden. Dialog show
     * action will display the bar. This method is used to avoid displaying hidden bar.

     * @param promptDialog dialog which is shown
     * *
     * @param activity     which activity is currently running
     */
    fun showDialogImmersive(promptDialog: PromptDialog, activity: Activity) {
        promptDialog.window!!.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

        promptDialog.show()

        //Set the promptDialog to immersive
        promptDialog.window!!.decorView.systemUiVisibility = activity.window.decorView.systemUiVisibility
        //Clear the not focusable flag from the window
        promptDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    fun hideNavigationBar(activity: Activity) {
        var flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hides nav bar (buttons)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags = flags or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        activity.window.decorView.systemUiVisibility = flags
    }

    fun createFinishListener(activity: Activity): PromptDialog.OnPositiveListener {
        return PromptDialog.OnPositiveListener { dialog ->
            dialog.dismiss()
            activity.finish()
        }
    }
}
