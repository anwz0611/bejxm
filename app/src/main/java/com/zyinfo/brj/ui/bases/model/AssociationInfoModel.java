package com.zyinfo.brj.ui.bases.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.AssociationInfoBeans;
import com.zyinfo.brj.ui.bases.contract.AssociationInfoContract;
import com.zyinfo.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class AssociationInfoModel implements AssociationInfoContract.Model {

    @Override
    public Observable<List<AssociationInfoBeans>> getAssociationInfoData( String keyWord,String pageSize, String pageNum) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getAssociationInfo(keyWord,pageSize,pageNum)
                .compose(RxSchedulers.<List<AssociationInfoBeans>>io_main());
    }
}
