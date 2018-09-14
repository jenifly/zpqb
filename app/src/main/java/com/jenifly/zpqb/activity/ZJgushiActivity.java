package com.jenifly.zpqb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.fragment.FragmentZJPV;
import com.jenifly.zpqb.fragment.FragmentZJPics;
import com.jenifly.zpqb.fragment.FragmentZJVideo;
import com.jenifly.zpqb.helper.StuBarTranslucentAPI21Helper;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jenifly on 2017/8/25.
 */

public class ZJgushiActivity extends AppCompatActivity {

    @BindView(R.id.zjgushi_tab) TabLayout colorTrackTabLayout;
    @BindView(R.id.zjgushi_viewPager) ViewPager mViewPager;

    private String[] titles = new String[]{"视频", "图文", "混s排"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zjgushi_mian);
        StuBarTranslucentAPI21Helper.initState(this);
        ButterKnife.bind(this);
        loadViewLayout();
    }

    private void loadViewLayout() {
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentZJVideo());
        fragments.add(new FragmentZJPics());
        fragments.add(new FragmentZJPV());

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        colorTrackTabLayout.setupWithViewPager(mViewPager,true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }
}
