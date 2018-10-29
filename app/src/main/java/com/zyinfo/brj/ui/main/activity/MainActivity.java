package com.zyinfo.brj.ui.main.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zyinfo.brj.R;
import com.zyinfo.brj.app.AppConstant;
import com.zyinfo.brj.bean.TabEntity;
import com.zyinfo.brj.ui.main.fragment.CareMainFragment;
import com.zyinfo.brj.ui.main.fragment.JiChuTFragment;
import com.zyinfo.brj.ui.main.fragment.JichuFragment;
import com.zyinfo.brj.ui.main.fragment.MapFragment;
import com.zyinfo.brj.ui.main.fragment.MoreFragment;
import com.zyinfo.brj.ui.main.fragment.NewsMainFragment;
import com.zyinfo.brj.ui.main.fragment.PhotosMainFragment;
import com.zyinfo.brj.ui.main.fragment.VideoMainFragment;
import com.zyinfo.common.base.BaseActivity;
import com.zyinfo.common.baseapp.AppConfig;
import com.zyinfo.common.commonutils.LogUtils;
import com.zyinfo.common.daynightmodeutils.ChangeModeController;

import java.util.ArrayList;

import butterknife.Bind;
import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * des:主界面
 *
 *
 */
public class MainActivity extends BaseActivity {
    @Bind(R.id.tab_layout)
    CommonTabLayout tabLayout;

    @Bind(R.id.my_toolbar)
    Toolbar toolbar;

    private String[] mTitles = {"首页", "巡查","基础","更多"};
    private int[] mIconUnselectIds = {
            R.mipmap.sy_no,R.mipmap.xc_no,R.mipmap.jc_no,R.mipmap.gd_no};
    private int[] mIconSelectIds = {
            R.mipmap.sy_sl,R.mipmap.xc_sl, R.mipmap.jc_sl,R.mipmap.gd_sl};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private JichuFragment jichuFragment;
    private NewsMainFragment newsMainFragment;
    private PhotosMainFragment photosMainFragment;
    private MapFragment mapFragment;
    private VideoMainFragment videoMainFragment;
    private CareMainFragment careMainFragment;
    private MoreFragment moreFragment;
    private JiChuTFragment jiChuTFragment;
    private static int tabLayoutHeight;

    public MainActivity() {
    }

    /**
     * 入口
     * @param activity
     */
    public static void startAction(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.zyinfo.common.R.anim.fade_out);
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void initPresenter() {

    }
    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        //此处填上在http://fir.im/注册账号后获得的API_TOKEN以及APP的应用ID
        UpdateKey.API_TOKEN = AppConfig.API_FIRE_TOKEN;
        UpdateKey.APP_ID = AppConfig.APP_FIRE_ID;
        //如果你想通过Dialog来进行下载，可以如下设置
//        UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG;
        UpdateFunGO.init(this);
        //初始化菜单
        initTab();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //切换daynight模式要立即变色的页面
        ChangeModeController.getInstance().init(this,R.attr.class);
        super.onCreate(savedInstanceState);
        //初始化frament
        initFragment(savedInstanceState);
        tabLayout.measure(0,0);
        tabLayoutHeight=tabLayout.getMeasuredHeight();
//        //监听菜单显示或隐藏
//        mRxManager.on(AppConstant.MENU_SHOW_HIDE, new Action1<Boolean>() {
//
//            @Override
//            public void call(Boolean hideOrShow) {
//                startAnimation(hideOrShow);
//            }
//        });
    }
    /**
     * 初始化tab
     */
    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        //点击监听
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                SwitchTo(position);
            }
            @Override
            public void onTabReselect(int position) {
            }
        });
    }
    /**
     * 初始化碎片
     */
    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            newsMainFragment = (NewsMainFragment) getSupportFragmentManager().findFragmentByTag("newsMainFragment");
            mapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("mapFragment");
//            videoMainFragment = (VideoMainFragment) getSupportFragmentManager().findFragmentByTag("videoMainFragment");
            jiChuTFragment = (JiChuTFragment) getSupportFragmentManager().findFragmentByTag("jiChuTFragment");
//            careMainFragment = (CareMainFragment) getSupportFragmentManager().findFragmentByTag("careMainFragment");
            moreFragment = (MoreFragment) getSupportFragmentManager().findFragmentByTag("moreFragment");
            currentTabPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            newsMainFragment = new NewsMainFragment();
            mapFragment = new MapFragment();
//            videoMainFragment = new VideoMainFragment();

            jiChuTFragment = new JiChuTFragment();
//            careMainFragment = new CareMainFragment();
            moreFragment = new MoreFragment();

            transaction.add(R.id.fl_body, newsMainFragment, "newsMainFragment");
            transaction.add(R.id.fl_body, mapFragment, "mapFragment");
            transaction.add(R.id.fl_body, jiChuTFragment, "jiChuTFragment");
            transaction.add(R.id.fl_body, moreFragment, "moreFragment");
        }
        transaction.commit();
        SwitchTo(currentTabPosition);
        tabLayout.setCurrentTab(currentTabPosition);
    }

    /**
     * 切换
     */
    private void SwitchTo(int position) {
        LogUtils.logd("主页菜单position" + position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            //首页
            case 0:
//                invalidateOptionsMenu();
                transaction.hide(mapFragment);
//                transaction.hide(videoMainFragment);
                transaction.hide(jiChuTFragment);
//                transaction.hide(careMainFragment);
                transaction.hide(moreFragment);
                transaction.show(newsMainFragment);
                transaction.commitAllowingStateLoss();
                break;
            //巡查
            case 1:
                transaction.hide(newsMainFragment);
//                transaction.hide(videoMainFragment);
                transaction.hide(jiChuTFragment);
//                transaction.hide(careMainFragment);
                transaction.hide(moreFragment);
                transaction.show(mapFragment);
                transaction.commitAllowingStateLoss();
                break;
            //基础
            case 2:
//                invalidateOptionsMenu();
                transaction.hide(newsMainFragment);
                transaction.hide(mapFragment);
//                transaction.hide(careMainFragment);
                transaction.hide(moreFragment);
                transaction.show(jiChuTFragment);
                transaction.commitAllowingStateLoss();
                break;
//            //更多
            case 3:
                transaction.hide(newsMainFragment);
                transaction.hide(mapFragment);
                transaction.hide(jiChuTFragment);
//                transaction.show(careMainFragment);
                transaction.show(moreFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    /**
     * 菜单显示隐藏动画
     * @param showOrHide
     */
    private void startAnimation(boolean showOrHide){
        final ViewGroup.LayoutParams layoutParams = tabLayout.getLayoutParams();
        ValueAnimator valueAnimator;
        ObjectAnimator alpha;
        if(!showOrHide){
             valueAnimator = ValueAnimator.ofInt(tabLayoutHeight, 0);
            alpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 1, 0);
        }else{
             valueAnimator = ValueAnimator.ofInt(0, tabLayoutHeight);
            alpha = ObjectAnimator.ofFloat(tabLayout, "alpha", 0, 1);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                layoutParams.height= (int) valueAnimator.getAnimatedValue();
                tabLayout.setLayoutParams(layoutParams);
            }
        });
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(valueAnimator,alpha);
        animatorSet.start();
    }

    /**
     * 监听全屏视频时返回键
     */
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //奔溃前保存位置
        LogUtils.loge("onSaveInstanceState进来了1");
        if (tabLayout != null) {
            LogUtils.loge("onSaveInstanceState进来了2");
            outState.putInt(AppConstant.HOME_CURRENT_TAB_POSITION, tabLayout.getCurrentTab());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateFunGO.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGO.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChangeModeController.onDestory();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
