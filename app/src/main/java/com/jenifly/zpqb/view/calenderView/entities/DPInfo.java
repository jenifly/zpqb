package com.jenifly.zpqb.view.calenderView.entities;

/**
 * 日历数据实体
 * 封装日历绘制时需要的数据
 * 
 * Entity of calendar
 *
 * @author AigeStudio 2015-03-26
 */
public class DPInfo {
    public String strY, strM, strG, strF;
    public boolean isHoliday;
    public boolean isToday, isWeekend;
    public boolean isSolarTerms, isFestival, isDeferred;
    public boolean isDecorBG;
    public boolean isDecorT;
    public boolean isLastMonth,isNaxtMonth;;
}