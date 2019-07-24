package com.medical.waste.module.model;

import com.medical.waste.app.App;
import com.medical.waste.base.BaseLifecycleModel;
import com.medical.waste.base.BaseSubscriber;
import com.medical.waste.base.BaseTransformer;
import com.medical.waste.base.LifecycleProvider;
import com.medical.waste.bean.RubbishType;
import com.medical.waste.callback.RequestCallback;
import com.medical.waste.module.contract.AddContract;

import java.util.List;

public class AddModel extends BaseLifecycleModel implements AddContract.Model {

    public AddModel(LifecycleProvider provider) {
        super(provider);
    }

    @Override
    public void getRubbishTypes(RequestCallback<List<RubbishType>> callback) {
        App.getApiService().getTypes()
                .compose(provider.bindLifecycle())
                .compose(new BaseTransformer<>())
                .subscribe(new BaseSubscriber<>(callback));
    }
}
