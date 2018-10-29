package com.zyinfo.brj.ui.bases.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.EventBusBeans;
import com.zyinfo.brj.bean.stationInfoBeans;

import com.zyinfo.brj.ui.bases.contract.StationInfoContract;

import com.zyinfo.brj.ui.bases.model.StationInfoModel;

import com.zyinfo.brj.ui.bases.presenter.StationInfoPresenter;

import com.zyinfo.brj.ui.main.activity.LocationMapActivity;
import com.zyinfo.brj.ui.news.activity.WSDActivity;
import com.zyinfo.brj.view.CommonRadioGroup;
import com.zyinfo.common.base.BaseFragment;
import com.zyinfo.common.commonwidget.LoadingTip;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;


/**
 * Created by Zwei  on 2018/6/7.
 * E-Mail Address：592296083@qq.com
 */

public class StationInfoFragment extends BaseFragment<StationInfoPresenter, StationInfoModel> implements StationInfoContract.View, OnRefreshListener, OnLoadMoreListener, RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.sta_irc)
    IRecyclerView irc;
    @Bind(R.id.sta_loadedTip)
    LoadingTip loadedTip;

//    @Bind(R.id.sta_fab)
//    FloatingActionButton fab;

    private ColorStateList white;
    private ColorStateList black;
    @Bind(R.id.radioGroup)
    CommonRadioGroup radioGroup;//头部单选
    @Bind(R.id.btn0)
    RadioButton radioButton0;
    @Bind(R.id.btn1)
    RadioButton radioButton1;
    @Bind(R.id.btn2)
    RadioButton radioButton2;
    private int mStartPage = 1;
    private String pageNum = "10";
    private CommonRecycleViewAdapter<stationInfoBeans> WSListAdapter;
    private List<stationInfoBeans> datas = new ArrayList<>();
    String stationType = "ZZ";

    @Override
    public void initView() {

        radioButton1.setVisibility(View.VISIBLE);
        radioButton0.setText("河道站");
        radioButton1.setText("水库站");
        radioButton2.setText("雨量站");
        white = getResources().getColorStateList(R.color.white);
        black = getResources().getColorStateList(R.color.gray_text3);
        radioButton0.setTextColor(white);
        radioGroup.setOnCheckedChangeListener(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        irc.setLayoutManager(new LinearLayoutManager(getContext()));
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                irc.smoothScrollToPosition(0);
//            }
//        });
        WSListAdapter = new CommonRecycleViewAdapter<stationInfoBeans>(getContext(), R.layout.stationinfo_list_item) {

            @Override
            public void convert(ViewHolderHelper helper, final stationInfoBeans stationInfoBeans) {
                helper.setText(R.id.admn, stationInfoBeans.getStnm());
                helper.setText(R.id.admn_city, "(" + stationInfoBeans.getStcd() + ")");
                if (stationInfoBeans.getSttp().equals("ZZ")) {
                    helper.setText(R.id.totalp, stationInfoBeans.getStlc() + "河道站");
                } else if (stationInfoBeans.getSttp().equals("RR")) {
                    helper.setText(R.id.totalp, stationInfoBeans.getStlc() + "水库站");
                } else if (stationInfoBeans.getSttp().equals("PP")) {
                    helper.setText(R.id.totalp, stationInfoBeans.getStlc() + "雨量站");
                }

                helper.setText(R.id.totalp, stationInfoBeans.getStlc());
                helper.setText(R.id.textView5, stationInfoBeans.getStlc());

                helper.setText(R.id.textView8, stationInfoBeans.getRvnm());
                ImageButton map_show = helper.getView(R.id.map_show);
                map_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!stationInfoBeans.getLgtd().equals("") && stationInfoBeans.getLgtd() != null) {

                            LocationMapActivity.startAction(getContext(), stationInfoBeans.getLgtd(), stationInfoBeans.getLttd());
                        } else {
                            Toast.makeText(mContext, "没有坐标信息", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                CardView cv = helper.getView(R.id.wsf_cd);
                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String ThisTime = format.format(new Date());
                        WSDActivity.startAction(getContext(), stationInfoBeans.getStcd(), ThisTime + " 00:00:00.0");
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
            mPresenter.getStationInfoDataRequest("", stationType, mStartPage + "", pageNum);
        }
    }

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext) {
        Intent intent = new Intent(mContext, StationInfoFragment.class);

        mContext.startActivity(intent);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.stationinfo_activity;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void returnStationInfoData(List<stationInfoBeans> stationInfoBeans) {
        if (stationInfoBeans != null) {
            mStartPage += 1;
            if (WSListAdapter.getPageBean().isRefresh()) {
                irc.setRefreshing(false);
                WSListAdapter.replaceAll(stationInfoBeans);
            } else {
                if (stationInfoBeans.size() > 0) {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    WSListAdapter.addAll(stationInfoBeans);
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
        mPresenter.getStationInfoDataRequest("", stationType, mStartPage + "", pageNum);
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        WSListAdapter.getPageBean().setRefresh(false);
        //发起请求0000
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        mPresenter.getStationInfoDataRequest("", stationType, mStartPage + "", pageNum);

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


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            //雨量站
            case R.id.btn0:
                stationType = "ZZ";
                radioButton0.setTextColor(white);
                radioButton1.setTextColor(black);
                radioButton2.setTextColor(black);
                onRefresh();

                break;
            //河道站
            case R.id.btn1:
                stationType = "RR";
                radioButton0.setTextColor(black);
                radioButton1.setTextColor(white);
                radioButton2.setTextColor(black);
                onRefresh();

                break;
            //水库站
            case R.id.btn2:
                stationType = "PP";

                radioButton0.setTextColor(black);
                radioButton1.setTextColor(black);
                radioButton2.setTextColor(white);
                onRefresh();
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onEvent(EventBusBeans eb) {
        if (eb.getFlag() == 1) {
            WSListAdapter.getPageBean().setRefresh(true);
            mStartPage = 1;
            //发起请求
            irc.setRefreshing(true);
            mPresenter.getStationInfoDataRequest(eb.getData(), stationType, mStartPage + "", pageNum);
        }

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}