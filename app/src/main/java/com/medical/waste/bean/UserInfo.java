package com.medical.waste.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {

    /**
     * baby : string
     * channelId : 0
     * checkMobileFlag : 0
     * clientType : string
     * createTime : 2019-06-11T08:34:51.628Z
     * gender : string
     * id : 0
     * loginStatus : 0
     * marry : string
     * mobile : string
     * token : string
     * updateTime : 2019-06-11T08:34:51.628Z
     * userName : string
     */

    private String baby;
    private int channelId;
    private int checkMobileFlag;
    private String clientType;
    private String createTime;
    private String gender;
    private long id;
    private int loginStatus;
    private String marry;
    private String mobile;
    private String token;
    private String updateTime;
    private String userName;

    public String getBaby() {
        return baby;
    }

    public void setBaby(String baby) {
        this.baby = baby;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getCheckMobileFlag() {
        return checkMobileFlag;
    }

    public void setCheckMobileFlag(int checkMobileFlag) {
        this.checkMobileFlag = checkMobileFlag;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getMarry() {
        return marry;
    }

    public void setMarry(String marry) {
        this.marry = marry;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
