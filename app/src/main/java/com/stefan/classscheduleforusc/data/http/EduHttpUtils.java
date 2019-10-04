package com.stefan.classscheduleforusc.data.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.stefan.classscheduleforusc.R;
import com.stefan.classscheduleforusc.app.Url;
import com.stefan.classscheduleforusc.app.app;
import com.stefan.classscheduleforusc.utils.spec.ParseCourse;
import com.stefan.classscheduleforusc.utils.LogUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

/**
 * Created by stefan on 2019/10/03.
 */

public class EduHttpUtils {
    public static final String ACCESS_ERR = "抱歉,访问出错,请重试";

    private EduHttpUtils() {
    }


    private static class Holder {
        private static final EduHttpUtils HTTP_UTILS = new EduHttpUtils();
    }

    public static EduHttpUtils newInstance() {
        return Holder.HTTP_UTILS;
    }


    public void testUrl(String url, final HttpCallback<String> callback) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onFail(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                callback.onSuccess(response);
            }
        });
    }

    public void loadCaptcha(final File dir, final String schoolUrl, final HttpCallback<Bitmap> callback) {
        OkHttpUtils.post().url(schoolUrl + "/" +
                (schoolUrl.contains(Url.default2) ? "" : Url.default2))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onFail(ACCESS_ERR);
            }

            @Override
            public void onResponse(String response, int id) {
                Url.VIEWSTATE_LOGIN_CODE = ParseCourse.parseViewStateCode(response);
                LogUtil.d(this, "login_code:" + Url.VIEWSTATE_LOGIN_CODE);
                toLoadCaptcha(dir, schoolUrl, callback);
            }
        });
    }

    private void toLoadCaptcha(final File dir, String schoolUrl, final HttpCallback<Bitmap> callback) {
        OkHttpUtils.get().url(schoolUrl + "/" + Url.CheckCode).build().execute(
                new FileCallBack(dir.getAbsolutePath(), "loadCaptcha.jpg") {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(ACCESS_ERR);
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Bitmap bitmap = BitmapFactory.decodeFile(
                                dir.getAbsolutePath() + "/loadCaptcha.jpg");
                        callback.onSuccess(bitmap);
                    }
                });
    }

    public void login(final String schoolUrl, final String xh, String passwd, String catpcha,
                      final String courseTime, final String term,
                      final HttpCallback<String> callback) {
        OkHttpUtils.post().url(schoolUrl + "/" +
                (schoolUrl.contains(Url.default2) ? "" : Url.default2))
                .addParams("__VIEWSTATE", Url.VIEWSTATE_LOGIN_CODE)
                .addParams("txtUserName", xh)
                .addParams("Textbox1", "")
                .addParams("Textbox2", passwd)
                .addParams("txtSecretCode", catpcha)
                .addParams("RadioButtonList1", "学生")
                .addParams("Button1", "")
                .addParams("lbLanguage", "")
                .addParams("hidPdrs	", "")
                .addParams("hidsc", "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                callback.onFail(ACCESS_ERR);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.e(this, response);

                if (response.contains(app.mContext.getString(R.string.captcha_err))) {
                    callback.onFail(app.mContext.getString(R.string.captcha_err));
                } else if (response.contains(app.mContext.getString(R.string.pwd_err))) {
                    callback.onFail(app.mContext.getString(R.string.pwd_err));
                } else {
                    if (TextUtils.isEmpty(courseTime) || TextUtils.isEmpty(term)) {
                        toImpt(xh, schoolUrl, callback);
                        return;
                    }
                    toImpt(xh, schoolUrl, courseTime, term, callback);
                }
            }
        });
    }

    /**
     * get normal
     *
     * @param xh
     * @param callback
     */
    public void toImpt(String xh, String schoolUrl, final HttpCallback<String> callback) {
        LogUtil.d(this, "get normal+" + xh);
        OkHttpUtils.get().url(schoolUrl + "/" + Url.xskbcx)
                .addHeader("Referer", schoolUrl + "/" + Url.xskbcx + "?xh=" + xh)
                .addParams(Url.PARAM_XH, xh)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(ACCESS_ERR);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Url.VIEWSTATE_POST_CODE = ParseCourse.parseViewStateCode(response);
                        callback.onSuccess(response);
                    }
                });
    }

    /**
     * @param xh
     * @param courseTime 格式: 2017-2018
     * @param term       学期num
     * @param callback
     */
    public void toImpt(String schoolUrl, String xh, String courseTime, String term,
                       final HttpCallback<String> callback) {
        LogUtil.d("toImpt", "xh" + xh + "c" + courseTime + "t" + term);
        OkHttpUtils.post().url(schoolUrl + "/" + Url.xskbcx + "?xh=" + xh + "&xm=%D1%EE%D3%D1%C1%D6&gnmkdm=N121603")
                .addHeader("Referer", schoolUrl + "/" + Url.xskbcx + "?xh=" + xh + "&xm=%D1%EE%D3%D1%C1%D6&gnmkdm=N121603")
                .addHeader("Connection", "keep-alive")







                .addParams("__EVENTTARGET", "xnd")
                .addParams("__EVENTARGUMENT", "")
                .addParams("__VIEWSTATE", Url.VIEWSTATE_POST_CODE)
                .addParams(Url.PARAM_XND, courseTime)
                .addParams(Url.PARAM_XQD, term)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        callback.onFail(ACCESS_ERR);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Url.VIEWSTATE_POST_CODE = ParseCourse.parseViewStateCode(response);
                        callback.onSuccess(response);
                    }
                });
    }

    /**
     * 上传解析失败的课表html
     *
     * @param tag
     * @param html
     */
    public void uploadParsingFailedTable(Object tag, String html) {
        String newTag = LogUtil.getNewTag(tag);

    }

}


