package com.zyinfo.brj.ui.more.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
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
import com.zyinfo.brj.bean.PolicyLawInfoBeans;
import com.zyinfo.brj.ui.more.contract.PolicyLawInfoContract;
import com.zyinfo.brj.ui.more.model.PolicyLawInfoModel;
import com.zyinfo.brj.ui.more.presenter.PolicyLawInfoPresenter;
import com.zyinfo.common.base.BaseActivity;
import com.zyinfo.common.commonwidget.LoadingTip;
import com.zyinfo.common.commonwidget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Zwei  on 2018/6/7.
 * E-Mail Address：592296083@qq.com
 */

public class PolicyLawInfoActivity extends BaseActivity<PolicyLawInfoPresenter, PolicyLawInfoModel> implements PolicyLawInfoContract.View, OnRefreshListener, OnLoadMoreListener {
    @Bind(R.id.irc)
    IRecyclerView irc;
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.ntb)
    NormalTitleBar ntb;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private int mStartPage = 1;
    private String pageNum = "10";
    private CommonRecycleViewAdapter<PolicyLawInfoBeans> WSListAdapter;
    private List<AssociationInfoBeans> datas = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.policylawinfo_activity;
    }

    @Override
    public void initView() {
        ntb.setTitleText(getString(R.string.zhengce));
        irc.setLayoutManager(new LinearLayoutManager(mContext));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irc.smoothScrollToPosition(0);
            }
        });
        WSListAdapter = new CommonRecycleViewAdapter<PolicyLawInfoBeans>(mContext, R.layout.policylawinfo_list_item) {

            @Override
            public void convert(ViewHolderHelper helper, PolicyLawInfoBeans policyLawInfoBeans) {
                helper.setText(R.id.textView9, policyLawInfoBeans.getFilename());




            }
        };
        WSListAdapter.openLoadAnimation(new ScaleInAnimation());
        irc.setAdapter(WSListAdapter);
        irc.setOnRefreshListener(this);
        irc.setOnLoadMoreListener(this);

        //数据为空才重新发起请求
        if (WSListAdapter.getSize() <= 0) {
            mStartPage = 1;
            mPresenter.getPolicyLawInfoDataRequest(mStartPage + "", pageNum);
        }
    }

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext) {
        Intent intent = new Intent(mContext, PolicyLawInfoActivity.class);

        mContext.startActivity(intent);

    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void returnPolicyLawInfoData(List<PolicyLawInfoBeans> policyLawInfoBeans) {
        if (policyLawInfoBeans != null) {
            mStartPage += 1;
            if (WSListAdapter.getPageBean().isRefresh()) {
                irc.setRefreshing(false);
                WSListAdapter.replaceAll(policyLawInfoBeans);
            } else {
                if (policyLawInfoBeans.size() > 0) {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    WSListAdapter.addAll(policyLawInfoBeans);
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
        mPresenter.getPolicyLawInfoDataRequest(mStartPage + "", pageNum);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        WSListAdapter.getPageBean().setRefresh(false);
        //发起请求0000
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getPolicyLawInfoDataRequest(mStartPage + "", pageNum);

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
                finish();
                break;
        }
    }
}
