package com.medical.waste.module.contract;

import com.medical.waste.base.BasePresenter;
import com.medical.waste.base.BaseView;
import com.medical.waste.bean.Rubbish;
import com.medical.waste.bean.RubbishType;
import com.medical.waste.callback.RequestCallback;

import java.util.List;

public interface RubbishRecordContract {
    interface Model{
        void getRubbishById(String id,RequestCallback<Rubbish> callback);
        void getRubbishRecords(String start,String end,RequestCallback<List<Rubbish>> callback);
    }
    interface Presenter extends BasePresenter {
        void getRubbishById(String id);
    }
    interface RubbishRecordListPresenter extends BasePresenter {
        void getRubbishRecords(String start,String end);
    }
    interface View extends BaseView {
        void showRubblish(Rubbish rubbish);
    }
    interface RubbishRecordListView extends BaseView {
        void showRubblishs(List<Rubbish> data);
    }
}
