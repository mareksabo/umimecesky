/*
 * Copyright (c) 2018 Marek Sabo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.muni.fi.umimecesky.game.shared.util

import android.app.Activity
import android.os.Build
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import cn.refactor.lib.colordialog.PromptDialog

/**
 * User interface extension methods.
 */

object GuiUtil {

    /**
     * When activity is in immersive fullscreen mode, f.e. navigation bar is hidden. Dialog show
     * action will display the bar. This method is used to avoid displaying hidden bar.
     *
     * @param promptDialog dialog which is shown
     */
    fun Activity.showDialogImmersive(promptDialog: PromptDialog) {
        if (this.isFinishing) return

        promptDialog.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)

        promptDialog.show()

        //Set the promptDialog to immersive
        promptDialog.window?.decorView?.systemUiVisibility = window.decorView.systemUiVisibility
        //Clear the not focusable flag from the window
        promptDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    fun Activity.hideNavigationBar() {
        var flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hides nav bar (buttons)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags = flags or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        this.window.decorView.systemUiVisibility = flags
    }

    fun Activity.createFinishListener(): PromptDialog.OnPositiveListener {
        return PromptDialog.OnPositiveListener { dialog ->
            dialog.dismiss()
            this.finish()
        }
    }

    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
            LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

}
