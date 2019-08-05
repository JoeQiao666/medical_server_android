package com.medical.waste.ui.activity;

import android.content.Intent;
import android.os.Handler;


import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.R;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.LoginData;
import com.medical.waste.module.contract.UserContract;
import com.medical.waste.module.presenter.UserPresenter;



@ActivityFragmentInject(contentViewId = R.layout.activity_login)
public class LoginActivity extends BaseActivity<UserContract.Presenter> implements UserContract.View {
    @Override
    protected void initView() {
        new Handler().postDelayed(() -> mPresenter.login("0"), 2000);
    }


    @Override
    protected UserContract.Presenter initPresenter() {
        return new UserPresenter(this);
    }


    @Override
    public void loginSuccess(LoginData loginData) {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
