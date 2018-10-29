package com.zyinfo.brj.ui.main.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zyinfo.brj.R;
import com.zyinfo.brj.utils.SelfPermissionGranted;
import com.zyinfo.brj.utils.SharedUtil;
import com.zyinfo.common.base.BaseActivity;
import com.yuyh.library.imgsel.common.Constant;

import butterknife.Bind;

/**
 * des:启动页
 */
public class SplashActivity extends BaseActivity {
    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.tv_name)
    TextView tvName;
    String LoginState = "false";

    /**
     * 入口
     *
     * @param mContext
     */
    public static void startAction(Context mContext) {
        Intent intent = new Intent(mContext, SplashActivity.class);
        mContext.startActivity(intent);

    }

    @Override
    public int getLayoutId() {
        return R.layout.act_splash;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        LoginState = SharedUtil.getValue(mContext, "LoginState", "");
        SetTranslanteBar();
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 0.3f, 1f);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.3f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.3f, 1f);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(tvName, alpha, scaleX, scaleY);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(ivLogo, alpha, scaleX, scaleY);


        if(!SelfPermissionGranted.selfPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION, getApplicationContext())){
                        ActivityCompat.requestPermissions(this,
                                         new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                       Constant.LOCATION_STATE);
            /*Constant.LOCATION_STATE 为自己定义的一个常量，为权限弹窗回调时使用*/
                    }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator1, objectAnimator2);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.setDuration(2000);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (LoginState.equals("true")) {
                    MainActivity.startAction(SplashActivity.this);
                    finish();
                } else {
                    LoginActivity.startAction(SplashActivity.this);
                    finish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length<=0){ return;}
        if (requestCode == Constant.LOCATION_STATE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(SplashActivity.this, "获取位置权限被禁用", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
