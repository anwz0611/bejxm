package com.zyinfo.brj.ui.zone.model;

import com.zyinfo.brj.api.Api;
import com.zyinfo.brj.api.HostType;

import com.zyinfo.brj.bean.PatrolDataBeans;

import com.zyinfo.brj.bean.PatrolUploadingBeans;
import com.zyinfo.brj.ui.zone.contract.PatrolDataContract;
import com.zyinfo.common.baserx.RxSchedulers;

import java.util.List;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;


/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class PatrolDataModel implements PatrolDataContract.Model {

    @Override
    public Observable<PatrolDataBeans> getPatrolData() {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).getPatrolData()
                .compose(RxSchedulers.<PatrolDataBeans>io_main());
    }

    @Override
    public Observable<PatrolUploadingBeans> patrolUploading(RequestBody can, RequestBody cantype, RequestBody id, RequestBody classification, RequestBody grade, RequestBody tm, RequestBody part, RequestBody description, RequestBody mgr, RequestBody tel, RequestBody annex, RequestBody lgtd, RequestBody lttd, List<MultipartBody.Part> photo) {
        return Api.getDefault(HostType.NETEASE_NEWS_VIDEO).patrolUploading(can, cantype, id, classification, grade, tm, part, description, mgr, tel, annex, lgtd, lttd, photo)
                .compose(RxSchedulers.<PatrolUploadingBeans>io_main());
    }


}
