package com.zhd.mswcs.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

/**
 * 作者: Dream on 16/9/22 22:55
 * QQ:510278658
 * E-mail:510278658@qq.com
 */

public class SharedPreferencesConfig {

    private static final String NAME = "splash";
    private static final String VERSION_CODE = "versionCode";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    /**
     * 是否显示引导页(正常情况下:版本号判断是否要显示引导页)
     *
     * @param context
     * @return
     */
    public static boolean isShowGuide(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        int newVersionCode = getNewAppVersionCode(context);
        int versionCode = getVersionCode(context);
        if (newVersionCode != versionCode) {
            setVersionCode(context, newVersionCode);
            return true;
        }
        return false;
    }

    public static void setVersionCode(Context context, int versionCode) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putInt(VERSION_CODE, versionCode);
        editor.commit();
    }

    public static int getVersionCode(Context context) {
        return getSharedPreferences(context).getInt(VERSION_CODE, 0);
    }

    public static int getNewAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
