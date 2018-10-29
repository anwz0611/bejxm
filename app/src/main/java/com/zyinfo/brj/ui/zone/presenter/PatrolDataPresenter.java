package com.zyinfo.brj.ui.zone.presenter;

import com.zyinfo.brj.R;
import com.zyinfo.brj.bean.PatrolDataBeans;
import com.zyinfo.brj.bean.PatrolUploadingBeans;
import com.zyinfo.brj.ui.zone.contract.PatrolDataContract;
import com.zyinfo.common.baserx.RxSubscriber;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Zwei  on 2018/5/30.
 * E-Mail Address：592296083@qq.com
 */

public class PatrolDataPresenter extends PatrolDataContract.Presenter {
    @Override
    public void getPatrolDataRequest() {
        mRxManage.add(mModel.getPatrolData().subscribe(new RxSubscriber<PatrolDataBeans>(mContext, false) {

            @Override
            protected void _onNext(PatrolDataBeans patrolDataBeans) {
                mView.returnPatrolData(patrolDataBeans);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));


    }

    @Override
    public void getPatrolUploadingRequest(RequestBody can, RequestBody cantype, RequestBody id, RequestBody classification, RequestBody grade, RequestBody tm, RequestBody part, RequestBody description, RequestBody mgr, RequestBody tel, RequestBody annex,RequestBody lgtd, RequestBody lttd,  List<MultipartBody.Part> photo) {
        mRxManage.add(mModel.patrolUploading(can,cantype,id,classification,grade,tm,part,description,mgr,tel,annex, lgtd,  lttd, photo).subscribe(new RxSubscriber<PatrolUploadingBeans>(mContext, false){

            @Override
            protected void _onNext(PatrolUploadingBeans stringResult) {
                mView.returnPatrolUploading(stringResult);
                mView.stopLoading();

            }

            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onError(String message) {
                mView.stopLoading();
                mView.showErrorTip("上传失败");
            }
        }));
    }

    @Override
    public void onStart() {
        super.onStart();

    }


}
