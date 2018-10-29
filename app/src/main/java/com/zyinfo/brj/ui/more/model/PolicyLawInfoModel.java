package com.zyinfo.brj.ui.more.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.PolicyLawInfoBeans;
import com.zyinfo.brj.ui.more.contract.PolicyLawInfoContract;
import com.zyinfo.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class PolicyLawInfoModel implements PolicyLawInfoContract.Model {

    @Override
    public Observable<List<PolicyLawInfoBeans>> getPolicyLawInfoData(String pageSize, String pageNum) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getPolicyLawInfo(pageSize,pageNum)
                .compose(RxSchedulers.<List<PolicyLawInfoBeans>>io_main());
    }
}
