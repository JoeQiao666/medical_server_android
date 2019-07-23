package com.medical.waste.module.model;

import com.medical.waste.base.LifecycleProvider;
import com.medical.waste.bean.StartCaptcha;
import com.medical.waste.callback.RequestCallback;
import com.medical.waste.app.App;
import com.medical.waste.base.BaseLifecycleModel;
import com.medical.waste.base.BaseSubscriber;
import com.medical.waste.base.BaseTransformer;
import com.medical.waste.bean.UserInfo;
import com.medical.waste.module.contract.UserContract;
import com.medical.waste.utils.UserData;

import java.util.Map;

import io.reactivex.functions.Consumer;


public class UserModel extends BaseLifecycleModel implements UserContract.Model {

    public UserModel(LifecycleProvider provider) {
        super(provider);
    }

    @Override
    public void sendVertifyCode(String mobileNo, RequestCallback<Boolean> callback) {
        App.getApiService().sendVertifyCode(mobileNo)
                .compose(provider.bindLifecycle())
                .compose(new BaseTransformer<>())
                .subscribe(new BaseSubscriber<>(callback));
    }

    @Override
    public void getStartCaptcha(String clientType, RequestCallback<StartCaptcha> callback) {
        App.getApiService().getStartCaptcha(clientType)
                .compose(provider.bindLifecycle())
                .compose(new BaseTransformer<>())
                .subscribe(new BaseSubscriber<>(callback));

    }

    @Override
    public void verifyLogin(StartCaptcha startCaptcha, RequestCallback<String> callback) {
        App.getApiService().verifyLogin(startCaptcha)
                .compose(provider.bindLifecycle())
                .compose(new BaseTransformer<>())
                .subscribe(new BaseSubscriber<>(callback));
    }

    @Override
    public void checkVertifyCode(Map<String, String> params, RequestCallback<UserInfo> callback) {
        App.getApiService().checkVertifyCode(params)
                .compose(provider.bindLifecycle())
                .compose(new BaseTransformer<UserInfo>())
                //成功保存用户信息
                .doOnNext(new Consumer<UserInfo>() {
                    @Override
                    public void accept(UserInfo userInfo) throws Exception {
                        UserData.getInstance().saveUserInfo(userInfo);
                    }
                })
                .subscribe(new BaseSubscriber<>(callback));
    }

    @Override
    public void getUserInfo(RequestCallback<UserInfo> callback) {
        App.getApiService().getUserInfo()
                .compose(provider.bindLifecycle())
                .compose(new BaseTransformer<>())
                .subscribe(new BaseSubscriber<>(callback));
    }
}
