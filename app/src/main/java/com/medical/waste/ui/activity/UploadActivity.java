package com.medical.waste.ui.activity;

import android.content.Intent;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;

import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_upload,
        isShowLeftBtn = false,
        toolbarTitle = R.string.upload)
public class UploadActivity extends BaseActivity {
    private String departmentId = "";
    @Override
    protected void initView() {

    }
    @OnClick(R.id.last)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.next)
    void next() {
//        if(TextUtils.isEmpty(departmentId)){
//            toast(R.string.empty_department);
//            return;
//        }
        startActivity(new Intent(this,AddActivity.class));
    }
}
