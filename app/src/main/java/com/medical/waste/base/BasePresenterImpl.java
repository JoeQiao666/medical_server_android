package com.medical.waste.base;


import com.medical.waste.utils.RxBus;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * ClassName: BasePresenterBasePresenterImpl<p>
 * Author:jiajia<p>
 * Fuction: 代理的基类实现<p>
 * CreateDate:2016/2/14 1:45<p>
 * UpdateUser:<p>
 * UpdateDate:<p>
 */
public class BasePresenterImpl<View extends BaseView,Model> implements BasePresenter {
    protected Model mModel;
    private CompositeDisposable compositeDisposable;
    private Map<Observable, Object> observableMap;
    protected View mView;

    public BasePresenterImpl(View view) {
        mView = view;
        compositeDisposable = new CompositeDisposable();
        observableMap = new HashMap<>();
    }

    @Override
    public void onResume() {
    }

    @Override
    public void addDisposable(Disposable d) {
        if (d == null) {
            return;
        }
        compositeDisposable.add(d);
    }
    //用于Rxjava绑定生命周期
    @Override
    public <E> LifecycleTransformer<E> bindLifecycle() {
        return mView.bindLifecycle();
    }

    @Override
    public <E> void registerEvent(@NonNull Object tag, @NonNull Class<E> clazz, DisposableObserver<E> observer) {
        Observable<E> observable = RxBus.get().register(tag, clazz);
        observableMap.put(observable, tag);
        observable.subscribe(observer);
        addDisposable(observer);
    }

    @Override
    public void onDestroy() {
        for (Observable observable : observableMap.keySet()) {
            RxBus.get().unregister(observableMap.get(observable), observable);
        }
        observableMap.clear();
        compositeDisposable.dispose();
        observableMap = null;
        compositeDisposable = null;
    }
}
