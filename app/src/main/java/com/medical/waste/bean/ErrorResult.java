package com.medical.waste.bean;


/**
 * Created by jiajia on 2018/10/1.
 */
public class ErrorResult {
    public int code;
    public String message;

    public ErrorResult() {
    }

    public ErrorResult(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
