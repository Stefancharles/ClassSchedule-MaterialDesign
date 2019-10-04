package com.stefan.classscheduleforusc.mvp.add;

import com.stefan.classscheduleforusc.BasePresenter;
import com.stefan.classscheduleforusc.BaseView;
import com.stefan.classscheduleforusc.data.beanv2.CourseV2;

/**
 * Created by stefan on 2019/10/03.
 */

public interface AddContract {
    interface Presenter extends BasePresenter {
        void addCourse(CourseV2 courseV2);
        void removeCourse(long courseId);
        void updateCourse(CourseV2 courseV2);
    }

    interface View extends BaseView<AddContract.Presenter> {
        void showAddFail(String msg);
        void onAddSucceed(CourseV2 courseV2);
        void onDelSucceed();
        void onUpdateSucceed(CourseV2 courseV2);
    }
}
