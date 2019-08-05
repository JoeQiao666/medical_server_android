package com.medical.waste.module.contract;

import com.medical.waste.base.BasePresenter;
import com.medical.waste.base.BaseView;
import com.medical.waste.bean.InListData;
import com.medical.waste.bean.Result;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.bean.UploadData;
import com.medical.waste.callback.RequestCallback;

import java.util.List;
import java.util.Map;

public interface InOutContract {
    interface Model{
        void getRubbishs(Map<String,String> params, RequestCallback<InListData> callback);
        void store(Map<String,String> params, RequestCallback<Result> callback);
        void recycle(Map<String,String> params, RequestCallback<Result> callback);
    }
    interface Presenter extends BasePresenter {
        void getRubbishs(Map<String,String> params);
    }
    interface InPresenter extends Presenter {
        void store(Map<String,String> params);
    }
    interface InView extends BaseView {
        void showRubbish(InListData data);
        void showStoreResult(Result result);
    }
    interface OutPresenter extends Presenter {
        void recycle(Map<String,String> params);
    }
    interface OutView extends BaseView {
        void showRubbish(InListData data);
        void showRecycleResult(Result result);
    }
}
