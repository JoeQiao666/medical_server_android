package com.medical.waste.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.Rubbish;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_in_success,
        isShowLeftBtn = false,
        toolbarTitle = R.string.in_success)
public class InSuccessActivity extends BaseActivity{
    @BindView(R.id.bag_count)
    TextView mBagCount;
    @BindView(R.id.weight)
    TextView mTotalWeight;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private BaseQuickAdapter<Rubbish, BaseViewHolder> mAdapter;
    private List<Rubbish> rubbishList;


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
    }

    private void calculation() {
        mBagCount.setText(getString(R.string.total_bag_count, rubbishList.size()));
        String totalWeight = "0";
        BigDecimal bd1 = null;
        BigDecimal bd2 = null;
        for (Rubbish rubbish : rubbishList) {
            bd1 = new BigDecimal(rubbish.getWeight());
            bd2 = new BigDecimal(totalWeight);
            totalWeight =  bd1.add(bd2).toString();
        }
        mTotalWeight.setText(getString(R.string.total_weight, totalWeight));
    }


    @OnClick(R.id.last)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.next)
    void next() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
