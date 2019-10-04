package com.stefan.classscheduleforusc.utils.spec;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.stefan.classscheduleforusc.app.Url;
import com.stefan.classscheduleforusc.data.beanv2.VersionWrapper;
import com.stefan.classscheduleforusc.data.http.HttpCallback;
import com.stefan.classscheduleforusc.data.http.JsonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.lang.reflect.Field;

import okhttp3.Call;

/**
 * Created by stefan on 17-11-7.
 */

public class VersionUpdate {

    public void checkUpdate(final HttpCallback<VersionWrapper> callback) {
        OkHttpUtils.get().url(Url.URL_CHECK_UPDATE_APP)
                .build().execute(new JsonCallback<VersionWrapper>(VersionWrapper.class) {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                callback.onFail(e.getMessage());
            }

            @Override
            public void onResponse(VersionWrapper versionWrapper, int id) {
                callback.onSuccess(versionWrapper);
            }

            public  void testReflect(Object model) throws Exception{
                for (Field field : model.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    System.out.println(field.getName() + ":" + field.get(model) );
                }
            }
        });
    }

    /**
     * 获取本地软件版本号
     */
    public int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return localVersion;
    }

    public static  void goToMarket(Context context) {
        String packageName ="com.stefan.classscheduleforusc";
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
