package com.zyinfo.brj.ui.main.model;

import com.zyinfo.brj.app.AppApplication;
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.NewsChannelTable;
import com.zyinfo.brj.db.NewsChannelTableManager;
import com.zyinfo.brj.ui.main.contract.NewsMainContract;
import com.zyinfo.common.baserx.RxSchedulers;
import com.zyinfo.common.commonutils.ACache;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * des:
 *
 *
 */
public class NewsMainModel implements NewsMainContract.Model {
    @Override
    public Observable<List<NewsChannelTable>> lodeMineNewsChannels() {

        return Observable.create(new Observable.OnSubscribe<List<NewsChannelTable>>() {
            @Override
            public void call(Subscriber<? super List<NewsChannelTable>> subscriber) {
                ArrayList<NewsChannelTable> newsChannelTableList = (ArrayList<NewsChannelTable>) ACache.get(AppApplication.getAppContext()).getAsObject(AppConstant.CHANNEL_MINE);
               if(newsChannelTableList==null){
                   newsChannelTableList= (ArrayList<NewsChannelTable>) NewsChannelTableManager.loadNewsChannelsStatic();
                   ACache.get(AppApplication.getAppContext()).put(AppConstant.CHANNEL_MINE,newsChannelTableList);
               }
                subscriber.onNext(newsChannelTableList);
                subscriber.onCompleted();
            }
        }).compose(RxSchedulers.<List<NewsChannelTable>>io_main());
    }
}
