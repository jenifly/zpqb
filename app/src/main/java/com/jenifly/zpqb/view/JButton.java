package com.jenifly.zpqb.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jenifly on 2017/6/13.
 */

public class JButton extends Button {
    private GradientDrawable gradientDrawable;//控件的样式
    private int backColor = Color.TRANSPARENT;//背景色，int类型
    private int backColorSelected = 0;//按下后的背景色，int类型
    private int backGroundImage = 0;//背景图，只提供了Id
    private int backGroundImageSeleted = 0;//按下后的背景图，只提供了Id
    private int textColor = 0;//文字颜色，int类型
    private int textColorSeleted = 0;//按下后的文字颜色，int类型
    private boolean fillet = false;//是否设置圆角
    private boolean border = false; //是否设置边框
    private int borderColor = 0; //边框颜色，int类型
    private int borderColorSeleted = 0;//按下后的文字颜色，int类型
    private int borderwidth = 0; //边框宽度

    public JButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public JButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JButton(Context context) {
        this(context, null);
    }

    private void initView() {
        if (gradientDrawable == null)
            gradientDrawable = new GradientDrawable();
        setGravity(Gravity.CENTER);
        //设置Touch事件
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                setColor(event.getAction());
                return false;
            }
        });
    }

    private void setColor(int state) {
        if (state == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
            if (borderColorSeleted != 0)
                gradientDrawable.setStroke(borderwidth, borderColorSeleted);
            if (backColorSelected != 0) {
                gradientDrawable.setColor(backColorSelected);
            }
            if (textColorSeleted != 0)
                setTextColor(textColorSeleted);
            if (backGroundImageSeleted != 0)
                setBackgroundResource(backGroundImageSeleted);
            setBackgroundDrawable(gradientDrawable);
        }
        if (state == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
            //抬起
            if (backColor == 0)
                gradientDrawable.setColor(Color.TRANSPARENT);
            else {
                gradientDrawable.setColor(backColor);
            }
            //如果为设置字体颜色，默认为黑色
            if (textColor != 0)
                setTextColor(textColor);
            if (backGroundImage != 0)
                setBackgroundResource(backGroundImage);
            if (borderColor != 0)
                gradientDrawable.setStroke(borderwidth, borderColor);
            setBackgroundDrawable(gradientDrawable);
        }
    }


    // 设置按钮的背景色,如果未设置则默认为透明

    public void setBackColor(int backColor, int backColorSelected) {
        this.backColor = backColor;
        this.backColorSelected = backColorSelected;
        if(backColor != 0)
            gradientDrawable.setColor(backColor);
        setBackgroundDrawable(gradientDrawable);
    }

    // 设置按钮的背景图
    public void setBackGroundImage(int backGroundImage, int backGroundImageSeleted) {
        this.backGroundImage = backGroundImage;
        this.backGroundImageSeleted = backGroundImageSeleted;
        setBackgroundResource(backGroundImage);
    }

    //设置按钮文字颜色
    public void setTextColor(int textColor, int textColorSelected) {
        this.textColorSeleted = textColorSelected;
        this.textColor = textColor;
        setTextColor(textColor);
    }

    // 按钮的形状
    public void setShape(int shape) {
        gradientDrawable.setShape(shape);
    }

    // 设置圆角
    public void setFillet(Boolean fillet, float radius) {
        this.fillet = fillet;
        if(fillet) {
            if (gradientDrawable == null)
                gradientDrawable = new GradientDrawable();
            setShape(GradientDrawable.RECTANGLE);
            gradientDrawable.setCornerRadius(radius);
        }
    }

    public void  setCornerRadii(float[] radii){
        if (gradientDrawable == null)
            gradientDrawable = new GradientDrawable();
        setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadii(radii);
    }

    // 设置按钮的边框
    public void setBorder(boolean border,int borderwidth, int borderColor, int borderColorSeleted){
        this.border = border;
        this.borderwidth = borderwidth;
        this.borderColor = borderColor;
        this.borderColorSeleted = borderColorSeleted;
        if(border){
            gradientDrawable.setStroke(borderwidth, borderColor);
            setBackgroundDrawable(gradientDrawable);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}