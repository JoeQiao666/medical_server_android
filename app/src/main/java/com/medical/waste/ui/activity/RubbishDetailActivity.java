package com.medical.waste.ui.activity;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.common.AppConstant;
import com.medical.waste.utils.QRCodeUtil;

import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_rubbish_detail,
        toolbarTitle = R.string.rubbish_detail,
        isShowLeftBtn = false
)
public class RubbishDetailActivity extends BaseActivity {
    @BindView(R.id.basic_info)
    TextView mBasicInfo;
    @BindView(R.id.upload_info)
    TextView mUploadInfo;
    @BindView(R.id.in_info)
    TextView mInInfo;
    @BindView(R.id.out_info)
    TextView mOutInfo;
    @BindView(R.id.qrcode)
    ImageView mQRCode;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void initView() {
        Rubbish item = (Rubbish) getIntent().getSerializableExtra(AppConstant.RUBBISH);
        mBasicInfo.setText(getString(R.string.basic_info, item.getId(),
                item.getDepartmentName(),
                item.getTypeName(),
                item.getWeight() + "kg",
                getStatus(item.getStatus())));
        mUploadInfo.setText(getString(R.string.other_info, getNoEmptyString(item.getRecyclerName()), getNoEmptyString(item.getStaffName()), getDate(item.getOpAt())));
        mInInfo.setText(getString(R.string.other_info, getNoEmptyString(item.getRecyclerName()), getNoEmptyString(item.getAdministratorName()), getDate(item.getStoreAt())));
        mOutInfo.setText(getString(R.string.other_info, getNoEmptyString(item.getRecyclerName()), getNoEmptyString(item.getCompanyName()), getDate(item.getRecycleAt())));
        createQrCode(item.getId());
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


    private String getNoEmptyString(String str) {
        return str == null ? "" : str;
    }

    private String getDate(String str) {
        try {
            return format.format(Long.parseLong(str) * 1000);
        } catch (Exception e) {
            return "";
        }
    }

    private String getStatus(int status) {
        switch (status) {
            case 0:
                return "待入库";
            case 1:
                return "待出库";
            default:
                return "已出库";
        }
    }
    @OnClick(R.id.close)
    void close(){
        onBackPressed();
    }

}
