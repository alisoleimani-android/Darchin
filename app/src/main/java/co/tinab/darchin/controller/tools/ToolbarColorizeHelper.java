package co.tinab.darchin.controller.tools;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by A.S.R on 1/27/2018.
 */

public class ToolbarColorizeHelper {

    /**
     * Use this method to colorize toolbar icons to the desired target color
     * @param toolbarView toolbar view being colored
     * @param toolbarIconsColor the target color of toolbar icons
     */
    public static void colorizeToolbar(Toolbar toolbarView, int toolbarIconsColor) {
        ViewGroup viewGroup = (ViewGroup) toolbarView.getChildAt(0);
        for(int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if(v instanceof ImageButton) {
                //Action Bar back button
                ((ImageButton)v).setColorFilter(ContextCompat.getColor(v.getContext()
                        , toolbarIconsColor), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        }
    }
}