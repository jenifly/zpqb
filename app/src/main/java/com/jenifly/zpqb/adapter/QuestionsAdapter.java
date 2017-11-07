package com.jenifly.zpqb.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.cache.Cache;
import com.jenifly.zpqb.data.SqliteHelper;
import com.jenifly.zpqb.info.QB_Info;
import com.jenifly.zpqb.infomation.QB_zhjwdrz;
import com.jenifly.zpqb.view.AlertDialog;
import com.jenifly.zpqb.view.JImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by chenxiaoping on 2017/3/28.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<QB_Info> questionList = new ArrayList<>();

    public QuestionsAdapter(Context c,ArrayList<QB_Info> questionList) {
        mContext = c;
        this.questionList = questionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.qusetion_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final QB_Info info = questionList.get(position);
        holder.tv_question.setText(position+1+"、"+info.getQbqusetion());
        holder.tv_title.setText(info.getAnswer_false2().length()>0?"选择题":"判断题");
        int count = info.getQberror()+info.getQbcorrect();
        DecimalFormat df = new DecimalFormat("#00.0");
        if(count > 0) {
            if(info.getQberror()==0) holder.tv_accuracy.setText("正确率：100.0%（"+ count +"次）");
            else if(info.getQbcorrect()==0) holder.tv_accuracy.setText("正确率：00.0%（"+ count +"次）");
            else holder.tv_accuracy.setText("正确率："+df.format((float)info.getQbcorrect()/count*100)+"%（"+count+"次）");
        }else holder.tv_accuracy.setText("正确率：--.-%（0次）");
        if(info.getQbcollect() == 1) {
            holder.sbtn_collcet.setColorFilter(Cache.BaseColor);
            holder.sbtn_collcet.setCanScale(false);
        } else
            holder.sbtn_collcet.setCanScale(true);
        if(info.getQbdelet() == 1) {
            holder.sbtn_ok.setColorFilter(Cache.BaseColor);
            holder.sbtn_ok.setCanScale(false);
        }else
            holder.sbtn_ok.setCanScale(true);
        holder.sbtn_collcet.setClickListener(new JImageView.ClickListener() {
            @Override
            public void onClick() {
                if(info.getQbcollect() == 0) {
                    info.setQbcollect(1);
                    holder.sbtn_collcet.setCanScale(false);
                    holder.sbtn_collcet.setColorFilter(Cache.BaseColor);
                    Cache.dataHelper.updateBaseInfo(QB_zhjwdrz.QB_zhjwdrz_TB_NAME, info.getQbid(), QB_Info.QBCollect, 1);
                    Cache.dataHelper.updateBaseInfo(SqliteHelper.QBData_today, info.getQbid(), QB_Info.QBCollect, 1);
                    Alerdialog("        收藏成功！        ");
                }
                else {
                    info.setQbcollect(0);
                    holder.sbtn_collcet.setCanScale(true);
                    holder.sbtn_collcet.setColorFilter(Cache.perssColor);
                    Cache.dataHelper.updateBaseInfo(QB_zhjwdrz.QB_zhjwdrz_TB_NAME, info.getQbid(), QB_Info.QBCollect, 0);
                    Cache.dataHelper.updateBaseInfo(SqliteHelper.QBData_today, info.getQbid(), QB_Info.QBCollect, 0);
                    Alerdialog("        取消收藏！        ");
                }
            }
        });
        holder.sbtn_ok.setClickListener(new JImageView.ClickListener() {
            @Override
            public void onClick() {
                if (info.getQbdelet() == 0) {
                    info.setQbdelet(1);
                    holder.sbtn_ok.setCanScale(false);
                    holder.sbtn_ok.setColorFilter(Cache.BaseColor);
                    Cache.dataHelper.updateBaseInfo(QB_zhjwdrz.QB_zhjwdrz_TB_NAME, info.getQbid(), QB_Info.QBDelet, 1);
                    Cache.dataHelper.updateBaseInfo(SqliteHelper.QBData_today, info.getQbid(), QB_Info.QBDelet, 1);
                    Alerdialog("已将该题添加至已掌握！");
                } else {
                    info.setQbdelet(0);
                    holder.sbtn_ok.setCanScale(true);
                    holder.sbtn_ok.setColorFilter(Cache.perssColor);
                    Cache.dataHelper.updateBaseInfo(QB_zhjwdrz.QB_zhjwdrz_TB_NAME, info.getQbid(), QB_Info.QBDelet, 0);
                    Cache.dataHelper.updateBaseInfo(SqliteHelper.QBData_today, info.getQbid(), QB_Info.QBDelet, 0);
                    Alerdialog("已将该题从已掌握移除！");
                }
            }
        });
    }

    private static void scaleTo(View v, float scale) {
        if (Build.VERSION.SDK_INT >= 11) {
            v.setScaleX(scale);
            v.setScaleY(scale);
        } else {
            float oldScale = 1;
            if (v.getTag(Integer.MIN_VALUE) != null) {
                oldScale = (Float) v.getTag(Integer.MIN_VALUE);
            }
            final ViewGroup.LayoutParams params = v.getLayoutParams();
            params.width = (int) ((params.width / oldScale) * scale);
            params.height = (int) ((params.height / oldScale) * scale);
            v.setTag(Integer.MIN_VALUE, scale);
        }
    }

    private void Alerdialog(String context){
        new AlertDialog.Builder(mContext)
                .setTextTitle("提示")
                .setTextSubTitle("\n" + context + "\n")
                .setPositiveButtonText("确定")
                .setPositiveColor(Color.WHITE)
                .setNegativeColor(Cache.BaseColor)
                .setOnPositiveClicked(new AlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                    }
                })
                .build().show();
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_question;
        private TextView tv_title;
        private TextView tv_accuracy;
        private JImageView sbtn_collcet;
        private JImageView sbtn_ok;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_question = (TextView) itemView.findViewById(R.id.item_question);
            tv_title = (TextView) itemView.findViewById(R.id.question_item_title);
            tv_accuracy = (TextView)itemView.findViewById(R.id.qusetion_item_accuracy);
            sbtn_collcet = (JImageView)itemView.findViewById(R.id.qusetion_item_collcet);
            sbtn_ok = (JImageView)itemView.findViewById(R.id.qusetion_item_ok);
        }
    }
}
