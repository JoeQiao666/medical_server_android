package com.medical.waste.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.R;
import com.medical.waste.common.AppConstant;
import com.medical.waste.ui.widget.jsbridge.CallBackFunction;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

@ActivityFragmentInject(contentViewId = R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @BindView(R.id.login)
    TextView mLogin;
    private final RxPermissions rxPermissions = new RxPermissions(this);

    @Override
    protected void initView() {

    }

    @OnClick(R.id.login)
    void onClickBtn(View view) {
        switch (view.getId()) {
            case R.id.login:
                showScan(this::toast);
                break;
        }
    }

    void showScan(CallBackFunction function) {
        if (mPresenter != null) {
            mPresenter.addDisposable(rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe(granted -> {
                        if (granted) {
                            startActivity(new Intent(MainActivity.this, ScanActivity.class));
                            //绑定扫描结果事件
                            mPresenter.registerEvent(AppConstant.RxEvent.QR_CODE, String.class, new DisposableObserver<String>() {
                                @Override
                                public void onNext(String s) {
                                    //扫描结果
                                    if (!TextUtils.isEmpty(s)) {
                                        function.onCallBack(s);
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
}
