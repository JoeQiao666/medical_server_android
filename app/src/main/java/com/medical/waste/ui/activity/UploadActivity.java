package com.medical.waste.ui.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.bean.Department;
import com.medical.waste.module.contract.UploadContract;
import com.medical.waste.module.presenter.UploadPresenter;
import com.medical.waste.utils.UserData;

import org.json.JSONObject;

import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_upload,
        isShowLeftBtn = false,
        toolbarTitle = R.string.upload)
public class UploadActivity extends BaseScanActivity<UploadContract.Presenter> implements UploadContract.View {
    @Override
    protected void initView() {

    }

    @Override
    protected UploadContract.Presenter initPresenter() {
        return new UploadPresenter(this);
    }

    @OnClick(R.id.last)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.next)
    void next() {
        if (UserData.getInstance().getDepartment() == null) {
            toast(R.string.empty_department);
            return;
        }
        startActivity(new Intent(this, ScanScaleActivity2.class));
    }

    @Override
    public void onGetScanString(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(content);
            mPresenter.getDepartmentById(jsonObject.optString("id"));
        } catch (Exception e) {
            e.printStackTrace();
            toast(R.string.error_qrcode);
        }
    }

    @Override
    public void showDepartment(Department department) {
        if (department != null) {
            UserData.getInstance().clearUploadData();
            UserData.getInstance().setDepartment(department);
            startActivity(new Intent(this, ScanScaleActivity2.class));
        }
    }
}
