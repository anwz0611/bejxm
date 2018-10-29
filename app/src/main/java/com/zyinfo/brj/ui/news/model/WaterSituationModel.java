package com.zyinfo.brj.ui.news.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.waterSituation;
import com.zyinfo.brj.ui.news.contract.WaterSituationContract;
import com.zyinfo.common.baserx.RxSchedulers;

import java.util.List;
import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class WaterSituationModel implements WaterSituationContract.Model {
    @Override
    public Observable<List<waterSituation>> getWaterSituationData(String newDate,String historyDate, String flag, String pageSize, String pageNum, String sortContent, String sortType,String wptn,String keyWord) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getWaterSituation(newDate,historyDate,flag,pageSize,pageNum,sortContent,sortType, wptn, keyWord)
                .compose(RxSchedulers.<List<waterSituation>>io_main());


    }
}
