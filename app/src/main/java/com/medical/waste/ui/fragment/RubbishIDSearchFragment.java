package com.medical.waste.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.fragment_rubbish_id_search)
public class RubbishIDSearchFragment extends BaseFragment {
    @BindView(R.id.edit)
    AppCompatEditText mEdit;
    private OnInputIdListener onInputIdListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnInputIdListener){
            onInputIdListener = (OnInputIdListener) context;
        }
    }

    public static RubbishIDSearchFragment newInstance() {
        Bundle args = new Bundle();
        RubbishIDSearchFragment fragment = new RubbishIDSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initView() {

    }
    @OnClick(R.id.search)
    void search() {
        String id = mEdit.getText().toString();
        if(TextUtils.isEmpty(id)){
            toast(R.string.input_id);
            return;
        }
        onInputIdListener.onInputId(id);
    }
    public interface OnInputIdListener{
        void onInputId(String id);
    }
}
