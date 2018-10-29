package com.zyinfo.brj.ui.more.presenter;

import com.zyinfo.brj.R;
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.PolicyLawInfoBeans;
import com.zyinfo.brj.ui.more.contract.PolicyLawInfoContract;
import com.zyinfo.common.baserx.RxSubscriber;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class PolicyLawInfoPresenter extends PolicyLawInfoContract.Presenter {
    @Override
    public void getPolicyLawInfoDataRequest(String pageSize, String pageNum) {
        mRxManage.add(mModel.getPolicyLawInfoData(pageSize,pageNum).subscribe(new RxSubscriber<List<PolicyLawInfoBeans>>(mContext, false) {
            @Override
            protected void _onNext(List<PolicyLawInfoBeans> policyLawInfoBeans) {
                mView.returnPolicyLawInfoData(policyLawInfoBeans);
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
