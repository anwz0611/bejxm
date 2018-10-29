package com.zyinfo.brj.ui.zone.contract;


import com.zyinfo.brj.bean.PatrolDataBeans;
import com.zyinfo.brj.bean.PatrolUploadingBeans;
import com.zyinfo.common.base.BaseModel;
import com.zyinfo.common.base.BasePresenter;
import com.zyinfo.common.base.BaseView;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import rx.Observable;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public interface PatrolDataContract {

    interface Model extends BaseModel {

        Observable<PatrolDataBeans> getPatrolData();

        Observable<PatrolUploadingBeans> patrolUploading(RequestBody can, RequestBody cantype, RequestBody id, RequestBody classification, RequestBody grade
                , RequestBody tm, RequestBody part, RequestBody description, RequestBody mgr, RequestBody tel, RequestBody annex, RequestBody lgtd, RequestBody lttd, List<MultipartBody.Part> photo);
    }

    interface View extends BaseView {

        void returnPatrolData(PatrolDataBeans patrolDataBeans);

        void returnPatrolUploading(PatrolUploadingBeans Result);

    }

    abstract static class Presenter extends BasePresenter<PatrolDataContract.View, PatrolDataContract.Model> {

        public abstract void getPatrolDataRequest();

        public abstract void getPatrolUploadingRequest(RequestBody can, RequestBody cantype, RequestBody id, RequestBody classification, RequestBody grade
                , RequestBody tm, RequestBody part, RequestBody description, RequestBody mgr, RequestBody tel, RequestBody annex, RequestBody lgtd, RequestBody lttd, List<MultipartBody.Part> photo);
    }
}
