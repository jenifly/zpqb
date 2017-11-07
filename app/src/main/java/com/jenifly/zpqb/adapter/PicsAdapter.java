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

public class PicsAdapter extends RecyclerView.Adapter<PicsAdapter.PicsViewHolder> {

    private Context mContext;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public PicsAdapter(Context context, List<VideoUtils> videoList) {
        mContext = context;
    }

    @Override
    public PicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_pics, parent, false);
        PicsViewHolder holder = new PicsViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(PicsViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (onItemClick != null)
                    onItemClick.onClick(video);*/
            }
        });
    }

    public interface OnItemClick{
        void onClick(VideoUtils video);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class PicsViewHolder extends RecyclerView.ViewHolder {

       // public NiceVideoPlayer mVideoPlayer;

        public PicsViewHolder(View itemView) {
            super(itemView);
         //   mVideoPlayer = (NiceVideoPlayer) itemView.findViewById(R.id.nice_video_player);
        }


        public void bindData(VideoUtils video) {
         /*   Glide.with(itemView.getContext())
                    .load(video.getImageUrl())
                    .placeholder(R.mipmap.icon_loading)
                    .crossFade()
                    .into(mController.imageView());*/
        }
    }
}
