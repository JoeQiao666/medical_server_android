package com.medical.waste.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.app.App;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.Department;
import com.medical.waste.bean.UploadData;
import com.medical.waste.utils.QRCodeUtil;
import com.medical.waste.utils.UserData;
import com.medical.waste.utils.Utils;

import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_print,
        isShowLeftBtn = false,
        toolbarTitle = R.string.print_tag)
public class PrintActivity extends BaseActivity {
    @BindView(R.id.print_view)
    View mPrintView;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.qrcode)
    ImageView mQRCode;
    private PrintQueue mPrintQueue = null;
    private PosApi mPosApi;
    //检测黑标指令(detect the black label)
    private static final int PRINTER_CMD_KEY_CHECKBLACK = 1;
    //设置打印纸类型指令(setting the paper type instructions)
    private static final int PRINTER_CMD_KEY_PAPER_TYPE = 2;
    private boolean hasPrint = false;

    @Override
    protected void initView() {
        UploadData uploadData = UserData.getInstance().getLastUploadData();
        Department department = UserData.getInstance().getDepartment();
        mContent.setText(getString(R.string.print_content, uploadData.getTime(), uploadData.getTypeName(), uploadData.getWeight() + "kg", department.getName(), UserData.getInstance().getHospital()));
        mPosApi = App.getContext().getPosApi();
        //初始化接口时回调(instruction callback)
        mPosApi.setOnComEventListener(mCommEventListener);
        //获取状态时回调(get the state callback)
//        mPosSDK.setOnDeviceStateListener(onDeviceStateListener);
//
        mPosApi.initPosDev("PDA");
        mPrintQueue = new PrintQueue(this, mPosApi);
        mPrintQueue.init();
        mPrintQueue.setOnPrintListener(new PrintQueue.OnPrintListener() {
            @Override
            public void onPrinterSetting(int state) {
                switch (state) {
                    //黑标检测回复
                    case PRINTER_CMD_KEY_CHECKBLACK:
                        switch (state) {
                            case 0:
                                break;
                            case 1:
                                toast("缺纸");
                                break;
                            case 2:
                                toast("检测到黑标");
                                break;
                        }
                        break;

                    //设置打印纸类型回复
                    case PRINTER_CMD_KEY_PAPER_TYPE:
                        switch (state) {
                            case 0:
                                toast("设置打印纸类型成功");
                                break;
                            case 1:
                                toast("设置打印纸类型失败");
                                break;
                        }
                        break;
                }

            }

            @Override
            public void onFinish() {
                hideProgress();
                toast("打印完成");
            }

            @Override
            public void onFailed(int state) {
                hideProgress();
                // TODO Auto-generated method stub
                switch (state) {

                    case PosApi.ERR_POS_PRINT_NO_PAPER://打印机缺纸
                        showTip("打印机缺纸");
                        break;
                    case PosApi.ERR_POS_PRINT_FAILED://打印机失败
                        showTip("打印机失败");
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_LOW://打印机电压低
                        showTip("打印机电压低");

                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_HIGH://打印机电压高
                        showTip("打印机电压高");
                        break;
                }
                //Toast.makeText(PrintBarcodeActivity.this, "打印失败  错误码:"+state, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onGetState(int state) {
                switch (state) {
                    case 0:
                        break;

                    case 1:
                        toast("打印机缺纸");

                        break;

                }
            }


        });
        createQrCode(uploadData.getId());
    }
    private void createQrCode(String content) {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(content, 300, 300);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mQRCode.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }



    PosApi.OnCommEventListener mCommEventListener = new PosApi.OnCommEventListener() {

        @Override
        public void onCommState(int cmdFlag, int state, byte[] resp, int respLen) {
            // TODO Auto-generated method stub
            switch (cmdFlag) {
                case PosApi.POS_INIT:
                    if (state == PosApi.COMM_STATUS_SUCCESS) {
                        toast("设备初始化成功");
                    } else {
                        toast("设备初始化失败");
                    }
                    break;
            }
        }

    };

    private void showTip(String msg) {
        new MaterialDialog.Builder(this)
                .content(msg)
                .positiveText("确定")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPosApi!=null){
            mPosApi.closeDev();
        }
        if(mPrintQueue!=null){
            mPrintQueue.close();
        }
    }

    @OnClick(R.id.last)
    void last() {
        finish();
        startActivity(new Intent(this, ContinueUploadActivity.class));
    }

    @OnClick(R.id.next)
    void next() {
        showProgress("正在打印...");
        if (hasPrint) {
            mPrintQueue.printStart();
        } else {
            print();
        }

    }

    private void print() {
        hasPrint = true;
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_medical_waste);
                if (bitmap == null) return;
                // ivImage.setImageBitmap(bitmap);//展示
                int mLeft = 152;
                byte[] printData = Utils.bitmap2PrinterBytes(Utils.gray2Binary(bitmap));
                mPrintQueue.addBmp(25, mLeft, bitmap.getWidth(), bitmap.getHeight(), printData);
                bitmap.recycle();
                Bitmap bitmap1 = Utils.gray2Binary(Utils.getViewBitmap(mPrintView));//对图像二值化
                byte[] printData1 = Utils.bitmap2PrinterBytes(bitmap1);
                mPrintQueue.addBmp(25, 0, bitmap1.getWidth(), bitmap1.getHeight(), printData1);
                bitmap1.recycle();
                runOnUiThread(() -> mPrintQueue.printStart());
            }
        });

    }


//    private void printImageDemo() {
//        int concentration = 25;//浓度
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_medical_waste);
//        if (bitmap == null) return;
//        // ivImage.setImageBitmap(bitmap);//展示
//        int mLeft = 10;
//        bitmap = Utils.gray2Binary(bitmap);//对图像二值化
//        byte[] printData = Utils.bitmap2PrinterBytes(bitmap);
//        mPrintQueue.addBmp(concentration, mLeft, bitmap.getWidth(), bitmap.getHeight(), printData);
//
//
//        PrintQueue.TextData tData = mPrintQueue.new TextData();//构造TextData实例
//        tData.addParam(PrintQueue.PARAM_ALIGN_MIDDLE);
//        tData.addText("请扫描二维码关注我们!\n");
//        tData.addText("\n\n\n");
//        mPrintQueue.addText(concentration, tData);//添加到打印队列
//
//        mPrintQueue.printStart();
//        bitmap.recycle();
//    }
}
