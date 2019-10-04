package com.stefan.classscheduleforusc.mvp.about;

import com.stefan.classscheduleforusc.BasePresenter;
import com.stefan.classscheduleforusc.BaseView;
import com.stefan.classscheduleforusc.data.beanv2.VersionWrapper;

/**
 * Created by stefan on 2019/10/03.
 */

public interface AboutContract {
    interface Presenter extends BasePresenter {
        void checkUpdate();
    }

    interface View extends BaseView<AboutContract.Presenter> {
        void showMassage(String notice);
        void showUpdateVersionInfo(VersionWrapper.Version version);
    }
}
