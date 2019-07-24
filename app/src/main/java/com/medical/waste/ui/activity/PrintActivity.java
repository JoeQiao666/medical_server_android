package com.medical.waste.ui.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;

import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_print,
        isShowLeftBtn = false,
        toolbarTitle = R.string.print_tag)
public class PrintActivity extends BaseActivity {
    @Override
    protected void initView() {

    }
    @OnClick(R.id.last)
    void last() {
        startActivity(new Intent(this,ContinueUploadActivity.class));
    }

    @OnClick(R.id.next)
    void next() {
        // TODO: 2019-07-24 打印标签
    }
}
