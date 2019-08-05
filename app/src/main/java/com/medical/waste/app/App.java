package com.medical.waste.app;

import android.app.Application;
import android.posapi.PosApi;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.medical.waste.http.retrofit.HttpComponent;
import com.medical.waste.http.ApiService;
import com.medical.waste.slideback.ActivityHelper;

/**
 * Created by jiajia on 2018/10/1.
 */

public class App extends Application {
    public static Gson gson = new Gson() ;
    private static App sApplicationContext;
    private static HttpComponent httpComponent;
    private ActivityHelper mActivityHelper;
    private PosApi mPosApi = null;
    @Override
    public void onCreate() {
        super.onCreate();
        httpComponent = HttpComponent.HttpInitialize.init();
        sApplicationContext = this;
        //初始化Fresco
        Fresco.initialize(this);
        mActivityHelper = new ActivityHelper();
        registerActivityLifecycleCallbacks(mActivityHelper);
        mPosApi = PosApi.getInstance(this);
    }


    public ActivityHelper getActivityHelper() {
        return mActivityHelper;
    }




    // 获取ApplicationContext
    public static App getContext() {
        return sApplicationContext;
    }

    public static ApiService getApiService() {
        return httpComponent.getApiService();
    }

    public PosApi getPosApi() {
        return mPosApi;
    }
}
