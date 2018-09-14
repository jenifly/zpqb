package com.jenifly.zpqb.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * Created by Jenifly on 2018/3/4.
 */

public class NavLayout extends LinearLayout implements NestedScrollingParent {

    private View topView;
    private View childView;

    private int topViewHeight, childViewTop, topViewTop;

    private ValueAnimator mOffsetAnimator;

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
    }

    @Override
    public void onStopNestedScroll(View target) {
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        topViewTop = topView.getTop();
        childViewTop = childView.getTop();
//        int offset = getOffset(Math.abs(dy), 0);
//        childView.offsetTopAndBottom(offset);
//        topView.offsetTopAndBottom(offset);
        consumed[1] = dy;
    }

    private int getOffset(int offset, int maxOffset) {
        if (offset > maxOffset) {
            return maxOffset;
        }
        return offset;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        topView.layout(0, childViewTop - topViewHeight, r, childViewTop);
        ViewGroup.LayoutParams layoutParams = childView.getLayoutParams();
        childView.layout(0, childViewTop, r, layoutParams.height + childViewTop);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public int getNestedScrollAxes()
    {
        return 0;
    }

//    /**
//     * 根据速度计算滚动动画持续时间
//     * @param velocityY
//     * @return
//     */
//    private int computeDuration(float velocityY) {
//        final int distance;
//        if (velocityY > 0) {
//            distance = Math.abs(mTop.getHeight() - getScrollY());
//        } else {
//            distance = Math.abs(mTop.getHeight() - (mTop.getHeight() - getScrollY()));
//        }
//        final int duration;
//        velocityY = Math.abs(velocityY);
//        if (velocityY > 0) {
//            duration = 3 * Math.round(1000 * (distance / velocityY));
//        } else {
//            final float distanceRatio = (float) distance / getHeight();
//            duration = (int) ((distanceRatio + 1) * 150);
//        }
//
//        return duration;
//
//    }

//    private void animateScroll(float velocityY, final int duration,boolean consumed) {
//        final int currentOffset = getScrollY();
//        final int topHeight = mTop.getHeight();
//        if (mOffsetAnimator == null) {
//            mOffsetAnimator = new ValueAnimator();
//            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    if (animation.getAnimatedValue() instanceof Integer) {
//                        scrollTo(0, (Integer) animation.getAnimatedValue());
//                    }
//                }
//            });
//        } else {
//            mOffsetAnimator.cancel();
//        }
//        mOffsetAnimator.setDuration(Math.min(duration, 600));
//
//        if (velocityY >= 0) {
//            mOffsetAnimator.setIntValues(currentOffset, topHeight);
//            mOffsetAnimator.start();
//        }else {
//            if(!consumed){
//                mOffsetAnimator.setIntValues(currentOffset, 0);
//                mOffsetAnimator.start();
//            }
//
//        }
//    }

    public NavLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topView = getChildAt(0);
        childView = getChildAt(1);
        childViewTop = childView.getTop();
        topViewTop = topView.getTop();
        topViewHeight = topView.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams params = childView.getLayoutParams();
        params.height = getMeasuredHeight() - topViewHeight;
    }
}
