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
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.second)
    TextView mSecond;

    @Override
    protected void initView() {
        mImage.setImageURI(Uri.parse("https://photo.16pic.com/00/78/54/16pic_7854241_b.jpg"));
        //开启倒计时
        mPresenter.startCountdown();
    }

    @Override
    protected SplashContract.Presenter initPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    public void onCountDown(String second) {
        mSecond.setText(second);
    }

    @Override
    public void onCountDownFinish() {
        if (UserData.getInstance().getUserInfo() == null) {
            //未登录
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            //已登录
            Bundle bundle = new Bundle();
            bundle.putString(AppConstant.URL, "http://dev02wechatshop.dnatime.com/andall-report/demo");
            startActivity(new Intent(this, WebViewActivity.class)
                    .putExtras(bundle)
            );
        }
        finish();
    }
}
