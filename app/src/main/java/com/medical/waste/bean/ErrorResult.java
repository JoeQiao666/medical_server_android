package com.medical.waste.bean;


/**
 * Created by jiajia on 2018/10/1.
 */
public class ErrorResult {
    public int code;
    public String msg;

    public ErrorResult() {
    }

    public ErrorResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
