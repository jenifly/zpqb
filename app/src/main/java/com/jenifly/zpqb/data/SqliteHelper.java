package com.jenifly.zpqb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jenifly.zpqb.cache.Cache;
import com.jenifly.zpqb.info.QB_Info;
import com.jenifly.zpqb.info.Question_Info;

/**
 * Created by Jenifly on 2017/7/7.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    //表名
    public static final String QBData_today = "qbata_today";
    public static final String ZJ_SettingData = "zj_settingdata";
    public static final String Setting_Key = "setting_key";
    public static final String Setting_Value = "setting_value";
    public static final String Setting_TodayStudy = "setting_todaystudy";
    public static final String Setting_TodayStudyCount = "setting_todaystudycount";
    public static final String Setting_Today = "setting_today";
    public static final String Setting_Completeday = "setting_completeday";

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE IF NOT EXISTS "+
                QBData_today + " (" +
                QB_Info.QBID + " int," +
                QB_Info.QBQusetion + " varchar," +
                QB_Info.QBAnswer_correct + " varchar," +
                QB_Info.QBAnswer_false1 + " varchar," +
                QB_Info.QBAnswer_false2 + " varchar," +
                QB_Info.QBAnswer_false3 + " varchar," +
                QB_Info.QBCollect + " int," +
                QB_Info.QBCorrect + " int," +
                QB_Info.QBError + " int," +
                QB_Info.QBDelet + " int," +
                QB_Info.QBSelect + " varchar)");
        db.execSQL( "CREATE TABLE IF NOT EXISTS "+
                ZJ_SettingData + " (" +
                Setting_Key + " varchar," +
                Setting_Value + " varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + QBData_today );
        db.execSQL( "DROP TABLE IF EXISTS " + ZJ_SettingData );
        onCreate(db);
    }
}
