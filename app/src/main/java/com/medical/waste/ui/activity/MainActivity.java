package com.medical.waste.ui.activity;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.R;
import com.medical.waste.bean.User;
import com.medical.waste.utils.ActivityUtils;
import com.medical.waste.utils.UserData;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @BindView(R.id.username)
    AppCompatTextView mUserName;

    @Override
    protected void initView() {
        User user = UserData.getInstance().getUserInfo();
        if (user != null) {
            mUserName.setText(user.getUsername());
        }
    }

    @OnClick({R.id.upload, R.id.storage, R.id.deposit, R.id.history, R.id.traceback, R.id.logout})
    void onClickBtn(View view) {
        switch (view.getId()) {
            case R.id.upload:
                break;
            case R.id.storage:
                break;
            case R.id.deposit:
                break;
            case R.id.history:
                break;
            case R.id.traceback:
                break;
            case R.id.logout:
                UserData.getInstance().clearUser();
                ActivityUtils.getInstance().finishAll();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

}
