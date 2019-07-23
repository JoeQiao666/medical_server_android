package com.medical.waste.utils;

import android.text.TextUtils;

import com.medical.waste.app.App;
import com.medical.waste.bean.UserInfo;
import com.medical.waste.common.AppConstant;

public class UserData {
    private static UserData userData;
    private UserInfo userInfo;

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

    public void saveUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            SpUtil.putUserString(AppConstant.USER_INFO, App.gson.toJson(userInfo));
            this.userInfo = userInfo;
        }
    }

    public UserInfo getUserInfo() {
        if (userInfo == null) {
            String userInfoJson = SpUtil.getUserString(AppConstant.USER_INFO);
            if (!TextUtils.isEmpty(userInfoJson)) {
                userInfo = App.gson.fromJson(userInfoJson, UserInfo.class);
            }
        }
        return userInfo;
    }

    public void clearUser() {
        userInfo = null;
        //清空用户相关SharedPreferences
        SpUtil.clearUser();
    }

    public String getToken() {
        if (getUserInfo() != null && !TextUtils.isEmpty(getUserInfo().getToken())) {
            return getUserInfo().getToken();
        }
        return "";
    }
}
