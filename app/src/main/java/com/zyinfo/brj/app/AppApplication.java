package com.zyinfo.brj.app;
import com.zyinfo.brj.BuildConfig;
import com.zyinfo.common.baseapp.BaseApplication;
import com.zyinfo.common.commonutils.LogUtils;

/**
 * APPLICATION
 */
public class AppApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化logger
        LogUtils.logInit(BuildConfig.LOG_DEBUG);

    }
}
