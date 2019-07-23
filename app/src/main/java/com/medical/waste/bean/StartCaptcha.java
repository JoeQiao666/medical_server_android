package com.medical.waste.bean;

import java.io.Serializable;

public class StartCaptcha implements Serializable {

    /**
     *
     * 接收
     * challenge : string
     * gt : string
     * success : string
     * uuid : string
     *
     * 请求需要
     * {
     *   "challenge": "string",
     *   "clientType": "string",
     *   "ipAddress": "string",
     *   "secCode": "string",
     *   "uuid": "string",
     *   "validate": "string"
     * }
     */

    private String challenge;
    private String gt;
    private String success;
    private String uuid;

    private String clientType;
    private String ipAddress;
    private String secCode;
    private String validate;


    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public String getGt() {
        return gt;
    }

    public void setGt(String gt) {
        this.gt = gt;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSecCode() {
        return secCode;
    }

    public void setSecCode(String secCode) {
        this.secCode = secCode;
    }


    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }
}
