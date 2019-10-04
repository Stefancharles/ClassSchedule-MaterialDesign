package com.stefan.classscheduleforusc.mvp.login;

import com.stefan.classscheduleforusc.BasePresenter;
import com.stefan.classscheduleforusc.BaseView;
import com.stefan.classscheduleforusc.data.beanv2.BaseBean;

public interface SignContact {
    interface Presenter extends BasePresenter {
        void signIn(String email, String password);

        void signUp(String email, String password);
    }

    interface View extends BaseView<Presenter> {
        void showMassage(String msg);

        void showLoading(String msg);

        void stopLoading();

        void signInSucceed(BaseBean bean);

        void signInFailed(String msg);

        void signUpSucceed(BaseBean bean);

        void signUpFailed(String msg);

    }
}
