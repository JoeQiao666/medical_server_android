package com.medical.waste.ui.fragment;

import android.os.Bundle;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseFragment;
@ActivityFragmentInject(contentViewId = R.layout.fragment_rubbish_scan)
public class RubbishScanFragment extends BaseFragment {
    public static RubbishScanFragment newInstance() {
        Bundle args = new Bundle();
        RubbishScanFragment fragment = new RubbishScanFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initView() {

    }
}
