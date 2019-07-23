package com.medical.waste.module.presenter;


import android.text.TextUtils;

import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.base.ScheduleTransformer;
import com.medical.waste.bean.ErrorResult;
import com.medical.waste.bean.StartCaptcha;
import com.medical.waste.callback.SimpleCallback;
import com.medical.waste.utils.Utils;
import com.medical.waste.R;
import com.medical.waste.bean.UserInfo;
import com.medical.waste.common.AppConstant;
import com.medical.waste.module.contract.UserContract;
import com.medical.waste.module.model.UserModel;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class UserPresenter extends BasePresenterImpl<UserContract.View, UserModel> implements UserContract.Presenter {

    public UserPresenter(UserContract.View view) {
        super(view);
        mModel = new UserModel(this);
    }

    @Override
    public void sendVertifyCode(String mobileNo) {
        if (!Utils.isChinaPhoneLegal(mobileNo)) {
            mView.toast(R.string.phone_valid_error);
            return;
        }
        mModel.sendVertifyCode(mobileNo, new SimpleCallback<Boolean>(mView) {
            @Override
            public void requestSuccess(Boolean success) {
                mView.onGetCode(success);
            }
        });
    }

    @Override
    public void getStartCaptcha() {
        mModel.getStartCaptcha(AppConstant.APP, new SimpleCallback<StartCaptcha>(mView) {
            @Override
            public void requestSuccess(StartCaptcha data) {
                mView.showStartCaptcha(data);
            }
        });
    }

    @Override
    public void verifyLogin(StartCaptcha startCaptcha) {
        mModel.verifyLogin(startCaptcha, new SimpleCallback<String>(mView) {
            @Override
            public void requestError(ErrorResult error) {
                super.requestError(error);
                mView.verifyLoginResult(false);
            }

            @Override
            public void requestSuccess(String data) {
                mView.verifyLoginResult(true);
            }
        });
    }

    @Override
    public void startCountdown() {
        int totalSeconds = 60;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .compose(mView.bindLifecycle())
                .take(totalSeconds + 1)
                .map(aLong -> totalSeconds - aLong)
                .compose(new ScheduleTransformer<>())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        mView.onCountDown(String.valueOf(aLong));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //完成倒计时跳转
                        mView.onCountDownFinish();
                    }
                });
    }

    @Override
    public void checkVertifyCode(String mobileNo, String vertifyCode) {
        if (!Utils.isChinaPhoneLegal(mobileNo)) {
            mView.toast(R.string.phone_valid_error);
            return;
        }
        if (TextUtils.isEmpty(vertifyCode)) {
            mView.toast(R.string.vertifycode_valid_error);
            return;
        }
        Map<String, String> params = Utils.getDefaultParams();
        params.put(AppConstant.MOBILE_NO, mobileNo);
        params.put(AppConstant.CODE, vertifyCode);
        mModel.checkVertifyCode(params, new SimpleCallback<UserInfo>(mView) {
            @Override
            public void requestSuccess(UserInfo data) {
                mView.loginSuccess();
            }
        });
    }
}
