package com.zyinfo.brj.ui.map.activity;

import com.amap.api.maps.model.LatLng;


/**
 *
 */

public class RegionItem implements ClusterItem {
    private LatLng mLatLng;
    private String mTitle;
    private  int mI;
    public RegionItem(LatLng latLng, String title, int i) {
        mLatLng=latLng;
        mTitle=title;
        mI=i;
    }

    @Override
    public LatLng getPosition() {
        // TODO Auto-generated method stub
        return mLatLng;
    }

    @Override
    public int i() {
        return mI;
    }

    public String getTitle(){
        return mTitle;
    }

}
