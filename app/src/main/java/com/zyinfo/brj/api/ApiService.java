package com.zyinfo.brj.api;

import com.zyinfo.brj.bean.AssociationInfoBeans;
import com.zyinfo.brj.bean.GirlData;
import com.zyinfo.brj.bean.IPQCbeans;
import com.zyinfo.brj.bean.LoginsBeans;
import com.zyinfo.brj.bean.NewsDetail;
import com.zyinfo.brj.bean.NewsSummary;
import com.zyinfo.brj.bean.PatrolDataBeans;
import com.zyinfo.brj.bean.PatrolUploadingBeans;
import com.zyinfo.brj.bean.PolicyLawInfoBeans;
import com.zyinfo.brj.bean.SendMessageBeans;
import com.zyinfo.brj.bean.VideoData;
import com.zyinfo.brj.bean.addressBookInfoBeans;
import com.zyinfo.brj.bean.patrolRecordBeans;
import com.zyinfo.brj.bean.sluiceInfoBean;
import com.zyinfo.brj.bean.stationInfoBeans;
import com.zyinfo.brj.bean.userInfoBeans;
import com.zyinfo.brj.bean.waterChannelBeans;
import com.zyinfo.brj.bean.waterSituation;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * des:ApiService
 *
 */
public interface ApiService {
//
//    @GET("login")
//    Observable<BaseRespose<User>> login(@Query("username") String username, @Query("password") String password);

    @GET("nc/article/{postId}/full.html")
    Observable<Map<String, NewsDetail>> getNewDetail(
            @Header("Cache-Control") String cacheControl,
            @Path("postId") String postId);

    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    Observable<Map<String, List<NewsSummary>>> getNewsList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type, @Path("id") String id,
            @Path("startPage") int startPage);

    @GET
    Observable<ResponseBody> getNewsBodyHtmlPhoto(
            @Header("Cache-Control") String cacheControl,
            @Url String photoPath);
    //@Url，它允许我们直接传入一个请求的URL。这样以来我们可以将上一个请求的获得的url直接传入进来，baseUrl将被无视
    // baseUrl 需要符合标准，为空、""、或不合法将会报错

    @GET("data/{size}/{page}")
    Observable<GirlData> getPhotoList(
            @Header("Cache-Control") String cacheControl,
            @Path("size") int size,
            @Path("page") int page);

    @GET("nc/video/list/{type}/n/{startPage}-10.html")
    Observable<Map<String, List<VideoData>>> getVideoList(
            @Header("Cache-Control") String cacheControl,
            @Path("type") String type,
            @Path("startPage") int startPage);

    //布尔津项目


    //登录
    @POST("login")
    Observable<LoginsBeans> login(
            @Query("loginid") String loginid,
            @Query("loginpwd") String loginpwd

    );


    //河道水情列表
    @POST("waterSituation")
    Observable<List<waterSituation>> getWaterSituation(
            @Query("newDate") String newDate,
            @Query("historyDate") String historyDate,
            @Query("flag") String flag,
            @Query("pageSize") String pageSize,
            @Query("pageNum") String pageNum,
            @Query("sortContent") String sortContent,
            @Query("sortType") String sortType,
            @Query("wptn") String wptn,
            @Query("keyWord") String keyWord
    );


    //地图渠道水情
    @POST("waterMapSituation")
    Observable<List<waterSituation>> getWaterMapSituation(

            @Query("date") String date
    );


    //河道水情详情
    @POST("waterSituationDetail")
    Observable<List<waterSituation>> getwaterSituationDetail(
            @Query("stcd") String stcd,
            @Query("sttp") String sttp,
            @Query("startTime") String startTime,
            @Query("endTime") String endTime
    );
    //渠道
    @POST("waterChannel")
    Observable<List<waterChannelBeans>> getWaterChannel(


    );
    //渠道1
    @POST("waterChannelDetail")
    Observable<List<waterChannelBeans>> getWaterChannelDetail(
            @Query("id") String id,
            @Query("type") String type

    );
    //巡查事件列表
    @POST("IPQC")
    Observable<List<IPQCbeans>> getIPQC(
            @Query("tm") String tm,
            @Query("pageSize") String pageSize,
            @Query("pageNum") String pageNum
    );

    //测站
    @POST("stationInfo")
    Observable<List<stationInfoBeans>> getStationInfo(
            @Query("keyWord") String keyWord,
            @Query("stationType") String stationType,
            @Query("pageSize") String pageSize,
            @Query("pageNum") String pageNum
    );

    //协会
    @POST("associationInfo")
    Observable<List<AssociationInfoBeans>> getAssociationInfo(
            @Query("keyWord") String keyWord,
            @Query("pageSize") String pageSize,
            @Query("pageNum") String pageNum
    );


    //协会
    @POST("sluiceInfo")
    Observable<List<sluiceInfoBean>> getsLuiceInfo(
            @Query("keyWord") String keyWord,
            @Query("pageSize") String pageSize,
            @Query("pageNum") String pageNum
    );

    //用水户
    @POST("userInfo")
    Observable<List<userInfoBeans>> getUserInfo(
            @Query("keyWord") String keyWord,
            @Query("pageSize") String pageSize,
            @Query("pageNum") String pageNum
    );


    //通讯录
    @POST("addressBookInfo")
    Observable<List<addressBookInfoBeans>> getAddressBookInfo(

    );


    //政策法规
    @POST("policyLawInfo")
    Observable<List<PolicyLawInfoBeans>> getPolicyLawInfo(
            @Query("pageSize") String pageSize,
            @Query("pageNum") String pageNum
    );

    //巡查上传选择列表
    @POST("patrolRecord")
    Observable<List<patrolRecordBeans>> getPatrolRecord(
    );

    //
    @POST("patrolData")
    Observable<PatrolDataBeans> getPatrolData(
    );



    //
    @POST("sendMessage")
    Observable<SendMessageBeans> sendMessage(
            @Query("phoneList") String phoneList,
            @Query("content") String content
    );

    //巡查上传
    @Multipart
    @POST("patrolUploading")
    Observable<PatrolUploadingBeans> patrolUploading(
            @Part("can") RequestBody can,
            @Part("cantype") RequestBody cantype,
            @Part("id") RequestBody id,
            @Part("classification") RequestBody classification,
            @Part("grade") RequestBody grade,
            @Part("tm") RequestBody tm,
            @Part("part") RequestBody part,
            @Part("description") RequestBody description,
            @Part("mgr") RequestBody mgr,
            @Part("tel") RequestBody tel,
            @Part("annex") RequestBody annex,
            @Part("lgtd") RequestBody lgtd   ,
            @Part("lttd") RequestBody lttd ,
            @Part List<MultipartBody.Part> file

          );
}
