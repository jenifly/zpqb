package com.jenifly.zpqb.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.jenifly.zpqb.cache.Cache;

/**
 * Created by Jenifly on 2017/8/13.
 */

public class JLinearLayout extends LinearLayout {

    public JLinearLayout(Context context) {
        super(context);
        initView();
    }

    public JLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        this.setBackgroundColor(Color.WHITE);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                setBackColor(event.getAction());
                return false;
            }
        });
    }

    private void setBackColor(int state) {
        if (state == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
            this.setBackgroundColor(Cache.PressColor);
        }
        if (state == MotionEvent.ACTION_UP) {
            getParent().requestDisallowInterceptTouchEvent(false);
            this.setBackgroundColor(Color.WHITE);
        }
    }

    public void Restore(){
        this.setBackgroundColor(Color.WHITE);
    }

    public void setState(boolean state){
        if(state)
            this.setBackgroundColor(Cache.BaseColor);
        else
            this.setBackgroundColor(Cache.ErrorColor);
    }
}
