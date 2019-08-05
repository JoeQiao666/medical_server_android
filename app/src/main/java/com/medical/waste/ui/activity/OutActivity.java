package com.medical.waste.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.InListData;
import com.medical.waste.bean.Result;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.common.AppConstant;
import com.medical.waste.module.contract.InOutContract;
import com.medical.waste.module.presenter.InPresenter;
import com.medical.waste.module.presenter.OutPresenter;
import com.medical.waste.utils.Utils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_out,
        toolbarTitle = R.string.activity_out,
        isShowLeftBtn = false
)
public class OutActivity extends BaseActivity<InOutContract.OutPresenter> implements CompoundButton.OnCheckedChangeListener, InOutContract.OutView {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.checkbox)
    AppCompatCheckBox mCheckbox;
    @BindView(R.id.bag_count)
    TextView mBagCount;
    @BindView(R.id.total_weight)
    TextView mTotalWeight;
    private int pageNumber = 0;
    private int pageSize = 20;
    private BaseQuickAdapter<Rubbish, BaseViewHolder> mAdapter;
    private List<Rubbish> mChooseRubbish = new ArrayList<>();

    @Override
    protected void initView() {
        mBagCount.setText(getString(R.string.bag_count, 0));
        mTotalWeight.setText(getString(R.string.weight, "0"));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .sizeResId(R.dimen.divider)
                .colorResId(R.color.divider)
                .build()
        );
        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<Rubbish, BaseViewHolder>(R.layout.item_in) {
            @Override
            protected void convert(BaseViewHolder helper, Rubbish item) {
                helper.setText(R.id.time, item.getCreatedTime())
                        .setText(R.id.department, item.getDepartmentName())
                        .setText(R.id.type, item.getTypeName())
                        .setText(R.id.weight, getString(R.string.weight, item.getWeight()));
                ImageView choose = helper.getView(R.id.choose);
                choose.setSelected(mChooseRubbish.contains(item));
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Rubbish item = mAdapter.getItem(position);
                if (mChooseRubbish.contains(item)) {
                    mChooseRubbish.remove(item);
                } else {
                    mChooseRubbish.add(item);
                }
                mAdapter.notifyDataSetChanged();
                calculation();
            }
        });
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        mCheckbox.setOnCheckedChangeListener(this);
        refresh();
    }

    private void refresh() {
        pageNumber = 0;
        getRubbish();
    }

    private void loadMore() {
        pageNumber++;
        getRubbish();
    }

    public void getRubbish() {
        Map<String, String> params = Utils.getDefaultParams();
        params.put("pageNumber", String.valueOf(pageNumber));
        params.put("pageSize", String.valueOf(pageSize));
//        params.put("start", "");
//        params.put("end", "");
        params.put("status", "1");
//        params.put("departmentId", UserData.getInstance().getLoginData().user.getDepartmentId());
        mPresenter.getRubbishs(params);
    }


    @Override
    protected InOutContract.OutPresenter initPresenter() {
        return new OutPresenter(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mChooseRubbish.clear();
        if (isChecked) {
            mChooseRubbish.addAll(mAdapter.getData());
        }

        mAdapter.notifyDataSetChanged();
        calculation();
    }

    private void calculation() {
        mBagCount.setText(getString(R.string.bag_count, mChooseRubbish.size()));
        String totalWeight = "0";
        BigDecimal bd1 = null;
        BigDecimal bd2 = null;
        for (Rubbish rubbish : mChooseRubbish) {
            bd1 = new BigDecimal(rubbish.getWeight());
            bd2 = new BigDecimal(totalWeight);
            totalWeight =  bd1.add(bd2).toString();
        }
        mTotalWeight.setText(getString(R.string.weight, totalWeight));
    }

    @Override
    public void showRubbish(InListData data) {
        if (data == null || data.list == null) {
            return;
        }
        if (pageNumber == 0) {
            mAdapter.setNewData(data.list);
        } else {
            mAdapter.addData(data.list);
            mAdapter.loadMoreComplete();
        }
        mAdapter.setEnableLoadMore(data.list.size() >= pageSize);
    }

    @Override
    public void showRecycleResult(Result result) {
        startActivity(new Intent(this, OutSuccessActivity.class).putExtra("rubbish", new ArrayList<>(mChooseRubbish)));
        mChooseRubbish.clear();
        mAdapter.notifyDataSetChanged();
        calculation();
        refresh();
    }

    @OnClick({R.id.top, R.id.last, R.id.next})
    void click(View view) {
        switch (view.getId()) {
            case R.id.top:
                mRecyclerView.scrollToPosition(0);
                break;
            case R.id.last:
                onBackPressed();
                break;
            case R.id.next:
                if (mChooseRubbish.size() == 0) {
                    toast("请选择要入库的垃圾");
                    return;
                }
                startActivityForResult(new Intent(this, ConfirmActivity.class), 1001);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            String id = data.getStringExtra(AppConstant.ID);
            StringBuffer sb = new StringBuffer();
            for (Rubbish rubbish : mChooseRubbish) {
                sb.append(rubbish.getId()).append(",");
            }
            String ids = sb.substring(0, sb.length() - 1);
            Map<String, String> params = Utils.getDefaultParams();
            params.put(AppConstant.IDS, ids);
            // TODO: 2019-08-05  
//            params.put(AppConstant.ADMINISTRATOR_ID, id);
            params.put(AppConstant.COMPANY_ID, "51896f7d67714f1e80711abcafa0872e");
            mPresenter.recycle(params);
        }
    }
}
