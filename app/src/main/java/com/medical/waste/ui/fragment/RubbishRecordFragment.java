package com.medical.waste.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseFragment;
import com.medical.waste.bean.EventFilter;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.common.AppConstant;
import com.medical.waste.module.contract.RubbishRecordContract;
import com.medical.waste.module.presenter.RubbishRecordListPresenter;
import com.medical.waste.ui.activity.RubbishDetailActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;

@ActivityFragmentInject(contentViewId = R.layout.fragment_rubbish_record)
public class RubbishRecordFragment extends BaseFragment<RubbishRecordContract.RubbishRecordListPresenter> implements RubbishRecordContract.RubbishRecordListView {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.filter)
    TextView mFilter;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private BaseQuickAdapter<Rubbish, BaseViewHolder> mAdapter;
    private String start = "";
    private String end = "";

    public static RubbishRecordFragment newInstance(String start, String end) {
        Bundle args = new Bundle();
        args.putString(AppConstant.START, start);
        args.putString(AppConstant.END, end);
        RubbishRecordFragment fragment = new RubbishRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initView() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                start = "";
                end = "";
                mFilter.setVisibility(View.GONE);
                mFilter.setText("");
                getRubbish();
            }
        });
        start = getArguments().getString(AppConstant.START);
        end = getArguments().getString(AppConstant.END);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .sizeResId(R.dimen.divider)
                .colorResId(R.color.divider)
                .build()
        );
        mRecyclerView.setAdapter(mAdapter = new BaseQuickAdapter<Rubbish, BaseViewHolder>(R.layout.item_rubbish_record) {
            @Override
            protected void convert(BaseViewHolder helper, Rubbish item) {
                helper.setText(R.id.content, getString(R.string.rubbish_record_content, item.getId(),
                        item.getDepartmentName(),
                        item.getTypeName(),
                        item.getWeight() + "kg",
                        getStatus(item.getStatus())));
//                helper.getView(R.id.show_detail).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
            }
        });
        mAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, null));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), RubbishDetailActivity.class)
                        .putExtra(AppConstant.RUBBISH, mAdapter.getData().get(position)));
            }
        });

        mAdapter.setEnableLoadMore(false);
//        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                loadMore();
//            }
//        }, mRecyclerView);
        getRubbish();
        mPresenter.registerEvent(AppConstant.RxEvent.FILTER, EventFilter.class, new DisposableObserver<EventFilter>() {
            @Override
            public void onNext(EventFilter eventFilter) {
                start = eventFilter.start;
                end = eventFilter.end;
                mFilter.setText(eventFilter.filterContent);
                mFilter.setVisibility(View.VISIBLE);
                getRubbish();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        mPresenter.registerEvent(AppConstant.RxEvent.REFRESH_RECORD, String.class, new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                getRubbish();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
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


//    private void refresh() {
//        pageNumber = 0;
//        getRubbish();
//    }

//    private void loadMore() {
//        pageNumber++;
//        getRubbish();
//    }

    public void getRubbish() {
        mPresenter.getRubbishRecords(start, end);
//        showProgress(getString(R.string.loading));
//        Executors.newCachedThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                RubbishDao dao = App.getContext().getDaoSession().getRubbishDao();
//                List<Rubbish> all = dao.loadAll();
//                if (all != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            hideProgress();
//                            showRubblishs(all);
//                        }
//                    });
//                }
//            }
//        });
    }


    @Override
    protected RubbishRecordContract.RubbishRecordListPresenter initPresenter() {
        return new RubbishRecordListPresenter(this);
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        start = format.format(calendar.getTimeInMillis()) + " 00:00";
        mFilter.setText(start + " ~ 24:00");
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        end = format.format(calendar.getTimeInMillis()) + " 00:00";
    }

    @Override
    public void showRubblishs(List<Rubbish> data) {
        mAdapter.setNewData(data);
        mRefreshLayout.setRefreshing(false);
    }
}
