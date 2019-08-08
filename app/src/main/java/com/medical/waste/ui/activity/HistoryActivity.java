package com.medical.waste.ui.activity;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseActivity;
import com.medical.waste.bean.EventFilter;
import com.medical.waste.common.AppConstant;
import com.medical.waste.ui.fragment.ChooseTimeDialogFragment;
import com.medical.waste.ui.fragment.HistoryFragment;
import com.medical.waste.utils.RxBus;
import com.medical.waste.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.activity_history,
        isShowLeftBtn = false,
        toolbarTitle = R.string.history_record
)
public class HistoryActivity extends BaseActivity implements ChooseTimeDialogFragment.ConfirmCallback {
    @BindView(R.id.viewpager)
    ViewPager mViewPgaer;
    @BindView(R.id.tab_layout)
    XTabLayout mTabLayout;
    @BindView(R.id.filter)
    TextView mFilter;
    private String[] tabs = {"待入库", "待出库", "已出库"};
    private List<Fragment> fragments = new ArrayList<>();
    private String start = "";
    private String end = "";

    @Override
    protected void initView() {
        initTime();
        fragments.add(HistoryFragment.newInstance("0",start,end));
        fragments.add(HistoryFragment.newInstance("1",start,end));
        fragments.add(HistoryFragment.newInstance("2",start,end));
        mViewPgaer.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPgaer);
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        start = format.format(calendar.getTimeInMillis()) + " 00:00";
        mFilter.setText(start+" ~ 24:00");
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        end = format.format(calendar.getTimeInMillis()) + " 00:00";
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
        RxBus.get().post(AppConstant.RxEvent.FILTER, filter);
        mFilter.setText(filterContent);
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
