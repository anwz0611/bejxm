package com.zyinfo.brj.ui.map.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.AlphaAnimation;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.waterSituation;
import com.zyinfo.brj.ui.news.activity.WSDActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 整体设计采用了两个线程,一个线程用于计算组织聚合数据,一个线程负责处理Marker相关操作
 */
public class ClusterOverlay implements AMap.OnCameraChangeListener,
        AMap.OnMarkerClickListener, AMap.InfoWindowAdapter {
    private AMap mAMap;
    private Context mContext;
    private List<ClusterItem> mClusterItems;
    private List<Cluster> mClusters;
    private int mClusterSize;
    private ClusterClickListener mClusterClickListener;
    private ClusterRender mClusterRender;
    private List<Marker> mAddMarkers = new ArrayList<Marker>();
    private double mClusterDistance;
    private LruCache<Integer, BitmapDescriptor> mLruCache;
    private HandlerThread mMarkerHandlerThread = new HandlerThread("addMarker");
    private HandlerThread mSignClusterThread = new HandlerThread("calculateCluster");
    private Handler mMarkerhandler;
    private Handler mSignClusterHandler;
    private float mPXInMeters;
    private boolean mIsCanceled = false;
    List<waterSituation> list1 = new ArrayList<waterSituation>();
    int isFrist = 0;

    public ClusterOverlay() {


    }

    /**
     * 构造函数
     *
     * @param amap
     * @param clusterSize 聚合范围的大小（指点像素单位距离内的点会聚合到一个点显示）
     * @param context
     */
    public ClusterOverlay(AMap amap, int clusterSize, Context context) {
        this(amap, null, clusterSize, context, null);
        mContext = context;

    }

    /**
     * 构造函数,批量添加聚合元素时,调用此构造函数
     *
     * @param amap
     * @param clusterItems 聚合元素
     * @param clusterSize
     * @param context
     */
    public ClusterOverlay(AMap amap, List<ClusterItem> clusterItems,
                          int clusterSize, Context context, List<waterSituation> list) {
//默认最多会缓存80张图片作为聚合显示元素图片,根据自己显示需求和app使用内存情况,可以修改数量
        mLruCache = new LruCache<Integer, BitmapDescriptor>(80) {
            protected void entryRemoved(boolean evicted, Integer key, BitmapDescriptor oldValue, BitmapDescriptor newValue) {
                oldValue.getBitmap().recycle();
            }
        };
        if (clusterItems != null) {
            mClusterItems = clusterItems;
        } else {
            mClusterItems = new ArrayList<ClusterItem>();
        }
        if (list != null) {
            list1 = list;
        } else {
            list1 = new ArrayList<waterSituation>();
        }
        mContext = context;
        mClusters = new ArrayList<Cluster>();
        this.mAMap = amap;
        mClusterSize = clusterSize;
        mPXInMeters = mAMap.getScalePerPixel();
        mClusterDistance = mPXInMeters * mClusterSize;
        amap.setOnCameraChangeListener(this);
        amap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        amap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式

        initThreadHandler();
        assignClusters();
    }

    /**
     * 设置聚合点的点击事件
     *
     * @param clusterClickListener
     */
    public void setOnClusterClickListener(
            ClusterClickListener clusterClickListener) {
        mClusterClickListener = clusterClickListener;
    }

    public void mapClear() {
        mAMap.clear();
    }

    /**
     * 添加一个聚合点
     *
     * @param item
     */
    public void addClusterItem(ClusterItem item) {
        Message message = Message.obtain();
        message.what = SignClusterHandler.CALCULATE_SINGLE_CLUSTER;
        message.obj = item;
        mSignClusterHandler.sendMessage(message);
    }

    /**
     * 设置聚合元素的渲染样式，不设置则默认为气泡加数字形式进行渲染
     *
     * @param render
     */
    public void setClusterRenderer(ClusterRender render) {
        mClusterRender = render;
    }

    public void onDestroy() {
        mIsCanceled = true;
        mSignClusterHandler.removeCallbacksAndMessages(null);
        mMarkerhandler.removeCallbacksAndMessages(null);
        mSignClusterThread.quit();
        mMarkerHandlerThread.quit();
        for (Marker marker : mAddMarkers) {
            marker.remove();
        }
        mAddMarkers.clear();
        mLruCache.evictAll();
    }

    //初始化Handler
    private void initThreadHandler() {
        mMarkerHandlerThread.start();
        mSignClusterThread.start();
        mMarkerhandler = new MarkerHandler(mMarkerHandlerThread.getLooper());
        mSignClusterHandler = new SignClusterHandler(mSignClusterThread.getLooper());
    }

    @Override
    public void onCameraChange(CameraPosition arg0) {
        Log.e("CameraPosition", "CameraPosition= " + arg0);

    }

    @Override
    public void onCameraChangeFinish(CameraPosition arg0) {
        mPXInMeters = mAMap.getScalePerPixel();
        mClusterDistance = mPXInMeters * mClusterSize;
        assignClusters();
    }

    //点击事件
    @Override
    public boolean onMarkerClick(Marker arg0) {

        if (mClusterClickListener == null) {
            return true;
        }

        Cluster cluster = (Cluster) arg0.getObject();

        if (cluster != null) {
            if (cluster.getClusterItems().size() == 1) {
                jumpPoint(arg0);
                return false;
            }
            mClusterClickListener.onClick(arg0, cluster.getClusterItems());
            return true;
        }
        return false;
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final LatLng point = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mAMap.getProjection();
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


    /**
     * 将聚合元素添加至地图上
     */
    private void addClusterToMap(List<Cluster> clusters) {

        ArrayList<Marker> removeMarkers = new ArrayList<>();
        removeMarkers.addAll(mAddMarkers);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        MyAnimationListener myAnimationListener = new MyAnimationListener(removeMarkers);
        for (Marker marker : removeMarkers) {
            marker.setAnimation(alphaAnimation);
            marker.setAnimationListener(myAnimationListener);
            marker.startAnimation();
        }
        for (Cluster cluster : clusters) {
            if (clusters.size() == 1) {
                addSingleClusterToMap(cluster);
            } else {
                addSingleClusterToMap(cluster);
            }
        }
    }

    private AlphaAnimation mADDAnimation = new AlphaAnimation(0, 1);

    /**
     * 将单个聚合元素添加至地图显示
     *
     * @param cluster
     */
    private void addSingleClusterToMap(Cluster cluster) {
        LatLng latlng = cluster.getCenterLatLng();
        MarkerOptions markerOptions = new MarkerOptions();
        if (cluster.getClusterCount() == 1) {
            int i = cluster.getClusterItems().get(0).i();
            View view = View.inflate(mContext, R.layout.cz_marker, null);
            TextView textView = (TextView) view.findViewById(R.id.wenzi);
            ImageView imageView = (ImageView) view.findViewById(R.id.tubiao);
            textView.setText("流量:" + list1.get(i).getFlow());
            int wptn = 4;
            if (list1.get(i).getWptn() == null || list1.get(i).getWptn().equals("")) {
                wptn = 4;
            } else {
                wptn = Integer.parseInt(list1.get(i).getWptn().trim());
            }
            if (wptn == 4) {
                imageView.setImageResource(R.drawable.hedao_down);
            } else if (wptn == 5) {
                imageView.setImageResource(R.drawable.hedao_normal);
            } else if (wptn == 6) {
                imageView.setImageResource(R.drawable.hedao_up);
            }
            Bitmap bitmap = convertViewToBitmap(view);
            markerOptions.anchor(0.5f, 0.5f).position(latlng).title(list1.get(i).getStName())
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap)).snippet("" + i);
        } else {
            markerOptions.anchor(0.5f, 0.5f)
                    .icon(getBitmapDes(cluster.getClusterCount()).getBitmapDescriptor()).position(latlng).title("");
        }
        Marker marker = mAMap.addMarker(markerOptions);

        marker.setAnimation(mADDAnimation);
        marker.setObject(cluster);
        startGrowAnimation(mAMap, marker);
        marker.startAnimation();
        cluster.setMarker(marker);
        mAddMarkers.add(marker);

    }

    public void startGrowAnimation(AMap aMap, Marker growMarker) {
        try {
            if (growMarker != null) {
                Animation animation = new ScaleAnimation(0, 1, 0, 1);
                animation.setInterpolator(new LinearInterpolator());
                //整个移动所需要的时间
                animation.setDuration(150);
                //设置动画
                growMarker.setAnimation(animation);
                //开始动画
                growMarker.startAnimation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateClusters() {
        mIsCanceled = false;
        mClusters.clear();
        LatLngBounds visibleBounds = mAMap.getProjection().getVisibleRegion().latLngBounds;
        for (ClusterItem clusterItem : mClusterItems) {
            if (mIsCanceled) {
                return;
            }
            LatLng latlng = clusterItem.getPosition();
            int i = clusterItem.i();

            if (visibleBounds.contains(latlng)) {
                Cluster cluster = getCluster(latlng, mClusters);
                if (cluster != null) {
                    cluster.addClusterItem(clusterItem);
                } else {
                    cluster = new Cluster(latlng, i);
                    mClusters.add(cluster);
                    cluster.addClusterItem(clusterItem);
                }
            }
        }
        //复制一份数据，规避同步
        List<Cluster> clusters = new ArrayList<Cluster>();
        clusters.addAll(mClusters);
        Message message = Message.obtain();
        message.what = MarkerHandler.ADD_CLUSTER_LIST;
        message.obj = clusters;
        if (mIsCanceled) {
            return;
        }
        mMarkerhandler.sendMessage(message);
    }

    /**
     * 对点进行聚合
     */
    private void assignClusters() {
        mIsCanceled = true;
        mSignClusterHandler.removeMessages(SignClusterHandler.CALCULATE_CLUSTER);
        mSignClusterHandler.sendEmptyMessage(SignClusterHandler.CALCULATE_CLUSTER);
    }

    /**
     * 在已有的聚合基础上，对添加的单个元素进行聚合
     *
     * @param clusterItem
     */
    private void calculateSingleCluster(ClusterItem clusterItem) {
        LatLngBounds visibleBounds = mAMap.getProjection().getVisibleRegion().latLngBounds;
        LatLng latlng = clusterItem.getPosition();
        int i = clusterItem.i();

        if (!visibleBounds.contains(latlng)) {
            return;
        }
        Cluster cluster = getCluster(latlng, mClusters);
        if (cluster != null) {
            cluster.addClusterItem(clusterItem);
            Message message = Message.obtain();
            message.what = MarkerHandler.UPDATE_SINGLE_CLUSTER;
            message.obj = cluster;
            mMarkerhandler.removeMessages(MarkerHandler.UPDATE_SINGLE_CLUSTER);
            mMarkerhandler.sendMessageDelayed(message, 5);

        } else {
            cluster = new Cluster(latlng, i);
            mClusters.add(cluster);
            cluster.addClusterItem(clusterItem);
            Message message = Message.obtain();
            message.what = MarkerHandler.ADD_SINGLE_CLUSTER;
            message.obj = cluster;
            mMarkerhandler.sendMessage(message);
        }
    }
    /**
     * 根据一个点获取是否可以依附的聚合点，没有则返回null
     *
     * @param latLng
     * @return
     */
    private Cluster getCluster(LatLng latLng, List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            LatLng clusterCenterPoint = cluster.getCenterLatLng();
            double distance = AMapUtils.calculateLineDistance(latLng, clusterCenterPoint);
            if (distance * 2 < mClusterDistance && mAMap.getCameraPosition().zoom < 13) {
                return cluster;
            }
        }
        return null;
    }
    /**
     * 获取每个聚合点的绘制样式
     */
    private BitmapBean getBitmapDes(int num) {
        BitmapBean bitmapBean = new BitmapBean();
        BitmapDescriptor bitmapDescriptor = mLruCache.get(num);
        if (bitmapDescriptor == null) {
            TextView textView = new TextView(mContext);
//            if (num==1){
//                textView.setBackgroundResource(R.drawable.defaultcluster);
//                bitmapDescriptor = BitmapDescriptorFactory.fromView(textView);
//                mLruCache.put(num, bitmapDescriptor);
//                bitmapBean.setSingle(true);
//                bitmapBean.setBitmapDescriptor(bitmapDescriptor);
//                return bitmapBean;
//            }
            if (num > 1) {
                String tile = String.valueOf(num);
                textView.setText(tile);
            }
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            if (mClusterRender != null && mClusterRender.getDrawAble(num) != null) {
                textView.setBackgroundDrawable(mClusterRender.getDrawAble(num));
                bitmapBean.setSingle(false);
            } else {
                textView.setBackgroundResource(R.drawable.location);
                bitmapBean.setSingle(true);
            }
            bitmapDescriptor = BitmapDescriptorFactory.fromView(textView);
            mLruCache.put(num, bitmapDescriptor);

        }
        bitmapBean.setBitmapDescriptor(bitmapDescriptor);
        return bitmapBean;
    }
    /**
     * 更新已加入地图聚合点的样式
     */
    private void updateCluster(Cluster cluster) {
        Marker marker = cluster.getMarker();
        marker.setIcon(getBitmapDes(cluster.getClusterCount()).getBitmapDescriptor());
    }
    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = null;
        infoWindow = showQDStation(marker);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
//-----------------------辅助内部类用---------------------------------------------

    /**
     * marker渐变动画，动画结束后将Marker删除
     */
    class MyAnimationListener implements Animation.AnimationListener {
        private List<Marker> mRemoveMarkers;

        MyAnimationListener(List<Marker> removeMarkers) {
            mRemoveMarkers = removeMarkers;
        }

        @Override
        public void onAnimationStart() {

        }

        @Override
        public void onAnimationEnd() {
            for (Marker marker : mRemoveMarkers) {
                marker.remove();
            }
            mRemoveMarkers.clear();
        }
    }

    /**
     * 处理market添加，更新等操作
     */
    class MarkerHandler extends Handler {

        static final int ADD_CLUSTER_LIST = 0;

        static final int ADD_SINGLE_CLUSTER = 1;

        static final int UPDATE_SINGLE_CLUSTER = 2;

        MarkerHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {

            switch (message.what) {
                case ADD_CLUSTER_LIST:
                    if (mAMap.getCameraPosition().zoom >= 13) {
                        isFrist++;
                        Log.e("isFrist", "isFrist=" + isFrist);
                    } else {
                        if (isFrist>0)
                        isFrist--;
                    }
                    if (isFrist >= 2) {
                        isFrist = 0;
                        break;
                    }
                    List<Cluster> clusters = (List<Cluster>) message.obj;
                    addClusterToMap(clusters);
                    break;
                case ADD_SINGLE_CLUSTER:
                    Cluster cluster = (Cluster) message.obj;

                    addSingleClusterToMap(cluster);
                    break;
                case UPDATE_SINGLE_CLUSTER:
                    Cluster updateCluster = (Cluster) message.obj;
                    updateCluster(updateCluster);
                    break;
            }
        }
    }

    /**
     * 处理聚合点算法线程
     */
    class SignClusterHandler extends Handler {
        static final int CALCULATE_CLUSTER = 0;
        static final int CALCULATE_SINGLE_CLUSTER = 1;

        SignClusterHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case CALCULATE_CLUSTER:
                    Log.e("sssssssssssssssssss", mAMap.getCameraPosition().zoom + "");
                    calculateClusters();
                    break;
                case CALCULATE_SINGLE_CLUSTER:
                    ClusterItem item = (ClusterItem) message.obj;
                    mClusterItems.add(item);
                    Log.i("yiyi.qi", "calculate single cluster");
                    calculateSingleCluster(item);
                    break;
            }
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        return view.getDrawingCache(true);
    }
    private View showQDStation(final Marker marker) {
        final View infoWindow;
        infoWindow = View.inflate(mContext, R.layout.cz_hedao_info_window, null);
//        infoWindow = mContext.getLayoutInflater().inflate(R.layout.cz_hedao_info_window, null);
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
        stnm.setText(station.getStName().trim() + "(" + station.getStcd().trim() + ")");
        stcd.setText(station.getStcd().trim());
        sttp.setText("渠道站");
        stlc.setText(station.getSttp().trim());
        z.setText(station.getStage().trim());
        q.setText(station.getFlow().trim().equals("") ? "0.0" : station.getFlow().trim());
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
                WSDActivity.startAction(mContext, station.getStcd(), station.getTm());
            }
        });
        return infoWindow;
    }
}