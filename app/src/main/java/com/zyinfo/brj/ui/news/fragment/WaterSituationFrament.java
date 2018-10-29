package com.zyinfo.brj.ui.news.fragment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.aspsine.irecyclerview.widget.LoadMoreFooterView;
import com.zyinfo.brj.R;
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.EventBusBeans;
import com.zyinfo.brj.bean.waterSituation;
import com.zyinfo.brj.entity.ShapeLoadingDialog;
import com.zyinfo.brj.ui.news.activity.WSDActivity;
import com.zyinfo.brj.ui.news.adapter.ConstellationAdapter;
import com.zyinfo.brj.ui.news.adapter.GirdDropDownAdapter;
import com.zyinfo.brj.ui.news.adapter.ListDropDownAdapter;
import com.zyinfo.brj.ui.news.contract.WaterSituationContract;
import com.zyinfo.brj.ui.news.model.WaterSituationModel;
import com.zyinfo.brj.ui.news.presenter.WaterSituationPresenter;
import com.zyinfo.brj.widget.DateChooseWheelViewDialog;
import com.zyinfo.common.base.BaseFragment;
import com.zyinfo.common.commonutils.TimeUtil;
import com.zyinfo.common.commonwidget.LoadingTip;
import com.yyydjk.library.DropDownMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * des:fragment
 * <p>
 * on
 */
public class WaterSituationFrament extends BaseFragment<WaterSituationPresenter, WaterSituationModel> implements WaterSituationContract.View, OnRefreshListener, OnLoadMoreListener {
    @Bind(R.id.irc)
    IRecyclerView irc;
    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
    @Bind(R.id.frame_wsf)
    FrameLayout frame;

    @Bind(R.id.nosj)
    FrameLayout nosj;

    @Bind(R.id.notp)
    ImageView notp;

    private GirdDropDownAdapter cityAdapter;
    private ListDropDownAdapter ageAdapter;
    private ListDropDownAdapter sexAdapter;
    private ConstellationAdapter constellationAdapter;
    private String headers[] = {"水势", "排序"};
    private List<View> popupViews = new ArrayList<>();
    private String citys[] = {"不限", "渠道1", "渠道2", "渠道3", "渠道4", "渠道5", "渠道6", "渠道7", "渠道8", "渠道9", "渠道10", "渠道11"};
    private String ages[] = {"不限", "水势涨", "水势落", "水势平"};
    //    private String sexs[] = {"不限", "男", "女"};
    private String constellations[] = {"不限", "时间升序", "时间降序", "站码升序", "站码降序"};
    private int constellationPosition = 0;

    private List<waterSituation> datas = new ArrayList<>();
    private CommonRecycleViewAdapter<waterSituation> WSListAdapter;
    private String mNewsId;
    private String mNewsType;
    private int mStartPage = 1;
    private ShapeLoadingDialog dialog;
    private String historyDate = "";
    private String flag;
    private String pageSize = "1";
    private String pageNum = "10";
    private String sortContent = "";
    private String sortType = "";
    private String wptn = "";
    boolean isPrepareds;
    private String keyWord = "";
    String s = "";
    // 标志位，标志已经初始化完成。
    private boolean isPrepared;
    private boolean isVisible;
    String ThisTime;

    @Override
    protected int getLayoutResource() {
        return R.layout.framents_news;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        dialog = new ShapeLoadingDialog(getContext());
        if (getArguments() != null) {
            mNewsId = getArguments().getString(AppConstant.NEWS_ID);
            mNewsType = getArguments().getString(AppConstant.NEWS_TYPE);
            if (mNewsId.equals("T1348647909107")) {
                flag = "0";
            } else {
                flag = "1";
            }
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        ThisTime = format.format(new Date());
        sortContent = "tm";
        sortType = "desc";
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -1); //减1
        Date lastDate = ca.getTime(); //结果
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        historyDate = sf.format(lastDate) + "";
        initDropDownMenu();
        irc.setLayoutManager(new LinearLayoutManager(getContext()));
        datas.clear();

        WSListAdapter = new CommonRecycleViewAdapter<waterSituation>(getContext(), R.layout.river_list_item, datas) {
            @Override
            public void convert(ViewHolderHelper helper, final waterSituation waterSituation) {
                helper.setText(R.id.river_city, waterSituation.getStName());
                helper.setText(R.id.river_cezhanname, waterSituation.getStName() + "(" + waterSituation.getStcd() + ")");
                helper.setText(R.id.river_shuiwei, waterSituation.getStage());
                helper.setText(R.id.river_liul, waterSituation.getFlow());
                helper.setText(R.id.river_data, TimeUtil.formatDate1(waterSituation.getTm()));

                if (waterSituation.getWptn() == null || waterSituation.getWptn().equals("")) {
                    s = "5";
                } else {
                    s = waterSituation.getWptn();
                }
//                String s = waterSituation.getWptn().equals("")?"5":waterSituation.getWptn();
                if (s.equals("5")) {
                    helper.setText(R.id.river_shuishi1, "平");
                } else if (s.equals("6")) {
                    helper.setText(R.id.river_shuishi1, "涨");
                    helper.setBackgroundColor(R.id.river_shuishi1, R.color.red);

                } else if (s.equals("4")) {
                    helper.setText(R.id.river_shuishi1, "落");
                    helper.setBackgroundColor(R.id.river_shuishi1, R.color.green);
                }
                CardView cv = helper.getView(R.id.wsf_cd);
                cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        WSDActivity.startAction(getContext(), waterSituation.getStcd(), waterSituation.getTm());
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
            isPrepareds = true;
            mStartPage = 1;
            if (flag.equals("0")) {
                mPresenter.getWaterSituationDataRequest(ThisTime, "", flag, mStartPage + "", pageNum, sortContent, sortType, wptn, keyWord);
            } else {
                mPresenter.getWaterSituationDataRequest("", historyDate, flag, mStartPage + "", pageNum, sortContent, sortType, wptn, keyWord);
            }
        }
    }

    private void initDropDownMenu() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //测试tabView扩展功能
        final TextView textView = (TextView) inflater.inflate(R.layout.tab_text, null);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        textView.setTextColor(getResources().getColor(R.color.drop_down_selected));
        textView.setText("时间");
        if (flag.equals("0")) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }

        //init city menu
        final ListView cityView = new ListView(getContext());
        cityAdapter = new GirdDropDownAdapter(getContext(), Arrays.asList(citys));
        cityView.setDividerHeight(0);
        cityView.setAdapter(cityAdapter);

        //init age menu
        final ListView ageView = new ListView(getContext());
        ageView.setDividerHeight(0);
        ageAdapter = new ListDropDownAdapter(getContext(), Arrays.asList(ages));
        ageView.setAdapter(ageAdapter);

//        //init sex menu
//        final ListView sexView = new ListView(getContext());
//        sexView.setDividerHeight(0);
//        sexAdapter = new ListDropDownAdapter(getContext(), Arrays.asList(sexs));
//        sexView.setAdapter(sexAdapter);

        //init constellation

        final View constellationView = inflater.inflate(R.layout.custom_layout, null);
        GridView constellation = ButterKnife.findById(constellationView, R.id.constellation);
        constellationAdapter = new ConstellationAdapter(getContext(), Arrays.asList(constellations));
        constellation.setAdapter(constellationAdapter);
        TextView ok = ButterKnife.findById(constellationView, R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropDownMenu.setTabText(constellationPosition == 0 ? headers[1] : constellations[constellationPosition]);
                switch (constellations[constellationPosition]) {
                    case "不限":
                        sortContent = "";
                        sortType = "";
                        break;
                    case "时间升序":
                        sortContent = "tm";
                        sortType = "asc";
                        break;
                    case "时间降序":
                        sortContent = "tm";
                        sortType = "desc";
                        break;
                    case "站码升序":
                        sortContent = "stcd";
                        sortType = "asc";
                        break;
                    case "站码降序":
                        sortContent = "stcd";
                        sortType = "desc";
                        break;


                    default:
                        break;
                }

                mDropDownMenu.closeMenu();
                irc.setRefreshing(true);
                mStartPage = 1;
                if (flag.equals("0")) {
                    mPresenter.getWaterSituationDataRequest(ThisTime, "", flag, mStartPage + "", pageNum, sortContent, sortType, wptn, keyWord);
                } else {
                    mPresenter.getWaterSituationDataRequest("", historyDate, flag, mStartPage + "", pageNum, sortContent, sortType, wptn, keyWord);
                }
            }
        });
        //添加时间
        //keyboard3 为支持可以外部控制popViews里的View可以设置layoutParams。注意必须是FrameLayout的布局参数
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(200, 100, 200, 0);
        cityView.setLayoutParams(layoutParams);
        //init popupViews
//        popupViews.add(cityView);
        popupViews.add(ageView);
//        popupViews.add(sexView);
        popupViews.add(constellationView);

        //add item click event
        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : citys[position]);
                mDropDownMenu.closeMenu();
                textView.setTextColor(getResources().getColor(R.color.drop_down_unselected));
            }
        });

        ageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ageAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : ages[position]);

                switch (ages[position]) {
                    case "不限":
                        wptn = "";
                        break;

                    case "水势涨":
                        wptn = "6";
                        break;
                    case "水势平":
                        wptn = "5";
                        break;
                    case "水势落":
                        wptn = "4";
                        break;

                    default:
                        break;
                }
                mDropDownMenu.closeMenu();
                textView.setTextColor(getResources().getColor(R.color.drop_down_unselected));
                irc.setRefreshing(true);
                mStartPage = 1;
                if (flag.equals("0")) {
                    mPresenter.getWaterSituationDataRequest(ThisTime, "", flag, mStartPage + "", pageNum, sortContent, sortType, wptn, keyWord);
                } else {
                    mPresenter.getWaterSituationDataRequest("", historyDate, flag, mStartPage + "", pageNum, sortContent, sortType, wptn, keyWord);
                }
            }
        });

//        sexView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                sexAdapter.setCheckItem(position);
//                mDropDownMenu.setTabText(position == 0 ? headers[2] : sexs[position]);
//                mDropDownMenu.closeMenu();
//                textView.setTextColor(getResources().getColor(R.color.drop_down_unselected));
//            }
//        });

        constellation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                constellationAdapter.setCheckItem(position);
                constellationPosition = position;
            }
        });

        //init context view
        TextView contentView = new TextView(getContext());
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        //初始化 dropdownview

        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, irc);
        mDropDownMenu.addTab(textView, 2);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag.equals("0")) {

                    Toast.makeText(getContext(), "最新水情不能选时间！", Toast.LENGTH_LONG).show();
                    return;
                }
                DateChooseWheelViewDialog startDateChooseDialog = new DateChooseWheelViewDialog(getContext(), new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        textView.setText(time);
                        if (flag.equals("0")) {

                        } else {
                            historyDate = time;
                            irc.setRefreshing(true);
                            mStartPage = 1;
                            mPresenter.getWaterSituationDataRequest("", historyDate, flag, mStartPage + "", pageNum, sortContent, sortType, wptn, keyWord);
                        }
                    }
                });
                startDateChooseDialog.setTimePickerGone(true);
                startDateChooseDialog.setDateDialogTitle("选择日期");

                startDateChooseDialog.showDateChooseDialog();
                if (mDropDownMenu.isActive()) {

                    textView.setTextColor(getResources().getColor(R.color.drop_down_selected));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.drop_down_unselected));
                }
            }
        });
        mDropDownMenu.setOnItemMenuClickListener(new DropDownMenu.OnItemMenuClickListener() {
            @Override
            public void OnItemMenuClick(TextView tabView, int position) {
                textView.setTextColor(getResources().getColor(R.color.drop_down_unselected));
            }
        });

    }


    @Override
    public void returnWaterSituationData(List<waterSituation> newsSummaries) {
        datas.addAll(newsSummaries);
        if (datas.size() == 0 && isPrepareds) {
            nosj.setVisibility(View.VISIBLE);
            frame.setVisibility(View.GONE);


        } else {
            frame.setVisibility(View.VISIBLE);
            nosj.setVisibility(View.GONE);
        }
        isPrepareds = false;
        if (newsSummaries != null) {
            mStartPage += 1;
            if (WSListAdapter.getPageBean().isRefresh()) {
                irc.setRefreshing(false);
                WSListAdapter.replaceAll(newsSummaries);
            } else {
                if (newsSummaries.size() > 0) {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.GONE);
                    WSListAdapter.addAll(newsSummaries);
                } else {
                    irc.setLoadMoreStatus(LoadMoreFooterView.Status.THE_END);
                }
            }
        }
    }


    /**
     * 返回顶部
     */
    @Override
    public void scrolltoTop() {
        irc.smoothScrollToPosition(0);
    }


    @Override
    public void onRefresh() {
        WSListAdapter.getPageBean().setRefresh(true);
        mStartPage = 1;
        //发起请求
        irc.setRefreshing(true);
        if (flag.equals("0")) {
            mPresenter.getWaterSituationDataRequest(ThisTime, "", flag, mStartPage + "", pageNum, sortContent, sortType, wptn, keyWord);
        } else {
            mPresenter.getWaterSituationDataRequest("", historyDate, flag, mStartPage + "", pageNum, sortContent, sortType, wptn, keyWord);
        }
    }

    @Override
    public void onLoadMore(View loadMoreView) {
        WSListAdapter.getPageBean().setRefresh(false);
        //发起请求0000
        irc.setLoadMoreStatus(LoadMoreFooterView.Status.LOADING);
        if (flag.equals("0")) {
            mPresenter.getWaterSituationDataRequest(ThisTime, "", flag, mStartPage + "", pageNum, sortContent, sortType, wptn, keyWord);
        } else {
            mPresenter.getWaterSituationDataRequest("", historyDate, flag, mStartPage + "", pageNum, sortContent, sortType, wptn, keyWord);
        }
    }

    @Override
    public void showLoading(String title) {
        if (WSListAdapter.getPageBean().isRefresh()) {
            if (WSListAdapter.getSize() <= 0) {
                loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
                dialog.loading("加载中....");
            }
        }
    }

    @Override
    public void stopLoading() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        dialog.dismiss();
    }

    @Override
    public void showErrorTip(String msg) {
        if (WSListAdapter.getPageBean().isRefresh()) {
            if (WSListAdapter.getSize() <= 0) {
                loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
                loadedTip.setTips(msg);
                dialog.dismiss();
            }
            irc.setRefreshing(false);
        } else {
            dialog.dismiss();
            irc.setLoadMoreStatus(LoadMoreFooterView.Status.ERROR);
        }
    }

    @Subscribe
    public void onEvent(EventBusBeans eb) {
        WSListAdapter.getPageBean().setRefresh(true);
        mStartPage = 1;
        //发起请求

        if (eb.getFlag() == 0) {

            mPresenter.getWaterSituationDataRequest(ThisTime, "", flag, mStartPage + "", pageNum, sortContent, sortType, wptn, eb.getData());

        } else {
            mPresenter.getWaterSituationDataRequest("", historyDate, flag, mStartPage + "", pageNum, sortContent, sortType, wptn, eb.getData());
        }
    }

}
