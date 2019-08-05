package com.medical.waste.module.presenter;



import com.medical.waste.base.BasePresenterImpl;
import com.medical.waste.bean.LoginData;
import com.medical.waste.callback.RequestCallback;
import com.medical.waste.callback.SimpleCallback;
import com.medical.waste.module.contract.UserContract;
import com.medical.waste.module.model.UserModel;


public class UserPresenter extends BasePresenterImpl<UserContract.View, UserModel> implements UserContract.Presenter {

    public UserPresenter(UserContract.View view) {
        super(view);
        mModel = new UserModel(this);
    }


    @Override
    public void login(String cardId) {
        mModel.login(cardId, new SimpleCallback<LoginData>(mView) {
            @Override
            public void requestSuccess(LoginData data) {
                mView.loginSuccess(data);
            }
        });
    }

    @Override
    public void confirm(String cardId) {
        mModel.confirm(cardId, new SimpleCallback<LoginData>(mView) {
            @Override
            public void requestSuccess(LoginData data) {
                mView.loginSuccess(data);
            }
        });
    }
}
