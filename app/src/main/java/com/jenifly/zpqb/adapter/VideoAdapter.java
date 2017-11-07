package com.jenifly.zpqb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jenifly.zpqb.R;
import com.jenifly.zpqb.utils.VideoUtils;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.util.List;

/**
 * Created by Jenifly on 2017/5/21.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context mContext;
    private List<VideoUtils> mVideoList;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public VideoAdapter(Context context, List<VideoUtils> videoList) {
        mContext = context;
        mVideoList = videoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        VideoViewHolder holder = new VideoViewHolder(itemView);
        TxVideoPlayerController controller = new TxVideoPlayerController(mContext);
        holder.setController(controller);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final VideoUtils video = mVideoList.get(position);
        holder.bindData(video);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null)
                    onItemClick.onClick(video);
            }
        });
    }

    public interface OnItemClick{
        void onClick(VideoUtils video);
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        public TxVideoPlayerController mController;
        public NiceVideoPlayer mVideoPlayer;

        public VideoViewHolder(View itemView) {
            super(itemView);
            mVideoPlayer = (NiceVideoPlayer) itemView.findViewById(R.id.nice_video_player);
            // 将列表中的每个视频设置为默认16:9的比例
            ViewGroup.LayoutParams params = mVideoPlayer.getLayoutParams();
            params.width = itemView.getResources().getDisplayMetrics().widthPixels; // 宽度为屏幕宽度
            params.height = (int) (params.width * 9f / 16f);    // 高度为宽度的9/16
            mVideoPlayer.setLayoutParams(params);
        }

        public void setController(TxVideoPlayerController controller) {
            mController = controller;
            mVideoPlayer.setController(mController);
        }

        public void bindData(VideoUtils video) {
            mController.setTitle(video.getTitle());
            mController.setLenght(video.getLength());
            Glide.with(itemView.getContext())
                    .load(video.getImageUrl())
                    .placeholder(R.mipmap.icon_loading)
                    .crossFade()
                    .into(mController.imageView());
            mVideoPlayer.setUp(video.getVideoUrl(), null);
        }
    }
}
