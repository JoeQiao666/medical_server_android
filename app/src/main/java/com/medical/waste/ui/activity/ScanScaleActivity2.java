package com.medical.waste.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import android.text.TextUtils;
import android.widget.TextView;


import com.lecheng.furiblesdk.BluetoothLeActivity;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.common.AppConstant;
import com.medical.waste.utils.SpUtil;
import com.socks.library.KLog;


import butterknife.BindView;

import android.annotation.SuppressLint;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
@SuppressLint("NewApi")
@ActivityFragmentInject(contentViewId = R.layout.activity_scan_scale)
public class ScanScaleActivity2 extends DeviceListActivity {
    @BindView(R.id.text)
    TextView mContent;

    @Override
    public void getLeDevice(BluetoothDevice device) {
        KLog.e("BluetoothDevice=" + device.getName() + "-" + device.getAddress());
        if (device.getName() != null && device.getName().contains("Furi")) {
            SpUtil.writeString(AppConstant.SP_DEVICE_ADDRESS,device.getAddress());
            SpUtil.writeString(AppConstant.SP_DEVICE_NAME,device.getName());
            startActivity(new Intent(ScanScaleActivity2.this, AddActivity.class)
                    .putExtra(BluetoothLeActivity.EXTRAS_DEVICE_NAME, device.getName())
                    .putExtra(BluetoothLeActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress()));
            scanLeDevice(false);
            finish();
            return;
        }
    }

    @Override
    protected void onScanStop() {
//        btnScan.setVisibility(View.VISIBLE);
//        ivState.clearAnimation();
//        new MyToast(this, "扫描停止", 0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ivState.clearAnimation();
    }

    @Override
    protected void initView() {
        String address = SpUtil.readString(AppConstant.SP_DEVICE_ADDRESS);
        String name = SpUtil.readString(AppConstant.SP_DEVICE_NAME);
        if (!TextUtils.isEmpty(address)) {
            startActivity(new Intent(ScanScaleActivity2.this, AddActivity.class)
                    .putExtra(BluetoothLeActivity.EXTRAS_DEVICE_NAME, name)
                    .putExtra(BluetoothLeActivity.EXTRAS_DEVICE_ADDRESS, address));
            scanLeDevice(false);
            finish();
            return;
        }
        showProgress("正在连接称重设备...");
        scanLeDevice(true);
//        startActivity(new Intent(ScanScaleActivity2.this, AddActivity.class)
//                .putExtra(BluetoothLeActivity.EXTRAS_DEVICE_NAME, "Furi_0227F5")
//                .putExtra(BluetoothLeActivity.EXTRAS_DEVICE_ADDRESS, "78:9C:E7:02:27:F5"));
//        scanLeDevice(false);
//        finish();
    }
}