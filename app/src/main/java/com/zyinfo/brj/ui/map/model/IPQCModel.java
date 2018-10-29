package com.zyinfo.brj.ui.map.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.IPQCbeans;
import com.zyinfo.brj.ui.map.contract.IPQCContract;
import com.zyinfo.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class IPQCModel implements IPQCContract.Model {

    @Override
    public Observable<List<IPQCbeans>> getIPQCData(String tm,String pageSize, String pageNum) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getIPQC(tm,pageSize,pageNum)
                .compose(RxSchedulers.<List<IPQCbeans>>io_main());
    }
}
