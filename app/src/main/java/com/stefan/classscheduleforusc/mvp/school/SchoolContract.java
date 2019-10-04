package com.stefan.classscheduleforusc.mvp.school;


import com.stefan.classscheduleforusc.BasePresenter;
import com.stefan.classscheduleforusc.BaseView;

/**
 * Created by stefan on 2019/10/03.
 */


public interface SchoolContract {
    interface Presenter extends BasePresenter {
        void testUrl(String url);
    }

    interface View extends BaseView<Presenter> {
        void showNotice(String notice);

        void showInputDialog();

        void testingUrl(boolean bool);

        void testUrlFailed(String url);

        void testUrlSucceed(String url);
    }
}
