package com.medical.waste.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseFullScreenActivity;
import com.medical.waste.common.AppConstant;
import com.medical.waste.module.contract.SplashContract;
import com.medical.waste.module.presenter.SplashPresenter;
import com.medical.waste.utils.UserData;

import butterknife.BindView;

@ActivityFragmentInject(contentViewId = R.layout.activity_splash,
        statusBackground = R.color.transparent,
        isShowDarkStatusBarIcon = false
)
public class SplashActivity extends BaseFullScreenActivity<SplashContract.Presenter> implements SplashContract.View {

    @Override
    protected void initView() {
        //开启倒计时
        mPresenter.startCountdown();
    }

    @Override
    protected SplashContract.Presenter initPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    public void onCountDown(String second) {
    }

    @Override
    public void onCountDownFinish() {
        if (UserData.getInstance().getUserInfo() == null) {
            //未登录
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            //已登录
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
