package com.zyinfo.brj.ui.more.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.zyinfo.brj.R;

import com.zyinfo.brj.utils.MapDateUtlis;
import com.zyinfo.brj.view.AbstractSpinerAdapter;
import com.zyinfo.brj.view.CommonRadioGroup;
import com.zyinfo.brj.view.ProgressWebView;
import com.zyinfo.brj.view.SpinerPopWindow;
import com.zyinfo.common.base.BaseActivity;
import com.zyinfo.common.commonwidget.NormalTitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Zwei  on 2018/7/2.
 * E-Mail Address：592296083@qq.com
 */

public class MeteorologicalActivity extends BaseActivity implements WeatherSearch.OnWeatherSearchListener, AbstractSpinerAdapter.IOnItemSelectListener, RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.webview)
    ProgressWebView webView;// webview

    @Bind(R.id.webview1)
    ProgressWebView webView1;// webview


    @Bind(R.id.date_selecter)
    LinearLayout date_selecter;// 日期显示

    private TextView reporttime1;
    private TextView reporttime2;
    private TextView weather;
    private TextView Temperature;
    private TextView wind;
    private TextView humidity;
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private LocalWeatherForecast weatherforecast;
    private List<LocalDayWeatherForecast> forecastlist = null;
    private String cityname;//
    private TextView ib_back;
    private TextView tv_title;
    private ProgressBar probarshishi, probaryubao;
    private ImageView shishiImage;
    private GridView forecastgridview;
    private int[] weather_images = {R.drawable.d00, R.drawable.d01, R.drawable.d02, R.drawable.d03, R.drawable.d04,
            R.drawable.d05, R.drawable.d06, R.drawable.d07, R.drawable.d08, R.drawable.d09, R.drawable.d10,
            R.drawable.d11, R.drawable.d12, R.drawable.d13, R.drawable.d14, R.drawable.d15, R.drawable.d16,
            R.drawable.d17, R.drawable.d18, R.drawable.d19, R.drawable.d20, R.drawable.d21, R.drawable.d22,
            R.drawable.d23, R.drawable.d24, R.drawable.d25, R.drawable.d26, R.drawable.d27, R.drawable.d28,
            R.drawable.d29, R.drawable.d30, R.drawable.d31, R.drawable.d53, R.drawable.d99};
    private String[] weather_str = {"晴", "多云", "阴", "阵雨", "雷阵雨", "雷阵雨伴有冰雹", "雨夹雪", "小雨", "中雨", "大雨", "暴雨", "大暴雨",
            "特大暴雨", "阵雪", "小雪", "中雪", "大雪", "暴雪", "雾", "冻雨", "沙尘暴", "小到中雨", "中到大雨", "大到暴雨", "暴雨到大暴雨", "大暴雨到特大暴雨",
            "小到中雪", "中到大雪", "大到暴雪", "浮尘", "扬沙", "强沙尘暴", "霾", "无"};

//    @Bind(R.id.progress_bar1)
//    ProgressBar progress_bar;//

    @Bind(R.id.Tianqi)
    View include;//

    private ColorStateList white;
    private ColorStateList black;
    private List<String> list = new ArrayList<String>();
    private List<String> listDate = new ArrayList<String>();
    @Bind(R.id.radioGroup)
    CommonRadioGroup radioGroup;// 头部单选

    @Bind(R.id.btn0)
    RadioButton radioButton0;// 天气预报
    @Bind(R.id.btn1)
    RadioButton radioButton1;// 气象云图
    // @ViewInject(R.id.btn_3)
    // private RadioButton radioButton3;//红外云图
    // @ViewInject(R.id.btn_4)
    // private RadioButton radioButton4;//水汽云图
    @Bind(R.id.btn2)
    RadioButton radioButton2;// 雷达图
    private int crrruntPage = 0;

    /*
     * spinner数据
     */
    @Bind(R.id.tv_value)
    TextView mTView;
    @Bind(R.id.bt_dropdown)
    ImageButton mBtnDropDown;
    @Bind(R.id.Stv_value)
    TextView mTView1;
    @Bind(R.id.Sbt_dropdown)
   ImageButton mBtnDropDown1;
    private int chooseStart = 0;
    private int chooseEnd;
    private int choose;
    private String isThis;

    @Override
    public int getLayoutId() {
        return R.layout.activity_meteorological;
    }

    @Override
    public void initPresenter() {

    }

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext) {
        Intent intent = new Intent(mContext, MeteorologicalActivity.class);

        mContext.startActivity(intent);

    }

    @Override
    public void initView() {
        ntb.setTitleText(getString(R.string.qixiang));
        probarshishi = (ProgressBar) findViewById(R.id.probarshishi);
        probaryubao = (ProgressBar) findViewById(R.id.probaryubao);
        TextView city = (TextView) findViewById(R.id.city);
        cityname = "布尔津";
        city.setText(cityname);
        reporttime1 = (TextView) findViewById(R.id.reporttime1);
        reporttime2 = (TextView) findViewById(R.id.reporttime2);
        weather = (TextView) findViewById(R.id.weather);
        Temperature = (TextView) findViewById(R.id.temp);
        wind = (TextView) findViewById(R.id.wind);
        humidity = (TextView) findViewById(R.id.humidity);
        forecastgridview = (GridView) findViewById(R.id.forecastgridview);
        shishiImage = (ImageView) findViewById(R.id.shishiImage);
        searchliveweather();
        searchforcastsweather();
        /**
         * radioButton
         */
        white = getResources().getColorStateList(R.color.white);
        black = getResources().getColorStateList(R.color.gray_text2);
        radioButton0.setTextColor(white);
        radioButton1.setVisibility(View.VISIBLE);
        radioButton0.setTextSize(14);
        radioButton1.setTextSize(14);
        radioButton2.setTextSize(14);
        radioButton0.setText("天气预报");
        radioButton1.setText("气象云图");
        radioButton2.setText("雷达图");
        radioGroup.setOnCheckedChangeListener(this);
        mSpinerPopWindow = new SpinerPopWindow(this);
        mSpinerPopWindow.setItemListener(this);
    }

    /**
     * 获取气象云图list
     */
    private void getData() {

        List<String> yuntuList1 = new ArrayList<String>();
        List<String> yuntuList1Date = new ArrayList<String>();
        yuntuList1 = MapDateUtlis.getLDUrlDate();
        yuntuList1Date = MapDateUtlis.getLDDate();
        for (int i = yuntuList1.size() - 1; i > -1; i--) {
            list.add(yuntuList1.get(i));
            listDate.add(yuntuList1Date.get(i));
        }

    }

    /**
     * 获取气象云图list
     */
    private void getData1() {
        // TODO Auto-generated method stub
        List<String> yuntuList = new ArrayList<String>();
        List<String> yuntuListdate = new ArrayList<String>();
        yuntuList = MapDateUtlis.getYunTuUrlDate();
        yuntuListdate = MapDateUtlis.getYunTuDate();
        for (int i = yuntuList.size() - 1; i > -1; i--) {
            list.add(yuntuList.get(i));
            listDate.add(yuntuListdate.get(i));
        }
    }

    /**
     * radioGroup CheckedChanged
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        switch (checkedId) {
            // 天气预报
            case R.id.btn0:
                radioButton0.setTextColor(white);
                radioButton1.setTextColor(black);
                radioButton2.setTextColor(black);
                webView.setVisibility(View.GONE);
                webView1.setVisibility(View.GONE);
                date_selecter.setVisibility(View.GONE);
                include.setVisibility(View.VISIBLE);
                searchliveweather();
                searchforcastsweather();
                // Toast.makeText(context, "天气预报", 1000).show();
                break;
            // 气象云图
            case R.id.btn1:
                isThis = "yuntu";
                radioButton0.setTextColor(black);
                radioButton1.setTextColor(white);
                radioButton2.setTextColor(black);
                webView.setVisibility(View.VISIBLE);
                webView1.setVisibility(View.GONE);
                date_selecter.setVisibility(View.VISIBLE);
                include.setVisibility(View.GONE);
                mTView1.setText("");
                mTView.setText("");
                list.clear();
                listDate.clear();
                getData1();
                mSpinerPopWindow.refreshData(listDate, 0);
                if (list.size()!=0&&listDate.size()!=0) {
                    webView.getWebViewLoadUrl(list, listDate);
                    Log.e("气象云图", list.get(crrruntPage));
                    webView.getWebView().loadUrl(list.get(list.size() - 1));
                }
                break;
            // 雷达图
            case R.id.btn2:
                isThis = "lda";
                radioButton0.setTextColor(black);
                radioButton1.setTextColor(black);
                radioButton2.setTextColor(white);
                webView1.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                date_selecter.setVisibility(View.VISIBLE);
                include.setVisibility(View.GONE);
                mTView1.setText("");
                mTView.setText("");
                list.clear();
                listDate.clear();
                getData();

                mSpinerPopWindow.refreshData(listDate, 0);
                webView1.getWebViewLoadUrl(list, listDate);
                webView1.getWebView().loadUrl(list.get(list.size() - 1));

                break;

            default:
                break;
        }
    }

    /**
     * @param pos 获取开始的时间
     */
    private void setHero(int pos) {

        if (pos >= 0 && pos <= listDate.size()) {
            chooseStart = pos;
            String value = listDate.get(pos);

            mTView.setText(value);
        }
    }

   public SpinerPopWindow mSpinerPopWindow;

    private void showSpinWindow(int count) {
        Log.e("", "showSpinWindow");
        choose = count;
        if (count == 0) {
            mSpinerPopWindow.setWidth(mTView.getWidth() + 10);
            mSpinerPopWindow.showAsDropDown(mTView);
        } else {
            mSpinerPopWindow.setWidth(mTView.getWidth() + 10);
            mSpinerPopWindow.showAsDropDown(mTView1);
        }
    }

    @Override
    public void onItemClick(int pos) {

        if (choose == 0) {
            setHero(pos);
        } else {
            if (pos > chooseStart) {
                setHero1(pos);
            } else {
                Toast.makeText(mContext, "不能小于开始时间", 0).show();
            }
        }

    }

    /**
     * @param pos 获取结束的时间
     */
    private void setHero1(int pos) {
        // TODO Auto-generated method stub
        if (pos >= 0 && pos <= listDate.size()) {
            chooseEnd = pos;
            String value = listDate.get(pos);
            mTView1.setText(value);
        }
        List<String> yuntuList = new ArrayList<String>();
        List<String> yuntuListdate = new ArrayList<String>();
        yuntuList.add(list.get(chooseStart));
        yuntuListdate.add(listDate.get(chooseStart));
        for (int i = chooseStart; i < chooseEnd + 1; i++) {
            Log.e("date", i + "");
            Log.e("chooseEnd", chooseEnd + "'");
            Log.e("*******", "********************");
            yuntuList.add(list.get(i));
            yuntuListdate.add(listDate.get(i));
        }
        if (isThis.equals("yuntu")) {
            webView.getWebViewLoadUrl(yuntuList, yuntuListdate);
            Log.e("气象云图", list.get(crrruntPage));
            webView.getWebView().loadUrl(yuntuList.get(0));
        } else {
            webView1.getWebViewLoadUrl(yuntuList, yuntuListdate);
            Log.e("气象云图", list.get(crrruntPage));
            webView1.getWebView().loadUrl(yuntuList.get(0));
        }

    }

    private void searchforcastsweather() {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_FORECAST);// 检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); // 异步搜索
    }

    private void searchliveweather() {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);// 检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); // 异步搜索
    }

    /**
     * 实时天气查询回调
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        probarshishi.setVisibility(View.GONE);
        Log.e("rCode>>>>>>>>>>>>>>>>>", rCode + "");
        if (rCode == 1000) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                weatherlive = weatherLiveResult.getLiveResult();
                for (int i = 0; i < weather_str.length; i++) {
                    if (weather_str[i].equals(weatherlive.getWeather())) {
                        shishiImage.setImageResource(weather_images[i]);
                    }
                }
                reporttime1.setText(weatherlive.getReportTime() + "发布");
                weather.setText(weatherlive.getWeather());
                Temperature.setText(weatherlive.getTemperature() + "°");
                wind.setText(weatherlive.getWindDirection() + "风     " + weatherlive.getWindPower() + "级");
                humidity.setText("湿度     " + weatherlive.getHumidity() + "%");
            } else {
            }
        } else {
        }
    }

    /**
     * 天气预报查询结果回调
     */
    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult weatherForecastResult, int rCode) {
        probaryubao.setVisibility(View.GONE);
        if (rCode == 1000) {
            if (weatherForecastResult != null && weatherForecastResult.getForecastResult() != null
                    && weatherForecastResult.getForecastResult().getWeatherForecast() != null
                    && weatherForecastResult.getForecastResult().getWeatherForecast().size() > 0) {
                weatherforecast = weatherForecastResult.getForecastResult();
                forecastlist = weatherforecast.getWeatherForecast();
                fillforecast();

            } else {
            }
        } else {
        }
    }

    private void fillforecast() {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        reporttime2.setText(weatherforecast.getReportTime() + "发布");
        for (int i = 0; i < forecastlist.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            LocalDayWeatherForecast localdayweatherforecast = forecastlist.get(i);
            String week = null;
            switch (Integer.valueOf(localdayweatherforecast.getWeek())) {
                case 1:
                    week = "周一";
                    break;
                case 2:
                    week = "周二";
                    break;
                case 3:
                    week = "周三";
                    break;
                case 4:
                    week = "周四";
                    break;
                case 5:
                    week = "周五";
                    break;
                case 6:
                    week = "周六";
                    break;
                case 7:
                    week = "周日";
                    break;
                default:
                    break;
            }
            String temp = String.format("%-3s/%3s", localdayweatherforecast.getDayTemp() + "°",
                    localdayweatherforecast.getNightTemp() + "°");
            String date = localdayweatherforecast.getDate();
            String tianqi = localdayweatherforecast.getDayWeather();
            String tianqi2 = tianqi;
            int len = tianqi.indexOf("-");
            if (!(len == -1)) {
                tianqi2 = tianqi.substring(0, len);
            }
            Log.i("weather", tianqi);
            for (int j = 0; j < weather_str.length; j++) {
                if (weather_str[j].equals(tianqi2)) {
                    map.put("image", weather_images[j]);
                }
            }
            map.put("week", week);
            map.put("tianqi", tianqi);
            map.put("temp", temp);
            map.put("date", date);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(mContext, list, R.layout.qx_weatherinfo3_grid,
                new String[]{"week", "image", "temp", "tianqi", "date"},
                new int[]{R.id.week, R.id.image, R.id.temp, R.id.tianqi, R.id.date});
        forecastgridview.setAdapter(adapter);
    }
    /**
     * 单机
     */
    @Nullable
    @OnClick({ R.id.tv_back, R.id.bt_dropdown, R.id.Sbt_dropdown, R.id.player })
    public void onClick(View view) {
        switch (view.getId()) {
            // actionBar上面的返回
            case R.id.tv_back:

                onBackPressed();
                break;
            case R.id.bt_dropdown:
                showSpinWindow(0);
                break;
            case R.id.Sbt_dropdown:
                showSpinWindow(1);
                break;
            default:
                break;
        }
    }
}
