package com.medical.waste.module.presenter;

import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.bean.Department;
import com.medical.waste.bean.Result;
import com.medical.waste.bean.UploadData;
import com.medical.waste.callback.SimpleCallback;
import com.medical.waste.module.contract.ContinueUploadContract;
import com.medical.waste.module.contract.UploadContract;
import com.medical.waste.module.model.UploadModel;

import java.util.List;

public class ContinueUploadPresenter extends BasePresenterImpl<ContinueUploadContract.View, ContinueUploadContract.Model> implements ContinueUploadContract.Presenter {


    public ContinueUploadPresenter(ContinueUploadContract.View view) {
        super(view);
        mModel = new UploadModel(this);
    }

    @Override
    public void upload(List<UploadData> uploadDatas) {
        mModel.upload(uploadDatas, new SimpleCallback<Result>(mView) {
            @Override
            public void requestSuccess(Result data) {
                mView.showUploadResult(data);
            }
        });
    }
}
