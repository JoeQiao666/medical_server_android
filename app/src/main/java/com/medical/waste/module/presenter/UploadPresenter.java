package com.medical.waste.module.presenter;

import com.medical.waste.R;
import com.medical.waste.app.App;
import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.bean.Department;
import com.medical.waste.bean.RubbishType;
import com.medical.waste.callback.SimpleCallback;
import com.medical.waste.module.contract.AddContract;
import com.medical.waste.module.contract.UploadContract;
import com.medical.waste.module.model.AddModel;
import com.medical.waste.module.model.UploadModel;
import com.medical.waste.module.model.UserModel;
import com.medical.waste.utils.ToastUtils;

import java.util.List;

public class UploadPresenter extends BasePresenterImpl<UploadContract.View, UploadContract.Model> implements UploadContract.Presenter {
    public UploadPresenter(UploadContract.View view) {
        super(view);
        mModel = new UploadModel(this);
    }

    public void getDepartmentById(String id) {
        mModel.getDepartmentById(id, new SimpleCallback<Department>(mView) {
            @Override
            public void requestSuccess(Department data) {
                if (mView != null) {
                    mView.showDepartment(data);
                } else {
                    ToastUtils.toastShort(App.getContext(), R.string.error_scan);
                }
            }
        });
    }
}
