package com.zyinfo.brj.ui.main.presenter;

import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.NewsChannelTable;
import com.zyinfo.brj.ui.main.contract.NewsMainContract;
import com.zyinfo.common.baserx.RxSubscriber;

import java.util.List;

import rx.functions.Action1;

/**
 * des:
 *
 *
 */
public class NewsMainPresenter extends NewsMainContract.Presenter{

    @Override
    public void onStart() {
        super.onStart();
        //频道变化刷新
        mRxManage.on(AppConstant.NEWS_CHANNEL_CHANGED, new Action1<List<NewsChannelTable>>() {

            @Override
            public void call(List<NewsChannelTable> newsChannelTables) {
                if(newsChannelTables!=null){
                    mView.returnMineNewsChannels(newsChannelTables);
                }
            }
        });
    }

    @Override
    public void lodeMineChannelsRequest() {
        mRxManage.add(mModel.lodeMineNewsChannels().subscribe(new RxSubscriber<List<NewsChannelTable>>(mContext,false) {
            @Override
            protected void _onNext(List<NewsChannelTable> newsChannelTables) {
                mView.returnMineNewsChannels(newsChannelTables);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
