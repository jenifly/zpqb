package com.jenifly.zpqb.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.jenifly.zpqb.R;

/**
 * Created by Jenifly on 2017/8/11.
 */

public class ArcProgressBar extends View{

    private int mWidth;
    private int mHeight;
    private int diameter = 500;  //直径
    private float centerX;  //圆心X坐标
    private float centerY;  //圆心Y坐标

    private Paint allArcPaint;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;

    private RectF bgRect;

    private ValueAnimator progressAnimator;
    private PaintFlagsDrawFilter mDrawFilter;
    private SweepGradient sweepGradient;
    private Matrix rotateMatrix;

    private float startAngle = 135;
    private float sweepAngle = 270;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float maxValues = 60;
    private float curValues = 0;
    private float bgArcWidth = dipToPx(1.2f);
    private float progressWidth = dipToPx(10);
    private float textSize = dipToPx(68);
    private float hintSize = dipToPx(18);
    private float curSpeedSize = dipToPx(16);
    private int aniSpeed = 1000;
    private float longdegree = dipToPx(14);
    private float shortdegree = dipToPx(10);
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(4);

    private String hintColor = "#676767";
    private String longDegreeColor = "#66DDDDDD";
    private String shortDegreeColor = "#66DDDDDD";
    private String bgArcColor = "#FFFFFF";
    private String titelColor = "#FFFFFF";
    private String titleString;
    private String hintString;

    private boolean isNeedTitle = true;
    private boolean isNeedUnit = true;
    private boolean isNeedDial = true;
    private boolean isNeedContent = true;

    // sweepAngle / maxValues 的值
    private float k;

    public ArcProgressBar(Context context) {
        super(context, null);
        initView();
    }

    public ArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initView();
    }

    public ArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
        int height= (int) (progressWidth + diameter + 3 * DEGREE_PROGRESS_DISTANCE);
        setMeasuredDimension(width, height);
    }

    private void initView() {
        diameter = 3 * getScreenWidth() / 5;
        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = progressWidth/2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (progressWidth/2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (progressWidth/2 + DEGREE_PROGRESS_DISTANCE);

        //圆心
        centerX = (progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;
        centerY = (progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE)/2;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setStrokeWidth(bgArcWidth);
        allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setStrokeWidth(1);
        progressPaint.setColor(Color.WHITE);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(textSize);
        vTextPaint.setColor(Color.parseColor(titelColor));
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        //显示单位文字
        hintPaint = new Paint();
        hintPaint.setTextSize(hintSize);
        hintPaint.setColor(Color.WHITE);
        hintPaint.setTextAlign(Paint.Align.CENTER);

        //显示标题文字
        curSpeedPaint = new Paint();
        curSpeedPaint.setTextSize(curSpeedSize);
        curSpeedPaint.setColor(Color.WHITE);
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        rotateMatrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //抗锯齿
        canvas.setDrawFilter(mDrawFilter);

        if (isNeedDial) {
            //画刻度线
            for (int i = 0; i < 120; i++) {
                if (i > 45 && i < 75) {
                    canvas.rotate(3, centerX, centerY);
                    continue;
                }
                if ((i+1) % 10 == 0) {
                    degreePaint.setStrokeWidth(dipToPx(1.0f));
                    if(currentAngle>135){
                        if(currentAngle-135>i*3&&i<=45||i>=75)
                            degreePaint.setColor(Color.WHITE);
                        else
                            degreePaint.setColor(Color.parseColor(longDegreeColor));
                    }else{
                        if(currentAngle>=i*3-225&&i>=75)
                            degreePaint.setColor(Color.WHITE);
                        else
                            degreePaint.setColor(Color.parseColor(longDegreeColor));
                    }
                    canvas.drawLine(centerX,
                            centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE+dipToPx(29),
                            centerX,
                            centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - longdegree+dipToPx(29),
                            degreePaint);
                } else{
                    degreePaint.setStrokeWidth(dipToPx(0.8f));
                    if(currentAngle>=135){
                        if(currentAngle-135>=i*3&&i<=45||i>=75)
                            degreePaint.setColor(Color.WHITE);
                        else
                            degreePaint.setColor(Color.parseColor(shortDegreeColor));
                    }else {
                        if(currentAngle>=i*3-225&&i>=75)
                            degreePaint.setColor(Color.WHITE);
                        else
                            degreePaint.setColor(Color.parseColor(shortDegreeColor));
                    }
                    canvas.drawLine(centerX,
                            centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2+dipToPx(29),
                            centerX,
                            centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE -
                                    (longdegree - shortdegree) / 2 - shortdegree+dipToPx(29),
                            degreePaint);
                }
                canvas.rotate(3, centerX, centerY);
            }
        }

        //整个弧
        canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);

        if (isNeedContent) {
            canvas.drawText(String.format("%.0f", curValues), centerX, centerY + textSize / 3, vTextPaint);
        }
        if (isNeedUnit) {
            canvas.drawText("已掌握知识点", centerX, centerY + textSize*1.58f, hintPaint);
        }
        if (isNeedTitle) {
            if(curValues/100==1)
                canvas.drawText("%",centerX + textSize, centerY -  textSize / 5, curSpeedPaint);
            else if(curValues/10==0)
                canvas.drawText("%",centerX + textSize/2, centerY -  textSize / 5, curSpeedPaint);
            else
                canvas.drawText("%", centerX + 3*textSize/4, centerY -  textSize / 5, curSpeedPaint);
        }
        drawPolygon(canvas,centerX,
                centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (longdegree - shortdegree) / 2 + dipToPx(13));
        invalidate();

    }

    private void drawPolygon(Canvas canvas,float _centerX, float _centerY){
        Path path = new Path();
        float curR = dipToPx(5);
        path.reset();
        float angle =  (float) (Math.PI*2/5); ;
        float x = (float) (_centerX-curR*Math.sin(angle));
        float y = (float) (_centerY+curR*Math.cos(angle));
        path.moveTo(x,y);
        float x1 = (float) (_centerX-curR);
        float y1 = (float) (_centerY-curR*4/3);
        path.lineTo(x1,y1);
        float x2 = (float) (_centerX+curR);
        float y2 = (float) (_centerY-curR*4/3);
        path.lineTo(x2,y2);
        float x3 = (float) (_centerX+curR*Math.sin(angle));
        float y3 = (float) (_centerY+curR*Math.cos(angle));
        path.lineTo(x3,y3);
        float x4 = (float) (_centerX);
        float y4 = (float) (_centerY+curR);
        path.lineTo(x4,y4);
        path.close();
        canvas.rotate(225+currentAngle, centerX, centerY);
        canvas.drawPath(path, progressPaint);

    }

    /**
     * 设置最大值
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle/maxValues;
    }

    /**
     * 设置当前值
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed);
    }

    /**
     * 设置整个圆弧宽度
     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置单位文字大小
     * @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**
     * 设置单位文字
     * @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    }

    /**
     * 设置直径大小
     * @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = dipToPx(diameter);
    }

    /**
     * 设置标题
     * @param title
     */
    private void setTitle(String title){
        this.titleString = title;
    }

    /**
     * 设置是否显示标题
     * @param isNeedTitle
     */
    private void setIsNeedTitle(boolean isNeedTitle) {
        this.isNeedTitle = isNeedTitle;
    }

    /**
     * 设置是否显示单位文字
     * @param isNeedUnit
     */
    private void setIsNeedUnit(boolean isNeedUnit) {
        this.isNeedUnit = isNeedUnit;
    }

    /**
     * 设置是否显示外部刻度盘
     * @param isNeedDial
     */
    private void setIsNeedDial(boolean isNeedDial) {
        this.isNeedDial = isNeedDial;
    }

    /**
     * 为进度设置动画
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle= (float) animation.getAnimatedValue();
                curValues = currentAngle/k;
            }
        });
        progressAnimator.start();
    }

    /**
     * dip 转换成px
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
