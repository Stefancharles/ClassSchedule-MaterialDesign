package com.stefan.classscheduleforusc.mvp.impt;


import android.widget.ImageView;

import com.stefan.classscheduleforusc.BasePresenter;
import com.stefan.classscheduleforusc.BaseView;
import com.stefan.classscheduleforusc.data.bean.CourseTime;

/**
 * Created by stefan on 2019/10/03.
 */

public interface ImptContract {
    interface Presenter extends BasePresenter {

        void importCustomCourses(String courseTime, String term);

        void importDefaultCourses(String courseTime, String term);

        void loadCourseTimeAndTerm(String xh, String pwd, String captcha);

        void getCaptcha();
    }

    interface View extends BaseView<Presenter> {
        ImageView getCaptchaIV();

        void showImpting();

        void hideImpting();

        void captchaIsLoading(boolean isLoading);

        void showErrToast(String errMsg, boolean reLoad);

        void showSucceed();

        void showCourseTimeDialog(CourseTime times);
    }

    interface Model {
        void getCaptcha(ImageView iv);
    }
}
