package com.santoni7.interactiondemo.lib;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class AndroidUtils {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static void animateViewFadeIn(View view, int duration) {
        ObjectAnimator.ofFloat(view, "alpha", 0, 1)
                .setDuration(duration)
                .start();
    }


    public static void animateViewFadeOut(View view, int duration) {
        ObjectAnimator.ofFloat(view, "alpha", 1, 0)
                .setDuration(duration)
                .start();
    }
}
