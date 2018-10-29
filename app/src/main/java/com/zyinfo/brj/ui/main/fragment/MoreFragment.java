package com.zyinfo.brj.ui.main.fragment;

import android.view.View;
import android.widget.Button;

import com.zyinfo.brj.R;
import com.zyinfo.brj.ui.main.activity.SplashActivity;
import com.zyinfo.brj.ui.more.activity.MeteorologicalActivity;
import com.zyinfo.brj.ui.more.activity.PanoramagramActivity;
import com.zyinfo.brj.ui.more.activity.PhoneListActivity;
import com.zyinfo.brj.ui.more.activity.PolicyLawInfoActivity;
import com.zyinfo.brj.ui.news.activity.AboutActivity;
import com.zyinfo.brj.utils.SharedUtil;
import com.zyinfo.common.base.BaseFragment;
import com.zyinfo.common.commonutils.ToastUitl;
import com.zyinfo.common.commonwidget.NormalTitleBar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Zwei  on 2018/6/7.
 * E-Mail Address：592296083@qq.com
 */

public class MoreFragment extends BaseFragment {
    @Bind(R.id.ntb)
    NormalTitleBar ntb;

    @Bind(R.id.exit_login_btn)
    Button exit_login_btn;
    @Override
    protected int getLayoutResource() {
        return R.layout.more_fragment;
    }

    @Override
    public void initPresenter() {

    }


    @Override
    protected void initView() {



        ntb.setTvLeftVisiable(false);
        ntb.setTitleText(getString(R.string.more));
    }
    @OnClick({R.id.layout_base_quhua, R.id.layout_ad_station, R.id.layout_small_watersh, R.id.layout_heliu, R.id.layout_difang,R.id.layout_difang1,R.id.layout_panoramagram,R.id.exit_login_btn})
    public void OnClick(View view){
    switch (view.getId()){

        case R.id.layout_base_quhua:
            PhoneListActivity.startAction(getContext());
            break;
        case R.id.layout_ad_station:
            PolicyLawInfoActivity.startAction(getContext());
            break;
        case R.id.layout_panoramagram:
            PanoramagramActivity.startAction(getContext());
            break;

        case R.id.layout_small_watersh:
            MeteorologicalActivity.startAction(getContext());
            break;
        case R.id.layout_heliu:
            ToastUitl.showShort("已经是最新版本！");
            break;
        case R.id.layout_difang:
            AboutActivity.startAction(getContext());
            break;
        case R.id.layout_difang1:
//            CirclePublishActivity.startAction(getContext());
            break;
        case R.id.exit_login_btn:

            SharedUtil.deleteValue(getContext(),"LoginState");
            SplashActivity.startAction(getContext());
            break;
    }


    }
}
