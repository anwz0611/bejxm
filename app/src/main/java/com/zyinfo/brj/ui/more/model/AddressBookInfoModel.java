package com.zyinfo.brj.ui.more.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;
import com.zyinfo.brj.bean.addressBookInfoBeans;
import com.zyinfo.brj.ui.more.contract.AddressBookInfoContract;
import com.zyinfo.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class AddressBookInfoModel implements AddressBookInfoContract.Model {

    @Override
    public Observable<List<addressBookInfoBeans>> getAddressBookInfoData() {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getAddressBookInfo()
                .compose(RxSchedulers.<List<addressBookInfoBeans>>io_main());
    }


}
