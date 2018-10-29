package com.zyinfo.brj.ui.main.contract;

import com.zyinfo.brj.bean.NewsChannelTable;
import com.zyinfo.common.base.BaseModel;
import com.zyinfo.common.base.BasePresenter;
import com.zyinfo.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * des:
 */
public interface NewsMainContract {

    interface Model extends BaseModel {
        Observable< List<NewsChannelTable> > lodeMineNewsChannels();
    }

    interface View extends BaseView {
        void returnMineNewsChannels(List<NewsChannelTable> newsChannelsMine);
    }
    abstract static class Presenter extends BasePresenter<View, Model> {
        public abstract void lodeMineChannelsRequest();
    }
}
