package com.medical.waste.ui.activity;

import android.os.Vibrator;
import android.view.View;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.common.AppConstant;
import com.medical.waste.utils.RxBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;


@ActivityFragmentInject(contentViewId = R.layout.activity_scan,
        toolbarTitle = R.string.scan_qr,
        titleTextColor = R.color.material_white,
        toolbarIndicator = R.mipmap.ic_back_btn_white,
        statusBackground = R.color.transparent,
        toolbarBackgroundDrawableRes = R.color.transparent,
        isShowDarkStatusBarIcon = false
)

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {
    @BindView(R.id.zbarview)
    ZBarView mZBarView;

    @Override
    protected void initView() {

        mZBarView.setDelegate(this);
    }

    //手动输入
    @OnClick(R.id.input_bind)
    void inputBind() {
        finish();
        RxBus.get().post(AppConstant.RxEvent.QR_CODE,AppConstant.INPUT_QR_CODE);
    }

    //打开闪光灯
    @OnClick(R.id.open_flash)
    void openFlash(View view) {
        if (view.isSelected()) {
            mZBarView.closeFlashlight();
        } else {
            mZBarView.openFlashlight();
        }
        view.setSelected(!view.isSelected());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 打开后置摄像头开始预览，但是并未开始识别
        mZBarView.startCamera();
        // 显示扫描框，并开始识别
        mZBarView.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        // 关闭摄像头预览，并且隐藏扫描框
        mZBarView.closeFlashlight();
        mZBarView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 销毁二维码扫描控件
        mZBarView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        toast(R.string.scan_success);
        vibrate();
        RxBus.get().post(AppConstant.RxEvent.QR_CODE, result);
        finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        if (isDark) {
            toast(R.string.ambientBrightnessTip);
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        toast(R.string.scan_again);
    }
}
