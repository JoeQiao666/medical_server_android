package com.medical.waste.module.contract;

import com.medical.waste.base.BasePresenter;
import com.medical.waste.base.BaseView;
import com.medical.waste.bean.Department;
import com.medical.waste.bean.Result;
import com.medical.waste.bean.UploadData;
import com.medical.waste.callback.RequestCallback;

import java.util.List;

public interface ContinueUploadContract {
    interface Model{
        void upload(List<UploadData> uploadDatas, RequestCallback<Result> callback);
    }
    interface Presenter extends BasePresenter {
        void upload(List<UploadData> uploadDatas);
    }
    interface View extends BaseView {
        void showUploadResult(Result result);
    }
}
