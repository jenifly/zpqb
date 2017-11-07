package com.jenifly.zpqb.info;

/**
 * Created by Jenifly on 2017/8/15.
 */

public class QB_Info {

    public static final String ID = "_id";
    public static final String QBID = "_qbid";
    public static final String QBQusetion = "_qbqusetion";
    public static final String QBAnswer_correct = "_qbanswer_correct";
    public static final String QBAnswer_false1 = "_qbanswer_false1";
    public static final String QBAnswer_false2 = "answer_false2";
    public static final String QBAnswer_false3 = "answer_false3";
    public static final String QBCollect = "_qbcollect";
    public static final String QBCorrect = "_qbcorrect";
    public static final String QBError = "_qberror";
    public static final String QBDelet = "_qbdelet";
    public static final String QBBookmarks = "_qbbookmarks";
    public static final String QBSelect = "_qbselect";

    private int qbid;

    public void setQbcollect(int qbcollect) {
        this.qbcollect = qbcollect;
    }

    private int qbcollect;
    private int qbcorrect;
    private int qberror;

    public void setQbdelet(int qbdelet) {
        this.qbdelet = qbdelet;
    }

    private int qbdelet;
    private int qbbookmarks;
    private String qbqusetion,answer_correct,answer_false1,answer_false2,answer_false3,qbselect;

    public QB_Info(int qbid, String qbqusetion, String answer_correct, String answer_false1, String answer_false2,
                   String answer_false3, int qbcollect, int qbcorrect, int qberror, int qbdelet, int qbbookmarks) {
        this.qbid = qbid;
        this.qbcollect = qbcollect;
        this.qbcorrect = qbcorrect;
        this.qberror = qberror;
        this.qbdelet = qbdelet;
        this.qbbookmarks = qbbookmarks;
        this.qbqusetion = qbqusetion;
        this.answer_correct = answer_correct;
        this.answer_false1 = answer_false1;
        this.answer_false2 = answer_false2;
        this.answer_false3 = answer_false3;
    }

    public String getQbselect() {
        return qbselect;
    }

    public void setQbselect(String qbselect) {
        this.qbselect = qbselect;
    }

    public QB_Info(int qbid, String qbqusetion, String answer_correct, String answer_false1, String answer_false2,
                   String answer_false3, int qbcollect, int qbcorrect, int qberror, int qbdelet, String qbselect) {
        this.qbid = qbid;
        this.qbcollect = qbcollect;
        this.qbcorrect = qbcorrect;
        this.qberror = qberror;
        this.qbdelet = qbdelet;
        this.qbqusetion = qbqusetion;
        this.qbselect = qbselect;
        this.answer_correct = answer_correct;
        this.answer_false1 = answer_false1;
        this.answer_false2 = answer_false2;
        this.answer_false3 = answer_false3;
    }

    public int getQbid() {
        return qbid;
    }

    public int getQbcollect() {
        return qbcollect;
    }

    public void setQbcorrect(int qbcorrect) {
        this.qbcorrect = qbcorrect;
    }

    public void setQberror(int qberror) {
        this.qberror = qberror;
    }

    public int getQbcorrect() {
        return qbcorrect;
    }

    public int getQberror() {
        return qberror;
    }

    public int getQbdelet() {
        return qbdelet;
    }

    public int getQbbookmarks() {
        return qbbookmarks;
    }

    public String getQbqusetion() {
        return qbqusetion;
    }

    public String getAnswer_correct() {
        return answer_correct;
    }

    public String getAnswer_false1() {
        return answer_false1;
    }

    public String getAnswer_false2() {
        return answer_false2;
    }

    public String getAnswer_false3() {
        return answer_false3;
    }
}
