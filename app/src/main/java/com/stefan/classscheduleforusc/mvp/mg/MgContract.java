package com.stefan.classscheduleforusc.mvp.mg;

import com.stefan.classscheduleforusc.BasePresenter;
import com.stefan.classscheduleforusc.BaseView;

/**
 * Created by stefan on 2019/10/03.
 */

public interface MgContract {
    interface Presenter extends BasePresenter {
        void deleteCsName(long csNameId);
        void switchCsName(long csNameId);
        void reloadCsNameList();
        void addCsName(String csName);
        void editCsName(long id, String newCsName);
    }

    interface View extends BaseView<Presenter> {
        void showList();
        void showNotice(String notice);
        void gotoCourseActivity();
        void deleteFinish();
        void addCsNameSucceed();
        void editCsNameSucceed();
    }

}
