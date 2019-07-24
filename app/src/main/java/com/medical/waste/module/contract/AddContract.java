package com.medical.waste.module.contract;

import com.medical.waste.base.BasePresenter;
import com.medical.waste.base.BaseView;
import com.medical.waste.bean.LoginData;
import com.medical.waste.bean.Result;
import com.medical.waste.bean.RubbishType;
import com.medical.waste.callback.RequestCallback;

import java.util.List;

public interface AddContract {
    interface Model{
        void getRubbishTypes(RequestCallback<List<RubbishType>> callback);
    }
    interface Presenter extends BasePresenter {
        void getRubbishTypes();
    }
    interface View extends BaseView {
        void showRubbishTypes(List<RubbishType> rubbishTypes);
    }
}
