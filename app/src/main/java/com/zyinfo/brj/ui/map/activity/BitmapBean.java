package com.zyinfo.brj.ui.map.activity;

import com.amap.api.maps.model.BitmapDescriptor;

/**
 * Created by Zwei  on 2018/7/18.
 * E-Mail Address：592296083@qq.com
 */

public class BitmapBean {

    BitmapDescriptor bitmapDescriptor;
    boolean isSingle;

    public BitmapDescriptor getBitmapDescriptor() {
        return bitmapDescriptor;
    }

    public void setBitmapDescriptor(BitmapDescriptor bitmapDescriptor) {
        this.bitmapDescriptor = bitmapDescriptor;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }
}
