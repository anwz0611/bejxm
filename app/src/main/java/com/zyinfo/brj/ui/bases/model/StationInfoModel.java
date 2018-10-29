package com.zyinfo.brj.ui.bases.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.stationInfoBeans;
import com.zyinfo.brj.ui.bases.contract.StationInfoContract;
import com.zyinfo.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class StationInfoModel implements StationInfoContract.Model {

    @Override
    public Observable<List<stationInfoBeans>> getStationInfoData(String keyWord,String stationType, String pageSize, String pageNum) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getStationInfo(keyWord,stationType,pageSize,pageNum)
                .compose(RxSchedulers.<List<stationInfoBeans>>io_main());
    }
}
