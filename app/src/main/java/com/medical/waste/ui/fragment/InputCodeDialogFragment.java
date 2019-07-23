package com.medical.waste.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.dialog_fragment_input_code)
public class InputCodeDialogFragment extends BaseDialogFragment {
    @BindView(R.id.content)
    AppCompatEditText mContent;
    private ConfirmCallback confirmCallback;

    public static InputCodeDialogFragment newInstance() {
        Bundle args = new Bundle();
        InputCodeDialogFragment fragment = new InputCodeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ConfirmCallback) {
            this.confirmCallback = (ConfirmCallback) context;
        }
    }

    public InputCodeDialogFragment setConfirmCallback(ConfirmCallback confirmCallback) {
        this.confirmCallback = confirmCallback;
        return this;
    }


    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
    }

    @OnClick({R.id.confirm, R.id.close})
    void onClickBtn(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                confirm();
                break;
            case R.id.close:
                dismissAllowingStateLoss();
                break;
        }
    }

    //确认
    private void confirm() {
        String content = mContent.getText().toString().trim();
        if (TextUtils.isEmpty(content) || content.length() != 14 || !content.startsWith("AA")) {
            toast(R.string.input_code_error);
            return;
        }
        if (confirmCallback != null) {
            confirmCallback.onConfirm(content);
        }
        dismissAllowingStateLoss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        return dialog;
    }


    public interface ConfirmCallback {
        void onConfirm(String content);
    }
}
