package com.jenifly.zpqb.helper;

import android.app.Activity;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Jenifly on 2017/7/18.
 */

public class StuBarTranslucentAPI21Helper {
    public static void initState(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        View mChildView = ((ViewGroup)activity.findViewById(Window.ID_ANDROID_CONTENT)).getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }
}
