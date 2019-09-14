package com.medical.waste.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseBluetoothLeActivity;
import com.medical.waste.bean.Department;
import com.medical.waste.bean.RubbishType;
import com.medical.waste.bean.UploadData;
import com.medical.waste.module.contract.AddContract;
import com.medical.waste.module.presenter.AddPresenter;
import com.medical.waste.utils.UserData;
import com.medical.waste.utils.Utils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_add
)
public class AddActivity extends BaseBluetoothLeActivity<AddContract.Presenter> implements AddContract.View {
    @BindView(R.id.bluetooth_linked)
    TextView mBlueToothLinked;
    @BindView(R.id.top_title)
    TextView mTitle;
    @BindView(R.id.department)
    TextView mDepartment;
    @BindView(R.id.weight)
    TextView mWeight;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private double weight = 0d;
    private BaseQuickAdapter<RubbishType, BaseViewHolder> mAdapter;
    private boolean isConnected = false;
    private RubbishType rubbishType;

    @Override
    protected AddContract.Presenter initPresenter() {
        return new AddPresenter(this);
    }

    @Override
    protected void initView() {
        Department department = UserData.getInstance().getDepartment();
        if (department != null) {
            mDepartment.setText(department.getName());
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(Color.TRANSPARENT)
                .sizeResId(R.dimen.dp10)
                .build()
        );
        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<RubbishType, BaseViewHolder>(R.layout.item_rubbish_type) {
            @Override
            protected void convert(BaseViewHolder helper, RubbishType item) {
                helper.setText(R.id.type, item.getName());
                helper.getView(R.id.type).setSelected(rubbishType == item);
            }
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            rubbishType = mAdapter.getItem(position);
            mAdapter.notifyDataSetChanged();
        });
        mPresenter.getRubbishTypes();
    }

    @OnClick({R.id.btn_back, R.id.last})
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.next)
    void next() {
        if (weight <= 0) {
            toast(R.string.no_rubbish);
            return;
        }
        Department department = UserData.getInstance().getDepartment();
        UploadData uploadData = new UploadData();
        uploadData.setId(Utils.getUUid());
        uploadData.setDepartmentId(department.getId());
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String time = format.format(System.currentTimeMillis());
        uploadData.setTime(time);
        uploadData.setIsBottle(0);
        uploadData.setRecyclerId(UserData.getInstance().getUserInfo().getId());
        uploadData.setWeight(String.valueOf(weight));
        uploadData.setTypeId(rubbishType.getId());
        uploadData.setTypeName(rubbishType.getName());
        UserData.getInstance().addUploadData(uploadData);
        startActivity(new Intent(this, PrintActivity.class));
    }


    @Override
    public void showRubbishTypes(List<RubbishType> rubbishTypes) {
        if (rubbishTypes != null && rubbishTypes.size() > 0) {
            rubbishType = rubbishTypes.get(0);
        }
        mAdapter.setNewData(rubbishTypes);
    }

    //获取到称重信息
    @Override
    public void getData(String w, String type, String typeName, String unit) {
        if (type.equals("OL")) {
            toast(R.string.ol);
            return;
        }
        try {
            mBlueToothLinked.setText(R.string.bluetooth_linked);
            if (!TextUtils.isEmpty(w)) {
                this.weight = Double.parseDouble(w);
                if (weight < 0) {
                    weight = 0;
                }
                String wu = weight + unit;
                mWeight.setText(wu);
            }
        } catch (Exception e) {

        }
    }

    //连接状态
    @Override
    public void getConnectionState(String connStateInfo, boolean isConnected) {
        this.isConnected = isConnected;
        mBlueToothLinked.setText(isConnected ? R.string.bluetooth_linked : R.string.bluetooth_unlinked);
    }
}
