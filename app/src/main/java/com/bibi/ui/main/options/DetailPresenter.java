package com.bibi.ui.main.options;

import java.util.List;

import com.bibi.data.DataSource;
import com.bibi.entity.OptionEntity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/12
 */
public class DetailPresenter implements OptionsContract.DetailPresenter {
    private OptionsContract.DetailView view;
    private DataSource dataRepository;

    public DetailPresenter(DataSource dataRepository, OptionsContract.DetailView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getHistory(String token, int pageNo, String symbol) {
        dataRepository.getEryuanHistory(token,pageNo,symbol,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getHistorySuccess((List<OptionEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void getHold(String token, int pageNo) {
        dataRepository.getErryuanHold(token,pageNo,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getHoldSuccess((List<OptionEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }
}
