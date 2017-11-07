package com.jenifly.zpqb.view.fastscrollrecyclerview;

import android.content.res.Resources;
import android.os.Build;
import android.view.View;

/**
 * Created by Jenifly on 2017/8/29.
 */

final class Utilities {

    static boolean isRtl(Resources res) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                res.getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

}