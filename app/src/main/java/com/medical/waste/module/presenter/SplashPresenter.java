package com.medical.waste.module.presenter;

import com.medical.waste.R;
import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.base.ScheduleTransformer;
import com.medical.waste.module.contract.SplashContract;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class SplashPresenter extends BasePresenterImpl<SplashContract.View, SplashContract> implements SplashContract.Presenter {

    public SplashPresenter(SplashContract.View view) {
        super(view);
    }

    @Override
    public void startCountdown() {
        int totalSeconds = 3;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .compose(mView.bindLifecycle())
                .take(totalSeconds + 1)
                .map(aLong -> totalSeconds - aLong)
                .compose(new ScheduleTransformer<>())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        mView.onCountDown(String.format(mView.getStr(R.string.resendable_after_seconds), String.valueOf(aLong)));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        //完成倒计时跳转
                        mView.onCountDownFinish();
                    }
                });
    }
}
