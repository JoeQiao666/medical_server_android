package com.medical.waste.module.presenter;


import com.medical.waste.R;
import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.bean.HospitalInfo;
import com.medical.waste.bean.LoginData;
import com.medical.waste.callback.SimpleCallback;
import com.medical.waste.module.contract.UserContract;
import com.medical.waste.module.model.UserModel;

import java.util.List;


public class LoginPresenter extends BasePresenterImpl<UserContract.LoginView, UserModel> implements UserContract.LoginPresenter {

    public LoginPresenter(UserContract.LoginView view) {
        super(view);
        mModel = new UserModel(this);
    }


    @Override
    public void login(String cardId) {
        mModel.login(cardId, new SimpleCallback<LoginData>(mView) {
            @Override
            public void requestSuccess(LoginData data) {
                mView.loginSuccess(data);
            }
        });
    }

    @Override
    public void confirm(String cardId) {
        mModel.confirm(cardId, new SimpleCallback<LoginData>(mView) {
            @Override
            public void requestSuccess(LoginData data) {
                mView.loginSuccess(data);
            }
        });
    }

    @Override
    public void getHospital() {
        mModel.getHospital(new SimpleCallback<List<HospitalInfo>>(mView) {
            @Override
            public void requestSuccess(List<HospitalInfo> data) {
                if (data != null && data.size() > 0) {
                    mView.showHosPital(data.get(0));
                } else {
                    mView.toast(R.string.no_hospital);
                }
            }
        });
    }
}
