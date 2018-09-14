package com.jenifly.zpqb.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.jenifly.zpqb.R;
import com.jenifly.zpqb.cache.Cache;
import com.jenifly.zpqb.data.SqliteHelper;
import com.jenifly.zpqb.helper.StuBarTranslucentAPI21Helper;
import com.jenifly.zpqb.infomation.QB_zhjwdrz;
import com.jenifly.zpqb.view.AlertDialog;
import com.jenifly.zpqb.view.ChooseTSDialog;
import com.jenifly.zpqb.view.InfoPopWindow;
import com.jenifly.zpqb.view.ChooseQBDialog;
import com.jenifly.zpqb.view.JLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yarolegovich on 07.03.2017.
 */

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.setting_mian_ll) LinearLayout setting_mian_ll;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        StuBarTranslucentAPI21Helper.initState(this);
        ButterKnife.bind(this);
    }

    private boolean check = false;
    private int jint = 0;
    @OnClick({R.id.setting_back,R.id.setting_studymodel,R.id.setting_about,R.id.setting_checkupdate,
            R.id.setting_choose_qb,R.id.setting_clear_all,R.id.setting_cleartoday})void click(View view){
        switch (view.getId()){
            case R.id.setting_back:
                finish();
                break;
            case R.id.setting_studymodel:
                new ChooseTSDialog.Builder(SettingActivity.this)
                        .setOnListItemClicked(new ChooseTSDialog.OnListItemClicked() {
                            @Override
                            public void OnClick(int count) {
                                jint = count;
                            }
                        })
                        .setOnPositiveClicked(new ChooseTSDialog.OnPositiveClicked() {
                            @Override
                            public void OnClick() {
                                if(jint == 0) {
                                    new InfoPopWindow.Builder(SettingActivity.this, "未选择！").build().show(setting_mian_ll);
                                }else {
                                    Cache.dataHelper.UpdateQBSettingTodayStudyCount(jint);
                                    Cache.dataHelper.DROPTABLE(SqliteHelper.QBData_today);
                                    Cache.dataHelper.ResttingQBSettingTodayStudy();
                                    new InfoPopWindow.Builder(SettingActivity.this, "设置成功！").build().show(setting_mian_ll);
                                }
                            }
                        })
                        .setOnNegativeClicked(new ChooseTSDialog.OnNegativeClicked() {
                            @Override
                            public void OnClick() {
                                new InfoPopWindow.Builder(SettingActivity.this, "取消选择！").build().show(setting_mian_ll);
                            }
                        })
                        .build().show();
                break;
            case R.id.setting_choose_qb:
                new ChooseQBDialog.Builder(SettingActivity.this)
                        .setOnListItemClicked(new ChooseQBDialog.OnListItemClicked() {
                            @Override
                            public void OnClick(String ItemContext) {
                                if(Cache.CrruentQBDataName != null) {
                                    //   if(!Cache.CrruentQBDataName.equals(QB_zhjwdrz.QB_zhjwdrz_TB_NAME)&&ItemContext.equals("2017年8月先考后培复习题"))
                                    //        Cache.CrruentQBDataName = QB_zhjwdrz.QB_zhjwdrz_TB_NAME;
                                } else {
                                    Cache.CrruentQBDataName = QB_zhjwdrz.QB_zhjwdrz_TB_NAME;
                                    check = true;
                                }
                            }
                        })
                        .setOnPositiveClicked(new ChooseQBDialog.OnPositiveClicked() {
                            @Override
                            public void OnClick() {
                                if(Cache.CrruentQBDataName == null) {
                                    new InfoPopWindow.Builder(SettingActivity.this, "未选择！").build().show(setting_mian_ll);
                                }else {
                                    if(check) {
                                        Cache.ReloadQB = true;
                                        Cache.dataHelper.addQBSetting("CrruentQBDataName", Cache.CrruentQBDataName);
                                        Cache.dataHelper.addQBSetting(SqliteHelper.Setting_TodayStudy, 0);
                                        Cache.dataHelper.addQBSetting(SqliteHelper.Setting_TodayStudyCount,"20");
                                    }
                                    finish();
                                }
                            }
                        })
                        .setOnNegativeClicked(new ChooseQBDialog.OnNegativeClicked() {
                            @Override
                            public void OnClick() {
                                new InfoPopWindow.Builder(SettingActivity.this, "取消选择！").build().show(setting_mian_ll);
                            }
                        })
                        .build().show();
                break;
            case R.id.setting_cleartoday:
                Alerdialog("你确定要重置今日的学习记录吗？",new AlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        Cache.dataHelper.DROPTABLE(SqliteHelper.QBData_today);
                        Cache.dataHelper.ResttingQBSettingTodayStudy();
                        new InfoPopWindow.Builder(SettingActivity.this,"已重置！").build().show(setting_mian_ll);
                    }
                });
                break;
            case R.id.setting_clear_all:
                Alerdialog("你确定清空所有缓存吗（包括题库在内所有数据）?该操作不可恢复！",new AlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {

                        new InfoPopWindow.Builder(SettingActivity.this,"测试版，该功能暂未添加").build().show(setting_mian_ll);
                    }
                });
                break;
            case R.id.setting_checkupdate:
                new AlertDialog.Builder(this)
                        .setTextTitle("icon_update")
                        .setTextSubTitle("\n    更新功能暂未开发，敬请期待。    \n")
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
            case R.id.setting_about:
                new AlertDialog.Builder(this)
                        .setTextTitle("icon_about")
                        .setTextSubTitle("\n    测试版本——题库模块。\n\n    功能建议、bug提交、共同开发，请联系1439916120@qq.com \n\n    by----Jenifly\n")
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
        }
    }

    private void Alerdialog(String context, AlertDialog.OnPositiveClicked clicked){
        new AlertDialog.Builder(SettingActivity.this)
                .setTextTitle("提示")
                .setTextSubTitle("\n" + context + "\n")
                .setPositiveButtonText("确定")
                .setNegativeColor(Cache.BaseColor)
                .setPositiveColor(Cache.BaseColor)
                .setNegativeButtonText("取消")
                .setOnPositiveClicked(clicked)
                .setOnNegativeClicked(new AlertDialog.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {

                    }
                }).build().show();
    }
}
