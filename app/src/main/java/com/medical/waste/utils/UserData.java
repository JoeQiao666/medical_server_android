package com.medical.waste.utils;

import android.text.TextUtils;

import com.medical.waste.app.App;
import com.medical.waste.bean.LoginData;
import com.medical.waste.bean.User;
import com.medical.waste.common.AppConstant;

public class UserData {
    private static UserData userData;
    private LoginData loginData;

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
}
