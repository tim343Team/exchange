package com.bibi.ui.login;


import com.bibi.base.Contract;
import com.bibi.entity.User;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface LoginContract {
    interface View extends Contract.BaseView<Presenter> {

        void loginFail(Integer code, String toastMessage);

        void loginSuccess(User obj);

        void getLoginAuthTypeSuccess(String obj);

        void getLoginAuthTypeFail(Integer code, String toastMessage);

        void sendLoginCodeSuccess(String obj);

        void sendLoginCodeFail(Integer code, String toastMessage);

        void phoneCodeSuccess(String obj);

        void phoneCodeFail(Integer code, String toastMessage);

        void signUpByPhoneSuccess(String obj);

        void signUpByPhoneFail(Integer code, String toastMessage);
    }
        
    interface Presenter extends Contract.BasePresenter {
        void login(String username, String password, String challenge, String validate, String seccode, boolean isPass);

        void getLoginAuthType(String phone);

        void sendLoginCode(String phone);

        void phoneCode(String phone, String country);

        void signUpByPhone(String phone, String username, String password, String country, String code, String tuijianma, String challenge, String validate, String seccode);
    }
}
