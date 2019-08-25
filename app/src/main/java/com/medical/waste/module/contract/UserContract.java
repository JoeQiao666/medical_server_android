package com.medical.waste.module.contract;

import com.medical.waste.base.BasePresenter;
import com.medical.waste.base.BaseView;
import com.medical.waste.bean.HospitalInfo;
import com.medical.waste.bean.LoginData;
import com.medical.waste.callback.RequestCallback;

import java.util.List;

public interface UserContract {
    interface Model{
        void login(String cardId, RequestCallback<LoginData> callback);
        void getHospital(RequestCallback<List<HospitalInfo>> callback);
        void confirm(String cardId, RequestCallback<LoginData> callback);
    }
    interface Presenter extends BasePresenter {
        void login(String cardId);
        void confirm(String cardId);
    }
    interface LoginPresenter extends Presenter {
        void getHospital();
    }
    interface View extends BaseView {
        void loginSuccess(LoginData loginData);
    }
    interface LoginView extends View{
        void showHosPital(HospitalInfo hospitalInfo);
    }
}
