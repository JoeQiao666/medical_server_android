package com.medical.waste.module.presenter;

import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.callback.SimpleCallback;
import com.medical.waste.module.contract.RubbishRecordContract;
import com.medical.waste.module.model.RubbishRecordModel;

import java.util.List;

public class RubbishRecordListPresenter extends BasePresenterImpl<RubbishRecordContract.RubbishRecordListView, RubbishRecordContract.Model> implements RubbishRecordContract.RubbishRecordListPresenter {
    public RubbishRecordListPresenter(RubbishRecordContract.RubbishRecordListView view) {
        super(view);
        mModel = new RubbishRecordModel(this);
    }

    @Override
    public void getRubbishRecords(String start, String end) {
        mModel.getRubbishRecords(start, end, new SimpleCallback<List<Rubbish>>(mView) {
            @Override
            public void requestSuccess(List<Rubbish> data) {
                mView.showRubblishs(data);
            }
        });
    }
}
