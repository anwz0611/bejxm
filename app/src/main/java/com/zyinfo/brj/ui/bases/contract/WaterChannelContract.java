package com.zyinfo.brj.ui.bases.contract;


import com.zyinfo.brj.bean.waterChannelBeans;
import com.zyinfo.common.base.BaseModel;
import com.zyinfo.common.base.BasePresenter;
import com.zyinfo.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public interface WaterChannelContract {

    interface Model extends BaseModel {
        //请求历史河道水情列表
        Observable<List<waterChannelBeans>> getWaterChannelDetailData(String id, String type);
        Observable<List<waterChannelBeans>> getWaterChannelData();
    }
    interface View extends BaseView {
        //返回获取的历史河道水情列表
        void returnWaterChannelDetailData(List<waterChannelBeans> waterChannelBeans);
        void returnWaterChannelData(List<waterChannelBeans> waterChannelBeans);
        //返回顶部
        void scrolltoTop();
    }

    abstract static class Presenter extends BasePresenter<WaterChannelContract.View, WaterChannelContract.Model> {
        //发起获取水情请求
        public abstract void getWaterChannelDetailDataRequest(String pageSize,String pageNum);
        public abstract void getWaterChannelDataRequest();
    }
}
