package com.jenifly.zpqb.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.utils.CircularImageViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentMine extends Fragment {

    @BindView(R.id.mine_ic_head) ImageView mine_ic_head;
    @BindView(R.id.appbar) AppBarLayout mAppbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.fragment_mine_topview) RelativeLayout fragment_mine_topview;
    @BindView(R.id.fragment_mine_titel) Toolbar fragment_mine_titel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this,view);
        view.findViewById(R.id.testss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        init();
        return view;
    }

    private void init(){
        mCollapsingToolbar.setTitle("我的");
        mCollapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbar.setExpandedTitleColor(Color.WHITE);
        CircularImageViewUtils.setCircularImageView(getContext(),R.mipmap.icon_head,mine_ic_head);
        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mCollapsingToolbar.setTitleEnabled(mCollapsingToolbar.getHeight() + verticalOffset <= (fragment_mine_titel.getHeight()-10) * 3);
            }
        });
    }


    private void initView(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }

            @Override
            protected Void doInBackground(Void... params) {

                return null;
            }
        }.execute();
    }

}
