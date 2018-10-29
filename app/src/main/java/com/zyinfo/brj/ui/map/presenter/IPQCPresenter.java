package com.zyinfo.brj.ui.map.presenter;

import com.zyinfo.brj.R;
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.IPQCbeans;
import com.zyinfo.brj.ui.map.contract.IPQCContract;
import com.zyinfo.common.baserx.RxSubscriber;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class IPQCPresenter extends IPQCContract.Presenter {
    @Override
    public void getIPQCDataRequest(String tm,String pageSize, String pageNum) {
        mRxManage.add(mModel.getIPQCData(tm,pageSize,pageNum).subscribe(new RxSubscriber<List<IPQCbeans>>(mContext, false) {
            @Override
            protected void _onNext(List<IPQCbeans> IPQCbeans) {
                mView.returnIPQCData(IPQCbeans);
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
