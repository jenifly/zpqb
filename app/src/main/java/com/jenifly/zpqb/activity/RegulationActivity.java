package com.jenifly.zpqb.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.adapter.RegulationAdapter;
import com.jenifly.zpqb.helper.StuBarTranslucentAPI21Helper;
import com.jenifly.zpqb.infomation.Regulation_Info;
import com.jenifly.zpqb.utils.ScreenUtil;
import com.jenifly.zpqb.view.fastscrollrecyclerview.FastScroll;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jenifly on 2017/8/25.
 */

public class RegulationActivity extends AppCompatActivity {

    @BindView(R.id.rv) RecyclerView recyclerView;
    @BindView(R.id.tv_header_view) TextView tvHeaderView;
    @BindView(R.id.rv_header_view) RelativeLayout rvHeaderView;
    @BindView(R.id.iv_header_view) ImageView ivHeaderView;
    @BindView(R.id.rv_FastScroll) FastScroll rv_FastScroll;

    private RegulationAdapter regulationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regulation_activity);
        StuBarTranslucentAPI21Helper.initState(this);
        ButterKnife.bind(this);

        regulationAdapter = new RegulationAdapter(this,new Regulation_Info().Info);

        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new ItemDecoration());
        recyclerView.setAdapter(regulationAdapter);
        rv_FastScroll.setRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                sss();
                View transInfoView = recyclerView.findChildViewUnder(rvHeaderView.getMeasuredWidth() / 2, rvHeaderView.getMeasuredHeight() + 1);
                if (transInfoView != null && transInfoView.getTag() != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - rvHeaderView.getMeasuredHeight();
                    if (transViewStatus == regulationAdapter.HAS_VIEW) {
                        if (transInfoView.getTop() > 0 && dealtY <= 0) {
                            rvHeaderView.setTranslationY(dealtY);
                        }
                    } else if (transViewStatus == regulationAdapter.NONE_VIEW) {
                        rvHeaderView.setTranslationY(0);
                    }
                }
            }
        });
        regulationAdapter.setOnHeaderClick(new RegulationAdapter.OnHeaderClick() {
            @Override
            public void onClick(int index) {
                regulationAdapter.OpenAndClose(index, recyclerView,false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sss();
                    }
                }, 200);
            }
        });
    }
    
    @OnClick({R.id.rv_header_view})void click(View view){
        switch (view.getId()){
            case R.id.rv_header_view:
                regulationAdapter.OpenAndClose(Integer.parseInt(rvHeaderView.getTag().toString()), recyclerView,true);
                if((boolean)ivHeaderView.getTag())
                    RegulationAdapter.rotationExpandIcon(ivHeaderView, 270, 180);
                else
                    RegulationAdapter.rotationExpandIcon(ivHeaderView, 180, 270);
                break;
        }
    }

    private void sss() {
        View InfoView = recyclerView.findChildViewUnder(rvHeaderView.getMeasuredWidth() / 2, 5);
        if (InfoView != null && InfoView.getContentDescription() != null) {
            String[] str = String.valueOf(InfoView.getContentDescription()).split("-=-");
            if (str[1].equals("1")){
                ivHeaderView.setRotation(270);
                ivHeaderView.setTag(true);
            }else {
                ivHeaderView.setRotation(180);
                ivHeaderView.setTag(false);
            }
            tvHeaderView.setText(str[0]);
            rvHeaderView.setTag(str[2]);
        }
    }

    class ItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, ScreenUtil.dip2px(getBaseContext(), 10));
        }
    }
}
