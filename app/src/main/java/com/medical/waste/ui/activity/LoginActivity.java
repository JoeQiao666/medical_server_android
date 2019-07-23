package com.medical.waste.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.R;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.StartCaptcha;
import com.medical.waste.common.AppConstant;
import com.medical.waste.module.contract.UserContract;
import com.medical.waste.module.presenter.UserPresenter;


import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_login,
        isShowLeftBtn = false,
        toolbarTitle = R.string.login
)
public class LoginActivity extends BaseActivity<UserContract.Presenter> implements UserContract.View {
    @BindView(R.id.et_phone)
    AppCompatEditText mEtPhone;
    @BindView(R.id.et_code)
    AppCompatEditText mEtCode;
    @BindView(R.id.tv_get_code)
    AppCompatTextView mTvGetCode;
    @BindView(R.id.tv_login)
    AppCompatTextView mTvLogin;

    @Override
    protected void initView() {
        //是否显示返回按钮
        setShowLeftBtn(getIntent().getBooleanExtra(AppConstant.IS_SHOW_BACK_BTN, true));
    }



    @OnClick(R.id.tv_get_code)
    void getCode() {
        mPresenter.sendVertifyCode(mEtPhone.getText().toString());
    }

    @OnClick(R.id.tv_login)
    void login() {
        //登录接口
        mPresenter.checkVertifyCode(mEtPhone.getText().toString(), mEtCode.getText().toString());
    }

    @Override
    protected UserContract.Presenter initPresenter() {
        return new UserPresenter(this);
    }

    @Override
    public void onGetCode(boolean success) {
        if (success) {
            mTvGetCode.setEnabled(false);
            mPresenter.startCountdown();
        } else {
            mTvGetCode.setEnabled(true);
            mTvGetCode.setText(R.string.resend);
            toast(R.string.get_verify_code_failed);
        }
    }

    @Override
    public void onCountDown(String second) {
        //倒计时
        mTvGetCode.setText(getString(R.string.resendable_after_seconds, second));
    }

    @Override
    public void onCountDownFinish() {
        //倒计时结束
        mTvGetCode.setEnabled(true);
        mTvGetCode.setText(R.string.resend);
    }

    @Override
    public void loginSuccess() {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.URL, "http://dev02wechatshop.dnatime.com/andall-sample/binding");
        startActivity(new Intent(this, WebViewActivity.class)
                .putExtras(bundle)
        );
        finish();
    }

    @Override
    public void showStartCaptcha(StartCaptcha data) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void verifyLoginResult(boolean success) {
        if (success) {
        } else {
        }
    }
}
