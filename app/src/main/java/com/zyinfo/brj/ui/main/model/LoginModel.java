package com.zyinfo.brj.ui.main.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.LoginsBeans;
import com.zyinfo.brj.ui.main.contract.LoginContract;
import com.zyinfo.common.baserx.RxSchedulers;

import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<LoginsBeans> getLoginStateData(String loginid, String loginpwd) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).login(loginid,loginpwd)
                .compose(RxSchedulers.<LoginsBeans>io_main());
    }
}
