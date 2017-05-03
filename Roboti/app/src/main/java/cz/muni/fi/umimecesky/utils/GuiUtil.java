package cz.muni.fi.umimecesky.utils;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import cn.refactor.lib.colordialog.PromptDialog;

import static cz.muni.fi.umimecesky.utils.Constant.DEFAULT_COLOR;
import static cz.muni.fi.umimecesky.utils.Constant.STROKE_WIDTH;

/**
 * User interface static methods.
 */

public class GuiUtil {

    /**
     * When activity is in immersive fullscreen mode, f.e. navigation bar is hidden. Dialog show
     * action will display the bar. This method is used to avoid displaying hidden bar.
     *
     * @param promptDialog dialog which is shown
     * @param activity     which activity is currently running
     */
    public static void showDialogImmersive(PromptDialog promptDialog, Activity activity) {
        promptDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        promptDialog.show();

        //Set the promptDialog to immersive
        promptDialog.getWindow().getDecorView().setSystemUiVisibility(
                activity.getWindow().getDecorView().getSystemUiVisibility());
        //Clear the not focusable flag from the window
        promptDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    public static void hideNavigationBar(Activity activity) {
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; // hides nav bar (buttons)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    public static void setDefaultColor(Button button) {
        button.setTextColor(DEFAULT_COLOR);
        ((GradientDrawable) button.getBackground()).setStroke(STROKE_WIDTH, DEFAULT_COLOR);
    }

    public static PromptDialog.OnPositiveListener createFinishListener(final Activity activity) {
        return new PromptDialog.OnPositiveListener() {
            @Override
            public void onClick(PromptDialog dialog) {
                dialog.dismiss();
                activity.finish();
            }
        };
    }
}
