package com.zyinfo.brj.ui.news.presenter;

import com.zyinfo.brj.R;
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.waterSituation;
import com.zyinfo.brj.ui.news.contract.WaterSituationDetailContract;
import com.zyinfo.common.baserx.RxSubscriber;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Zwei  on 2018/5/31.
 * E-Mail Address：592296083@qq.com
 */

public class WaterSituationDetailPresenter extends WaterSituationDetailContract.Presenter {

    @Override
    public void getWaterSituationDetailDataRequest(String stcd, String sttp, String startTime, String endTime) {
        mRxManage.add(mModel.getWaterSituationDetailData(stcd,sttp,startTime,endTime).subscribe(new RxSubscriber<List<waterSituation>>(mContext, false) {
            @Override
            protected void _onNext(List<waterSituation> waterSituations) {
                mView.returnWaterSituationDetailData(waterSituations);
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