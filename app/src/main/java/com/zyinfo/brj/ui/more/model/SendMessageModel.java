package com.zyinfo.brj.ui.more.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.SendMessageBeans;
import com.zyinfo.brj.ui.more.contract.SendMessageContract;
import com.zyinfo.common.baserx.RxSchedulers;

import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class SendMessageModel implements SendMessageContract.Model {

    @Override
    public Observable<SendMessageBeans> SendMessage(String phoneList, String content) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).sendMessage(phoneList,content)
                .compose(RxSchedulers.<SendMessageBeans>io_main());
    }

}
