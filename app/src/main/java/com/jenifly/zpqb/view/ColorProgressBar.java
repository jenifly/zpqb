package com.jenifly.zpqb.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.utils.ScreenUtil;

/**
 * Created by Jenifly on 2017/8/12.
 */

public class ColorProgressBar extends View {

    private int correctColor;
    private int baseColor;
    private int errorColor;
    private int maxValue;
    private int correctCount = 0;
    private int errorCount = 0;
    private Paint mPaint,tPaint;
    private  int correctWidth,errorWidth,baseWidth;

    public ColorProgressBar(Context context) {
        super(context);
        initView();
    }

    public ColorProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ColorProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        mPaint = new Paint();
        tPaint = new Paint();
        tPaint.setTextSize(ScreenUtil.dip2px(getContext(),12));
        tPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int tw = getTextWidth(tPaint, "0");
        int remaining = maxValue - correctCount - errorCount;
        if(width*remaining/maxValue>=0) {
            correctWidth = correctCount > 0 ? width * correctCount / maxValue : 0;
            errorWidth = errorCount > 0 ? width * errorCount / maxValue : 0;
            baseWidth = width - errorWidth;
        }
        if(correctCount > 0){
            mPaint.setColor(correctColor);
            tPaint.setColor(Color.WHITE);
            if(remaining>0) {
                if (correctWidth - tw * 2 < 0)
                    correctWidth = tw * 2;
            }
            canvas.drawRect(0,0,correctWidth,height,mPaint);
            canvas.drawText(String.valueOf(correctCount), String.valueOf(correctCount).length()<2?
                    correctWidth - tw:correctWidth - tw*3/2, height*0.78f, tPaint);
        }
        if(errorCount > 0) {
            mPaint.setColor(errorColor);
            tPaint.setColor(Color.WHITE);
            if(remaining>0) {
                if (errorWidth - tw * 2 < 0)
                    errorWidth = tw * 2;
            }
            baseWidth = width - errorWidth;
            canvas.drawRect(baseWidth, 0, width, height, mPaint);
            canvas.drawText(String.valueOf(errorCount), String.valueOf(errorCount).length()<2?
                    baseWidth + tw:baseWidth + tw*3/2, height*0.78f, tPaint);
        }
        if(remaining>0) {
            mPaint.setColor(baseColor);
            tPaint.setColor(getContext().getResources().getColor(R.color.black_1));
            if(baseWidth-tw*2-correctWidth<0)
                baseWidth = correctWidth+tw*2;
            canvas.drawRect(correctWidth,0,baseWidth,height,mPaint);
            if(String.valueOf(remaining).length()==1){
                canvas.drawText(String.valueOf(remaining), baseWidth - tw, height*0.78f, tPaint);
            }
            if(String.valueOf(remaining).length()==2){
                canvas.drawText(String.valueOf(remaining), baseWidth - tw*3/2, height*0.78f, tPaint);
            }
            if(String.valueOf(remaining).length()==3) {
                canvas.drawText(String.valueOf(remaining), baseWidth - tw*5/2, height*0.78f, tPaint);
            }
        }
        invalidate();
    }

    private int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setCorrectColor(int correctColor) {
        this.correctColor = correctColor;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
    }

    public void setErrorColor(int errorColor) {
        this.errorColor = errorColor;
    }

    public void addCorrect(){
        correctCount++;
    }

    public void addError(){
        errorCount++;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }
}
