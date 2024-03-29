package com.stefan.classscheduleforusc.mvp.impt;

import android.graphics.Bitmap;

import com.stefan.classscheduleforusc.R;
import com.stefan.classscheduleforusc.data.http.retrofit2;
import com.stefan.classscheduleforusc.app.Cache;
import com.stefan.classscheduleforusc.app.app;
import com.stefan.classscheduleforusc.data.bean.CourseTime;
import com.stefan.classscheduleforusc.data.beanv2.CourseGroup;
import com.stefan.classscheduleforusc.data.beanv2.CourseV2;
import com.stefan.classscheduleforusc.data.greendao.CourseGroupDao;
import com.stefan.classscheduleforusc.data.greendao.CourseV2Dao;
import com.stefan.classscheduleforusc.data.http.EduHttpUtils;
import com.stefan.classscheduleforusc.data.http.HttpCallback;
import com.stefan.classscheduleforusc.utils.LogUtil;
import com.stefan.classscheduleforusc.utils.Preferences;
import com.stefan.classscheduleforusc.utils.ToastUtils;
import com.stefan.classscheduleforusc.utils.spec.ParseCourse;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by stefan on 2019/10/03.
 */

public class ImptPresenter implements ImptContract.Presenter {

    private ImptContract.View mImptView;
    private String mSchoolUrl;

    private String xh;
    private String mNormalCourseHtml;
    private String mSelectYear;
    private String mSelectTerm;
    private retrofit2 retrofit;

    public ImptPresenter(ImptContract.View imptView, String schoolUrl) {
        mImptView = imptView;
        mImptView.setPresenter(this);

        mSchoolUrl = schoolUrl;
    }

    @Override
    public void start() {
        getCaptcha();
    }


    @Override
    public void importCustomCourses(final String year, final String term) {
        LogUtil.d(this, "importCustomCourses");
        LogUtil.d(this, "sy" + mSelectYear + "st" + mSelectTerm + "y" + year + "t" + term);
        if (year.equals(mSelectYear) && term.equals(mSelectTerm)) {
            importDefaultCourses(year, term);
            return;
        }

        mImptView.showImpting();
        EduHttpUtils.newInstance().toImpt(mSchoolUrl, xh, year, term, new HttpCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if (mImptView == null) {
                    //view被销毁
                    return;
                }
                parseCoursesHtmlToDb(s, year + "-" + term);
            }

            @Override
            public void onFail(String errMsg) {
                if (mImptView == null) {
                    //view被销毁
                    return;
                }
                mImptView.hideImpting();
                mImptView.showErrToast(errMsg, true);
            }
        });
    }

    @Override
    public void importDefaultCourses(final String year, final String term) {
        LogUtil.d(this, "importCustomCourses");
        mImptView.showImpting();
        parseCoursesHtmlToDb(mNormalCourseHtml, year + "-" + term);
    }

    @Override
    public void loadCourseTimeAndTerm(final String xh, String pwd, String captcha) {
        if (!verify(xh, pwd, captcha)) return;

        mImptView.showImpting();//view显示“导入中...”
        EduHttpUtils.newInstance().login(mSchoolUrl, xh, pwd, captcha, null, null,
                new HttpCallback<String>() {

                    @Override
                    public void onSuccess(String s) {
                        if (mImptView == null) {
                            //view被销毁
                            return;
                        }
                        ImptPresenter.this.xh = xh;
                        mNormalCourseHtml = s;
                        mImptView.hideImpting();
                        parseTimeTermHtmlToShow(s);
                    }

                    @Override
                    public void onFail(String errMsg) {
                        if (mImptView == null) {
                            //view被销毁
                            return;
                        }
                        mImptView.hideImpting();
                        mImptView.showErrToast(errMsg, true);
                    }
                });

    }

    @Override
    public void loginToUsc(String xh, String pwd,String captcha) {

        if (!verify(xh, pwd, captcha)) return;
        retrofit = new retrofit2();
        retrofit.LoginToUSC(xh,pwd).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int type = retrofit.Getloginresponse(response);
                if (type==1){
                    // TODO: 2019/10/5 成功登陆教务在线，接下来导入课程表


                    LogUtil.i(this, "成功状态值:" + type);
                    ToastUtils.show("成功");

                }else{
                    LogUtil.i(this, "失败状态值:" + type);
                    ToastUtils.show("失败，检查用户名或密码");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private void parseTimeTermHtmlToShow(String html) {
        CourseTime ct = ParseCourse.parseTime(html);

        if (ct == null || ct.years.size() == 0) {
            mImptView.showErrToast("导入学期失败", true);
            return;
        }
        mSelectYear = ct.selectYear;
        mSelectTerm = ct.selectTerm;
        mImptView.showCourseTimeDialog(ct);
    }

    private void parseCoursesHtmlToDb(final String html, final String courseTimeTerm) {
        try {
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    ArrayList<CourseV2> courseV2s = ParseCourse.parse(html);
                    //删除旧数据
                    CourseGroup group = Cache.instance().getCourseGroupDao()
                            .queryBuilder()
                            .where(CourseGroupDao.Properties.CgName.eq(courseTimeTerm))
                            .unique();

                    CourseV2Dao courseV2Dao = Cache.instance().getCourseV2Dao();
                    if (null != group) {
                        courseV2Dao
                                .queryBuilder()
                                .where(CourseV2Dao.Properties.CouCgId.eq(group.getCgId()))
                                .buildDelete()
                                .executeDeleteWithoutDetachingEntities();
                    } else {
                        // 不存在旧数据 创建新的课表组
                        group = new CourseGroup();
                        group.setCgName(courseTimeTerm);
                        Cache.instance().getCourseGroupDao().insert(group);
                    }

                    for (CourseV2 courseV2 : courseV2s) {
                        courseV2.setCouCgId(group.getCgId());
                        courseV2Dao.insert(courseV2);
                    }

                    emitter.onNext("导入成功");
                    emitter.onComplete();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(String s) {
                            if (mImptView == null) {
                                //view被销毁
                                return;
                            }
                            LogUtil.i(this, "导入成功:" + courseTimeTerm);

                            Long curGroupId = Cache.instance().getCourseGroupDao()
                                    .queryBuilder()
                                    .where(CourseGroupDao.Properties.CgName.eq(courseTimeTerm))
                                    .unique().getCgId();
                            Preferences.putLong(app.mContext.getString(
                                    R.string.app_preference_current_cs_name_id), curGroupId);

                            mImptView.hideImpting();
                            mImptView.showSucceed();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (mImptView == null) {
                                //view被销毁
                                return;
                            }
                            e.printStackTrace();
                            mImptView.hideImpting();
                            mImptView.showErrToast("插入数据库失败", true);
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            mImptView.hideImpting();
            mImptView.showErrToast("导入错误", true);
        }
    }


    private boolean verify(String xh, String pwd, String captcha) {
        if (mImptView == null) {
            //view被销毁
            return false;
        }
        if (xh.isEmpty()) {
            mImptView.showErrToast("请填写学号", false);
            return false;
        }

        if (pwd.isEmpty()) {
            mImptView.showErrToast("请填写密码", false);
            return false;
        }

        if (captcha.isEmpty()) {
            mImptView.showErrToast("请填写验证码", false);
            return false;
        }
        return true;
    }


    private boolean captchaIsLoading = false;

    @Override
    public void getCaptcha() {
        //防止重复点击加载验证码按钮导致多次执行
        if (captchaIsLoading) {
            return;
        }

        captchaIsLoading = true;
        mImptView.captchaIsLoading(true);

        EduHttpUtils.newInstance().loadCaptcha(app.mContext.getCacheDir(), mSchoolUrl,
                new HttpCallback<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        if (mImptView == null) {
                            //view被销毁
                            return;
                        }
                        mImptView.getCaptchaIV().setImageBitmap(bitmap);
                        captchaIsLoading = false;
                        mImptView.captchaIsLoading(false);
                    }

                    @Override
                    public void onFail(String errMsg) {
                        if (mImptView == null) {
                            //view被销毁
                            return;
                        }
                        ToastUtils.show(errMsg);
                        mImptView.getCaptchaIV().setImageResource(R.drawable.ic_svg_refresh);
                        captchaIsLoading = false;
                        mImptView.captchaIsLoading(false);
                    }
                });
    }

    @Override
    public void onDestroy() {
        mImptView = null;
        System.gc();
    }
}
