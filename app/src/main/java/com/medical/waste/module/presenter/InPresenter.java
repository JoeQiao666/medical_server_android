package com.medical.waste.module.presenter;

import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.bean.InListData;
import com.medical.waste.bean.Result;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.bean.RubbishType;
import com.medical.waste.callback.SimpleCallback;
import com.medical.waste.module.contract.AddContract;
import com.medical.waste.module.contract.InOutContract;
import com.medical.waste.module.model.AddModel;
import com.medical.waste.module.model.InOutModel;

import java.util.List;
import java.util.Map;

public class InPresenter extends BasePresenterImpl<InOutContract.InView,InOutContract.Model> implements InOutContract.InPresenter {
    public InPresenter(InOutContract.InView view) {
        super(view);
        mModel = new InOutModel(this);
    }


    @Override
    public void getRubbishs(Map<String, String> params) {
        mModel.getRubbishs(params,new SimpleCallback<InListData>(mView) {
            @Override
            public void requestSuccess(InListData data) {
                mView.showRubbish(data);
            }
        });
    }

    @Override
    public void store(Map<String, String> params) {
        mModel.store(params,new SimpleCallback<Result>(mView) {
            @Override
            public void requestSuccess(Result data) {
                mView.showStoreResult(data);
            }
        });
    }
}
