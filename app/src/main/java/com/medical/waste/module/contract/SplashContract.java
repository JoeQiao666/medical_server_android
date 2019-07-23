package com.medical.waste.module.contract;

import com.medical.waste.base.BasePresenter;
import com.medical.waste.base.BaseView;

public interface SplashContract {
    interface Presenter extends BasePresenter {
        void startCountdown();
    }
    interface View extends BaseView {
        void onCountDown(String second);
        void onCountDownFinish();
    }
}
