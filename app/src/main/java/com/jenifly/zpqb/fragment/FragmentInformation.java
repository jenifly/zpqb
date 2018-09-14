package com.jenifly.zpqb.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.NestedScrollingParent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.activity.ZJgushiActivity;
import com.jenifly.zpqb.adapter.PVAdapter;
import com.jenifly.zpqb.adapter.VideoAdapter;
import com.jenifly.zpqb.infomation.VideoData_Info;
import com.jenifly.zpqb.utils.ScreenUtil;
import com.jenifly.zpqb.view.fastscrollrecyclerview.FastScrollRecyclerView;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentInformation extends Fragment {

    @BindView(R.id.frag_imformation_titlebar) RelativeLayout titlebar;
//    @BindView(R.id.frag_imformation_carousel) View carousel;
  //  @BindView(R.id.frag_imformation_menull) LinearLayout menull;
    @BindView(R.id.fragment_zjzixun) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        PVAdapter adapter = new PVAdapter(getContext(), VideoData_Info.getVideoListData());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDecoration());
        recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                if(holder instanceof VideoAdapter.VideoViewHolder) {
                    NiceVideoPlayer niceVideoPlayer = ((VideoAdapter.VideoViewHolder) holder).mVideoPlayer;
                    if (niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                    }
                }
            }
        });
    }

//    @OnClick({R.id.infomation_zjgushi})void click(View view){
//        startActivity(new Intent(getActivity(), ZJgushiActivity.class));
//    }

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

    class ItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos=parent.getChildViewHolder(view).getAdapterPosition();
//            if(pos == 0) outRect.set(0, ScreenUtil.dip2px(getContext(),10),0, ScreenUtil.dip2px(getContext(),10));
//            else outRect.set(0,0,0, ScreenUtil.dip2px(getContext(),10));
            outRect.set(0,0,0, ScreenUtil.dip2px(getContext(),8));
        }
    }
}
