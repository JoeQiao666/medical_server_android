package com.medical.waste.module.presenter;

import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.bean.RubbishType;
import com.medical.waste.callback.SimpleCallback;
import com.medical.waste.module.contract.AddContract;
import com.medical.waste.module.model.AddModel;

import java.util.List;

public class AddPresenter extends BasePresenterImpl<AddContract.View,AddContract.Model> implements AddContract.Presenter {
    public AddPresenter(AddContract.View view) {
        super(view);
        mModel = new AddModel(this);
    }

    @Override
    public void getRubbishTypes() {
        mModel.getRubbishTypes(new SimpleCallback<List<RubbishType>>(mView) {
            @Override
            public void requestSuccess(List<RubbishType> data) {
                mView.showRubbishTypes(data);
            }
        });
    }
}
