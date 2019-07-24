package com.medical.waste.ui.activity;

import android.os.Handler;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.module.contract.UserContract;
import com.medical.waste.module.presenter.UserPresenter;

import butterknife.OnClick;


@ActivityFragmentInject(contentViewId = R.layout.activity_confirm)
public class ConfirmActivity extends BaseActivity<UserContract.Presenter> implements UserContract.View {
    @Override
    protected void initView() {
        new Handler().postDelayed(() -> mPresenter.login("0"), 2000);
    }


    @Override
    protected UserContract.Presenter initPresenter() {
        return new UserPresenter(this);
    }


    @Override
    public void loginSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.btn_back)
    void back() {
        onBackPressed();
    }
}
