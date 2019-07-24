package com.medical.waste.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.RubbishType;
import com.medical.waste.module.contract.AddContract;
import com.medical.waste.module.presenter.AddPresenter;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_add
)
public class AddActivity extends BaseActivity<AddContract.Presenter> implements AddContract.View {
    @BindView(R.id.bluetooth_linked)
    TextView mBlueToothLinked;
    @BindView(R.id.top_title)
    TextView mTitle;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private BaseQuickAdapter<RubbishType, BaseViewHolder> mAdapter;
    private String currentTypeId = "";

    @Override
    protected AddContract.Presenter initPresenter() {
        return new AddPresenter(this);
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<RubbishType, BaseViewHolder>(R.layout.item_rubbish_type) {
            @Override
            protected void convert(BaseViewHolder helper, RubbishType item) {
                helper.setText(R.id.type, item.getName());
                helper.getView(R.id.type).setSelected(currentTypeId.equals(item.getId()));
            }
        });
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            RubbishType rubbishType = mAdapter.getItem(position);
            if (rubbishType != null) {
                currentTypeId = rubbishType.getId();
                mAdapter.notifyDataSetChanged();
            }
        });
        mPresenter.getRubbishTypes();
    }

    @OnClick({R.id.btn_back, R.id.last})
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.next)
    void next() {
        startActivity(new Intent(this, PrintActivity.class));
    }


    @Override
    public void showRubbishTypes(List<RubbishType> rubbishTypes) {
        if (rubbishTypes != null && rubbishTypes.size() > 0) {
            currentTypeId = rubbishTypes.get(0).getId();
        }
        mAdapter.setNewData(rubbishTypes);
    }
}
