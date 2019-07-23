package com.medical.waste.callback;

import com.medical.waste.R;
import com.medical.waste.app.App;
import com.medical.waste.base.BaseView;
import com.medical.waste.bean.ErrorResult;

/**
 * Created by jiajia on 2018/8/10.
 */

public abstract class SimpleCallback<T> implements RequestCallback<T> {
    private BaseView baseView;

    public SimpleCallback(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void beforeRequest() {
        baseView.showProgress(App.getContext().getString(R.string.loading));
    }

    @Override
    public void requestError(ErrorResult error) {
        baseView.toast(error.message);
    }


    @Override
    public void requestComplete() {
        baseView.hideProgress();
    }

}
