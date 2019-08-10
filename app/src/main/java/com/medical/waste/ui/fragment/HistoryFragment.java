package com.medical.waste.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseFragment;
import com.medical.waste.bean.EventFilter;
import com.medical.waste.bean.InListData;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.common.AppConstant;
import com.medical.waste.module.contract.InOutContract;
import com.medical.waste.module.presenter.HistoryPresenter;
import com.medical.waste.ui.activity.PrintHistoryActivity;
import com.medical.waste.utils.UserData;
import com.medical.waste.utils.Utils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;

@ActivityFragmentInject(contentViewId = R.layout.fragment_history)
public class HistoryFragment extends BaseFragment<InOutContract.Presenter> implements InOutContract.View {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private int pageNumber = 0;
    private int pageSize = 20;
    private BaseQuickAdapter<Rubbish, BaseViewHolder> mAdapter;
    private List<Rubbish> mChooseRubbish = new ArrayList<>();
    private String start = "";
    private String end = "";

    public static HistoryFragment newInstance(String status, String start, String end) {
        Bundle args = new Bundle();
        args.putString(AppConstant.STATUS, status);
        args.putString(AppConstant.START, start);
        args.putString(AppConstant.END, end);
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        start = getArguments().getString(AppConstant.START);
        end = getArguments().getString(AppConstant.END);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .sizeResId(R.dimen.divider)
                .colorResId(R.color.divider)
                .build()
        );
        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<Rubbish, BaseViewHolder>(R.layout.item_history) {
            @Override
            protected void convert(BaseViewHolder helper, Rubbish item) {
                helper.setText(R.id.time, item.getCreatedTime1())
                        .setText(R.id.department, item.getDepartmentName())
                        .setText(R.id.type, item.getTypeName())
                        .setText(R.id.weight, getString(R.string.weight, item.getWeight()));
            }
        });
        mAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, null));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), PrintHistoryActivity.class)
                        .putExtra(AppConstant.RUBBISH, mAdapter.getData().get(position)));
            }
        });

        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        refresh();
        mPresenter.registerEvent(AppConstant.RxEvent.FILTER, EventFilter.class, new DisposableObserver<EventFilter>() {
            @Override
            public void onNext(EventFilter eventFilter) {
                start = eventFilter.start;
                end = eventFilter.end;
                refresh();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
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
        if (!TextUtils.isEmpty(start) && !TextUtils.isEmpty(end)) {
            params.put("start", start);
            params.put("end", end);
        }
        params.put("status", getArguments().getString(AppConstant.STATUS, "0"));
        params.put("departmentId", UserData.getInstance().getLoginData().user.getDepartmentId());
        mPresenter.getRubbishs(params);
    }


    @Override
    protected InOutContract.Presenter initPresenter() {
        return new HistoryPresenter(this);
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


}
