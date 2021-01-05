package com.bibi.ui.main.market;

import com.bibi.data.DataSource;
import com.bibi.entity.Currency;
import com.bibi.entity.SafeSetting;
import com.bibi.ui.main.MainContract;

import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/25
 */
public class TwoRootPresenter implements MainContract.TwoRootPresenter {
    private MainContract.TwoRootView view;
    private DataSource dataRepository;

    public TwoRootPresenter(DataSource dataRepository, MainContract.TwoRootView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }


    @Override
    public void setTickersZone(String token) {
        dataRepository.setTickersZone(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.getZoneSuccess((List<String>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.getZoneFail(code, toastMessage);

            }
        });
    }


    @Override
    public void setTickersPage(String token, String sort, String direction, String nav, String legalCurrency) {
        dataRepository.setTickersPage(token,sort,direction,nav,legalCurrency, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.getTickersPageSuccess((List<Currency>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.getZoneFail(code, toastMessage);

            }
        });
    }
}
