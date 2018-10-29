package com.zyinfo.brj.ui.news.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.animation.ScaleInAnimation;
import com.aspsine.irecyclerview.universaladapter.ViewHolderHelper;
import com.aspsine.irecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineData;
import com.zyinfo.brj.R;
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.waterSituation;
import com.zyinfo.brj.ui.news.contract.WaterSituationDetailContract;
import com.zyinfo.brj.ui.news.model.WaterSituationDetailModel;
import com.zyinfo.brj.ui.news.presenter.WaterSituationDetailPresenter;
import com.zyinfo.brj.utils.MPChartHelper;
import com.zyinfo.brj.widget.DateChooseWheelViewDialog;
import com.zyinfo.common.base.BaseActivity;
import com.zyinfo.common.commonutils.TimeUtil;
import com.zyinfo.common.commonwidget.LoadingTip;
import com.zyinfo.common.commonwidget.NormalTitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.zyinfo.common.commonutils.LogUtils.loge;


/**
 * Created by Zwei  on 2018/5/31.
 * E-Mail Address：592296083@qq.com
 */

public class WSDActivity extends BaseActivity<WaterSituationDetailPresenter, WaterSituationDetailModel> implements WaterSituationDetailContract.View {

    @Bind(R.id.irc)
    IRecyclerView irc;
    @Bind(R.id.spread_line_chart)
    public LineChart lineChart;
    @Bind(R.id.ntb)
    NormalTitleBar ntb;
    @Bind(R.id.tongji)
    TextView tongji;
    @Bind(R.id.max_flow)
    TextView max_flow;
    @Bind(R.id.max_stage)
    TextView max_stage;
    @Bind(R.id.Spinner)
    AppCompatSpinner Spinner;
    @Bind(R.id.starttime)
    TextView starttime;
    @Bind(R.id.endtime)
    TextView endtime;
    @Bind(R.id.wsd_tlb)
    TextView wsd_tlb;

    @Bind(R.id.wsd_kaiguan)
    SwitchCompat wsd_kaiguan;

    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;

    @Bind(R.id.kaishi)
    TextView kaishi;
    @Bind(R.id.chaxun)
    TextView chaxun;

    @Bind(R.id.wsd_liebiao)
    LinearLayout wsd_liebiao;

    @Bind(R.id.wsd_tubiao)
    RelativeLayout wsd_tubiao;

    public LineData lineData = null;
    private List<waterSituation> datas = new ArrayList<>();

    private CombinedData data;
    ArrayList<String> xValsValue;
    ArrayList<Float> entries = null;
    ArrayList<Float> entries1 = null;
    XAxis xAxis;

    private String TM;

    private String stcd, sttp, startTime, endTime;
    private CommonRecycleViewAdapter<waterSituation> WSListAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.wsd_activity;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }


    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext, String Stcd, String Tm) {
        Intent intent = new Intent(mContext, WSDActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(AppConstant.TM, Tm);
        intent.putExtra(AppConstant.STCD, Stcd);
        mContext.startActivity(intent);

    }

    @Override
    public void initView() {

        ntb.setTitleText(getString(R.string.czxq));
        stcd = getIntent().getStringExtra(AppConstant.STCD);
        TM = getIntent().getStringExtra(AppConstant.TM);
        TM = TM.substring(0, TM.length() - 11);
        sttp = "ZZ";
        startTime = TM + " 00:00:00";
        endTime = TM + " 23:59:59";

        starttime.setText(TM + " 00:00");
        endtime.setText(TM + " 23:59");
        Intent intent = new Intent(mContext, WSDActivity.class);
//        Stcd=intent.getString(AppConstant.NEWS_ID);
        //数据为空才重新发起请求
        if (datas.size() <= 0) {

            mPresenter.getWaterSituationDetailDataRequest(stcd, sttp, startTime, endTime);
        }
        irc.setLayoutManager(new LinearLayoutManager(mContext));
        WSListAdapter = new CommonRecycleViewAdapter<waterSituation>(WSDActivity.this, R.layout.wsd_list_item) {
            @Override
            public void convert(ViewHolderHelper helper, waterSituation waterSituation) {
                helper.setText(R.id.wsd_list_tm, waterSituation.getTm());
                helper.setText(R.id.wsd_list_liul, waterSituation.getStage());
                helper.setText(R.id.wsd_list_shuiwei, waterSituation.getFlow());


            }
        };

        Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar c = Calendar.getInstance();
                switch (i) {
                    case 0:
                        break;
                    case 1:
                        c.setTime(new Date());
                        c.add(Calendar.DATE, -7);
                        Date d = c.getTime();
                        String day = format.format(d);
                        starttime.setText(day.substring(0, 16));
                        endtime.setText(format.format(new Date()).toString().substring(0, 16));
                        mPresenter.getWaterSituationDetailDataRequest(stcd, sttp, day + ":00", format.format(new Date()) + ":00");

                        break;
                    case 2:
                        c.setTime(new Date());
                        c.add(Calendar.MONTH, -1);
                        Date m = c.getTime();
                        String mon = format.format(m);
                        starttime.setText(mon.substring(0, 16));
                        endtime.setText(format.format(new Date()).toString().substring(0, 16));
                        mPresenter.getWaterSituationDetailDataRequest(stcd, sttp, mon + ":00", format.format(new Date()) + ":00");
                        break;
                    case 3:
                        c.setTime(new Date());
                        c.add(Calendar.MONTH, -3);
                        Date m3 = c.getTime();
                        String mon3 = format.format(m3);
                        starttime.setText(mon3.substring(0, 16));
                        endtime.setText(format.format(new Date()).toString().substring(0, 16));
                        mPresenter.getWaterSituationDetailDataRequest(stcd, sttp, mon3 + ":00", format.format(new Date()) + ":00");
                        break;
                    case 4:
                        c.setTime(new Date());
                        c.add(Calendar.MONTH, -6);
                        Date m6 = c.getTime();
                        String mon6 = format.format(m6);
                        starttime.setText(mon6.substring(0, 16));
                        endtime.setText(format.format(new Date()).toString().substring(0, 16));
                        mPresenter.getWaterSituationDetailDataRequest(stcd, sttp, mon6 + ":00", format.format(new Date()) + ":00");
                        break;

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        WSListAdapter.openLoadAnimation(new ScaleInAnimation());
        irc.setAdapter(WSListAdapter);
        wsd_kaiguan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    wsd_tlb.setText("列表");
                    wsd_liebiao.setVisibility(View.VISIBLE);
                    wsd_tubiao.setVisibility(View.GONE);
                } else {
                    wsd_tlb.setText("图表");
                    wsd_liebiao.setVisibility(View.GONE);
                    wsd_tubiao.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    @Override
    public void showLoading(String title) {
        if (datas.size() <= 0) {

            loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);

        }
    }

    @Override
    public void stopLoading() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void returnWaterSituationDetailData(List<waterSituation> waterSituationDetails) {
        datas.clear();
        datas.addAll(waterSituationDetails);
        if (waterSituationDetails.size() > 0) {
            ntb.setTitleText(datas.get(0).getStName());

        } else {
            Toast.makeText(mContext, "这段时间没有数据！", Toast.LENGTH_LONG).show();
        }

        List<waterSituation> newList;
//        if (waterSituationDetails.size() > 50) {
//            newList = datas.subList(0, 50);
//
//        } else {
//            newList = datas;
//        }
        newList = datas;
        xValsValue = new ArrayList<String>();
        if (datas != null && datas.size() > 0) {

            for (int i = newList.size(), j = 0; i > 0; i--) {

                xValsValue.add(TimeUtil.formatDate1(newList.get(i - 1).getTm()));
            }

            WSListAdapter.replaceAll(newList);
            entries = new ArrayList<Float>();
            entries1 = new ArrayList<Float>();
            //水位
            float stage = 0;
            //流量
            float flow = 0;
            for (int index = newList.size(), i = 0; index > 0; index--) {
                stage = Float.parseFloat(newList.get(index - 1).getStage());
                flow = Float.parseFloat(newList.get(index - 1).getFlow());
                entries.add((float) (stage));
                entries1.add((float) (flow));
            }
//            MPChartHelper.setCombineChart(lineChart, xValsValue, entries1, entries, "流量", "水位");
            MPChartHelper.setTwolineChart(lineChart, xValsValue, entries1, entries, "流量", "水位");
            Float maxStage = Collections.max(entries);
            Float yMax = Double.valueOf(Collections.max(entries1)).floatValue();
            tongji.setText("统计数据：" + newList.size() + "条");
            max_flow.setText(yMax + "");
            max_stage.setText(maxStage.toString());
            lineChart.setVisibleXRangeMaximum(15);
            lineChart.notifyDataSetChanged();
            lineChart.invalidate();
        }
    }

    @Override
    public void scrolltoTop() {


    }

    @OnClick({R.id.tv_back, R.id.starttime, R.id.endtime, R.id.chaxun})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.starttime:
                DateChooseWheelViewDialog startDateChooseDialog = new DateChooseWheelViewDialog(WSDActivity.this, new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        starttime.setText(time);
                    }
                });

                startDateChooseDialog.setDateDialogTitle("开始时间");
                startDateChooseDialog.showDateChooseDialog();
                break;
            case R.id.endtime:
                DateChooseWheelViewDialog startDateChooseDialog1 = new DateChooseWheelViewDialog(WSDActivity.this, new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        endtime.setText(time);
                    }
                });

                startDateChooseDialog1.setDateDialogTitle("结束时间");
                startDateChooseDialog1.showDateChooseDialog();
                break;

            case R.id.chaxun:
                if (starttime.getText().toString().compareTo(endtime.getText().toString()) > 0) {
                    Toast.makeText(mContext, "开始时间不能大于结束时间！", Toast.LENGTH_LONG).show();
                } else {
                    mPresenter.getWaterSituationDetailDataRequest(stcd, sttp, starttime.getText() + ":00", endtime.getText() + ":00");
                }

                break;
        }
    }


}
