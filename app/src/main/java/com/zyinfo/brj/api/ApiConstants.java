/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.zyinfo.brj.api;

public class ApiConstants {
    public static final String NETEAST_HOST = "http://c.m.163.com/";
    public static final String END_URL = "-20.html";
    public static final String ENDDETAIL_URL = "/full.html";
//    public static final String BRJ_URL = "http://192.168.0.131:8080/jeeplus/a/szygl/waterManage/interface/"; //布尔津项目
//    public static final String BRJ_URL = "http://124.119.183.142:30002/jeeplus/a/szygl/waterManage/interface/"; //布尔津项目
    public static final String BRJ_URL = "http://60.13.230.147:30002/jeeplus/a/szygl/waterManage/interface/"; //布尔津项目
//    public static final String BRJ_URL = "http://192.168.0.36:8080/jeeplus/a/szygl/waterManage/interface/"; //布尔津项目
    //布尔津河项目URL
    public static final String waterSituation = BRJ_URL + "waterSituation";// 河道水情列表

    // 发送短信
    public static final String send_msm = BRJ_URL + "AppService.asmx";



    //
    public static final String NEWS_DETAIL = NETEAST_HOST + "nc/article/";

    //
    public static final String HEADLINE_TYPE = "headline";
    //
    public static final String HOUSE_TYPE = "house";
    //
    public static final String OTHER_TYPE = "list";


    // id
    public static final String HEADLINE_ID = "T1348647909107";
    // id
    public static final String HOUSE_ID = "5YyX5Lqs";

    public static final String Video = "nc/video/list/";
    public static final String VIDEO_CENTER = "/n/";
    public static final String VIDEO_END_URL = "-10.html";
    //
    public static final String VIDEO_HOT_ID = "V9LG4B3A0";
    //
    public static final String VIDEO_ENTERTAINMENT_ID = "V9LG4CHOR";
    //
    public static final String VIDEO_FUN_ID = "V9LG4E6VR";
    //
    public static final String VIDEO_CHOICE_ID = "00850FRB";

    /**
     * 天气预报url
     */
    public static final String WEATHER_HOST = "http://wthrcdn.etouch.cn/";

    /**
     *
     *
     */
    public static final String SINA_PHOTO_HOST = "http://gank.io/api/";

    //
    public static final String SINA_PHOTO_CHOICE_ID = "hdpic_toutiao";
    //
    public static final String SINA_PHOTO_FUN_ID = "hdpic_funny";
    //
    public static final String SINA_PHOTO_PRETTY_ID = "hdpic_pretty";
    //
    public static final String SINA_PHOTO_STORY_ID = "hdpic_story";

    // 图片详情
    public static final String SINA_PHOTO_DETAIL_ID = "hdpic_hdpic_toutiao_4";

    /**
     * id获取类型
     *
     * @param id
     * @return
     */
    public static String getType(String id) {
        switch (id) {
            case HEADLINE_ID:
                return HEADLINE_TYPE;
            case HOUSE_ID:
                return HOUSE_TYPE;
            default:
                break;
        }
        return OTHER_TYPE;
    }

    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.NETEASE_NEWS_VIDEO:
//                host = NETEAST_HOST;
                host =BRJ_URL;
                break;
            case HostType.GANK_GIRL_PHOTO:
                host = SINA_PHOTO_HOST;
                break;
            case HostType.NEWS_DETAIL_HTML_PHOTO:
                host = "http://kaku.com/";
                break;
            default:
                host = "";
                break;
        }
        return host;
    }
}
