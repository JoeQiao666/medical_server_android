package com.medical.waste.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.app.App;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.utils.UserData;
import com.medical.waste.utils.Utils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_in_success,
        isShowLeftBtn = false,
        toolbarTitle = R.string.in_success)
public class InSuccessActivity extends BaseActivity {
    @BindView(R.id.bag_count)
    TextView mBagCount;
    @BindView(R.id.weight)
    TextView mTotalWeight;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private BaseQuickAdapter<Rubbish, BaseViewHolder> mAdapter;
    private List<Rubbish> rubbishList;

    private PrintQueue mPrintQueue = null;
    private PosApi mPosApi;
    //检测黑标指令(detect the black label)
    private static final int PRINTER_CMD_KEY_CHECKBLACK = 1;
    //设置打印纸类型指令(setting the paper type instructions)
    private static final int PRINTER_CMD_KEY_PAPER_TYPE = 2;

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .sizeResId(R.dimen.divider)
                .colorResId(R.color.divider)
                .build());
        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<Rubbish, BaseViewHolder>(R.layout.item_upload) {
            @Override
            protected void convert(BaseViewHolder helper, Rubbish item) {
                helper.setText(R.id.time, item.getCreatedTime1())
                        .setText(R.id.type, item.getTypeName())
                        .setText(R.id.weight, item.getWeight() + "kg");
            }
        });
        rubbishList = (List<Rubbish>) getIntent().getSerializableExtra("rubbish");
        mAdapter.setNewData(rubbishList);
        calculation();
        initPrint();
    }

    private void initPrint() {

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
    }


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


    private void calculation() {
        mBagCount.setText(getString(R.string.total_bag_count, rubbishList.size()));
        mTotalWeight.setText(getString(R.string.total_weight, getTotalWeight()));
    }

    private String getTotalWeight() {
        String totalWeight = "0";
        BigDecimal bd1 = null;
        BigDecimal bd2 = null;
        for (Rubbish rubbish : rubbishList) {
            bd1 = new BigDecimal(rubbish.getWeight());
            bd2 = new BigDecimal(totalWeight);
            totalWeight = bd1.add(bd2).toString();
        }
        return totalWeight;
    }


    @OnClick(R.id.last)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.next)
    void next() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.print)
    void print() {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_medical_waste);
                int mLeft = 152;
                byte[] printData = Utils.bitmap2PrinterBytes(Utils.gray2Binary(bitmap));
                mPrintQueue.addBmp(25, mLeft, bitmap.getWidth(), bitmap.getHeight(), printData);
                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

                StringBuffer sb = new StringBuffer();
                sb.append("\n");
                sb.append("            医疗垃圾");
                sb.append("\n");
                sb.append("\n");
                sb.append("\n");
                sb.append("      时间：" + format.format(System.currentTimeMillis()));
                sb.append("\n");
                sb.append("\n");
//                sb.append("      总包数：" + 3);
                sb.append("      总包数：" + rubbishList.size());
                sb.append("\n");
                sb.append("\n");
//                sb.append("      总重量：" + "10kg");
                sb.append("      总重量：" + getTotalWeight());
                sb.append("\n");
                sb.append("\n");
                sb.append("      医院：" + UserData.getInstance().getHospital());
                sb.append("\n");
                sb.append("\n");
                sb.append("\n");
                sb.append("\n");
                sb.append("\n");
                sb.append("\n");
                sb.append("\n");
                sb.append("\n");
                PrintQueue.TextData tData = mPrintQueue.new TextData();//构造TextData实例
                tData.addText(sb.toString());//添加打印内容
                mPrintQueue.addText(25, tData);//添加到打印队列
                runOnUiThread(() -> mPrintQueue.printStart());
                bitmap.recycle();
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
//                        toast("设备初始化成功");
                    } else {
                        toast("打印设备初始化失败");
                    }
                    break;
            }
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPosApi != null) {
            mPosApi.closeDev();
        }
        if (mPrintQueue != null) {
            mPrintQueue.close();
        }
    }

}
