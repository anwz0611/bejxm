package com.zyinfo.brj.ui.bases.presenter;

import com.zyinfo.brj.R;
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.waterChannelBeans;
import com.zyinfo.brj.ui.bases.contract.WaterChannelContract;
import com.zyinfo.common.baserx.RxSubscriber;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class WaterChannelPresenter extends WaterChannelContract.Presenter {
    @Override
    public void getWaterChannelDetailDataRequest(String id, String type) {
        mRxManage.add(mModel.getWaterChannelDetailData(id,type).subscribe(new RxSubscriber<List<waterChannelBeans>>(mContext, false) {
            @Override
            protected void _onNext(List<waterChannelBeans> waterChannelBeans) {
                mView.returnWaterChannelDetailData(waterChannelBeans);
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
    public void getWaterChannelDataRequest() {
        mRxManage.add(mModel.getWaterChannelData().subscribe(new RxSubscriber<List<waterChannelBeans>>(mContext, false) {
            @Override
            protected void _onNext(List<waterChannelBeans> waterChannelBeans) {
                mView.returnWaterChannelData(waterChannelBeans);
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
