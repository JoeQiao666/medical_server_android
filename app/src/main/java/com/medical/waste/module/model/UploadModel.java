package com.medical.waste.module.model;

import com.medical.waste.app.App;
import com.medical.waste.base.BaseLifecycleModel;
import com.medical.waste.base.BaseSubscriber;
import com.medical.waste.base.BaseTransformer;
import com.medical.waste.base.LifecycleProvider;
import com.medical.waste.base.ScheduleTransformer;
import com.medical.waste.bean.Department;
import com.medical.waste.bean.Result;
import com.medical.waste.bean.UploadData;
import com.medical.waste.callback.RequestCallback;
import com.medical.waste.module.contract.ContinueUploadContract;
import com.medical.waste.module.contract.UploadContract;

import java.util.List;

public class UploadModel extends BaseLifecycleModel implements UploadContract.Model, ContinueUploadContract.Model {
    public UploadModel(LifecycleProvider provider) {
        super(provider);
    }

    @Override
    public void getDepartmentById(String id, RequestCallback<Department> callback) {
        App.getApiService().getDepartmentById(id)
                .compose(provider.bindLifecycle())
                .compose(new BaseTransformer<>())
                .subscribe(new BaseSubscriber<>(callback));
    }

    @Override
    public void upload(List<UploadData> uploadDatas, RequestCallback<Result> callback) {
        App.getApiService().addRubbish(uploadDatas)
                .compose(provider.bindLifecycle())
                .compose(new ScheduleTransformer<>())
                .subscribe(new BaseSubscriber<>(callback));
    }
}
