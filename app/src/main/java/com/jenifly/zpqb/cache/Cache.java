package com.jenifly.zpqb.cache;

import android.graphics.Color;

import com.jenifly.zpqb.data.DataHelper;
import com.jenifly.zpqb.info.QB_Info;

import java.util.ArrayList;

/**
 * Created by Jenifly on 2017/8/12.
 */

public class Cache {
    public static int BaseColor = Color.parseColor("#5CADAD");
    public static int PressColor = Color.parseColor("#88D0D0D0");
    public static int ErrorColor = Color.parseColor("#FFA07A");
    public static int perssColor = Color.parseColor("#DFDFDF");
    public static int Backgroud_1= Color.parseColor("#EAEAEA");
    public static int QBType;
    public static DataHelper dataHelper;
    public static int TodayStudyCount = 0;
    public static String CrruentQBDataName;
    public static boolean ReloadQB = false;
    public static String CrruentTime;
    public static String LastTime;
    public static int RegulationId;
}
