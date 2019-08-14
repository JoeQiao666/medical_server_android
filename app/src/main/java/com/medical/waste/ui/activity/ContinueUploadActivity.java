package com.medical.waste.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.Department;
import com.medical.waste.bean.Result;
import com.medical.waste.bean.UploadData;
import com.medical.waste.common.AppConstant;
import com.medical.waste.module.contract.ContinueUploadContract;
import com.medical.waste.module.presenter.ContinueUploadPresenter;
import com.medical.waste.utils.UserData;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_continue_upload,
        isShowLeftBtn = false,
        toolbarTitle = R.string.upload)
public class ContinueUploadActivity extends BaseActivity<ContinueUploadContract.Presenter> implements ContinueUploadContract.View {
    @BindView(R.id.total)
    TextView mTotal;
     @BindView(R.id.department)
    TextView mDepartment;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private BaseQuickAdapter<UploadData, BaseViewHolder> mAdapter;

    @Override
    protected ContinueUploadContract.Presenter initPresenter() {
        return new ContinueUploadPresenter(this);
    }

    @Override
    protected void initView() {
        Department department = UserData.getInstance().getDepartment();
        if (department != null) {
            mDepartment.setText(department.getName());
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .sizeResId(R.dimen.divider)
                .colorResId(R.color.divider)
                .build());
        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<UploadData, BaseViewHolder>(R.layout.item_upload) {
            @Override
            protected void convert(BaseViewHolder helper, UploadData item) {
                helper.setText(R.id.time, item.getTime())
                        .setText(R.id.type, item.getTypeName())
                        .setText(R.id.weight, item.getWeight() + "kg");
            }
        });
        mAdapter.setNewData(UserData.getInstance().getUploadDatas());
        mTotal.setText(getString(R.string.total, mAdapter.getData().size()));
    }

    @OnClick(R.id.last)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.continue_add)
    void continueAdd() {
        startActivity(new Intent(this, ScanScaleActivity2.class));
    }

    @OnClick(R.id.next)
    void next() {
        startActivityForResult(new Intent(this, ConfirmActivity.class), 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            UserData.getInstance().setStaffId(data.getStringExtra(AppConstant.ID));
            mPresenter.upload(UserData.getInstance().getUploadDatas());
        }
    }

    @Override
    public void showUploadResult(Result result) {
        if (result.code == 0) {
            startActivity(new Intent(this, UploadSuccessActivity.class));
        } else {
            toast(result.msg);
        }
    }
}
