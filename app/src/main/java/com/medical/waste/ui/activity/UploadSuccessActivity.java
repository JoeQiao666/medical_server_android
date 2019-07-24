package com.medical.waste.ui.activity;

import android.content.Intent;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;

import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_upload_success,
    isShowDarkStatusBarIcon = true
)
public class UploadSuccessActivity extends BaseActivity {
    @Override
    protected void initView() {

    }
    @OnClick(R.id.back_home)
    void backHome(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
