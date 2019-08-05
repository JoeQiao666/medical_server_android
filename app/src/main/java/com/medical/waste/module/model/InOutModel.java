package com.medical.waste.module.model;

import com.medical.waste.app.App;
import com.medical.waste.base.BaseLifecycleModel;
import com.medical.waste.base.BaseSubscriber;
import com.medical.waste.base.BaseTransformer;
import com.medical.waste.base.LifecycleProvider;
import com.medical.waste.base.ScheduleTransformer;
import com.medical.waste.bean.InListData;
import com.medical.waste.bean.Result;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.bean.RubbishType;
import com.medical.waste.callback.RequestCallback;
import com.medical.waste.module.contract.AddContract;
import com.medical.waste.module.contract.InOutContract;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Function;

public class InOutModel extends BaseLifecycleModel implements InOutContract.Model {

    public InOutModel(LifecycleProvider provider) {
        super(provider);
    }

    @Override
    public void getRubbishs(Map<String, String> params, RequestCallback<InListData> callback) {
        App.getApiService().getRubbish(params)
                .compose(provider.bindLifecycle())
                .compose(new BaseTransformer<>())
                .subscribe(new BaseSubscriber<>(callback));
    }

    @Override
    public void store(Map<String, String> params, RequestCallback<Result> callback) {
        App.getApiService().store(params)
                .compose(provider.bindLifecycle())
                .compose(new ScheduleTransformer<>())
                .subscribe(new BaseSubscriber<>(callback));
    }

    @Override
    public void recycle(Map<String, String> params, RequestCallback<Result> callback) {
        App.getApiService().recycle(params)
                .compose(provider.bindLifecycle())
                .compose(new ScheduleTransformer<>())
                .subscribe(new BaseSubscriber<>(callback));
    }
}
