package com.stefan.classscheduleforusc.mvp.course;


import com.stefan.classscheduleforusc.BasePresenter;
import com.stefan.classscheduleforusc.BaseView;
import com.stefan.classscheduleforusc.data.beanv2.CourseV2;

import java.util.List;

/**
 * Created by stefan on 2019/10/03.
 */


public interface CourseContract {
    interface Presenter extends BasePresenter {
        void updateCourseViewData(long csNameId);
        void deleteCourse(long courseId);
    }

    interface View extends BaseView<Presenter> {
        void initFirstStart();
        void setCourseData(List<CourseV2> courses);
        void updateCoursePreference();
    }


}
