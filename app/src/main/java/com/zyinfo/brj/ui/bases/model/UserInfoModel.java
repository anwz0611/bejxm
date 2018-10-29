package com.zyinfo.brj.ui.bases.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.userInfoBeans;
import com.zyinfo.brj.ui.bases.contract.UserInfoContract;
import com.zyinfo.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class UserInfoModel implements UserInfoContract.Model {

    @Override
    public Observable<List<userInfoBeans>> getUserInfoData(String keyWord,String pageSize, String pageNum) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getUserInfo(keyWord,pageSize,pageNum)
                .compose(RxSchedulers.<List<userInfoBeans>>io_main());
    }
}
