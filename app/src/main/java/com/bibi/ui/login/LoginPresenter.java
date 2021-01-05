package com.bibi.ui.login;


import com.bibi.data.DataSource;
import com.bibi.data.RemoteDataSource;
import com.bibi.entity.User;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/25.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private final DataSource dataRepository;
    private final LoginContract.View view;

    public LoginPresenter(DataSource dataRepository, LoginContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }
    
    @Override
    public void login(String username, String password, String code, String validate, String seccode,boolean isPass) {
        view.displayLoadingPopup();
        dataRepository.login(username, password, code, validate, seccode, isPass,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.loginSuccess((User) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.loginFail(code, toastMessage);
            }
        });
    }


    @Override
    public void getLoginAuthType(String phone) {
        view.displayLoadingPopup();
        dataRepository.getLoginAuthType( phone,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getLoginAuthTypeSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getLoginAuthTypeFail(code, toastMessage);
            }
        });
    }

    @Override
    public void sendLoginCode(String phone) {
        view.displayLoadingPopup();
        RemoteDataSource.getInstance().sendLoginCode( phone,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.sendLoginCodeSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.sendLoginCodeFail(code, toastMessage);
            }
        });
    }

    @Override
    public void phoneCode(String phone, String country) {
        dataRepository.phoneCode(phone, country, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.phoneCodeSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.phoneCodeFail(code, toastMessage);
            }
        });
    }

    @Override
    public void signUpByPhone(String phone, String username, String password, String country, String code, String tuijianma, String challenge, String validate, String seccode) {
        view.displayLoadingPopup();
        dataRepository.signUpByPhone(phone, username, password, country, code,tuijianma, challenge,validate,seccode,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.signUpByPhoneSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.signUpByPhoneFail(code, toastMessage);
            }
        });
    }
}
