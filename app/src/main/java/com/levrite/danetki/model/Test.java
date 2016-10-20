package com.levrite.danetki.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael Zaytsev on 26.09.2016.
 */

public class Test {

    String uid;
    String msg;

    public Test(String uid, String msg) {
        this.uid = uid;
        this.msg = msg;
    }

    public Test(){}

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("msg", "test");
        return result;

    }

}
