package com.bibi.ui.bind_google;

import com.bibi.data.DataSource;
import com.bibi.entity.GoogleCodeEntity;
import com.bibi.ui.bind_email.BindEmailContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/7
 */
public class BindGooglePresenter implements BindGoogleContract.Presenter {
    private final DataSource dataRepository;
    private final BindGoogleContract.View view;

    public BindGooglePresenter(DataSource dataRepository, BindGoogleContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getGoogleCode(String token) {
        view.displayLoadingPopup();
        dataRepository.getGoogleCode(token,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getGoogleSuccess((GoogleCodeEntity) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getGoogleFail(code, toastMessage);
            }
        });
    }

    @Override
    public void bindGoogle(String token, String secret, String code) {
        view.displayLoadingPopup();
        dataRepository.bindGoogle(token,secret,code,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.bindGoogleSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.bindGoogleFail(code, toastMessage);
            }
        });
    }
}
