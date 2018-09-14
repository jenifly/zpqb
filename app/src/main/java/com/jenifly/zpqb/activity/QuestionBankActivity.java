package com.jenifly.zpqb.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jenifly.zpqb.ItemDecoration.FloatingItemDecoration;
import com.jenifly.zpqb.R;
import com.jenifly.zpqb.helper.StuBarTranslucentAPI21Helper;
import com.jenifly.zpqb.info.QB_Info;
import com.jenifly.zpqb.cache.Cache;
import com.jenifly.zpqb.utils.KeywordUtil;
import com.jenifly.zpqb.utils.ScreenUtil;
import com.jenifly.zpqb.view.AlertDialog;
import com.jenifly.zpqb.view.BookmarksDialog;
import com.jenifly.zpqb.view.fastscrollrecyclerview.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jenifly on 2017/8/14.
 */

public class QuestionBankActivity extends AppCompatActivity {

    @BindView(R.id.rv) FastScrollRecyclerView rv;
    @BindView(R.id.questionbank_titel) TextView titel;
    @BindView(R.id.questionbank_bookmarks) ImageView bookmarks;

    private ArrayList<SpannableString> list = new ArrayList<>();
    private Map<Integer,String> keys = new HashMap<>();
    private MyAdapter adapter;
    private boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionbank_activity);
        StuBarTranslucentAPI21Helper.initState(this);
        ButterKnife.bind(this);
        ArrayList<QB_Info> questionList = new ArrayList<>();
        switch (Cache.QBType){
            case 0:
                titel.setText("题库");
                bookmarks.setImageDrawable(getResources().getDrawable(R.mipmap.icon_bookmarks));
                questionList = Cache.dataHelper.getQBInfoList(Cache.CrruentQBDataName);
                break;
            case 1:
                bookmarks.setImageDrawable(getResources().getDrawable(R.mipmap.icon_menu));
                questionList = Cache.dataHelper.getQBInfoList_OK(Cache.CrruentQBDataName);
                titel.setText("已掌握");
                break;
            case 2:
                bookmarks.setImageDrawable(getResources().getDrawable(R.mipmap.icon_menu));
                questionList = Cache.dataHelper.getQBInfoList_Collcet(Cache.CrruentQBDataName);
                titel.setText("收藏夹");
                break;
            case 3:
                bookmarks.setImageDrawable(getResources().getDrawable(R.mipmap.icon_menu));
                titel.setText("错题集");
                questionList = Cache.dataHelper.getQBInfoList_Error(Cache.CrruentQBDataName);
                break;
        }
        bookmarks.setColorFilter(Color.WHITE);
        keys.put(0,"选择题");
        for (int i = 0; i < questionList.size(); i++) {
            QB_Info info = questionList.get(i);
            if(info.getAnswer_false1().length()==0&&check){
                keys.put(i,"判断题");
                check = false;
            }
            if(info.getAnswer_false1().length() > 0) {
                String s = "（  " + info.getAnswer_correct() +  "  ）";
                list.add(KeywordUtil.matcherSearchTitle(Cache.BaseColor,
                        i + 1 + "、" + info.getQbqusetion().replace("（   ）", s ), s));
            }
            else {
                if( info.getAnswer_correct().equals("T")){
                    list.add(KeywordUtil.matcherSearchTitle(Cache.BaseColor,
                            i + 1 + "、" + info.getQbqusetion().replace("（   ）", "（  T  ）"), "（  T  ）"));
                }else {
                    list.add(KeywordUtil.matcherSearchTitle(Cache.ErrorColor,
                            i + 1 + "、" + info.getQbqusetion().replace("（   ）", "（  F  ）"), "（  F  ）"));
                }
            }
        }

        //设置adapter
        adapter=new MyAdapter(list,this);
        final FloatingItemDecoration floatingItemDecoration=new FloatingItemDecoration(this, 40, 10);
        floatingItemDecoration.setKeys(keys);
        rv.addItemDecoration(floatingItemDecoration);

        //设置布局管理器
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
    }

    @OnClick({R.id.questionbank_back,R.id.questionbank_bookmarks}) void onClick(View view){
        switch (view.getId()){
            case R.id.questionbank_back:
                finish();
                break;
            case R.id.questionbank_bookmarks:
                switch (Cache.QBType){
                    case 0:
                        new BookmarksDialog.Builder(this)
                                .setOnInsertClicked(new BookmarksDialog.OnInsertClicked() {
                                    @Override
                                    public void OnClick(View view, Dialog dialog) {
                                     //   Toast.makeText(getBaseContext(), "取消", Toast.LENGTH_SHORT).show();
                                    }
                                }).build().show();
                        break;
                    case 1:
                        new AlertDialog.Builder(this)
                                .setTextTitle("说明")
                                .setTextSubTitle("\n满足以下条件任意一条即视为你已掌握该题：\n\n  1、该题至少做过五遍且正确率达\n  到80%或以上；\n" +
                                        "  2、该题至少做过三遍且正确率为\n  100%；\n  3、该题为手动添加。\n\n备注：已掌握的题目不会出现在“今日学习”" +
                                        "中，除非清空缓存或剩余题数少于每日任务题数。\n")
                                .setPositiveButtonText("确定")
                                .setPositiveColor(Color.WHITE)
                                .setNegativeColor(Cache.BaseColor)
                                .setOnPositiveClicked(new AlertDialog.OnPositiveClicked() {
                                    @Override
                                    public void OnClick(View view, Dialog dialog) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                            }}, 200);
                                    }
                                })
                                .build().show();
                        break;
                    case 2:
                        new AlertDialog.Builder(this)
                                .setTextTitle("说明")
                                .setTextSubTitle("\n添加收藏的题目会出现在这里，除非手动移除或清空缓存。\n")
                                .setPositiveButtonText("确定")
                                .setPositiveColor(Color.WHITE)
                                .setNegativeColor(Cache.BaseColor)
                                .setOnPositiveClicked(new AlertDialog.OnPositiveClicked() {
                                    @Override
                                    public void OnClick(View view, Dialog dialog) {
                                    }
                                })
                                .build().show();
                        break;
                    case 3:
                        new AlertDialog.Builder(this)
                                .setTextTitle("说明")
                                .setTextSubTitle("\n在“今日学习”中做错的题会出现在这里，满足以下条件任意一条该题将从“错题集”移除：\n\n  " +
                                        "1、该题至少做过四遍且正确率达\n  到75%或以上；\n  2、手动从“错题集”移除；\n  " +
                                        "3、该题被手动标注为“已掌握”；\n  4、清空缓存。\n")
                                .setPositiveButtonText("确定")
                                .setPositiveColor(Color.WHITE)
                                .setNegativeColor(Cache.BaseColor)
                                .setOnPositiveClicked(new AlertDialog.OnPositiveClicked() {
                                    @Override
                                    public void OnClick(View view, Dialog dialog) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                            }}, 200);
                                    }
                                })
                                .build().show();
                        break;
                }
                break;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  implements FastScrollRecyclerView.SectionedAdapter {

        private ArrayList<SpannableString> datas;
        private Context mContext;

        public MyAdapter(ArrayList<SpannableString> datas,Context mContext){
            this.datas=datas;
            this.mContext=mContext;
        }
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyAdapter.MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.qusetionbank_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder holder, int position) {
            holder.tv.setText(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @NonNull
        @Override
        public String getSectionName(int position) {
            this.position = position;
            return String.valueOf(position+1);
        }

        int position;

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv;

            public MyViewHolder(final View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.tv);
            }
        }
    }
}