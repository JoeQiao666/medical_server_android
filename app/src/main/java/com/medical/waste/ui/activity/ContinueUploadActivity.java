package com.medical.waste.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_continue_upload,
        isShowLeftBtn = false,
        toolbarTitle = R.string.upload)
public class ContinueUploadActivity extends BaseActivity {
    @BindView(R.id.total)
    TextView mTotal;

    @Override
    protected void initView() {

    }

    @OnClick(R.id.last)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.continue_add)
    void continueAdd() {
        startActivity(new Intent(this, AddActivity.class));
    }

    @OnClick(R.id.next)
    void next() {
        startActivityForResult(new Intent(this, ConfirmActivity.class), 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            startActivity(new Intent(this, UploadSuccessActivity.class));
        }
    }
}
