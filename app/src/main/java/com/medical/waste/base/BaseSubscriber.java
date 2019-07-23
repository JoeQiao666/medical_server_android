package com.medical.waste.base;


import android.content.Intent;
import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.medical.waste.bean.ErrorResult;
import com.medical.waste.callback.RequestCallback;
import com.medical.waste.utils.ActivityUtils;
import com.medical.waste.utils.SpUtil;
import com.medical.waste.R;
import com.medical.waste.app.App;
import com.medical.waste.bean.Result;
import com.medical.waste.ui.activity.LoginActivity;
import com.socks.library.KLog;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by pc on 2018/7/7.
 */

public class BaseSubscriber<T> extends DisposableObserver<T> {
    private RequestCallback<T> requestCallback;

    public BaseSubscriber(RequestCallback<T> requestCallback) {
        this.requestCallback = requestCallback;
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCallback.beforeRequest();
    }

    @Override
    public void onComplete() {
        requestCallback.requestComplete();
        if (!isDisposed())
            dispose();
    }

    @Override
    public void onError(Throwable e) {
        KLog.e(e == null ? "Throwable is null" : e.toString());
        if (e instanceof HttpException) {
            HttpException ex = (HttpException) e;
            try {
                String exJson = ex.response().errorBody().string();
                ErrorResult error = App.gson.fromJson(exJson, ErrorResult.class);
                error.code = ex.code();
                if (TextUtils.isEmpty(error.msg)) {
                    error.msg = App.getContext().getString(R.string.server_error);
                }
                requestCallback.requestError(error);
                requestCallback.requestComplete();
                if (ex.code() == 401) {//登录过期
                    try {
                        SpUtil.clearUser();
                        ActivityUtils.getInstance().finishAll();
                        Intent intent = new Intent(App.getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        App.getContext().startActivity(intent);
                    } catch (Exception e1) {

                    }
                }
            } catch (Exception exception) {
                KLog.e(exception.toString());
                requestCallback.requestError(new ErrorResult(0, App.getContext().getString(R.string.server_error)));
            }
        } else if (e instanceof RequestFailedException) {
            Result result = ((RequestFailedException) e).result;
            if (result != null) {
                requestCallback.requestError(new ErrorResult(result.code, result.msg));
            }
        } else {
            requestCallback.requestError(new ErrorResult(0, App.getContext().getString(R.string.server_error)));
        }
        requestCallback.requestComplete();
        if (!isDisposed())
            dispose();
    }

    @Override
    public void onNext(T o) {
        if (o == null) {
            requestCallback.requestError(new ErrorResult(0, App.getContext().getString(R.string.server_error)));
        } else {
            requestCallback.requestSuccess(o);
        }
        requestCallback.requestComplete();
    }
}
