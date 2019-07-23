package com.medical.waste.callback;

import android.graphics.Bitmap;

import com.medical.waste.ui.widget.jsbridge.BridgeWebView;
import com.medical.waste.ui.widget.jsbridge.BridgeWebViewClient;
import com.medical.waste.utils.UserData;
import com.tencent.smtt.sdk.WebView;


public class CustomWebViewClient extends BridgeWebViewClient {
    private LoadCallBack mLoadCallBack;
    public CustomWebViewClient(BridgeWebView webView, LoadCallBack loadCallBack) {
        super(webView);
        this.mLoadCallBack = loadCallBack;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        mLoadCallBack.onPageStarted();
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        mLoadCallBack.onPageFinished();
        writeLocalData(view);
    }
    //写入token数据
    private void writeLocalData(WebView webView) {
        String key = "token";
        String token = UserData.getInstance().getToken();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("window.localStorage.setItem('"+ key +"','"+ token +"');", null);
        } else {
            webView.loadUrl("javascript:localStorage.setItem('"+ key +"','"+ token +"');");
        }

    }

    public interface LoadCallBack {
        void onPageStarted();
        void onPageFinished();
    }
}
