package com.zyinfo.brj.ui.news.presenter;

import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.NewsChannelTable;
import com.zyinfo.brj.ui.news.contract.NewsChannelContract;
import com.zyinfo.common.baserx.RxSubscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * des:新闻频道
 * Created by xsf
 * on 2016.09.17:43
 */
public class NewsChanelPresenter extends NewsChannelContract.Presenter{
    @Override
    public void lodeChannelsRequest() {
        mRxManage.add(mModel.lodeMineNewsChannels().subscribe(new RxSubscriber<List<NewsChannelTable>>(mContext,false) {
            @Override
            protected void _onNext(List<NewsChannelTable> newsChannelTables) {
               mView.returnMineNewsChannels(newsChannelTables);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
        mRxManage.add(mModel.lodeMoreNewsChannels().subscribe(new RxSubscriber<List<NewsChannelTable>>(mContext,false) {
            @Override
            protected void _onNext(List<NewsChannelTable> newsChannelTables) {
                mView.returnMoreNewsChannels(newsChannelTables);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

    @Override
    public void onItemSwap(final ArrayList<NewsChannelTable> newsChannelTableList, int fromPosition, int toPosition) {
        mRxManage.add( mModel.swapDb(newsChannelTableList,fromPosition,toPosition).subscribe(new RxSubscriber<String>(mContext,false) {
            @Override
            protected void _onNext(String s) {
                mRxManage.post(AppConstant.NEWS_CHANNEL_CHANGED,newsChannelTableList);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
       ;
    }

    @Override
    public void onItemAddOrRemove(final ArrayList<NewsChannelTable> mineChannelTableList, ArrayList<NewsChannelTable> moreChannelTableList) {
        mRxManage.add(mModel.updateDb(mineChannelTableList,moreChannelTableList).subscribe(new RxSubscriber<String>(mContext,false) {
            @Override
            protected void _onNext(String s) {
             mRxManage.post(AppConstant.NEWS_CHANNEL_CHANGED,mineChannelTableList);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
