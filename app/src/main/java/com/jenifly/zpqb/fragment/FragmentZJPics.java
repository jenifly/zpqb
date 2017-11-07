package com.jenifly.zpqb.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.adapter.PicsAdapter;
import com.jenifly.zpqb.adapter.VideoAdapter;
import com.jenifly.zpqb.infomation.VideoData_Info;
import com.jenifly.zpqb.utils.ScreenUtil;
import com.jenifly.zpqb.view.fastscrollrecyclerview.FastScrollRecyclerView;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class FragmentZJPics extends Fragment {

    @BindView(R.id.fragment_zjvideo_rv) FastScrollRecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_zjvideo, null);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        PicsAdapter adapter = new PicsAdapter(getContext(), VideoData_Info.getVideoListData());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDecoration());

    }

    class ItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos=parent.getChildViewHolder(view).getAdapterPosition();
            if(pos == 0) outRect.set(0,ScreenUtil.dip2px(getContext(),10),0, ScreenUtil.dip2px(getContext(),10));
            else outRect.set(0,0,0, ScreenUtil.dip2px(getContext(),10));
        }
    }
}
