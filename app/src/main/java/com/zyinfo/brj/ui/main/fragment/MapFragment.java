package com.zyinfo.brj.ui.main.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.patrolRecordBeans;
import com.zyinfo.brj.bean.waterSituation;
import com.zyinfo.brj.entity.ShapeLoadingDialog;
import com.zyinfo.brj.ui.map.activity.ClusterClickListener;
import com.zyinfo.brj.ui.map.activity.ClusterItem;
import com.zyinfo.brj.ui.map.activity.ClusterOverlay;
import com.zyinfo.brj.ui.map.activity.ClusterRender;
import com.zyinfo.brj.ui.map.activity.IPQCActivity;
import com.zyinfo.brj.ui.map.activity.RegionItem;
import com.zyinfo.brj.ui.map.contract.MapContract;
import com.zyinfo.brj.ui.map.model.MapModel;
import com.zyinfo.brj.ui.map.presenter.MapPresenter;
import com.zyinfo.brj.ui.news.activity.WSDActivity;
import com.zyinfo.brj.ui.zone.activity.CirclePublishActivity;
import com.zyinfo.common.base.BaseFragment;
import com.zyinfo.common.baserx.RxManager;
import com.zyinfo.common.commonutils.TUtil;
import com.zyinfo.common.commonwidget.LoadingTip;
import com.zyinfo.common.commonwidget.NormalTitleBar;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zwei  on 2018/7/2.
 * E-Mail Address：592296083@qq.com
 */

public class MapFragment extends BaseFragment<MapPresenter, MapModel> implements ClusterRender, ClusterClickListener, MapContract.View, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, DistrictSearch.OnDistrictSearchListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private AMap aMap = null;
    ArrayList<DistrictItem> district;
    @Bind(R.id.tabs)
    TabLayout tabs;

    @Bind(R.id.loadedTip)
    LoadingTip loadedTip;
    @Bind(R.id.map)
    TextureMapView mapView;
    @Bind(R.id.ntb)
    NormalTitleBar ntb;
    @Bind(R.id.qudaoview)
    LinearLayout qd;
    @Bind(R.id.yhview)
    LinearLayout yh;
    @Bind(R.id.rg)
    RadioGroup rg;
    @Bind(R.id.map_taime)
    TextView timeView;
    @Bind(R.id.fab)
    FloatingActionMenu fab;
    @Bind(R.id.fab_qiehuan)
    FloatingActionButton fab_qiehuan;
    @Bind(R.id.fab_list)
    FloatingActionButton fab_list;
    @Bind(R.id.fab_upload)
    FloatingActionButton fab_upload;
    String Lttd = "";
    String Lgtd = "";
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private int flagStationType = 1;
    private List<Marker> mapYHMarkers = null;// 巡检集合
    private List<Marker> mapHDMarkers = null;// 河道集合
    SwitchCompat wsd_kaiguan;
    //    @Bind(R.id.nav_view)
//    NavigationView nav_view;
    private ShapeLoadingDialog dialog;
    LatLng center;
    float zoomto;
    List<patrolRecordBeans> list = new ArrayList<patrolRecordBeans>();
    List<waterSituation> list1 = new ArrayList<waterSituation>();
    private View view = null;
    private UiSettings mUiSettings = null;
    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<Integer, Drawable>();
    private int clusterRadius = 100;
    private ClusterOverlay mClusterOverlay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(getLayoutResource(), container, false);
        mRxManager = new RxManager();
        ButterKnife.bind(this, rootView);
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = this.getActivity();
        }
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mapYHMarkers = new ArrayList<Marker>();
        mapHDMarkers = new ArrayList<Marker>();
        initPresenter();
        initView();
        return rootView;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.map_fragment;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String ThisTime = format.format(new Date());
        timeView.setText(ThisTime);
        Mapinit();
        fab.setClosedOnTouchOutside(true);
        dialog = new ShapeLoadingDialog(getContext());
        ntb.setTitleText("地图服务");
        ntb.setBackVisibility(false);
        ntb.setRightBackground(getContext().getResources().getDrawable(R.drawable.button_selector));
        ntb.setOnRightImagListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomto));
            }
        });
        rg.setOnCheckedChangeListener(this);
        tabs.addTab(tabs.newTab().setText("工程巡查"));
        tabs.addTab(tabs.newTab().setText("渠道水情"));
//        tabs.post(new Runnable() {
//            @Override
//            public void run() {
//                setIndicator(tabs,40,40);
//            }
//        });
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    yh.setVisibility(View.VISIBLE);
                    qd.setVisibility(View.GONE);
                    flagStationType = 1;
                    aMap.clear();
                    mClusterOverlay.onDestroy();
                    addBound();
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomto));
                    mPresenter.getPatrolRecordDataRequest();
                } else if (tab.getPosition() == 1) {
                    qd.setVisibility(View.VISIBLE);
                    yh.setVisibility(View.GONE);
                    flagStationType = 2;
                    aMap.clear();
                    addBound();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String ThisTime = format.format(new Date());
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomto));
//                    mPresenter.getWaterSituationDataRequest(ThisTime, "", "0", "1", "300", "", "", "");
                    mPresenter.getWaterMapSituationDataRequest(ThisTime);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
//        wsd_kaiguan = (SwitchCompat) nav_view.getMenu().findItem(R.id.nav_night_mode).getActionView().findViewById(R.id.wsd_kaiguan);
//        wsd_kaiguan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    aMap.setMapType(1);
//                } else {
//                    aMap.setMapType(2);
//                }
//            }
//        });
//        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                item.setChecked(true);
//                item.setCheckable(true);
//
//                switch (item.getItemId()) {
//                    case R.id.shangchuan:
//                        CirclePublishActivity.startAction(getContext());
//                        break;
//                    case R.id.nav_news:
//                        IPQCActivity.startAction(getContext());
//
//                        break;
//                }
//
//                return true;
//            }
//        });
        mPresenter.getPatrolRecordDataRequest();
    }
    private void Mapinit() {
        if (aMap == null) {
            aMap = mapView.getMap();
            center = new LatLng(48.016566, 87.003232);
            zoomto = Float.parseFloat("9");
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomto));
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setScaleControlsEnabled(true);// 显示比例尺
            mUiSettings.setCompassEnabled(true);// 显示指南针
            mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
            mUiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
            addBound();
            aMap.setMapType(2);
        }
    }

    private void addBound() {
        DistrictSearch search = new DistrictSearch(getContext().getApplicationContext());
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("布尔津县");//传入关键字
        query.setShowBoundary(true);//是否返回边界值
        search.setQuery(query);
        //不显示子区域边界

        search.setOnDistrictSearchListener(this);

        search.setQuery(query);
        search.searchDistrictAnsy();//开始搜索

    }

    private void addLinePoint() {


        new Thread(new Runnable() {
            @Override
            public void run() {

                PolylineOptions polylineOption = new PolylineOptions();
                polylineOption.addAll(parseData(district.get(0)));
                polylineOption.setDottedLine(true);
                polylineOption.setDottedLineType(PolylineOptions.DOTTEDLINE_TYPE_SQUARE);
                polylineOption.width(6).color(Color.BLUE);
                aMap.addPolyline(polylineOption);

            }
        }).start();
    }

    private List<LatLng> parseData(DistrictItem districtItem) {
        String[] polyStr = districtItem.districtBoundary();
        if (polyStr == null || polyStr.length == 0) {
            return null;
        }

        List<LatLng> list = new ArrayList<>();
        for (String str : polyStr) {
            String[] lat = str.split(";");
            boolean isFirst = true;
            LatLng firstLatLng = null;
            for (String latstr : lat) {
                String[] lats = latstr.split(",");
                if (isFirst) {
                    isFirst = false;
                    firstLatLng = new LatLng(Double
                            .parseDouble(lats[1]), Double
                            .parseDouble(lats[0]));
                }
                list.add(new LatLng(Double
                        .parseDouble(lats[1]), Double
                        .parseDouble(lats[0])));
            }
            if (firstLatLng != null) {
                list.add(firstLatLng);
            }
        }
        return list;
    }
    @OnClick({R.id.fab, R.id.fab_qiehuan, R.id.fab_list, R.id.fab_upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                break;
            case R.id.fab_qiehuan:
                if (aMap.getMapType() == 1) {
                    aMap.setMapType(2);
                } else if (aMap.getMapType() == 2) {
                    aMap.setMapType(1);
                }
                break;
            case R.id.fab_list:
                fab.close(true);
                IPQCActivity.startAction(getContext());
                break;
            case R.id.fab_upload:
                dialog.loading("定位中....");
                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
                location();
                fab.close(true);
                break;
        }

    }

    private void addHDCeZhan() {

        View view = View.inflate(getActivity(), R.layout.cz_marker, null);
        TextView textView = (TextView) view.findViewById(R.id.wenzi);
        ImageView imageView = (ImageView) view.findViewById(R.id.tubiao);
        List<ClusterItem> items = new ArrayList<ClusterItem>();
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).getLgtd().equals("") || !list1.get(i).getLttd().equals("")) {
                LatLng lat = new LatLng(Float.parseFloat(list1.get(i).getLttd()),
                        Float.parseFloat(list1.get(i).getLgtd()));
                int wptn = 4;
//                int wptn = list1.get(i).getWptn().equals("") ? 4
//                        : Integer.parseInt(list1.get(i).getWptn().trim());
                RegionItem regionItem = new RegionItem(lat,
                        "test" + i, i);
                items.add(regionItem);


//                if (list1.get(i).getWptn() == null || list1.get(i).getWptn().equals("")) {
//                    wptn = 4;
//                } else {
//                    wptn = Integer.parseInt(list1.get(i).getWptn().trim());
//                }
//
//                textView.setText("流量:" + list1.get(i).getFlow());
//                if (wptn == 4) {
//                    imageView.setImageResource(R.drawable.hedao_down);
//                    Bitmap bitmap = convertViewToBitmap(view);
////                    Marker marker_cz = aMap
////                            .addMarker(new MarkerOptions().position(lat).title(list1.get(i).getStName())
////                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)).snippet("" + i));
////                    mapHDMarkers.add(marker_cz);
////				Marker marker_cz = aMap
////						.addMarker(new MarkerOptions().position(lat).title(stationHeDaos.get(i).getSTNM())
////								.icon(BitmapDescriptorFactory.fromResource(R.drawable.hedao_down)).snippet("" + i));
////				mapCZMarkers.add(marker_cz);
//                } else if (wptn == 5) {
//                    imageView.setImageResource(R.drawable.hedao_normal);
//                    Bitmap bitmap = convertViewToBitmap(view);
//                    Marker marker_cz = aMap
//                            .addMarker(new MarkerOptions().position(lat).title(list1.get(i).getStName())
//                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)).snippet("" + i));
//                    mapHDMarkers.add(marker_cz);
//                } else if (wptn == 6) {
//                    imageView.setImageResource(R.drawable.hedao_up);
//                    Bitmap bitmap = convertViewToBitmap(view);
//                    Marker marker_cz = aMap
//                            .addMarker(new MarkerOptions().position(lat).title(list1.get(i).getStName())
//                                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)).snippet("" + i));
//                    mapHDMarkers.add(marker_cz);
//                }
            }
        }

        mClusterOverlay = new ClusterOverlay(aMap, items,
                dp2px(getActivity().getApplicationContext(), clusterRadius),
                getActivity().getApplicationContext(), list1);
        mClusterOverlay.setClusterRenderer(MapFragment.this);
        mClusterOverlay.setOnClusterClickListener(MapFragment.this);
    }

    private void addYHMakers(List<patrolRecordBeans> patrolRecord) {
        for (int i = 0; i < patrolRecord.size(); i++) {

            if (!patrolRecord.get(i).getLgtd().equals("") &&!patrolRecord.get(i).getLttd().equals("")) {
                LatLng lat = new LatLng(
                        Float.parseFloat(patrolRecord.get(i).getLgtd()),
                        Float.parseFloat(patrolRecord.get(i).getLttd()));
                String grade = patrolRecord.get(i).getGrade();
                if (grade.equals("一般隐患")) {
                    Marker marker_yh = aMap
                            .addMarker(new MarkerOptions().position(lat).title(patrolRecord.get(i).getCantype())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.yibanyh)).snippet("" + i));
                    mapYHMarkers.add(marker_yh);
                } else if (grade.equals("中等隐患")) {
                    Marker marker_yh = aMap
                            .addMarker(new MarkerOptions().position(lat).title(patrolRecord.get(i).getCantype())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.zhongdengyh)).snippet("" + i));
                    mapYHMarkers.add(marker_yh);
                } else if (grade.equals("严重隐患")) {
                    Marker marker_yh = aMap
                            .addMarker(new MarkerOptions().position(lat).title(patrolRecord.get(i).getCantype())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.yanzhongyh)).snippet("" + i));
                    mapYHMarkers.add(marker_yh);
                }
            }
        }
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final LatLng point = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        Point startPoint = proj.toScreenLocation(point);
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;
        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * point.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * point.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_train:
                yh.setVisibility(View.VISIBLE);
                qd.setVisibility(View.GONE);
                flagStationType = 1;
                aMap.clear();
                addBound();
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomto));
                mPresenter.getPatrolRecordDataRequest();

                break;
            case R.id.rb_examRecord:
                qd.setVisibility(View.VISIBLE);
                yh.setVisibility(View.GONE);
                flagStationType = 2;
                aMap.clear();
                addBound();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String ThisTime = format.format(new Date());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomto));
//                mPresenter.getWaterSituationDataRequest(ThisTime, "", "0", "1", "100", "", "", "");
                mPresenter.getWaterMapSituationDataRequest(ThisTime);

                break;
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = null;
        if (flagStationType == 1) {
            infoWindow = showYHStation(marker);
        } else if (flagStationType == 2) {
//            infoWindow = showQDStation(marker);
        }


        return infoWindow;
    }

    private View showYHStation(final Marker marker) {
        final View infoWindow;
        infoWindow = getActivity().getLayoutInflater().inflate(R.layout.yh_info_window, null);
        ImageButton infoClose = (ImageButton) infoWindow.findViewById(R.id.closeInfoBtn);
        infoClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				infoWindow.setVisibility(View.INVISIBLE);
                marker.hideInfoWindow();
            }
        });
        final patrolRecordBeans station = list.get(Integer.parseInt(marker.getSnippet()));
        TextView title = (TextView) infoWindow.findViewById(R.id.title);
        TextView yjnr = (TextView) infoWindow.findViewById(R.id.yjnr);

        title.setText(marker.getTitle());
        final String yujingnr = station.getAnnex();
        yjnr.setText(yujingnr);
        return infoWindow;
    }
    private View showQDStation(final Marker marker) {
        final View infoWindow;
        infoWindow = getActivity().getLayoutInflater().inflate(R.layout.cz_hedao_info_window, null);
        ImageButton infoClose = (ImageButton) infoWindow.findViewById(R.id.closeInfoBtn);
        infoClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				infoWindow.setVisibility(View.INVISIBLE);
                marker.hideInfoWindow();
            }
        });
        final waterSituation station = list1.get(Integer.parseInt(marker.getSnippet()));
        TextView stnm = (TextView) infoWindow.findViewById(R.id.stnm);
        TextView stcd = (TextView) infoWindow.findViewById(R.id.stcd);
        TextView sttp = (TextView) infoWindow.findViewById(R.id.sttp);
        TextView stlc = (TextView) infoWindow.findViewById(R.id.stlc);
        TextView z = (TextView) infoWindow.findViewById(R.id.z);
        TextView q = (TextView) infoWindow.findViewById(R.id.q);
        TextView wptn = (TextView) infoWindow.findViewById(R.id.wptn);
        TextView lasttime = (TextView) infoWindow.findViewById(R.id.lasttime);
        TextView more = (TextView) infoWindow.findViewById(R.id.more);
        stnm.setText(station.getStName().trim());
        stcd.setText(station.getStcd().trim());
        sttp.setText("渠道站");
        stlc.setText(station.getSttp().trim());
        z.setText(station.getXsavv().trim());
        q.setText(station.getXsmxv().trim().equals("") ? "0.0" : station.getXsmxv().trim());
        if (station.getWptn() == null || station.getWptn().trim().equals("4")) {
            wptn.setText("落");
        } else if (station.getWptn().trim().equals("5")) {
            wptn.setText("涨");
        } else if (station.getWptn().trim().equals("6")) {
            wptn.setText("平");
        }
        lasttime.setText(station.getTm().trim());
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                WSDActivity.startAction(getContext(), station.getStcd(), station.getTm());
            }
        });
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        jumpPoint(marker);
        return false;
    }

    @Override
    public void onDistrictSearched(DistrictResult districtResult) {
        district = districtResult.getDistrict();
        if (district.size()>0&&district!=null){
            addLinePoint();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mapView.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

        Log.e("onResume", "onResume");
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void showLoading(String title) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);

    }

    @Override
    public void stopLoading() {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
    }

    @Override
    public void showErrorTip(String msg) {
        loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
    }

    @Override
    public void returnWaterSituationData(List<waterSituation> waterSituations) {

    }

    @Override
    public void returnPatrolRecordData(List<patrolRecordBeans> patrolRecordBeans) {
        list.clear();
        list.addAll(patrolRecordBeans);
        addYHMakers(list);
    }

    @Override
    public void returnWaterMapSituationDate(List<waterSituation> waterSituations) {
        list1.clear();
        list1.addAll(waterSituations);
        addHDCeZhan();
    }

    @Override
    public void scrolltoTop() {

    }

    public static Bitmap convertViewToBitmap(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache(true);
    }

    public void location() {
        mLocationClient = new AMapLocationClient(getContext());
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (mLocationListener != null && amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    Lttd = amapLocation.getLatitude() + "";//获取纬度
                    Lgtd = amapLocation.getLongitude() + "";//获取经度
//                    amapLocation.getAccuracy();//获取精度信息
//                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                    amapLocation.getCountry();//国家信息
//                    amapLocation.getProvince();//省信息
//                    amapLocation.getCity();//城市信息
//                    amapLocation.getDistrict();//城区信息
//                    amapLocation.getStreet();//街道信息
//                    amapLocation.getStreetNum();//街道门牌号信息
//                    amapLocation.getCityCode();//城市编码
//                    amapLocation.getAdCode();//地区编码
//                    amapLocation.getAoiName();//获取当前定位点的AOI信息
                    dialog.dismiss();
                    CirclePublishActivity.startAction(getContext(), Lttd, Lgtd);
                } else {

                    Toast.makeText(getContext(), "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo(), Toast.LENGTH_LONG).show();
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            //通过反射得到tablayout的下划线的Field
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            //得到承载下划线的LinearLayout   //源码可以看到SlidingTabStrip继承得到承载下划线的LinearLayout
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        //循环设置下划线的左边距和右边距
        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public Drawable getDrawAble(int clusterNum) {
        int radius = dp2px(getActivity().getApplicationContext(), 80);
        if (clusterNum == 1) {
            Drawable bitmapDrawable = mBackDrawAbles.get(1);
            if (bitmapDrawable == null) {
                bitmapDrawable =
                        getActivity().getApplication().getResources().getDrawable(
                                R.drawable.location);
                mBackDrawAbles.put(1, bitmapDrawable);
            }
            return null;
        } else if (clusterNum < 5) {
            Drawable bitmapDrawable = mBackDrawAbles.get(2);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(159, 210, 154, 6)));
                mBackDrawAbles.put(2, bitmapDrawable);
            }
            return bitmapDrawable;
        } else if (clusterNum < 10) {
            Drawable bitmapDrawable = mBackDrawAbles.get(3);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(199, 217, 114, 0)));
                mBackDrawAbles.put(3, bitmapDrawable);
            }
            return bitmapDrawable;
        } else {
            Drawable bitmapDrawable = mBackDrawAbles.get(4);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(235, 215, 66, 2)));
                mBackDrawAbles.put(4, bitmapDrawable);
            }
            return bitmapDrawable;
        }
    }

    private Bitmap drawCircle(int radius, int color) {
        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
        paint.setColor(color);
        canvas.drawArc(rectF, 0, 360, true, paint);
        return bitmap;
    }

    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
        if (clusterItems.size() > 1) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (ClusterItem clusterItem : clusterItems) {
                builder.include(clusterItem.getPosition());
            }
            LatLngBounds latLngBounds = builder.build();
            aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0)
            );
        }
    }
}