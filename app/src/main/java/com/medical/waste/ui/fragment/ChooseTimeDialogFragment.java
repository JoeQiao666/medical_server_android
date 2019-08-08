package com.medical.waste.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.medical.waste.R;
import com.medical.waste.annotation.ActivityFragmentInject;
import com.medical.waste.base.BaseDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

@ActivityFragmentInject(contentViewId = R.layout.dialog_fragment_choose_time)
public class ChooseTimeDialogFragment extends BaseDialogFragment {
    @BindView(R.id.tab_time)
    View mTabTime;
    @BindView(R.id.tab_date)
    View mTabDate;
    @BindView(R.id.start)
    TextView mStart;
    @BindView(R.id.end)
    TextView mEnd;
    private ConfirmCallback confirmCallback;
    //type 0按时间，1按日期
    private int type = 0;
    private long startTime = 0;
    private long endTime = 0;

    public static ChooseTimeDialogFragment newInstance() {
        Bundle args = new Bundle();
        ChooseTimeDialogFragment fragment = new ChooseTimeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        chooseTab(type);
    }

    private void chooseTab(int type) {
        this.type = type;
        mStart.setText("");
        mEnd.setText("");
        startTime = 0;
        endTime = 0;
        if (type == 0) {
            mTabTime.setSelected(true);
            mTabDate.setSelected(false);
            mStart.setHint(R.string.start_time);
            mEnd.setHint(R.string.end_time);
        } else {
            mTabTime.setSelected(false);
            mTabDate.setSelected(true);
            mStart.setHint(R.string.start_date);
            mEnd.setHint(R.string.end_date);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ConfirmCallback) {
            this.confirmCallback = (ConfirmCallback) context;
        }
    }

    public ChooseTimeDialogFragment setConfirmCallback(ConfirmCallback confirmCallback) {
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

    @OnClick({R.id.start, R.id.end, R.id.confirm, R.id.tab_time, R.id.tab_date})
    void onClickBtn(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                confirm();
                break;
            case R.id.start:
                chooseTime(true);
                break;
            case R.id.end:
                chooseTime(false);
                break;
            case R.id.tab_time:
                if (!mTabTime.isSelected()) {
                    chooseTab(0);
                }
                break;
            case R.id.tab_date:
                if (!mTabDate.isSelected()) {
                    chooseTab(1);
                }
                break;
        }
    }

    private void chooseTime(boolean isStart) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, -1);
        if (type == 0) {
            new TimePickerDialog(getActivity(), (view, hourOfDay, minute) -> {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                if (isStart) {
                    startTime = calendar.getTimeInMillis();
                    mStart.setText(format.format(calendar.getTimeInMillis()));
                } else {
                    endTime = calendar.getTimeInMillis();
                    mEnd.setText(format.format(calendar.getTimeInMillis()));
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        } else {
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                if (isStart) {
                    mStart.setText(format.format(calendar.getTimeInMillis()));
                    startTime = calendar.getTimeInMillis();
                } else {
                    mEnd.setText(format.format(calendar.getTimeInMillis()));
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    endTime = calendar.getTimeInMillis();
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            dialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());
            dialog.show();
        }
    }

    //确认
    private void confirm() {
        if (startTime == 0) {
            toast(mTabTime.isSelected() ? R.string.input_start_time : R.string.input_start_date);
            return;
        }
        if (endTime == 0) {
            toast(mTabTime.isSelected() ? R.string.input_end_time : R.string.input_end_date);
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String filterContent;
        if (type == 0) {
            filterContent = format1.format(calendar.getTimeInMillis()) + " " + mStart.getText().toString() + " ~ " + mEnd.getText().toString();
        } else {
            filterContent = mStart.getText().toString() + " ~ " + mEnd.getText().toString();
        }
        if (confirmCallback != null) {
            confirmCallback.onConfirm(type, format.format(startTime), format.format(endTime), filterContent);
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
        void onConfirm(int type, String start, String end, String filterContent);
    }
}
