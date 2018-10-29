package com.zyinfo.brj.ui.bases.contract;


import com.zyinfo.brj.bean.userInfoBeans;
import com.zyinfo.common.base.BaseModel;
import com.zyinfo.common.base.BasePresenter;
import com.zyinfo.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public interface UserInfoContract {

    interface Model extends BaseModel {
        //请求历史河道水情列表
        Observable<List<userInfoBeans>> getUserInfoData(String keyWord,String pageSize, String pageNum);
    }
    interface View extends BaseView {
        //返回获取的历史河道水情列表
        void returnUserInfoData(List<userInfoBeans> userInfoBeans);
        //返回顶部
        void scrolltoTop();
        void returnKeyWord(String KeyWord);
    }

    abstract static class Presenter extends BasePresenter<UserInfoContract.View, UserInfoContract.Model> {
        //发起获取水情请求
        public abstract void getUserInfoDataRequest(String keyWord,String pageSize,String pageNum);
    }
}
