package com.medical.waste.ui.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.medical.waste.R;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.base.BasePresenter;

import java.io.IOException;

import android.zyapi.CommonApi;

public abstract class BaseScanActivity<T extends BasePresenter> extends BaseActivity<T> {
    private static final String ISMART_KEY_SCAN_DOWN = "ismart.intent.scandown";
    private CommonApi commonApi = null;
    //电源使能口
    private int GPIO_SCAN_POWER_EN = 11;//AE30
    //扫描触发口
    private int GPIO_SCAN_TRIG = 12; //AA28
    private final int MSG_SERIAL_RECV_BUFFER = 1;
    private final String COM_PATH = "/dev/ttyMT2";//串口号
    private final int MAX_RECV_BUF_SIZE = 512;
    private int mComFd = -1;
    private boolean isOpen = true;
    private byte[] recv;

    private MediaPlayer mediaPlayer = null;

    private static final float BEEP_VOLUME = 1.0f;

    private static final long VIBRATE_DURATION = 200L;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String mAction = intent.getAction();
            Log.d("hello", "keycode:" + mAction);

            if (mAction.endsWith(ISMART_KEY_SCAN_DOWN)) {
//                Toast.makeText(BaseScanActivity.this,"按下扫描键(press down the scan key)", Toast.LENGTH_SHORT).show();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        commonApi.setGpioOut(GPIO_SCAN_TRIG, 0);
                        try {
                            Thread.sleep(200);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        commonApi.setGpioOut(GPIO_SCAN_TRIG, 1);
                    }
                }.start();


            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SERIAL_RECV_BUFFER:
                    //收到扫描数据 关闭扫描触发，下一次手动开启(received the scanning data,close the scan trigger)
                    commonApi.setGpioOut(GPIO_SCAN_TRIG, 0);
                    String s = (String) msg.obj;
                    if (!TextUtils.isEmpty(s)) {
                        playSound();
                        onGetScanString(s);

                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeepSound();
        commonApi = new CommonApi();
        openDevice();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ISMART_KEY_SCAN_DOWN);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commonApi.setGpioOut(GPIO_SCAN_TRIG, 0);
    }

    private void openDevice() {
        //触发口先拉高，避免复位后触发扫描(The trigger openings are pulled up first to avoid trigger scan after reset)
        commonApi.setGpioOut(GPIO_SCAN_TRIG, 0);
        //turn on 打开设备电源(turn on the power)
        commonApi.setGpioOut(GPIO_SCAN_POWER_EN, 1);
        //打开串口(open the serial port)
        mComFd = commonApi.openCom(COM_PATH, 115200, 8, 'N', 1);


        if (mComFd > 0) {
//            mTv.setText("扫描设备打开成功(open the device success)\n");
//            mBtnClose.setEnabled(true);
//            mBtnOpen.setEnabled(false);
//            mBtnScan.setEnabled(true);
            //开启线程循环读取串口信息
            readSerial();
        } else {
            toast("扫描设备打开失败(open the device failed)");
        }
    }


    private void readSerial() {
        isOpen = true;
        new Thread() {
            public void run() {
                while (isOpen) {

                    int ret = 0;
                    byte[] buf = new byte[MAX_RECV_BUF_SIZE + 1];
                    //logV("~~~Com Reading...");
                    ret = commonApi.readComEx(mComFd, buf, MAX_RECV_BUF_SIZE, 0, 0);
                    if (ret <= 0) {
                        try {
                            sleep(300);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        continue;
                    }
                    //log(msg);
                    recv = new byte[ret];
                    System.arraycopy(buf, 0, recv, 0, ret);




                    /*
                    StringBuffer sb = new StringBuffer();
                    for(int i = 0;i<recv.length;i++){
                        if(recv[i]==0x0D){
                            sb.append("\n");
                        }else{
                            sb.append((char)recv[i]);
                        }
                    }
                    */

                    //String s = sb.toString();
                    String s = new String(recv);

                    Message msg = handler.obtainMessage(MSG_SERIAL_RECV_BUFFER);
                    msg.obj = s;
                    msg.sendToTarget();
                }

            }
        }.start();
    }

    private void initBeepSound() {

        if (mediaPlayer == null) {

            setVolumeControlStream(AudioManager.STREAM_MUSIC);

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.ding);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playSound() {
        mediaPlayer.start();

    }

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    public abstract void onGetScanString(String content);

}
