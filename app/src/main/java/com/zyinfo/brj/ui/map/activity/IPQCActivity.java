package com.zyinfo.brj.ui.map.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
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
import com.zyinfo.brj.bean.IPQCbeans;
import com.zyinfo.brj.ui.map.contract.IPQCContract;
import com.zyinfo.brj.ui.map.model.IPQCModel;
import com.zyinfo.brj.ui.map.presenter.IPQCPresenter;
import com.zyinfo.brj.widget.DateChooseWheelViewDialog;
import com.zyinfo.common.base.BaseActivity;
import com.zyinfo.common.commonutils.TimeUtil;
import com.zyinfo.common.commonutils.ToastUitl;
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

public class IPQCActivity extends BaseActivity<IPQCPresenter, IPQCModel> implements IPQCContract.View, OnRefreshListener, OnLoadMoreListener {
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
    private CommonRecycleViewAdapter<IPQCbeans> WSListAdapter;
    private List<IPQCbeans> datas = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.ipqc_activity;
    }

    @Override
    public void initView() {
        ntb.setTitleText(getString(R.string.xcsj));
        ntb.setRightTitleVisibility(true);
        ntb.setRightTitle("选择时间");
        ntb.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateChooseWheelViewDialog startDateChooseDialog = new DateChooseWheelViewDialog(IPQCActivity.this, new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        ntb.setRightTitle(time);

                        mStartPage = 1;
                        mPresenter.getIPQCDataRequest(time,mStartPage + "", pageNum);

                    }
                });
                startDateChooseDialog.setTimePickerGone(true);
                startDateChooseDialog.setDateDialogTitle("选择时间");
                startDateChooseDialog.showDateChooseDialog();
            }
        });
        irc.setLayoutManager(new LinearLayoutManager(mContext));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irc.smoothScrollToPosition(0);
            }
        });
        WSListAdapter = new CommonRecycleViewAdapter<IPQCbeans>(mContext, R.layout.ipqc_list_item) {

            @Override
            public void convert(ViewHolderHelper helper, IPQCbeans IPQCbeans) {
                helper.setText(R.id.river_city, IPQCbeans.getCan());
                helper.setText(R.id.river_cezhanname, TimeUtil.formatDate1(IPQCbeans.getTm()));
                helper.setText(R.id.river_shuiwei, IPQCbeans.getLabel() + "");
                helper.setText(R.id.river_liul, IPQCbeans.getGrade() + "");
                helper.setText(R.id.river_data, IPQCbeans.getPart() + "");

                CardView cv = helper.getView(R.id.wsf_cd);
                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToastUitl.showShort("没有坐标信息");
                    }
                });


            }
        };
        WSListAdapter.openLoadAnimation(new ScaleInAnimation());
        irc.setAdapter(WSListAdapter);
        irc.setOnRefreshListener(this);
        irc.setOnLoadMoreListener(this);

        //数据为空才重新发起请求
        if (WSListAdapter.getSize() <= 0) {
            mStartPage = 1;
            mPresenter.getIPQCDataRequest("",mStartPage + "", pageNum);
        }
    }

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext) {
        Intent intent = new Intent(mContext, IPQCActivity.class);

        mContext.startActivity(intent);

    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void returnIPQCData(List<IPQCbeans> IPQCbeans) {
        if (IPQCbeans != null) {
            mStartPage += 1;
            if (WSListAdapter.getPageBean().isRefresh()) {
                irc.setRefreshing(false);
                WSListAdapter.replaceAll(IPQCbeans);
            } else {
                if (IPQCbeans.size() > 0) {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    WSListAdapter.addAll(IPQCbeans);
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
        ntb.setRightTitle("选择时间");
        mPresenter.getIPQCDataRequest("",mStartPage + "", pageNum);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        WSListAdapter.getPageBean().setRefresh(false);
        //发起请求0000
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getIPQCDataRequest("",mStartPage + "", pageNum);

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
