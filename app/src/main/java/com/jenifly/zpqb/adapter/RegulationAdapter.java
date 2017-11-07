package com.jenifly.zpqb.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.infomation.Regulation_Info;
import com.jenifly.zpqb.view.fastscrollrecyclerview.FastScroll;

import java.util.ArrayList;

public class RegulationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FastScroll.SectionedAdapter{
    public static final int HAS_VIEW = 1;
    public static final int NONE_VIEW = 2;

    private Context context;
    private ArrayList<Vit> datas = new ArrayList<>();
    private OnHeaderClick onHeaderClick;
    private ArrayList<Vit> cache = new ArrayList<>();

    @NonNull
    @Override
    public String getSectionName(int position) {
        return datas.get(position).title;
    }

    class Vit{
        public int type;
        public int index;
        public int isOpen = 1;
        public String content;
        public String title;

        public Vit(int type, int index, int isOpen, String title, String content) {
            this.type = type;
            this.index = index;
            this.isOpen = isOpen;
            this.title = title;
            this.content = content;
        }
    }

    public RegulationAdapter(Context context, ArrayList<Regulation_Info.Section> baseData) {
        this.context = context;
        int index = 0;
        for (Regulation_Info.Section section : baseData) {
            if (section.getName().indexOf("-=") > -1) {
                index++;
                datas.add(new Vit(1, index, 1,"", section.getCentext()));
            } else
                datas.add(new Vit(0, index, -1, section.getName(),  "第" + section.getName() + "条\u3000" + section.getCentext()));
        }
    }

    public void setOnHeaderClick(OnHeaderClick onHeaderClick){
        this.onHeaderClick = onHeaderClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.regulation_item, parent, false);
            return new ChildHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.recyclerview_header_view, parent, false);
            return new ParentHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(datas.get(position).type == 1){
            return 1;
        }else
            return 0;
    }


    int position;
    public void OpenAndClose(int index, final RecyclerView recyclerView, boolean type){
        int count = 0;
        position = -1;
        int size= 0;
        for (int i = 0; i < datas.size(); i++){
            Vit v = datas.get(i);
            if(v.index == index) {
                if(position == -1) position = i+1;
                count++;
            }
        }
        if(count > 1) {
            for (int i = 0; i < datas.size(); i++) {
                Vit v = datas.get(i);
                if (v.type == 0 && v.index == index) {
                    cache.add(v);
                    size++;
                }
            }
            for (int i = 0; i < size; i++)
                datas.remove(position);
            datas.get(position-1).isOpen = 0;
            notifyItemRangeRemoved(position, count-1);
        }
        if(count == 1){
            int po = -1;
            for (int i = cache.size()-1; i >= 0; i--) {
                Vit v = cache.get(i);
                if (v.index == index) {
                    size++;
                    datas.add(position, v);
                }
            }
            for (int i = 0; i < cache.size(); i++) {
                Vit v = cache.get(i);
                if (v.index == index) {
                    if (po == -1)
                        po = i;
                }
            }
            for (int i = 0; i < size; i++)
                cache.remove(po);
            datas.get(position-1).isOpen = 1;
            notifyItemRangeInserted(position, size);
        }
        if (type)
            notifyItemChanged(position - 1);
        recyclerView.scrollToPosition(position-1);
    }

    private Vit getVit(int index){
        for (Vit v : datas){
            if (v.index == index)
                return v;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final Vit vit = datas.get(position);
        if (viewHolder instanceof ChildHolder) {
            ChildHolder recyclerViewHolder = (ChildHolder) viewHolder;
            recyclerViewHolder.tvName.setText(vit.content);
            recyclerViewHolder.itemView.setTag(NONE_VIEW);
            Vit v = getVit(vit.index);
            recyclerViewHolder.itemView.setContentDescription(v.content + "-=-"+ v.isOpen+"-=-"+ v.index);
        }else {
            final ParentHolder recyclerViewHolder = (ParentHolder) viewHolder;
            recyclerViewHolder.tvHeader.setText(vit.content);
            if(vit.isOpen == 1) {
                recyclerViewHolder.ivHeader.setRotation(270);
                recyclerViewHolder.ivHeader.setTag(true);
            } else {
                recyclerViewHolder.ivHeader.setRotation(180);
                recyclerViewHolder.ivHeader.setTag(false);
            }
            recyclerViewHolder.itemView.setContentDescription(vit.content+"-=-"+vit.isOpen+"-=-"+vit.index);
            recyclerViewHolder.itemView.setTag(HAS_VIEW);
            recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onHeaderClick!=null)
                        onHeaderClick.onClick(vit.index);
                    if((boolean)recyclerViewHolder.ivHeader.getTag()) {
                        rotationExpandIcon(recyclerViewHolder.ivHeader, 270, 180);
                        vit.isOpen = 0;
                    }
                    else {
                        rotationExpandIcon(recyclerViewHolder.ivHeader, 180, 270);
                        vit.isOpen = 1;
                    }
                }
            });
        }
    }

    public static void rotationExpandIcon(final View view, float from, float to) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(200);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setRotation((Float) valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.start();
        view.setTag(!(boolean)view.getTag());
    }

    public interface OnHeaderClick{
        void onClick(int index);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ChildHolder extends RecyclerView.ViewHolder {

        public TextView tvName;

        public ChildHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.regulation_item_tv);
        }
    }

    public class ParentHolder extends RecyclerView.ViewHolder {
        public TextView tvHeader;
        public ImageView ivHeader;

        public ParentHolder(View itemView) {
            super(itemView);
            tvHeader = (TextView) itemView.findViewById(R.id.tv_header_view);
            ivHeader = (ImageView) itemView.findViewById(R.id.iv_header_view);
            ivHeader.setTag(true);
            ivHeader.setRotation(270);
        }
    }

}
