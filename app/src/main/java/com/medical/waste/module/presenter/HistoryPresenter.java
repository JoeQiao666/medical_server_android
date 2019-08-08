package com.medical.waste.module.presenter;

import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.bean.InListData;
import com.medical.waste.bean.Result;
import com.medical.waste.callback.SimpleCallback;
import com.medical.waste.module.contract.InOutContract;
import com.medical.waste.module.model.InOutModel;

import java.util.Map;

public class HistoryPresenter extends BasePresenterImpl<InOutContract.View,InOutContract.Model> implements InOutContract.Presenter {
    public HistoryPresenter(InOutContract.View view) {
        super(view);
        mModel = new InOutModel(this);
    }


    @Override
    public void getRubbishs(Map<String, String> params) {
        mModel.getRubbishs(params,new SimpleCallback<InListData>(mView) {
            @Override
            public void beforeRequest() {

            }

            @Override
            public void requestSuccess(InListData data) {
                mView.showRubbish(data);
            }
        });
    }
}
