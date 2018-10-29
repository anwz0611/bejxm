package com.zyinfo.brj.ui.news.contract;


import com.zyinfo.brj.bean.waterSituation;
import com.zyinfo.common.base.BaseModel;
import com.zyinfo.common.base.BasePresenter;
import com.zyinfo.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public interface WaterSituationContract {

    interface Model extends BaseModel {
        //请求历史河道水情列表
        Observable<List<waterSituation>> getWaterSituationData(String newDate,String historyDate,  String flag, String pageSize,String pageNum,  String sortContent, String sortType,String wptn,String keyWord);
    }
    interface View extends BaseView {
        //返回获取的历史河道水情列表
        void returnWaterSituationData(List<waterSituation> waterSituations);
        //返回顶部
        void scrolltoTop();




    }

    abstract static class Presenter extends BasePresenter<WaterSituationContract.View, WaterSituationContract.Model> {
        //发起获取水情请求
        public abstract void getWaterSituationDataRequest(String newDate,String historyDate,  String flag, String pageSize,String pageNum,  String sortContent, String sortType,String wptn,String keyWord);

    }
}
