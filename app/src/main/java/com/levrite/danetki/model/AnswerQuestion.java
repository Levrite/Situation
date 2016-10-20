package com.levrite.danetki.model;

/**
 * Created by Michael Zaytsev on 02.10.2016.
 */

public class AnswerQuestion {

    public String author;
    public String answer;
    public String status;
    public String uid;


    public AnswerQuestion() {
    }

    public AnswerQuestion(String author, String answer, String status, String uid) {
        this.author = author;
        this.answer = answer;
        this.status = status;
        this.uid = uid;
    }

}
