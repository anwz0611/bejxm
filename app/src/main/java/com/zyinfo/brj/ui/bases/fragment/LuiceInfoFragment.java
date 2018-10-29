package com.zyinfo.brj.ui.bases.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.AssociationInfoBeans;
import com.zyinfo.brj.bean.EventBusBeans;
import com.zyinfo.brj.bean.sluiceInfoBean;
import com.zyinfo.brj.ui.bases.contract.LuiceInfoContract;
import com.zyinfo.brj.ui.bases.model.LuiceInfoModel;
import com.zyinfo.brj.ui.bases.presenter.LuiceInfoPresenter;
import com.zyinfo.common.base.BaseFragment;
import com.zyinfo.common.commonwidget.LoadingTip;
import com.zyinfo.common.commonwidget.NormalTitleBar;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Zwei  on 2018/6/7.
 * E-Mail Address：592296083@qq.com
 */

public class LuiceInfoFragment extends BaseFragment<LuiceInfoPresenter, LuiceInfoModel> implements LuiceInfoContract.View, OnRefreshListener, OnLoadMoreListener {
    @Bind(R.id.irc)
    IRecyclerView irc;
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.ntb)
    NormalTitleBar ntb;
//    @Bind(R.id.fab)
//    FloatingActionButton fab;

    private int mStartPage = 1;
    private String pageNum = "10";
    private CommonRecycleViewAdapter<sluiceInfoBean> WSListAdapter;
    private List<AssociationInfoBeans> datas = new ArrayList<>();


    @Override
    public void initView() {
//        ntb.setTitleText(getString(R.string.xiehui));
        ntb.setVisibility(View.GONE);
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
        irc.setLayoutManager(new LinearLayoutManager(getContext()));


//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                irc.smoothScrollToPosition(0);
//            }
//        });
        WSListAdapter = new CommonRecycleViewAdapter<sluiceInfoBean>(getContext(), R.layout.luiceinfo_list_item) {

            @Override
            public void convert(ViewHolderHelper helper, sluiceInfoBean sluiceInfoBean) {
                helper.setText(R.id.totalp, sluiceInfoBean.getWgName());

                if (sluiceInfoBean.getStrobenum() == null) {
                    helper.setText(R.id.area, "");
                } else {
                    helper.setText(R.id.area, sluiceInfoBean.getStrobenum() + "个");
                }
                helper.setText(R.id.textView5, "管理单位：" + sluiceInfoBean.getOffice());
                helper.setText(R.id.textView8, sluiceInfoBean.getSzhl() + "");


            }
        };



        WSListAdapter.openLoadAnimation(new ScaleInAnimation());
        irc.setAdapter(WSListAdapter);
        irc.setOnRefreshListener(this);
        irc.setOnLoadMoreListener(this);

        //数据为空才重新发起请求
        if (WSListAdapter.getSize() <= 0) {
            mStartPage = 1;
            mPresenter.getLuiceInfoDataRequest("",mStartPage + "", pageNum);
        }
    }

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext) {
        Intent intent = new Intent(mContext, LuiceInfoFragment.class);

        mContext.startActivity(intent);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.luiceinfo_activity;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void returnLuiceInfoData(List<sluiceInfoBean> sluiceInfoBean) {
        if (sluiceInfoBean != null) {
            mStartPage += 1;
            if (WSListAdapter.getPageBean().isRefresh()) {
                irc.setRefreshing(false);
                WSListAdapter.replaceAll(sluiceInfoBean);
            } else {
                if (sluiceInfoBean.size() > 0) {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    WSListAdapter.addAll(sluiceInfoBean);
                } else {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        WSListAdapter.getPageBean().setRefresh(true);
        mStartPage = 1;
        //发起请求
        irc.setRefreshing(true);
        mPresenter.getLuiceInfoDataRequest("",mStartPage + "", pageNum);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        WSListAdapter.getPageBean().setRefresh(false);
        //发起请求0000
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getLuiceInfoDataRequest("",mStartPage + "", pageNum);

    }

    @Override
    public void showLoading(String title) {
        if (WSListAdapter.getPageBean().isRefresh()) {
            if (WSListAdapter.getSize() <= 0) {
                loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
            }
        }
    }

    @Override
    public void stopLoading() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
    }

    @Override
    public void showErrorTip(String msg) {
        if (WSListAdapter.getPageBean().isRefresh()) {
            if (WSListAdapter.getSize() <= 0) {
                loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
                loadedTip.setTips(msg);
            }
            irc.setRefreshing(false);
        } else {
            irc.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }


    @Override
    public void scrolltoTop() {
        irc.smoothScrollToPosition(0);
    }

    @OnClick({R.id.tv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:

                break;
        }
    }

    @Subscribe
    public void onEvent(EventBusBeans eb) {
        if (eb.getFlag()==2){
            WSListAdapter.getPageBean().setRefresh(true);
            mStartPage = 1;
            //发起请求
            irc.setRefreshing(true);
            mPresenter.getLuiceInfoDataRequest(eb.getData(),mStartPage + "", pageNum);}

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
