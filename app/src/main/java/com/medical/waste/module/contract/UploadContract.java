package com.medical.waste.module.contract;

import com.medical.waste.base.BasePresenter;
import com.medical.waste.base.BaseView;
import com.medical.waste.bean.Department;
import com.medical.waste.bean.RubbishType;
import com.medical.waste.callback.RequestCallback;

import java.util.List;

public interface UploadContract {
    interface Model{
        void getDepartmentById(String id,RequestCallback<Department> callback);
    }
    interface Presenter extends BasePresenter {
        void getDepartmentById(String id);
    }
    interface View extends BaseView {
        void showDepartment(Department department);
    }
}
