package com.medical.waste.bean;

import java.io.Serializable;

public class GeetesResult implements Serializable {

    /**
     * geetest_challenge : 548884d5061a6a897b08ce22a7ba355e3d
     * geetest_seccode : 0e7190a69840eabe3efb11d914ae2434|jordan
     * geetest_validate : 0e7190a69840eabe3efb11d914ae2434
     */

    private String geetest_challenge;
    private String geetest_seccode;
    private String geetest_validate;

    public String getGeetest_challenge() {
        return geetest_challenge;
    }

    public void setGeetest_challenge(String geetest_challenge) {
        this.geetest_challenge = geetest_challenge;
    }

    public String getGeetest_seccode() {
        return geetest_seccode;
    }

    public void setGeetest_seccode(String geetest_seccode) {
        this.geetest_seccode = geetest_seccode;
    }

    public String getGeetest_validate() {
        return geetest_validate;
    }

    public void setGeetest_validate(String geetest_validate) {
        this.geetest_validate = geetest_validate;
    }
}
