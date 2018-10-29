package com.zyinfo.brj.ui.bases.contract;


import com.zyinfo.brj.bean.stationInfoBeans;
import com.zyinfo.common.base.BaseModel;
import com.zyinfo.common.base.BasePresenter;
import com.zyinfo.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public interface StationInfoContract {

    interface Model extends BaseModel {
        //请求历史河道水情列表
        Observable<List<stationInfoBeans>> getStationInfoData(String keyWord,String stationType,String pageSize, String pageNum);
    }
    interface View extends BaseView {
        //返回获取的历史河道水情列表
        void returnStationInfoData(List<stationInfoBeans> stationInfoBeans);
        //返回顶部
        void scrolltoTop();
    }

    abstract static class Presenter extends BasePresenter<StationInfoContract.View, StationInfoContract.Model> {
        //发起获取水情请求
        public abstract void getStationInfoDataRequest(String keyWord,String stationType,String pageSize,String pageNum);
    }
}
