package com.levrite.danetki.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael Zaytsev on 21.09.2016.
 */

public class QuestionFirebase{

    public String author;
    public String uid;
    public String article;
    public String question;
    public String answer;
    public String date;

    public QuestionFirebase(){}

    public QuestionFirebase(String uid, String author, String article, String question, String answer, String date){
        this.author = author;
        this.uid = uid;
        this.article = article;
        this.question = question;
        this.answer = answer;
        this.date = date;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("article", article);
        result.put("question", question);
        result.put("answer", answer);
        result.put("date", date);
        return result;
    }

}
