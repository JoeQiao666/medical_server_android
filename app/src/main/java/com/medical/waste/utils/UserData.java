package com.medical.waste.utils;

import android.text.TextUtils;

import com.medical.waste.app.App;
import com.medical.waste.bean.Department;
import com.medical.waste.bean.LoginData;
import com.medical.waste.bean.UploadData;
import com.medical.waste.bean.User;
import com.medical.waste.common.AppConstant;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private static UserData userData;
    private LoginData loginData;
    private Department department;
    private List<UploadData> uploadDatas = new ArrayList<>();
    public static UserData getInstance() {
        if (userData == null) {
            synchronized (UserData.class) {
                if (userData == null) {
                    userData = new UserData();
                }
            }
        }
        return userData;
    }

    public void saveLoginData(LoginData loginData) {
        if (loginData != null) {
            SpUtil.putUserString(AppConstant.USER_INFO, App.gson.toJson(loginData));
            this.loginData = loginData;
        }
    }

    public String getHospital() {
        if(getLoginData()!=null){
            if(loginData.hospitalInfo!=null&&!TextUtils.isEmpty(loginData.hospitalInfo.hospital)){
                return loginData.hospitalInfo.hospital;
            }
        }
        return "";
    }
    public User getUserInfo() {
        if (getLoginData() != null) {
            return loginData.user;
        }
        return null;
    }

    public LoginData getLoginData() {
        if (loginData == null) {
            String userInfoJson = SpUtil.getUserString(AppConstant.USER_INFO);
            if (!TextUtils.isEmpty(userInfoJson)) {
                loginData = App.gson.fromJson(userInfoJson, LoginData.class);
            }
        }
        return loginData;
    }

    public void clearUser() {
        App.getContext().setAlias("");
        loginData = null;
        //清空用户相关SharedPreferences
        SpUtil.clearUser();
    }

    public String getToken() {
        if (getLoginData() != null && !TextUtils.isEmpty(getLoginData().token)) {
            return getLoginData().token;
        }
        return "";
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }
    public void clearUploadData(){
        department = null;
        uploadDatas.clear();
    }

    public void addUploadData(UploadData uploadData) {
        uploadDatas.add(uploadData);
    }
    public UploadData getLastUploadData(){
        if(uploadDatas.size()>0){
            return uploadDatas.get(uploadDatas.size()-1);
        }
        return null;
    }
    public void setStaffId(String id) {
        for (UploadData data:uploadDatas){
            data.setStaffId(id);
        }
    }

    public List<UploadData> getUploadDatas() {
        return uploadDatas;
    }
}
