package com.jenifly.zpqb.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.adapter.QuestionsAdapter;
import com.jenifly.zpqb.cache.Cache;
import com.jenifly.zpqb.data.DataHelper;
import com.jenifly.zpqb.data.SqliteHelper;
import com.jenifly.zpqb.helper.StuBarTranslucentAPI21Helper;
import com.jenifly.zpqb.info.QB_Info;
import com.jenifly.zpqb.infomation.QB_zhjwdrz;
import com.jenifly.zpqb.view.AlertDialog;
import com.jenifly.zpqb.view.ColorProgressBar;
import com.jenifly.zpqb.view.JLinearLayout;
import com.jenifly.zpqb.view.recyclerCoverFlow.CoverFlowLayoutManger;
import com.jenifly.zpqb.view.recyclerCoverFlow.RecyclerCoverFlow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.widget.NumberPicker.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * Created by yarolegovich on 07.03.2017.
 */

public class QuestionsActivity extends AppCompatActivity {

    @BindView(R.id.question_list) RecyclerCoverFlow questionlist;
    @BindView(R.id.question_progress) ColorProgressBar colorProgressBar;
    @BindView(R.id.question_answer_a) JLinearLayout question_answer_a;
    @BindView(R.id.question_answer_b) JLinearLayout question_answer_b;
    @BindView(R.id.question_answer_c) JLinearLayout question_answer_c;
    @BindView(R.id.question_answer_d) JLinearLayout question_answer_d;
    @BindView(R.id.question_answer_tva) TextView question_answer_tva;
    @BindView(R.id.question_answer_tvb) TextView question_answer_tvb;
    @BindView(R.id.question_answer_tvc) TextView question_answer_tvc;
    @BindView(R.id.question_answer_tvd) TextView question_answer_tvd;
    @BindView(R.id.qusetion_slidingdraw) SlidingDrawer slidingDrawer;
    @BindView(R.id.qusetion_slidingdraw_handle) ImageView qusetion_slidingdraw_handle;

    private String correct;
    private int _width = 0;
    private int questionCount;
    private QuestionsAdapter questionsAdapter;
    private ArrayList<QB_Info> infos;
    private QB_Info info;
    private int a,b;
    private int scount = 0;
    private int jcorrect = 0,jerror = 0;
    private String[] answers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        StuBarTranslucentAPI21Helper.initState(this);
        ButterKnife.bind(this);
        loadData();
        initView();
    }

    private void loadData(){
        if(Cache.CrruentTime.equals(Cache.LastTime)){
            infos = Cache.dataHelper.getQBTodayInfoList();
            if (infos.size()==0){
                getinfos();
            }
        } else {
            Cache.dataHelper.DROPTABLE(SqliteHelper.QBData_today);
            Cache.dataHelper.ResttingQBSettingTodayStudy();
            Cache.dataHelper.UpdateQBSettingValue(SqliteHelper.Setting_Today,Cache.CrruentTime);
            getinfos();
        }
        for (QB_Info info:infos){
            if(info.getQbselect().length()>0){
                scount++;
                if(info.getQbselect().equals(info.getAnswer_correct())) jcorrect++;
                else jerror++;
            }
        }
        questionCount = infos.size();
    }

    private void getinfos(){
        infos =  Cache.dataHelper.getQBInfoList_Today(Cache.CrruentQBDataName,Cache.TodayStudyCount);
        for (QB_Info info : infos)
            Cache.dataHelper.addQBInfo(SqliteHelper.QBData_today,new QB_Info(
                    info.getQbid(),info.getQbqusetion(),info.getAnswer_correct(),info.getAnswer_false1(),
                    info.getAnswer_false2(),info.getAnswer_false3(),info.getQbcollect(),info.getQbcorrect(),
                    info.getQberror(),info.getQbdelet(),""));
        infos = Cache.dataHelper.getQBTodayInfoList();
        Collections.shuffle(infos);
    }

    private void initView() {
        info = infos.get(scount==questionCount?scount-1:scount);
        questionsAdapter = new QuestionsAdapter(this, infos);
        colorProgressBar.setMaxValue(questionsAdapter.getItemCount());
        colorProgressBar.setCorrectColor(Cache.BaseColor);
        colorProgressBar.setErrorColor(Cache.ErrorColor);
        colorProgressBar.setBaseColor(Color.WHITE);
        colorProgressBar.setCorrectCount(jcorrect);
        colorProgressBar.setErrorCount(jerror);
        questionlist.setAdapter(questionsAdapter);
        questionlist.setCanScrollNext(scount);
        questionlist.scrollToPosition(scount==questionCount?0:scount);
        setAnswers();
        questionlist.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {
                info = infos.get(questionlist.getSelectedPos());
                question_answer_a.Restore();
                question_answer_b.Restore();
                question_answer_c.Restore();
                question_answer_d.Restore();
                setAnswers();
            }
        });
        questionlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == SCROLL_STATE_IDLE) {
                    if(_width == 0)
                        _width = questionlist.getOffsetForPosition(1);
                    b = 0;
                    a = _width * (scount - questionlist.getSelectedPos());
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                b += dx;
                if(b>a)
                    questionlist.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                            SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
            }
        });
        //监听打开抽屉事件
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                qusetion_slidingdraw_handle.setImageResource(R.mipmap.icon_drag);
                qusetion_slidingdraw_handle.setBackgroundColor(getResources().getColor(R.color.backgroud_3));
            }
        });

        //监听关闭抽屉事件
        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                qusetion_slidingdraw_handle.setImageResource(R.mipmap.icon_drag);
                qusetion_slidingdraw_handle.setBackgroundColor(getResources().getColor(R.color.backgroud_2));
            }
        });
    }

    private void setAnswers(){
        if(info.getAnswer_false1().length()>0) {
            answers = new String[]{info.getAnswer_correct(), info.getAnswer_false1()};
            if (info.getAnswer_false2().length() > 0) {
                answers = new String[]{info.getAnswer_correct(), info.getAnswer_false1(), info.getAnswer_false2()};
                if (info.getAnswer_false3().length() > 0)
                    answers = new String[]{info.getAnswer_correct(), info.getAnswer_false1(), info.getAnswer_false2(), info.getAnswer_false3()};
            }
        }
        correct = info.getAnswer_correct();
        if(info.getAnswer_false1().length() > 0) {
            List list = Arrays.asList(answers);
            Collections.shuffle(list);
            answers = (String[]) list.toArray();
            if(info.getAnswer_false3().length() > 0) {
                question_answer_c.setVisibility(View.VISIBLE);
                question_answer_d.setVisibility(View.VISIBLE);
                question_answer_tva.setText("A、" + answers[0]);
                question_answer_tvb.setText("B、" + answers[1]);
                question_answer_tvc.setText("C、" + answers[2]);
                question_answer_tvd.setText("D、" + answers[3]);
            }
            if(info.getAnswer_false2().length()>0 && info.getAnswer_false3().length() == 0){
                question_answer_c.setVisibility(View.VISIBLE);
                question_answer_d.setVisibility(View.GONE);
                question_answer_tva.setText("A、" + answers[0]);
                question_answer_tvb.setText("B、" + answers[1]);
                question_answer_tvc.setText("C、" + answers[2]);
            }
            if(info.getAnswer_false2().length() == 0){
                question_answer_c.setVisibility(View.GONE);
                question_answer_d.setVisibility(View.GONE);
                question_answer_tva.setText("A、" + answers[0]);
                question_answer_tvb.setText("B、" + answers[1]);
            }
        }else {
            answers = correct.equals("T")?new String[]{"-T-","-F-"}:new String[]{"-F-","-T-"};
            question_answer_tva.setText("T");
            question_answer_tvb.setText("F");
            question_answer_c.setVisibility(View.GONE);
            question_answer_d.setVisibility(View.GONE);
        }
        if(info.getQbselect().length() == 0) {
            jEnabled(true);
            slidingDrawer.setVisibility(View.GONE);
        } else {
            jEnabled(false);
            if (info.getQbselect().equals(correct)) {
                jswitch(getIndex(info.getQbselect()), true);
                slidingDrawer.setVisibility(View.GONE);
            }
            else
            {
                jswitch(getIndex(correct), true);
                jswitch(getIndex(info.getQbselect()), false);
                slidingDrawer.setVisibility(View.VISIBLE);
            }
        }
    }

    private int getIndex(String stmp){
        int i = 1;
        if(stmp.equals("T"))
            return 1;
        if(stmp.equals("F"))
            return 2;
        for(String str:answers){
            if(str.equals(stmp))
                return i;
            i++;
        }
        return i;
    }

    private void jEnabled(boolean tf){
        question_answer_a.setEnabled(tf);
        question_answer_b.setEnabled(tf);
        question_answer_c.setEnabled(tf);
        question_answer_d.setEnabled(tf);
    }

    private void jswitch(int index,boolean tf){
        switch (index){
            case 1:
                question_answer_a.setState(tf);
                break;
            case 2:
                question_answer_b.setState(tf);
                break;
            case 3:
                question_answer_c.setState(tf);
                break;
            case 4:
                question_answer_d.setState(tf);
                break;
        }
    }

    @OnClick({R.id.question_back, R.id.question_answer_a,R.id.question_answer_b,
            R.id.question_answer_c,R.id.question_answer_d}) void click(View view){
        switch (view.getId()){
            case R.id.question_back:
                finish();
                break;
            case R.id.question_answer_a:
                clickAnswer(question_answer_a, question_answer_tva.getText().toString().replace("A、",""));
                break;
            case R.id.question_answer_b:
                clickAnswer(question_answer_b, question_answer_tvb.getText().toString().replace("B、",""));
                break;
            case R.id.question_answer_c:
                clickAnswer(question_answer_c, question_answer_tvc.getText().toString().replace("C、",""));
                break;
            case R.id.question_answer_d:
                clickAnswer(question_answer_d, question_answer_tvd.getText().toString().replace("D、",""));
                break;
        }
    }

    private void clickAnswer(final JLinearLayout jLinearLayout, String index){
        Cache.dataHelper.AddQBSettingTodayStudy();
        info.setQbselect(index);
        questionlist.setCanScrollNext(++scount);
        if(index.equals(correct)){
            info.setQbcorrect(info.getQbcorrect() + 1);
            Cache.dataHelper.updateBaseInfo(Cache.CrruentQBDataName,info.getQbid(), QB_Info.QBCorrect, info.getQbcorrect());
            Cache.dataHelper.updateTodayInfo(SqliteHelper.QBData_today,info);
            jLinearLayout.setState(true);
            colorProgressBar.addCorrect();
            if (scount < questionCount)
                questionlist.Nest();
        }else {
            info.setQberror(info.getQberror()+1);
            Cache.dataHelper.updateBaseInfo(Cache.CrruentQBDataName,info.getQbid(), QB_Info.QBError,info.getQberror());
            Cache.dataHelper.updateTodayInfo(SqliteHelper.QBData_today,info);
            jLinearLayout.setState(false);
            colorProgressBar.addError();
            slidingDrawer.setVisibility(View.VISIBLE);
            jswitch(getIndex(correct),true);
        }
        jEnabled(false);
        questionsAdapter.notifyDataSetChanged();
        checkLast();
    }

    private void checkLast(){
        if(scount == questionCount) {
            Cache.dataHelper.addQBSetting(SqliteHelper.Setting_Completeday,Cache.CrruentTime);
            new AlertDialog.Builder(this)
                    .setTextTitle("提示")
                    .setTextSubTitle("你已完成今日的测试！")
                    .setImageRecourse(R.mipmap.icon_complete)
                    .setPositiveButtonText("确定")
                    .setPositiveColor(Cache.BaseColor)
                    .setOnPositiveClicked(new AlertDialog.OnPositiveClicked() {
                        @Override
                        public void OnClick(View view, Dialog dialog) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }}, 200);
                        }
                    })
                    .setNegativeButtonText("回顾")
                    .setNegativeColor(Cache.BaseColor)
                    .setOnNegativeClicked(new AlertDialog.OnNegativeClicked() {
                        @Override
                        public void OnClick(View view, Dialog dialog) {
                            //  Toast.makeText(getBaseContext(), "取消", Toast.LENGTH_SHORT).show();
                        }
                    }).build().show();
        }
    }
}
