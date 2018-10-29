package com.zyinfo.brj.ui.bases.presenter;

import com.zyinfo.brj.R;
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.userInfoBeans;
import com.zyinfo.brj.ui.bases.contract.UserInfoContract;
import com.zyinfo.common.baserx.RxSubscriber;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class UserInfoPresenter extends UserInfoContract.Presenter {
    @Override
    public void getUserInfoDataRequest(String keyWord,String pageSize, String pageNum) {
        mRxManage.add(mModel.getUserInfoData(keyWord,pageSize,pageNum).subscribe(new RxSubscriber<List<userInfoBeans>>(mContext, false) {
            @Override
            protected void _onNext(List<userInfoBeans> userInfoBeans) {
                mView.returnUserInfoData(userInfoBeans);
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

    @Override
    public void onStart() {
        super.onStart();

        //监听返回顶部动作
        mRxManage.on(AppConstant.NEWS_LIST_TO_TOP, new Action1<Object>() {
            @Override
            public void call(Object o) {
                mView.scrolltoTop();
            }
        });
    }


}
