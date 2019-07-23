package com.medical.waste.base;


import com.medical.waste.bean.Result;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jiajia on 2018/9/29.
 */

public class BaseTransformer<T> implements ObservableTransformer<Result<T>, T> {
    @Override
    public ObservableSource<T> apply(Observable<Result<T>> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(tResult -> {
                    if (tResult.code==0) {
                        return tResult.result;
                    } else {
                        throw new RequestFailedException(tResult);

                    }
                });
    }
}
