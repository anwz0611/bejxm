package com.zyinfo.brj.ui.news.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.waterSituation;
import com.zyinfo.brj.ui.news.contract.WaterSituationDetailContract;
import com.zyinfo.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;

/**
 * Created by Zwei  on 2018/5/31.
 * E-Mail Address：592296083@qq.com
 */

public class WaterSituationDetailModel implements WaterSituationDetailContract.Model {
    @Override
    public Observable<List<waterSituation>> getWaterSituationDetailData( String stcd, String sttp, String startTime, String endTime) {
        return  Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getwaterSituationDetail(stcd,sttp,startTime,endTime)
                //转化时间


                .compose(RxSchedulers.<List<waterSituation>>io_main());
    }
}
