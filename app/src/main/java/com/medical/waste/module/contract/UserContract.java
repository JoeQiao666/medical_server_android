package com.medical.waste.module.contract;

import com.medical.waste.base.BasePresenter;
import com.medical.waste.base.BaseView;
import com.medical.waste.bean.StartCaptcha;
import com.medical.waste.callback.RequestCallback;
import com.medical.waste.bean.UserInfo;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Map;

public interface UserContract {
    interface Model{
        void getStartCaptcha(String clientType, RequestCallback<StartCaptcha> callback);
        void verifyLogin(StartCaptcha startCaptcha, RequestCallback<String> callback);
        void sendVertifyCode(String mobileNo, RequestCallback<Boolean> callback);
        void checkVertifyCode(Map<String,String> params,RequestCallback<UserInfo> callback);
        void getUserInfo(RequestCallback<UserInfo> callback);
    }
    interface Presenter extends BasePresenter {
        void sendVertifyCode(String mobileNo);
        //获取极验验证码
        void getStartCaptcha();
        void verifyLogin(StartCaptcha startCaptcha);
        void startCountdown();
        void checkVertifyCode(String mobileNo,String vertifyCode);
    }
    interface View extends BaseView {
        void onGetCode(boolean success);
        void onCountDown(String second);
        void onCountDownFinish();
        void loginSuccess();
        void showStartCaptcha(StartCaptcha data);
        void verifyLoginResult(boolean success);
    }
}
