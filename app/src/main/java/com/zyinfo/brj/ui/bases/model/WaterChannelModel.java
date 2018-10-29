package com.zyinfo.brj.ui.bases.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.waterChannelBeans;
import com.zyinfo.brj.ui.bases.contract.WaterChannelContract;
import com.zyinfo.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class WaterChannelModel implements WaterChannelContract.Model {

    @Override
    public Observable<List<waterChannelBeans>> getWaterChannelDetailData(String id, String type) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getWaterChannelDetail(id,type)
                .compose(RxSchedulers.<List<waterChannelBeans>>io_main());
    }

    @Override
    public Observable<List<waterChannelBeans>> getWaterChannelData() {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getWaterChannel()
                .compose(RxSchedulers.<List<waterChannelBeans>>io_main());
    }
}
