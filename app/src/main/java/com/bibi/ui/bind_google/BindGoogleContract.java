package com.bibi.ui.bind_google;

import com.bibi.base.Contract;
import com.bibi.entity.GoogleCodeEntity;
import com.bibi.ui.bind_email.BindEmailContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/7
 */
public class BindGoogleContract {
    interface View extends Contract.BaseView<Presenter> {
        void getGoogleSuccess(GoogleCodeEntity obj);

        void getGoogleFail(Integer code, String toastMessage);

        void bindGoogleSuccess(String obj);

        void bindGoogleFail(Integer code, String toastMessage);

    }

    interface Presenter extends Contract.BasePresenter {
        void getGoogleCode(String token);

        void bindGoogle(String token, String secret, String code);
    }
}
