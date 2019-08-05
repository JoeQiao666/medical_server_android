package com.medical.waste.ui.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.os.Build.VERSION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lecheng.furiblesdk.R.id;
import com.lecheng.furiblesdk.R.layout;
import com.lecheng.furiblesdk.R.string;
import com.medical.waste.base.BaseActivity;

@SuppressLint({"NewApi"})
public abstract class DeviceListActivity extends BaseActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 16000L;

    private LeScanCallback mLeScanCallback = new LeScanCallback() {
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            DeviceListActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    getLeDevice(device);
                }
            });
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        this.mHandler = new Handler();
        if (!this.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
            Toast.makeText(this, string.ble_not_supported, Toast.LENGTH_LONG).show();
            this.finish();
        }

        if (VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, 10);
        }

        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        this.mBluetoothAdapter = bluetoothManager.getAdapter();
        if (this.mBluetoothAdapter == null) {
            Toast.makeText(this, string.error_bluetooth_not_supported, Toast.LENGTH_LONG).show();
            this.finish();
        }
        super.onCreate(savedInstanceState);
    }

    protected void scanLeDevice(boolean enable) {
        if (enable) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    DeviceListActivity.this.mScanning = false;
                    DeviceListActivity.this.mBluetoothAdapter.stopLeScan(DeviceListActivity.this.mLeScanCallback);
                    DeviceListActivity.this.invalidateOptionsMenu();
                    onScanStop();
                }
            }, SCAN_PERIOD);
            this.mScanning = true;
            this.mBluetoothAdapter.startLeScan(this.mLeScanCallback);
        } else {
            this.mScanning = false;
            this.mBluetoothAdapter.stopLeScan(this.mLeScanCallback);
        }
        this.invalidateOptionsMenu();
    }

    protected abstract void onScanStop();

//    public boolean onCreateOptionsMenu(Menu menu) {
//        this.getMenuInflater().inflate(menu.main, menu);
//        if (!this.mScanning) {
//            menu.findItem(id.menu_stop).setVisible(false);
//            menu.findItem(id.menu_scan).setVisible(true);
//            menu.findItem(id.menu_refresh).setActionView((View) null);
//        } else {
//            menu.findItem(id.menu_stop).setVisible(true);
//            menu.findItem(id.menu_scan).setVisible(false);
//            menu.findItem(id.menu_refresh).setActionView(layout.unit_progress);
//        }

//        return true;
//    }

//    public boolean onOptionsItemSelected(MenuItem item) {
//        int i = item.getItemId();
//        if (i != id.menu_history) {
//            if (i == id.menu_scan) {
//                this.scanLeDevice(true);
//            } else if (i == id.menu_stop) {
//                this.scanLeDevice(false);
//            }
//        }
//
//        return true;
//    }

    public abstract void getLeDevice(BluetoothDevice bluetoothDevice);

    protected void onResume() {
        super.onResume();
        if (!this.mBluetoothAdapter.isEnabled() && !this.mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
            this.startActivityForResult(enableBtIntent, 1);
        }
        this.scanLeDevice(true);
    }

    protected void onPause() {
        super.onPause();
        this.scanLeDevice(false);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}