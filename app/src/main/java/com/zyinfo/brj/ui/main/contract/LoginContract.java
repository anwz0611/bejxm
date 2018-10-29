package com.zyinfo.brj.ui.main.contract;

import com.zyinfo.brj.bean.LoginsBeans;
import com.zyinfo.common.base.BaseModel;
import com.zyinfo.common.base.BasePresenter;
import com.zyinfo.common.base.BaseView;

import rx.Observable;

/**
 * des:
 *
 *
 */
public interface LoginContract {

    interface Model extends BaseModel {
        Observable<LoginsBeans> getLoginStateData(String loginid,String loginpwd);
    }

    interface View extends BaseView {
        void returnLoginState(LoginsBeans LoginState);

        void accountIsNull();

        void passWordIsNull();

        void loginSuccess();
    }
    abstract static class Presenter extends BasePresenter<LoginContract.View, LoginContract.Model> {
        public abstract void getLoginStateRequest(String loginid,String loginpwd);
    }
}
