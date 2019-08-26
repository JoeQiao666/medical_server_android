package com.medical.waste.callback;

import com.medical.waste.R;
import com.medical.waste.app.App;
import com.medical.waste.base.BaseView;
import com.medical.waste.bean.ErrorResult;
import com.medical.waste.utils.ToastUtils;

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
        if (baseView != null) {
            baseView.showProgress(App.getContext().getString(R.string.loading));
        }
    }

    @Override
    public void requestError(ErrorResult error) {
        if (baseView != null) {
            baseView.toast(error.msg);
        }else{
            ToastUtils.toastShort(App.getContext(),error.msg);
        }
    }


    @Override
    public void requestComplete() {
        if (baseView != null) {
            baseView.hideProgress();
        }
    }

}
