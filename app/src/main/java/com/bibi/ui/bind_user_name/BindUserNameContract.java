package com.bibi.ui.bind_user_name;

import com.bibi.base.Contract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/17
 */
public class BindUserNameContract {
    interface View extends Contract.BaseView<Presenter> {

        void bindUsernameSuccess(String obj);

        void bindUsernameFail(Integer code, String toastMessage);

    }

    interface Presenter extends Contract.BasePresenter {
        void bindUsername(String token, String username);
    }
}
