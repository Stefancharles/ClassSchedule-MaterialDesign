package com.stefan.classscheduleforusc.mvp.home;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.stefan.classscheduleforusc.BasePresenter;
import com.stefan.classscheduleforusc.BaseView;
import com.stefan.classscheduleforusc.data.beanv2.CourseGroup;
import com.stefan.classscheduleforusc.data.beanv2.UserWrapper;

import java.util.List;

public interface HomeContract {
    interface Presenter extends BasePresenter {
        void loadUserInfo();

        void showGroup();

        void createShare(long groupId, String groupName);

        void downShare(String url);

        void uploadCourse();

        void downCourse();
    }

    interface View extends BaseView<HomeContract.Presenter> {
        boolean isActive();

        void showMassage(String msg);

        void showCacheData();

        void showLoading(String msg);

        void stopLoading();

        /**
         * 未登录状态页面
         */
        void noSignInPage();

        /**
         * 登录状态页面
         */
        void signInPage(UserWrapper.User user);

        void pleaseLoginIn();

        void updateShowAvator(@NonNull String email);

        void showGroupDialog(List<CourseGroup> groups);

        void createQRCodeSucceed(Bitmap bitmap);

        void cloudToLocalSucceed();
    }
}
