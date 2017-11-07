package com.jenifly.zpqb.ItemDecoration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Dimension;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.cache.Cache;
import com.jenifly.zpqb.utils.BitmapUtils;
import com.jenifly.zpqb.utils.ItemKeyUtils;
import com.jenifly.zpqb.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by -- on 2017/1/13.
 * 头部悬浮的效果，只支持LinearLayoutManager垂直滑动的情况
 */

public class mFloatingItemDecoration extends RecyclerView.ItemDecoration{

    private int dividerHeight;
    private ArrayList<ItemKeyUtils> keys;
    private int mTitleHeight;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;
    private float mTextHeight;
    private float mTextBaselineOffset;
    private Context mContext;
    private Bitmap bitmap;
    private ArrayList<HeaderRects> mHeaderRects = new ArrayList();

    class HeaderRects{
        public String _name;
        public Rect _rect;

        public HeaderRects(String _name, Rect _rect) {
            this._name = _name;
            this._rect = _rect;
        }
    }

    /**
     * 自定义分割线
     * 也可以使用{@link Canvas#drawRect(float, float, float, float, Paint)}或者{@link Canvas#drawText(String, float, float, Paint)}等等
     * 结合{@link Paint}去绘制各式各样的分割线
     * @param context
     * @param titleHeight  单位为dp
     * @param dividerHeight  单位为dp
     */
    public mFloatingItemDecoration(Context context, @Dimension int titleHeight, @Dimension float dividerHeight) {
        this.mTitleHeight = ScreenUtil.dip2px(context,titleHeight);
        this.dividerHeight= ScreenUtil.dip2px(context,dividerHeight);
        init(context);
    }

    private void init(Context mContext){
        this.mContext=mContext;
        mTextPaint=new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,16,mContext.getResources().getDisplayMetrics()));
        mTextPaint.setColor(Cache.BaseColor);
        Paint.FontMetrics fm=mTextPaint.getFontMetrics();
        mTextHeight=fm.bottom-fm.top;//计算文字高度
        mTextBaselineOffset=fm.bottom;

        mBackgroundPaint=new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(Cache.Backgroud_1);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mContext.getResources().getDrawable(R.mipmap.icon_left);
        bitmap = adjustPhotoRotation(BitmapUtils.getAlphaBitmap(bitmapDrawable.getBitmap(),Cache.BaseColor),180);
    }

    private Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }
        final float[] values = new float[9];
        m.getValues(values);
        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];
        m.postTranslate(targetX - x1, targetY - y1);
        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);
        return bm1;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        drawVertical(c,parent);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int firstVisiblePos=((LinearLayoutManager)parent.getLayoutManager()).findFirstVisibleItemPosition();
        if(firstVisiblePos==RecyclerView.NO_POSITION){
            return;
        }
        String title = getTitle(firstVisiblePos);
        if(TextUtils.isEmpty(title)){
            return;
        }
        boolean flag = false;
        if(getTitle(firstVisiblePos+1)!=null&&!title.equals(getTitle(firstVisiblePos+1))){
            //说明是当前组最后一个元素，但不一定碰撞了
            View child=parent.findViewHolderForAdapterPosition(firstVisiblePos).itemView;
            if(child.getTop()+child.getMeasuredHeight()<mTitleHeight){
                //进一步检测碰撞
                c.save();//保存画布当前的状态
                flag=true;
                c.translate(0,child.getTop()+child.getMeasuredHeight()-mTitleHeight);//负的代表向上
            }
        }
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top = parent.getPaddingTop();
        int bottom = top + mTitleHeight;
        Rect rect = new Rect(left, top, right, bottom);
        c.drawRect(rect,mBackgroundPaint);
        int x = ScreenUtil.dip2px(mContext,16);
        float y = bottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset;//计算文字baseLine
        c.drawText(title,x,y,mTextPaint);
        int size = ScreenUtil.dip2px(mContext,18);
        int mlefe = right - ScreenUtil.dip2px(mContext,36);
        int center = (bottom - top)/2 + top;
        c.drawBitmap(bitmap, null, new Rect(mlefe, center - size / 2, mlefe + size, center + size/2), mTextPaint);
        mHeaderRects.add(new HeaderRects(title,rect));
        if(flag){
            c.restore();
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = Integer.parseInt(view.getTag().toString());
        boolean isTitle = false;
        int count = 1;
        for (ItemKeyUtils key : keys)
            if(key.index == pos ) {
                isTitle = true;
                break;
            }
        if(isTitle) outRect.set(0, mTitleHeight*count, 0, 0);else outRect.set(0,dividerHeight,0,0);
    }

    public ItemKeyUtils findHeaderPositionUnder(int x, int y) {
        for(HeaderRects headerRects:mHeaderRects)
            if(headerRects._rect.contains(x,y))
                for (ItemKeyUtils key : keys)
                    if (key.text.equals(headerRects._name)) return key;
        return null;
    }

    /**
     **如果该位置没有，则往前循环去查找标题，找到说明该位置属于该分组
     * @param position
     * @return
     */
    private String getTitle(int position) {
        while (position >= 0) {
            for (ItemKeyUtils key : keys){
                if(key.index-1 == position) {//留出头部偏移
                    return key.text;
                }
            }
            position--;
        }
        return null;
    }

    private String tilel;
    private boolean keyshaveposition(int position){
        for (ItemKeyUtils key : keys) {
            if (key.index == position) {
                tilel = key.text;
                return true;
            }
        }
        return false;
    }

    private void drawVertical(Canvas c, RecyclerView parent){
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top;
        int bottom;
        mHeaderRects.clear();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            for (ItemKeyUtils key : keys) {
                if (Integer.parseInt(child.getTag().toString()) == key.index) {
                    key.show = true;
                    tilel = key.text;
                    top = child.getTop()-params.topMargin-mTitleHeight;
                    if(top>parent.getPaddingTop()) {
                        bottom = top + mTitleHeight;
                        Rect rect = new Rect(left, top, right, bottom);
                        int x = ScreenUtil.dip2px(mContext, 16);
                        float y = bottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset;//计算文字baseLine
                        c.drawText(tilel, x, y, mTextPaint);
                        int size = ScreenUtil.dip2px(mContext, 18);
                        int mlefe = right - ScreenUtil.dip2px(mContext, 36);
                        int center = (bottom - top)/2 + top;
                        c.drawBitmap(bitmap, null, new Rect(mlefe, center - size / 2, mlefe + size, center + size/2), mTextPaint);
                        mHeaderRects.add(new HeaderRects(tilel,rect));
                    }
                }
            }
        }
    }

    public void setKeys(ArrayList<ItemKeyUtils> keys){
        this.keys = keys;
    }

}
