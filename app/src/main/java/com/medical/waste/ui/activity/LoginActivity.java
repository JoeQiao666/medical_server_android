package com.medical.waste.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;


import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.R;
import com.medical.waste.app.App;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.HospitalInfo;
import com.medical.waste.bean.LoginData;
import com.medical.waste.module.contract.UserContract;
import com.medical.waste.module.presenter.LoginPresenter;
import com.medical.waste.module.presenter.UserPresenter;
import com.medical.waste.utils.UserData;
import com.socks.library.KLog;


@ActivityFragmentInject(contentViewId = R.layout.activity_login)
public class LoginActivity extends BaseNFCActivity<UserContract.LoginPresenter> implements UserContract.LoginView {
    private LoginData loginData;
    @Override
    protected void initView() {
    }


    @Override
    protected UserContract.LoginPresenter initPresenter() {
        return new LoginPresenter(this);
    }


    @Override
    public void loginSuccess(LoginData loginData) {
        App.getContext().setAlias(loginData.user.getId());
        this.loginData = loginData;
        mPresenter.getHospital();
    }

    @Override
    public void onGetNfcId(String id) {
        if (TextUtils.isEmpty(id)) {
            toast(R.string.nfc_error);
            return;
        }
        KLog.e(id);
        mPresenter.login(id);
//        mPresenter.login("9946403c");
    }

    @Override
    public void showHosPital(HospitalInfo hospitalInfo) {
        loginData.hospitalInfo = hospitalInfo;
        UserData.getInstance().saveLoginData(loginData);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
