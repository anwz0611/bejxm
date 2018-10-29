package com.zyinfo.brj.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;

/**
 * Created by Zwei  on 2018/7/23.
 * E-Mail Address：592296083@qq.com
 */

public class SelfPermissionGranted {

     /*类MyUtils中自定义权限是否开启方法*/
  public static boolean selfPermissionGranted(String permission, Context context) {
               // For Android < Android M, self permissions are always granted.
              boolean result = true;
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                      if (Integer.valueOf(android.os.Build.VERSION.SDK) >= Build.VERSION_CODES.M) {
                              // targetSdkVersion >= Android M, we can
                            // use Context#checkSelfPermission
                                result = context.checkSelfPermission(permission)
                                      == PackageManager.PERMISSION_GRANTED;
                         } else {
                              // targetSdkVersion < Android M, we have to use PermissionChecker
                              result = PermissionChecker.checkSelfPermission(context, permission)
                                      == PermissionChecker.PERMISSION_GRANTED;
                           }
                   }
                return result;
           }
}
