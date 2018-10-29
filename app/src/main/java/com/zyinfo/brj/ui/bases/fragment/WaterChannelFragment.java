package com.zyinfo.brj.ui.bases.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.AssociationInfoBeans;
import com.zyinfo.brj.bean.waterChannelBeans;
import com.zyinfo.brj.ui.bases.activity.WaterChannelActivity;
import com.zyinfo.brj.ui.bases.adapter.CountyAdapter;
import com.zyinfo.brj.ui.bases.contract.WaterChannelContract;
import com.zyinfo.brj.ui.bases.model.WaterChannelModel;
import com.zyinfo.brj.ui.bases.presenter.WaterChannelPresenter;
import com.zyinfo.common.base.BaseFragment;
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

public class WaterChannelFragment extends BaseFragment<WaterChannelPresenter, WaterChannelModel> implements WaterChannelContract.View, OnRefreshListener, OnLoadMoreListener {
    @Bind(R.id.irc)
    IRecyclerView irc;
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.ntb)
    NormalTitleBar ntb;
//    @Bind(R.id.fab)
//    FloatingActionButton fab;
    private ListView countylist;
    private int mStartPage = 1;
    private String pageNum = "10";
    private String id = "";
    private String type = "";
    private CommonRecycleViewAdapter<waterChannelBeans> WSListAdapter;
    private List<AssociationInfoBeans> datas = new ArrayList<>();
    View popupWindow_view;
    private PopupWindow popupWindow;
    private RelativeLayout rela;
    private CountyAdapter mAdapter;
    List<waterChannelBeans> list;
    @Override
    public void initView() {
//        ntb.setTitleText(getString(R.string.xiehui));
        ntb.setVisibility(View.GONE);
        irc.setLayoutManager(new LinearLayoutManager(getContext()));
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                irc.smoothScrollToPosition(0);
//            }
//        });
        popupWindow_view = getLayoutInflater().inflate(R.layout.contrycity_right, null, false);
        WSListAdapter = new CommonRecycleViewAdapter<waterChannelBeans>(getContext(), R.layout.water_list_item) {

            @Override
            public void convert(ViewHolderHelper helper, final waterChannelBeans waterChannelBeans) {
                helper.setText(R.id.county_name, waterChannelBeans.getName());
                CardView cv = helper.getView(R.id.wsf_cd);
                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       mPresenter.getWaterChannelDetailDataRequest(waterChannelBeans.getId(),"4");
                    }
                });

            }
        };
        WSListAdapter.openLoadAnimation(new ScaleInAnimation());
        irc.setAdapter(WSListAdapter);
        irc.setOnRefreshListener(this);
        irc.setOnLoadMoreListener(this);
        showdialog();
        //数据为空才重新发起请求
        if (WSListAdapter.getSize() <= 0) {
            mStartPage = 1;
            mPresenter.getWaterChannelDataRequest();
        }

        countylist = (ListView) popupWindow_view.findViewById(R.id.countylist);
        countylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WaterChannelActivity.startAction(getContext(),list.get(i).getId(),list.get(i).getName());
            }
        });
    }

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext) {
        Intent intent = new Intent(mContext, WaterChannelFragment.class);

        mContext.startActivity(intent);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.waterchannel_activity;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void returnWaterChannelDetailData(List<waterChannelBeans> waterChannelBeans) {
        list =waterChannelBeans;
        if (list != null) {
            mAdapter = new CountyAdapter(getContext(), list);
            countylist.setAdapter(mAdapter);
        }
        getPopupWindow();
    }

    @Override
    public void returnWaterChannelData(List<waterChannelBeans> waterChannelBeans) {
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
        mPresenter.getWaterChannelDataRequest();
    }

    @Override
    public void onLoadMore(View loadMoreView) {
//        WSListAdapter.getPageBean().setRefresh(false);
//        //发起请求0000
//        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);


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

    private void getPopupWindow() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAsDropDown(ntb, 300, 0);
        }
    }
    protected void showdialog() {
        // TODO Auto-generated method stub
        WindowManager wm = (WindowManager) getActivity()
                .getSystemService(getActivity().WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        int w = width - 300;
        popupWindow = new PopupWindow(popupWindow_view, w,
                ActionBar.LayoutParams.MATCH_PARENT, true);

        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.AnimationFade);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
    }
}
