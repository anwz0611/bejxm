package com.zyinfo.brj.ui.more.contract;


import com.zyinfo.brj.bean.addressBookInfoBeans;
import com.zyinfo.common.base.BaseModel;
import com.zyinfo.common.base.BasePresenter;
import com.zyinfo.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public interface AddressBookInfoContract {

    interface Model extends BaseModel {
        //请求历史河道水情列表
        Observable<List<addressBookInfoBeans>> getAddressBookInfoData();
    }
    interface View extends BaseView {
        //返回获取的历史河道水情列表
        void returnAddressBookInfoData(List<addressBookInfoBeans> addressBookInfoBeans);
        //返回顶部

    }

    abstract static class Presenter extends BasePresenter<View, Model> {
        //发起获取水情请求
        public abstract void getAddressBookInfoDataRequest();
    }
}
