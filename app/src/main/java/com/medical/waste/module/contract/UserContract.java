package com.medical.waste.module.contract;

import com.medical.waste.base.BasePresenter;
import com.medical.waste.base.BaseView;
import com.medical.waste.bean.LoginData;
import com.medical.waste.callback.RequestCallback;

public interface UserContract {
    interface Model{
        void login(String cardId, RequestCallback<LoginData> callback);
    }
    interface Presenter extends BasePresenter {
        void login(String cardId);
    }
    interface View extends BaseView {
        void loginSuccess();
    }
}
