package com.medical.waste.module.model;

import com.medical.waste.base.LifecycleProvider;
import com.medical.waste.bean.LoginData;
import com.medical.waste.callback.RequestCallback;
import com.medical.waste.app.App;
import com.medical.waste.base.BaseLifecycleModel;
import com.medical.waste.base.BaseSubscriber;
import com.medical.waste.base.BaseTransformer;
import com.medical.waste.module.contract.UserContract;
import com.medical.waste.utils.UserData;

public class UserModel extends BaseLifecycleModel implements UserContract.Model {

    public UserModel(LifecycleProvider provider) {
        super(provider);
    }


    @Override
    public void login(String cardId, RequestCallback<LoginData> callback) {
        App.getApiService().login(cardId)
                .compose(provider.bindLifecycle())
                .compose(new BaseTransformer<>())
                .doOnNext(loginData -> UserData.getInstance().saveLoginData(loginData))
                .subscribe(new BaseSubscriber<>(callback));
    }
}
