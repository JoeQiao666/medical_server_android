package com.medical.waste.ui.activity;


import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.bean.EventFilter;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.common.AppConstant;
import com.medical.waste.module.contract.RubbishRecordContract;
import com.medical.waste.module.presenter.RubbishRecordPresenter;
import com.medical.waste.ui.fragment.ChooseTimeDialogFragment;
import com.medical.waste.ui.fragment.RubbishIDSearchFragment;
import com.medical.waste.ui.fragment.RubbishRecordFragment;
import com.medical.waste.ui.fragment.RubbishScanFragment;
import com.medical.waste.utils.RxBus;
import com.medical.waste.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_rubbish_record,
        isShowLeftBtn = false,
        toolbarTitle = R.string.rubbish_record
)
public class RubbishRecordActivity extends BaseScanActivity<RubbishRecordContract.Presenter> implements ChooseTimeDialogFragment.ConfirmCallback,RubbishRecordContract.View, RubbishIDSearchFragment.OnInputIdListener {
    @BindView(R.id.viewpager)
    ViewPager mViewPgaer;
    @BindView(R.id.tab_layout)
    XTabLayout mTabLayout;
    private String[] tabs = {"扫码追溯", "输入ID追溯", "历史追溯"};
    private List<Fragment> fragments = new ArrayList<>();
    private String start = "";
    private String end = "";

    @Override
    protected void initView() {
        mViewPgaer.setOffscreenPageLimit(3);
        fragments.add(RubbishScanFragment.newInstance());
        fragments.add(RubbishIDSearchFragment.newInstance());
        fragments.add(RubbishRecordFragment.newInstance(start,end));
        mViewPgaer.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPgaer);
    }



    @OnClick(R.id.last)
    void last() {
        onBackPressed();
    }

    @OnClick(R.id.next)
    void filter() {
        ChooseTimeDialogFragment.newInstance().show(getSupportFragmentManager(), Utils.getUUid());
    }

    //选择时间
    @Override
    public void onConfirm(int type, String start, String end,String filterContent) {
        this.start = start;
        this.end = end;
        //发送筛选刷新事件
        EventFilter filter = new EventFilter();
        filter.start = start;
        filter.end = end;
        filter.filterContent = filterContent;
        RxBus.get().post(AppConstant.RxEvent.FILTER, filter);
    }

    @Override
    public void onGetScanString(String content) {
        if(TextUtils.isEmpty(content)){
            toast(R.string.scan_error);
            return;
        }
        mPresenter.getRubbishById(content);
    }

    @Override
    protected RubbishRecordContract.Presenter initPresenter() {
        return new RubbishRecordPresenter(this);
    }

    @Override
    public void showRubblish(Rubbish rubbish) {
        startActivity(new Intent(this, RubbishDetailActivity.class)
                .putExtra(AppConstant.RUBBISH, rubbish));
    }

    @Override
    public void onInputId(String id) {
        mPresenter.getRubbishById(id);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

}
