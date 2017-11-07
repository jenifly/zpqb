package com.jenifly.zpqb.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by Jenifly on 2017/8/18.
 */

public class JImageView extends ImageView{
    private Animator anim1;
    private Animator anim2;
    private int mHeight;
    private int mWidth;
    private float mX, mY;
    private Handler mHandler = new Handler();
    private float scale = 0.4f;
    private ClickListener listener;
    private boolean canScale = true;

    public JImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        mX = getX();
        mY = getY();
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setCanScale(boolean canScale) {
        this.canScale = canScale;
    }

    private void init() {
        PropertyValuesHolder valueHolder_1 = PropertyValuesHolder.ofFloat("scaleX", 1f, scale);
        PropertyValuesHolder valuesHolder_2 = PropertyValuesHolder.ofFloat("scaleY", 1f, scale);
        anim1 = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_1, valuesHolder_2);
        anim1.setDuration(300);
        anim1.setInterpolator(new LinearInterpolator());
        PropertyValuesHolder valueHolder_3 = PropertyValuesHolder.ofFloat("scaleX", scale, 1f);
        PropertyValuesHolder valuesHolder_4 = PropertyValuesHolder.ofFloat("scaleY", scale, 1f);
        anim2 = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_3, valuesHolder_4);
        anim2.setDuration(300);
        anim2.setInterpolator(new LinearInterpolator());
    }

    public void setClickListener(ClickListener clickListener) {
        this.listener = clickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    getParent().requestDisallowInterceptTouchEvent(true);
                    if(canScale)
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                anim2.end();
                                anim1.start();
                            }
                        });
                    break;
                case MotionEvent.ACTION_UP:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    if(canScale)
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                anim1.end();
                                anim2.start();
                            }
                        });
                    if (listener != null) {
                        listener.onClick();
                    }
                    break;
            }
        return true;
    }

    /**
     *  点击事件处理回调　
     * @author renzhiqiang
     *
     */
    public interface ClickListener {

        void onClick();
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
    }

}
