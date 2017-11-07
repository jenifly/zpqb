package com.jenifly.zpqb.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jenifly.zpqb.info.QB_Info;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jenifly on 2017/7/7.
 */

public class DataHelper {
    // 数据库名称
    private static String DB_NAME = "zpqb.db";
    // 数据库版本
    private static int DB_VERSION = 2;
    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    public DataHelper(Context context) {
        dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void Close() {
        db.close();
        dbHelper.close();
    }

    public void CreateQBTable(String TB_NAME){
        db.execSQL( "CREATE TABLE IF NOT EXISTS "+
                TB_NAME + " ("+  QB_Info.ID + " integer primary key autoincrement," +
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
                QB_Info.QBBookmarks + " int)");
    }

    // 获取users表中的UserID、Access Token、Access Secret的记录
    public ArrayList<QB_Info> getQBInfoList(String tabelname) {
        ArrayList<QB_Info> infos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+ tabelname + " ORDER BY "+ QB_Info.QBID, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QB_Info info = new QB_Info(Integer.parseInt(cursor.getString(1)), cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),
                    cursor.getInt(7), cursor.getInt(8), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11));
            infos.add(info);
            cursor.moveToNext();
        }
        cursor.close();
        return infos;
    }

    public ArrayList<QB_Info> getQBTodayInfoList() {
        ArrayList<QB_Info> infos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+ SqliteHelper.QBData_today + " ORDER BY "+ QB_Info.QBID, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QB_Info info = new QB_Info(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),
                    cursor.getInt(6), cursor.getInt(7), cursor.getInt(8), cursor.getInt(9), cursor.getString(10));
            infos.add(info);
            cursor.moveToNext();
        }
        cursor.close();
        return infos;
    }

    public ArrayList<QB_Info> getQBInfoList_Today(String tabelname, int size){
        ArrayList<QB_Info> infos = new ArrayList();
        ArrayList<QB_Info> minfos = new ArrayList();
        ArrayList<QB_Info> baseinfos = getQBInfoList(tabelname);
        ArrayList<QB_Info> questionList = new ArrayList<>();
        for (QB_Info info:baseinfos){
            if((info.getQbcorrect()>2&&info.getQberror()==0) || info.getQbdelet()>0 ||
                    (info.getQberror()+info.getQbcorrect()>4&&(float)(info.getQbcorrect()/(info.getQberror()+info.getQbcorrect()))>=0.8f)){
                minfos.add(info);
            }else {
                infos.add(info);
            }
        }
        if(infos.size()>size){
            Collections.shuffle(infos);
            for (int i = 0; i < size; i++)
                questionList.add(infos.get(i));
        }else {
            for (int i = 0; i < infos.size(); i++)
                questionList.add(infos.get(i));
            Collections.shuffle(minfos);
            for (int i = 0;i<size-infos.size();i++)
                questionList.add(infos.get(i));
        }
        return questionList;
    }

    public ArrayList<QB_Info> getQBInfoList_Error(String tabelname){
        ArrayList<QB_Info> infos = new ArrayList();
        for (QB_Info info:getQBInfoList(tabelname)){
            if((info.getQbdelet()==0&&info.getQberror()>0) ||(info.getQberror()>0&&((float)info.getQbcorrect()/(float)(info.getQberror()+info.getQbcorrect()))<0.8f))
                infos.add(info);
        }
        return infos;
    }

    public ArrayList<QB_Info> getQBInfoList_Collcet(String tabelname){
        ArrayList<QB_Info> infos = new ArrayList();
        for (QB_Info info:getQBInfoList(tabelname)){
            if(info.getQbcollect() == 1)
                infos.add(info);
        }
        return infos;
    }

    public ArrayList<QB_Info> getQBInfoList_OK(String tabelname){
        ArrayList<QB_Info> infos = new ArrayList<>();
        for (QB_Info info:getQBInfoList(tabelname)){
            if((info.getQbcorrect()>2&&info.getQberror()==0) || info.getQbdelet()>0 ||
                    (info.getQberror()+info.getQbcorrect()>4&&(float)(info.getQbcorrect()/(info.getQberror()+info.getQbcorrect()))>=0.8f)){
                infos.add(info);
            }
        }
        return infos;
    }

    public void DROPTABLE (String tabelname){
        db.execSQL("DELETE FROM "+ tabelname);
    }

    public ArrayList<QB_Info> getBaseInfoListBy(String tabelname, String key,String value) {
        ArrayList<QB_Info> infos = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ tabelname + " WHERE "+ key+"=" + value, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QB_Info info = new QB_Info(Integer.parseInt(cursor.getString(1)), cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),
                    cursor.getInt(7), cursor.getInt(8), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11));
            infos.add(info);
            cursor.moveToNext();
        }
        cursor.close();
        return infos;
    }


    public QB_Info getInfoById(String tabelname, int qbId) {
        Cursor cursor = db.query(tabelname, null, QB_Info.QBID
                + "=?", new String[]{String.valueOf(qbId)}, null, null, null );
        if(cursor.moveToFirst()){
            QB_Info info = new QB_Info(Integer.parseInt(cursor.getString(1)), cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),
                    cursor.getInt(7), cursor.getInt(8), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11));
        }
        cursor.close();
        return null;
    }

    public QB_Info getBaseInfo(String tabelname, String key,String value) {
        Cursor cursor = db.query(tabelname, null, key
                + "=?", new String[]{value}, null, null, null );
        if(cursor.moveToFirst()){
            QB_Info info = new QB_Info(Integer.parseInt(cursor.getString(1)), cursor.getString(2),
                    cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),
                    cursor.getInt(7), cursor.getInt(8), cursor.getInt(9), cursor.getInt(10), cursor.getInt(11));
        }
        cursor.close();
        return null;
    }

    public int updateBaseInfo(String tabelname, int qbId, ArrayList<String[]> list) {
        ContentValues values = new ContentValues();
        for(String[] strs : list){
            values.put(strs[0], strs[1]);
        }
        int i = db.update(tabelname, values, QB_Info.QBID + "=?" , new String[]{String.valueOf(qbId)});
        return i;
    }

    public void updateBaseInfo(String tabelname, int qbId, String[] strs) {
        ContentValues values = new ContentValues();
        values.put(strs[0], strs[1]);
        db.update(tabelname, values, QB_Info.QBID + "=?" , new String[]{String.valueOf(qbId)});
    }

    public void updateBaseInfo(String tabelname, int qbId, String key, int value) {
        ContentValues values = new ContentValues();
        values.put(key,value);
        db.update(tabelname, values, QB_Info.QBID + "=?" , new String[]{String.valueOf(qbId)});
    }

    public void updateTodayInfo(String tabelname, QB_Info info) {
       // DataHelper(tabelname);
        ContentValues values = new ContentValues();
        values.put(QB_Info.QBCollect, info.getQbcollect());
        values.put(QB_Info.QBCorrect, info.getQbcorrect());
        values.put(QB_Info.QBError,info.getQberror());
        values.put(QB_Info.QBDelet, info.getQbdelet());
        values.put(QB_Info.QBSelect, info.getQbselect());
        db.update(tabelname, values, QB_Info.QBID + "=?" , new String[]{String.valueOf(info.getQbid())});
    }

    // 添加users表的记录
    public void addQBInfo(String tabelname, QB_Info info) {
        ContentValues values = new ContentValues();
        values.put(QB_Info.QBID, info.getQbid());
        values.put(QB_Info.QBQusetion, info.getQbqusetion());
        values.put(QB_Info.QBAnswer_correct, info.getAnswer_correct());
        values.put(QB_Info.QBAnswer_false1, info.getAnswer_false1());
        values.put(QB_Info.QBAnswer_false2, info.getAnswer_false2());
        values.put(QB_Info.QBAnswer_false3, info.getAnswer_false3());
        values.put(QB_Info.QBCollect, info.getQbcollect());
        values.put(QB_Info.QBCorrect, info.getQbcorrect());
        values.put(QB_Info.QBError,info.getQberror());
        values.put(QB_Info.QBDelet, info.getQbdelet());
        if(tabelname.equals(SqliteHelper.QBData_today))
            values.put(QB_Info.QBSelect, info.getQbselect());
        else
            values.put(QB_Info.QBBookmarks, info.getQbbookmarks());
        db.insert(tabelname, null, values);
    }

    public void addQBSetting(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.Setting_Key, key);
        values.put(SqliteHelper.Setting_Value, value);
        db.insert(SqliteHelper.ZJ_SettingData, null, values);
    }

    public void addQBSetting(String key, int value) {
        addQBSetting(key, String.valueOf(value));
    }

    public String getQBSettingValue(String key) {
        Cursor cursor = db.query(SqliteHelper.ZJ_SettingData, null, SqliteHelper.Setting_Key
                + "=?", new String[]{key}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(1);
        }
        cursor.close();
        return null;
    }

    public void AddQBSettingTodayStudy() {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.Setting_Value, getQBSettingTodayStudy()+1);
        db.update(SqliteHelper.ZJ_SettingData, values, SqliteHelper.Setting_Key + "=?" ,
                new String[]{SqliteHelper.Setting_TodayStudy});
    }

    public void ResttingQBSettingTodayStudy() {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.Setting_Value, 0);
        db.update(SqliteHelper.ZJ_SettingData, values, SqliteHelper.Setting_Key + "=?" ,
                new String[]{SqliteHelper.Setting_TodayStudy});
    }

    public int getQBSettingTodayStudyCount() {
        Cursor cursor = db.query(SqliteHelper.ZJ_SettingData, null, SqliteHelper.Setting_Key
                + "=?", new String[]{SqliteHelper.Setting_TodayStudyCount}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(1);
        }
        cursor.close();
        return 0;
    }

    public void UpdateQBSettingValue(String key, String value){
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.Setting_Value, value);
        db.update(SqliteHelper.ZJ_SettingData, values, SqliteHelper.Setting_Key + "=?" ,
                new String[]{key});
    }

    public void UpdateQBSettingTodayStudyCount(int count) {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.Setting_Value, count);
        db.update(SqliteHelper.ZJ_SettingData, values, SqliteHelper.Setting_Key + "=?" ,
                new String[]{SqliteHelper.Setting_TodayStudyCount});
    }

    public int getQBSettingTodayStudy() {
        Cursor cursor = db.query(SqliteHelper.ZJ_SettingData, null, SqliteHelper.Setting_Key
                + "=?", new String[]{SqliteHelper.Setting_TodayStudy}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(1);
        }
        cursor.close();
        return 0;
    }
}
