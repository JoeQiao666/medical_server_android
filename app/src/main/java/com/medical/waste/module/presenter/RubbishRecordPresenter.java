package com.medical.waste.module.presenter;

import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.callback.SimpleCallback;
import com.medical.waste.module.contract.RubbishRecordContract;
import com.medical.waste.module.model.RubbishRecordModel;

public class RubbishRecordPresenter extends BasePresenterImpl<RubbishRecordContract.View,RubbishRecordContract.Model> implements RubbishRecordContract.Presenter {
    public RubbishRecordPresenter(RubbishRecordContract.View view) {
        super(view);
        mModel = new RubbishRecordModel(this);
    }

    @Override
    public void getRubbishById(String id) {
        mModel.getRubbishById(id, new SimpleCallback<Rubbish>(mView) {
            @Override
            public void requestSuccess(Rubbish data) {
                mView.showRubblish(data);
            }
        });
    }
}
