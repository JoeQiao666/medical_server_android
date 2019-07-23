package com.medical.waste.callback;


import com.medical.waste.ui.fragment.WebViewFragment;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

public class CustomWebChromeClient extends WebChromeClient {
    protected WebViewFragment.WebViewCallBack callBack;

    public CustomWebChromeClient(WebViewFragment.WebViewCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        //获取标题
        if (callBack != null) {
            callBack.onGetTitle(title);
        }
    }
}
