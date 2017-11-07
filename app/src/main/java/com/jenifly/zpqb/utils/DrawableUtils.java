package com.jenifly.zpqb.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

/**
 * Created by Jenifly on 2017/7/1.
 */

public class DrawableUtils {

    public static Drawable TintDrawable(Drawable drawable, int colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(colors));
        return wrappedDrawable;
    }

    public static void setImageViewColor(ImageView view, int color) {
        Drawable modeDrawable = view.getDrawable().mutate();
        Drawable temp = DrawableCompat.wrap(modeDrawable);
        ColorStateList colorStateList =  ColorStateList.valueOf(color);
        DrawableCompat.setTintList(temp, colorStateList);
        view.setImageDrawable(temp);
    }
}