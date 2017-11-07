package com.jenifly.zpqb.info;

/**
 * Created by jenifly on 08.08.2017.
 */

public class Question_Info {

    private int id;
    private String question,answer_true,answer_false_1,answer_false_2,answer_false_3;

    public Question_Info(int id, String question, String answer_true, String answer_false_1, String answer_false_2, String answer_false_3) {
        this.id = id;
        this.question = question;
        this.answer_true = answer_true;
        this.answer_false_1 = answer_false_1;
        this.answer_false_2 = answer_false_2;
        this.answer_false_3 = answer_false_3;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer_true() {
        return answer_true;
    }

    public String getAnswer_false_1() {
        return answer_false_1;
    }

    public String getAnswer_false_2() {
        return answer_false_2;
    }

    public String getAnswer_false_3() {
        return answer_false_3;
    }
}
