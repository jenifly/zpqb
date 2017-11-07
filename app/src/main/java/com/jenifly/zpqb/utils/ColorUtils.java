package com.jenifly.zpqb.utils;

/**
 * Created by Jenifly on 2017/7/7.
 */

public class ColorUtils {

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) alpha )) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

}
