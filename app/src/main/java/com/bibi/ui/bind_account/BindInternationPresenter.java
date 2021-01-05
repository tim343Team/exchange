package com.bibi.ui.bind_account;

import com.bibi.data.DataSource;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/29
 */
public class BindInternationPresenter implements BindAccountContact.InternationPresenter {
    private final DataSource dataRepository;
    private final BindAccountContact.InternationView view;

    public BindInternationPresenter(DataSource dataRepository, BindAccountContact.InternationView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getUpdateInter(String token, String name, String address, String jyPassword) {
        view.displayLoadingPopup();
        dataRepository.getUpdateInter(token,name,address,jyPassword,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getBindSwiftSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getBindSwiftFail(code,toastMessage);
            }
        });
    }

    @Override
    public void updateInter(String token, String name, String address, String jyPassword) {
        view.displayLoadingPopup();
        dataRepository.updateInter(token,name,address,jyPassword,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getBindSwiftSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getBindSwiftFail(code,toastMessage);
            }
        });
    }
}
