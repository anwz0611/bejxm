package com.zyinfo.brj.ui.main.presenter;

import android.text.TextUtils;

import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.LoginsBeans;
import com.zyinfo.brj.ui.main.contract.LoginContract;
import com.zyinfo.common.baserx.RxSubscriber;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class LoginPresenter extends LoginContract.Presenter{


    @Override
    public void getLoginStateRequest(String loginid, String loginpwd) {

        if (loginid ==null ||TextUtils.isEmpty(loginid)){
            mView.accountIsNull();
            return;
        }
        if (loginpwd ==null ||TextUtils.isEmpty(loginpwd)) {
            mView.passWordIsNull();
            return;
        }

        mRxManage.add(mModel.getLoginStateData(loginid,loginpwd).subscribe(new RxSubscriber<LoginsBeans>(mContext, false) {
            @Override
            protected void _onNext(LoginsBeans LoginState){

                mView.returnLoginState(LoginState);
                mView.stopLoading();
            }

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
