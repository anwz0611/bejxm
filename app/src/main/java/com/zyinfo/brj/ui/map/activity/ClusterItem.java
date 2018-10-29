package com.zyinfo.brj.ui.map.activity;

import com.amap.api.maps.model.LatLng;

/**
 *
 */

public interface ClusterItem {
    /**
     * 返回聚合元素的地理位置
     *
     * @return
     */
     LatLng getPosition();
    int i();
}
