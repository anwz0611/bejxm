package com.zyinfo.brj.ui.bases.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.sluiceInfoBean;
import com.zyinfo.brj.ui.bases.contract.LuiceInfoContract;
import com.zyinfo.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class LuiceInfoModel implements LuiceInfoContract.Model {

    @Override
    public Observable<List<sluiceInfoBean>> getLuiceInfoData(String keyWord,String pageSize, String pageNum) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getsLuiceInfo( keyWord,pageSize,pageNum)
                .compose(RxSchedulers.<List<sluiceInfoBean>>io_main());
    }
}
