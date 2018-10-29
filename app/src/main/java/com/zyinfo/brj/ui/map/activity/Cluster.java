package com.zyinfo.brj.ui.map.activity;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class Cluster {


    private LatLng mLatLng;
    private List<ClusterItem> mClusterItems;
    private Marker mMarker;
    private  int mI;


    Cluster(LatLng latLng, int i) {
        int mI=i;
        mLatLng = latLng;
        mClusterItems = new ArrayList<ClusterItem>();
    }

    void addClusterItem(ClusterItem clusterItem) {
        mClusterItems.add(clusterItem);
    }

    int getClusterCount() {
        return mClusterItems.size();
    }



    LatLng getCenterLatLng() {
        return mLatLng;
    }

    int  getI(){
        return mI;
    }

    void setMarker(Marker marker) {
        mMarker = marker;
    }

    Marker getMarker() {
        return mMarker;
    }

    List<ClusterItem> getClusterItems() {
        return mClusterItems;
    }
}
