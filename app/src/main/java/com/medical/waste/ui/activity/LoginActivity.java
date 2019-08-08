package com.medical.waste.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;


import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.R;
import com.medical.waste.app.App;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.LoginData;
import com.medical.waste.module.contract.UserContract;
import com.medical.waste.module.presenter.UserPresenter;



@ActivityFragmentInject(contentViewId = R.layout.activity_login)
public class LoginActivity extends BaseNFCActivity<UserContract.Presenter> implements UserContract.View {
    @Override
    protected void initView() {
    }


    @Override
    protected UserContract.Presenter initPresenter() {
        return new UserPresenter(this);
    }


    @Override
    public void loginSuccess(LoginData loginData) {
        App.getContext().setAlias(loginData.user.getId());
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    public void onGetNfcId(String id) {
        if(TextUtils.isEmpty(id)){
            toast(R.string.nfc_error);
            return;
        }
        mPresenter.login("0");
    }
}
