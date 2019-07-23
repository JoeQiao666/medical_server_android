package com.medical.waste.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.R;
import com.medical.waste.app.App;
import com.medical.waste.base.BaseFragment;
import com.medical.waste.base.BasePresenter;
import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.bean.Result;
import com.medical.waste.bean.User;
import com.medical.waste.callback.CustomWebChromeClient;
import com.medical.waste.callback.CustomWebViewClient;
import com.medical.waste.common.AppConstant;
import com.medical.waste.ui.activity.ScanActivity;
import com.medical.waste.ui.widget.jsbridge.BridgeWebView;
import com.medical.waste.ui.widget.jsbridge.CallBackFunction;
import com.medical.waste.utils.UserData;
import com.medical.waste.utils.Utils;
import com.socks.library.KLog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.smtt.sdk.WebSettings;


import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;

@ActivityFragmentInject(contentViewId = R.layout.fragment_webview)
public class WebViewFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.webview)
    BridgeWebView mWebView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private WebViewCallBack mWebViewCallBack;
    private boolean firstLoad;
    private RxPermissions rxPermissions;

    public static WebViewFragment newInstance(String url) {
        return newInstance(url, "");
    }

    @Override
    protected BasePresenter initPresenter() {
        return new BasePresenterImpl<>(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        rxPermissions = new RxPermissions(this);
        if (context instanceof WebViewCallBack) {
            mWebViewCallBack = (WebViewCallBack) context;
        }
    }

    public static WebViewFragment newInstance(String url, String title) {
        Bundle args = new Bundle();
        args.putString(AppConstant.URL, url);
        args.putString(AppConstant.TITLE, title);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static WebViewFragment newInstance(Bundle bundle) {
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            setToolbarTitle(getArguments().getString(AppConstant.TITLE));
        }
        mRefreshLayout.setOnRefreshListener(this);
        //初始化webview
        initWebView();
        //注入h5需要调用的相关方法
        registerHandler();
        loadUrl();
    }

    //h5调用方法注入
    private void registerHandler() {
        //注册getMobile方法
        mWebView.registerHandler(AppConstant.H5Method.GET_MOBILE, (data, function) -> getMobile(function));
        //扫码
        mWebView.registerHandler(AppConstant.H5Method.SCAN_QR_CODE, (data, function) -> scan(function));
    }

    //获取手机号码
    private void getMobile(CallBackFunction function) {
//        User userInfo = UserData.getInstance().getUserInfo();
//        Map<String, Object> params = new HashMap<>();
//        Result<Map<String, Object>> result = new Result<>();
//        if (userInfo != null && !TextUtils.isEmpty(userInfo.getMobile())) {
//            params.put(AppConstant.MOBILE, userInfo.getMobile());
//        } else {
//            params.put(AppConstant.MOBILE, "");
//        }
//        result.result = params;
//        function.onCallBack(App.gson.toJson(result));
    }

    //调用扫码
    private void scan(CallBackFunction function) {
        if (mPresenter != null) {
            mPresenter.addDisposable(rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe(granted -> {
                        if (granted) {
                            //跳转扫码页
                            startActivityForResult(new Intent(getActivity(), ScanActivity.class), AppConstant.REQUEST_CODE_SCAN_CODE);
                            //绑定扫描结果事件
                            mPresenter.registerEvent(AppConstant.RxEvent.QR_CODE, String.class, new DisposableObserver<String>() {
                                @Override
                                public void onNext(String s) {
                                    if (AppConstant.INPUT_QR_CODE.equals(s)) {
                                        getChildFragmentManager().beginTransaction()
                                                .add(InputCodeDialogFragment.newInstance()
                                                                .setConfirmCallback(content -> showQrcode(function, content))
                                                        , InputCodeDialogFragment.class.getSimpleName())
                                                .commitAllowingStateLoss();
                                    }else{
                                        showQrcode(function,s);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                        } else {
                            toast(R.string.permission_camera_denied);
                        }
                    }));
        }
    }

    //处理获得到的校验码
    private void showQrcode(CallBackFunction function, String qrcode) {
        if (!TextUtils.isEmpty(qrcode)) {
            Map<String, Object> params = new HashMap<>();
            Result<Map<String, Object>> result = new Result<>();
            params.put(AppConstant.QR_CODE, qrcode);
            result.data = params;
            function.onCallBack(App.gson.toJson(result));
        } else {
            toast(R.string.scan_again);
        }
    }

    private void loadUrl() {
        String url = getArguments() == null ? null : getArguments().getString(AppConstant.URL);
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        //设置UserAgent
        webSettings.setUserAgentString(Utils.getUserAgent(webSettings.getUserAgentString()));
        webSettings.setJavaScriptEnabled(true);
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);
        //开启localstorage
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = App.getContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        //禁止缩放操作
//        webSettings.setSupportZoom(false);
//        webSettings.setBuiltInZoomControls(false);
//        webSettings.setDisplayZoomControls(false);
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //支持通过JS打开新窗口
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        //设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");

        mWebView.setWebChromeClient(new CustomWebChromeClient(mWebViewCallBack));
        mWebView.setWebViewClient(new CustomWebViewClient(mWebView, new CustomWebViewClient.LoadCallBack() {
            @Override
            public void onPageStarted() {
                if (firstLoad) {
                    firstLoad = false;
                    showLoadingView();
                }
            }

            @Override
            public void onPageFinished() {
                hideLoadingView();
                mRefreshLayout.setRefreshing(false);
            }
        }));
    }

    public void onBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finishActivity();
        }
    }

    @Override
    public void onRefresh() {
        //下拉刷新 重新加载
        mWebView.callHandler(AppConstant.H5Method.RELOAD_WEBVIEW, "{}", data -> {
            KLog.d("handler reloadWebview" + data);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            });
        });
    }

    public interface WebViewCallBack {
        void onGetTitle(String title);
    }

}
