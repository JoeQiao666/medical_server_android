package com.medical.waste.ui.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.bean.LoginData;
import com.medical.waste.common.AppConstant;
import com.medical.waste.module.contract.UserContract;
import com.medical.waste.module.presenter.UserPresenter;

import butterknife.OnClick;


@ActivityFragmentInject(contentViewId = R.layout.activity_confirm)
public class ConfirmActivity extends BaseNFCActivity<UserContract.Presenter> implements UserContract.View {

    @Override
    protected void initView() {
    }


    @Override
    protected UserContract.Presenter initPresenter() {
        return new UserPresenter(this);
    }



    @OnClick(R.id.btn_back)
    void back() {
        onBackPressed();
    }

    @Override
    public void onGetNfcId(String id) {
        if(TextUtils.isEmpty(id)){
            return;
        }
//        mPresenter.login(id);
//        mPresenter.confirm(id);
        Intent intent = new Intent();
        intent.putExtra(AppConstant.ID,id);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void loginSuccess(LoginData loginData) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.ID,loginData.user.getId());
        setResult(RESULT_OK,intent);
        finish();
    }
}
