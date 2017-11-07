package com.jenifly.zpqb.view.calenderView.bizs.calendars;

import android.text.TextUtils;

import com.jenifly.zpqb.view.calenderView.entities.DPInfo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 日期管理器
 * The manager of date picker.
 *
 * @author AigeStudio 2015-06-12
 */
public final class DPCManager {
    private static final HashMap<Integer, HashMap<Integer, DPInfo[][]>> DATE_CACHE = new HashMap<>();

    private List<DecorDate> DECOR_CACHE_BG = new LinkedList<>();
    private List<DecorDate> DECOR_CACHE_T = new LinkedList<>();

    private static DPCManager sManager;

    private DPCalendar c;

    private DPCManager() {
        // 默认显示为中文日历
        String locale = Locale.getDefault().getCountry().toLowerCase();
        if (locale.equals("cn")) {
            initCalendar(new DPCNCalendar());
        } else {
            initCalendar(new DPUSCalendar());
        }
    }

    /**
     * 获取月历管理器
     * Get calendar manager
     *
     * @return 月历管理器
     */
    public static DPCManager getInstance() {
        if (null == sManager) {
            sManager = new DPCManager();
        }
        return sManager;
    }

    /**
     * 初始化日历对象
     * <p/>
     * Initialization Calendar
     *
     * @param c ...
     */
    public void initCalendar(DPCalendar c) {
        this.c = c;
    }

    /**
     * 设置有背景标识物的日期
     * <p/>
     * Set date which has decor of background
     *
     * @param date 日期列表 List of date
     */
    public void setDecorBG(List<String> date) {
        DECOR_CACHE_BG = setDecor(date);
    }

    /**
     * 设置顶部有标识物的日期
     * <p/>
     * Set date which has decor on Top
     *
     * @param date 日期列表 List of date
     */
    public void setDecorT(List<String> date) {
        DECOR_CACHE_T = setDecor(date);
    }

    /**
     * 获取指定年月的日历对象数组
     *
     * @param year  公历年
     * @param month 公历月
     * @return 日历对象数组 该数组长度恒为6x7 如果某个下标对应无数据则填充为null
     */
    public DPInfo[][] obtainDPInfo(int year, int month) {
        HashMap<Integer, DPInfo[][]> dataOfYear = DATE_CACHE.get(year);
        if (null != dataOfYear && dataOfYear.size() != 0) {
            DPInfo[][] dataOfMonth = dataOfYear.get(month);
            if (dataOfMonth != null) {
                return dataOfMonth;
            }
            dataOfMonth = buildDPInfo(year, month);
            dataOfYear.put(month, dataOfMonth);
            return dataOfMonth;
        }
        if (null == dataOfYear) dataOfYear = new HashMap<>();
        DPInfo[][] dataOfMonth = buildDPInfo(year, month);
        dataOfYear.put((month), dataOfMonth);
        DATE_CACHE.put(year, dataOfYear);
        return dataOfMonth;
    }

    private List<DecorDate> setDecor(List<String> date) {
        List<DecorDate> cache = new LinkedList<>();
        for (String str : date) {
            DecorDate decorDate = new DecorDate();
            String[] tmp = str.split("-");
            decorDate.year = tmp[0];
            decorDate.month = tmp[1];
            decorDate.day = tmp[2];
            cache.add(decorDate);
        }
       return cache;
    }

    private DPInfo[][] buildDPInfo(int year, int month) {
        DPInfo[][] info = new DPInfo[6][7];
        String[][] strG = c.buildMonthG(year, month);
        String[][] strF = c.buildMonthFestival(year, month);
        Set<String> strHoliday = c.buildMonthHoliday(year, month);
        Set<String> strWeekend = c.buildMonthWeekend(year, month);
        int lastyear = year,nastyear = year, lastmonth = month, naxtmonth = month;
        if(month == 1){
            lastmonth = 12;
            lastyear--;
        }else
            lastmonth--;
        if(month == 12){
            naxtmonth = 1;
            nastyear++;
        }else
            naxtmonth++;
        List<DecorDate> decorBG = DECOR_CACHE_BG;
        List<DecorDate> decorT = DECOR_CACHE_T;
        for (int i = 0; i < info.length; i++) {
            for (int j = 0; j < info[i].length; j++) {
                DPInfo tmp = new DPInfo();
                tmp.strG = strG[i][j];
                if(!TextUtils.isEmpty(tmp.strG)){
                    if(i == 0 && Integer.valueOf(strG[i][j]) > strG[i].length){
                        tmp.isLastMonth = true;
                    }
                    if(i > 2 && Integer.valueOf(strG[i][j]) < strG[i].length){
                        tmp.isNaxtMonth = true;
                    }
                }
                if (c instanceof DPCNCalendar)
                    tmp.strF = strF[i][j].replace("F", "");
                else
                    tmp.strF = strF[i][j];

                if (!TextUtils.isEmpty(tmp.strG) && null != decorT ) {
                    for(DecorDate decor : decorT){
                        if(tmp.isLastMonth){
                            if (decor.year.equals(String.valueOf(lastyear)) &&
                                    decor.month.equals(String.valueOf(lastmonth)) && decor.day.equals(tmp.strG)) {
                                tmp.isDecorT = true;
                            }
                        }else if(tmp.isNaxtMonth){
                            if (decor.year.equals(String.valueOf(nastyear)) &&
                                    decor.month.equals(String.valueOf(naxtmonth)) && decor.day.equals(tmp.strG)) {
                                tmp.isDecorT = true;
                            }
                        }else {
                            if (decor.year.equals(String.valueOf(year)) &&
                                    decor.month.equals(String.valueOf(month)) && decor.day.equals(tmp.strG)) {
                                tmp.isDecorT = true;
                            }
                        }
                    }
                }


                if(!TextUtils.isEmpty(tmp.strG) && strHoliday.contains(tmp.strG))
                    tmp.isHoliday = true;


                if (!TextUtils.isEmpty(tmp.strG))
                    tmp.isToday = c.isToday(year, month, Integer.valueOf(tmp.strG));


                if (strWeekend.contains(tmp.strG))
                    tmp.isWeekend = true;


                if (c instanceof DPCNCalendar) {
                    if (!TextUtils.isEmpty(tmp.strG)) tmp.isSolarTerms =
                            ((DPCNCalendar) c).isSolarTerm(year, month, Integer.valueOf(tmp.strG));
                    if (!TextUtils.isEmpty(strF[i][j]) && strF[i][j].endsWith("F"))
                        tmp.isFestival = true;
                    if (!TextUtils.isEmpty(tmp.strG))
                        tmp.isDeferred = ((DPCNCalendar) c)
                                .isDeferred(year, month, Integer.valueOf(tmp.strG));
                } else {
                    tmp.isFestival = !TextUtils.isEmpty(strF[i][j]);
                }
                if (null != decorBG && decorBG.contains(tmp.strG)) tmp.isDecorBG = true;

             //   else if (null != decorTLastMonth && decorTLastMonth.contains(tmp.strG)) tmp.isDecorT = true;
            //    else if (null != decorTNaxtMonth && decorTNaxtMonth.contains(tmp.strG)) tmp.isDecorT = true;
                info[i][j] = tmp;
            }
        }
        return info;
    }
   private class DecorDate{
        private String year;
        private String month;
        private String day;
    }
}
