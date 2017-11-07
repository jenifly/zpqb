package com.jenifly.zpqb.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jenifly.zpqb.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Jenifly on 2017/3/11.
 */

public class InfoPopWindow {

    @BindView(R.id.info) TextView infotv;

    private PopupWindow mPopupWindow;
    private Builder mBuilder;

    static public class Builder{
        private Activity mActivity;
        private String info;

        public Builder(Activity mActivity, String info){
            this.mActivity = mActivity;
            this.info = info;
        }

        public InfoPopWindow build(){
            return new InfoPopWindow(this);
        }

    }
    private InfoPopWindow(final Builder builder){
        mBuilder = builder;
        //init PopWindow
        View popview = View.inflate(builder.mActivity, R.layout.info_popwindow, null);
        mPopupWindow = new PopupWindow(popview, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        initViews(mPopupWindow.getContentView());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        new Handler().postDelayed(new Runnable(){
            public void run() {
                dismiss();
            }
        }, 3000);
    }

    private void initViews(final View root) {
        ButterKnife.bind(this,root);
        infotv.setText(mBuilder.info);
    }

    public void dismiss() {
        if (mPopupWindow != null){
            mPopupWindow.dismiss();
        }
    }

    /**
     * parent is the popwindow show location
     * @param parent
     */
    public void show(View parent){
        if (mPopupWindow != null){
            backgroundAlpha(0.8f);
            mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        }
    }
    /**
     * set background alpha
     * @param alpha
     */
    public void backgroundAlpha(float alpha) {
        try {
            WindowManager.LayoutParams lp = mBuilder.mActivity.getWindow().getAttributes();
            lp.alpha = alpha; //0.0-1.0
            mBuilder.mActivity.getWindow().setAttributes(lp);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
