package com.jenifly.zpqb.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.activity.QuestionBankActivity;
import com.jenifly.zpqb.activity.QuestionsActivity;
import com.jenifly.zpqb.activity.SettingActivity;
import com.jenifly.zpqb.cache.Cache;
import com.jenifly.zpqb.data.DataHelper;
import com.jenifly.zpqb.data.SqliteHelper;
import com.jenifly.zpqb.info.QB_Info;
import com.jenifly.zpqb.info.Question_Info;
import com.jenifly.zpqb.infomation.QB_zhjwdrz;
import com.jenifly.zpqb.utils.ColorUtils;
import com.jenifly.zpqb.utils.ScreenUtil;
import com.jenifly.zpqb.view.ArcProgressBar;
import com.jenifly.zpqb.view.ChooseQBDialog;
import com.jenifly.zpqb.view.InfoPopWindow;
import com.jenifly.zpqb.view.JButton;
import com.jenifly.zpqb.view.SigninDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jenifly.zpqb.cache.Cache.QBType;

public class FragmentDailyTest extends Fragment {

    @BindView(R.id.main_start) JButton btn_start;
    @BindView(R.id.main_progressBar) ArcProgressBar progressBar;
    @BindView(R.id.main_study_all) TextView main_study_all;
    @BindView(R.id.main_todystudy) TextView main_todystudy;
    @BindView(R.id.mian_bottomtab) LinearLayout mian_bottomtab;
    @BindView(R.id.main_todyremaining) TextView main_todyremaining;

    private int count;
    private boolean init = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dailytest, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){
        Cache.dataHelper = new DataHelper(getContext());
        if(Cache.dataHelper.getQBSettingValue("CrruentQBDataName") == null)
            new ChooseQBDialog.Builder(getActivity())
                    .setOnListItemClicked(new ChooseQBDialog.OnListItemClicked() {
                        @Override
                        public void OnClick(String ItemContext) {
                            if(Cache.CrruentQBDataName != null) {
                                if (!Cache.CrruentQBDataName.equals(QB_zhjwdrz.QB_zhjwdrz_TB_NAME) && ItemContext.equals("2017年8月先考后培复习题"))
                                    Cache.CrruentQBDataName = QB_zhjwdrz.QB_zhjwdrz_TB_NAME;
                            } else
                                Cache.CrruentQBDataName = QB_zhjwdrz.QB_zhjwdrz_TB_NAME;
                        }
                    })
                    .setOnPositiveClicked(new ChooseQBDialog.OnPositiveClicked() {
                        @Override
                        public void OnClick() {
                            if(Cache.CrruentQBDataName == null) {
                                new InfoPopWindow.Builder(getActivity(), "未选择！").build().show(mian_bottomtab);
                            }else {
                                Cache.dataHelper.addQBSetting("CrruentQBDataName", Cache.CrruentQBDataName);
                                Cache.dataHelper.addQBSetting(SqliteHelper.Setting_TodayStudy, 0);
                                Cache.dataHelper.addQBSetting(SqliteHelper.Setting_TodayStudyCount,"20");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String time = sdf.format(new Date());
                                Cache.dataHelper.addQBSetting(SqliteHelper.Setting_Today,time);
                                initView();
                            }
                        }
                    })
                    .setOnNegativeClicked(new ChooseQBDialog.OnNegativeClicked() {
                        @Override
                        public void OnClick() {
                            new InfoPopWindow.Builder(getActivity(), "取消选择！").build().show(mian_bottomtab);
                        }
                    })
                    .build().show();
        else
            initView();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(init)
            refeshView();
        if(Cache.ReloadQB)
            initView();
    }

    private void refeshView(){
        Cache.TodayStudyCount = Cache.dataHelper.getQBSettingTodayStudyCount();
        main_study_all.setText(String.valueOf(count));
        main_todystudy.setText(String.valueOf(Cache.TodayStudyCount));
        main_todyremaining.setText(String.valueOf(Cache.TodayStudyCount - Cache.dataHelper.getQBSettingTodayStudy()));
        int i = Cache.dataHelper.getQBInfoList_OK(Cache.CrruentQBDataName).size();
        progressBar.setCurrentValues(i == 0?0:i*100/count);
    }

    private void initView(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                progressBar.setMaxValues(100);
                progressBar.setCurrentValues(0);
                btn_start.setBackColor(Cache.BaseColor, ColorUtils.getColorWithAlpha(180, Cache.BaseColor));
                btn_start.setFillet(true, ScreenUtil.dip2px(getContext(),30));
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void result) {
                refeshView();
                super.onPostExecute(result);
            }

            @Override
            protected Void doInBackground(Void... params) {
                Cache.BaseColor = getResources().getColor(R.color.actionbar);
                Cache.CrruentQBDataName = Cache.dataHelper.getQBSettingValue("CrruentQBDataName");
                Cache.dataHelper.CreateQBTable(Cache.CrruentQBDataName);
                count = Cache.dataHelper.getQBInfoList(Cache.CrruentQBDataName).size();
                if(count == 0) {
                    for (Question_Info info : QB_zhjwdrz.QB_CHOICE) {
                        Cache.dataHelper.addQBInfo(Cache.CrruentQBDataName, new QB_Info(info.getId(), info.getQuestion(), info.getAnswer_true(),
                                info.getAnswer_false_1(), info.getAnswer_false_2(), info.getAnswer_false_3(), 0, 0, 0, 0, 0));
                    }
                    count = Cache.dataHelper.getQBInfoList(Cache.CrruentQBDataName).size();
                }
                Cache.TodayStudyCount = Cache.dataHelper.getQBSettingTodayStudyCount();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Cache.CrruentTime = sdf.format(new Date());
                Cache.LastTime = Cache.dataHelper.getQBSettingValue(SqliteHelper.Setting_Today);
                init = true;
                return null;
            }
        }.execute();
    }

    @OnClick({R.id.main_qbank,R.id.main_start,R.id.main_signin    ,R.id.main_menu,R.id.main_knowledge,
            R.id.main_favorites,R.id.main_worngtopic}) void click(View view){
        switch (view.getId()){
            case R.id.main_start:
                startActivity(new Intent(getActivity(),QuestionsActivity.class));
                break;
            case R.id.main_qbank:
                QBType = 0;
                startActivity(new Intent(getActivity(),QuestionBankActivity.class));
                break;
            case R.id.main_signin:
                new SigninDialog.Builder(getActivity())
                        .setOnInsertClicked(new SigninDialog.OnInsertClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {

                            }
                        }).build().show();
                break;
            case R.id.main_menu:
                startActivity(new Intent(getActivity(),SettingActivity.class));
                break;
            case R.id.main_knowledge:
                QBType = 1;
                startActivity(new Intent(getActivity(),QuestionBankActivity.class));
                break;
            case R.id.main_favorites:
                QBType = 2;
                startActivity(new Intent(getActivity(),QuestionBankActivity.class));
                break;
            case R.id.main_worngtopic:
                QBType = 3;
                startActivity(new Intent(getActivity(),QuestionBankActivity.class));
                break;
        }
    }
}
