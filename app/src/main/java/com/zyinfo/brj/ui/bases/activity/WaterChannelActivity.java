package com.zyinfo.brj.ui.bases.activity;

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
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.waterChannelBeans;
import com.zyinfo.brj.ui.bases.contract.WaterChannelContract;
import com.zyinfo.brj.ui.bases.model.WaterChannelModel;
import com.zyinfo.brj.ui.bases.presenter.WaterChannelPresenter;
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

public class WaterChannelActivity extends BaseActivity<WaterChannelPresenter, WaterChannelModel> implements WaterChannelContract.View, OnRefreshListener, OnLoadMoreListener {
    @Bind(R.id.irc)
    IRecyclerView irc;
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    String id1;
//    @Bind(R.id.fab)
//    FloatingActionButton fab;
    private int mStartPage = 1;
    private String pageNum = "10";
    private CommonRecycleViewAdapter<waterChannelBeans> WSListAdapter;
    private List<waterChannelBeans> datas = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.associationinfo_activity;
    }

    @Override
    public void initView() {
        id1 = getIntent().getStringExtra(AppConstant.ID);
        ntb.setTitleText(getIntent().getStringExtra(AppConstant.NAME)+"");
        irc.setLayoutManager(new LinearLayoutManager(mContext));
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                irc.smoothScrollToPosition(0);
//            }
//        });
        WSListAdapter = new CommonRecycleViewAdapter<waterChannelBeans>(mContext, R.layout.waterchannel_list_item) {

            @Override
            public void convert(ViewHolderHelper helper, waterChannelBeans waterChannelBeans) {
                helper.setText(R.id.admn, waterChannelBeans.getName());
                if (waterChannelBeans.getCoefficient().equals("") || !waterChannelBeans.getCoefficient().equals("0")) {
                    helper.setText(R.id.textView4, "利用系数：" + waterChannelBeans.getCoefficient());
                } else

                {
                    helper.setText(R.id.textView4, "利用系数：" +"");
                }

                if (waterChannelBeans.getType().equals("") || !waterChannelBeans.getType().equals("0")) {




                    helper.setText(R.id.textView6, "渠道类型：" + "斗渠"+"("+waterChannelBeans.getType()+")");
                } else

                {
                    helper.setText(R.id.textView6, "渠道类型：" +"");
                }
                if (waterChannelBeans.getCnLength().equals("") || !waterChannelBeans.getCnLength().equals("0")) {
                    helper.setText(R.id.textView5, "长度：" + waterChannelBeans.getType());
                } else

                {
                    helper.setText(R.id.textView5, "长度：" +"米");
                }


            }
        }
            ;

        WSListAdapter.openLoadAnimation(new ScaleInAnimation());
        irc.setAdapter(WSListAdapter);
        irc.setOnRefreshListener(this);
        irc.setOnLoadMoreListener(this);

        //数据为空才重新发起请求
        if (WSListAdapter.getSize() <= 0) {
            mStartPage = 1;
            mPresenter.getWaterChannelDetailDataRequest(id1 + "", "5");
        }
    }

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext,String id,String name) {
        Intent intent = new Intent(mContext, WaterChannelActivity.class);
        intent.putExtra(AppConstant.ID, id);
        intent.putExtra(AppConstant.NAME, name);
        mContext.startActivity(intent);

    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void returnWaterChannelDetailData(List<waterChannelBeans> waterChannelBeans) {
        if (waterChannelBeans != null) {
            mStartPage += 1;
            if (WSListAdapter.getPageBean().isRefresh()) {
                irc.setRefreshing(false);
                WSListAdapter.replaceAll(waterChannelBeans);
            } else {
                if (waterChannelBeans.size() > 0) {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    WSListAdapter.addAll(waterChannelBeans);
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
        mPresenter.getWaterChannelDetailDataRequest(id1 + "", "5");
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        WSListAdapter.getPageBean().setRefresh(true);
//        //发起请求0000
//        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
//        mPresenter.getWaterChannelDetailDataRequest(id1 + "", "5");

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
    public void returnWaterChannelData(List<waterChannelBeans> waterChannelBeans) {

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
