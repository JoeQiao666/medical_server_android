package com.medical.waste.app;

import android.app.Application;
import android.os.Handler;
import android.posapi.PosApi;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.medical.waste.http.retrofit.HttpComponent;
import com.medical.waste.http.ApiService;
import com.medical.waste.slideback.ActivityHelper;
import com.socks.library.KLog;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

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
        KLog.init(true,"KLOG");
        httpComponent = HttpComponent.HttpInitialize.init();
        sApplicationContext = this;
        //初始化Fresco
        Fresco.initialize(this);
        mActivityHelper = new ActivityHelper();
        registerActivityLifecycleCallbacks(mActivityHelper);
        mPosApi = PosApi.getInstance(this);
        //jpush初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
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


    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    public void setAlias(String alias) {
        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    KLog.i(logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    KLog.i(logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    KLog.e(logs);
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    KLog.d("Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    KLog.i("Unhandled msg - " + msg.what);
            }
        }
    };
}
