package com.bibi.ui.main.home;

import com.bibi.ui.main.MainContract;
import com.bibi.data.DataSource;
import com.bibi.entity.BannerEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/24.
 */

public class OnePresenter implements MainContract.OnePresenter {
    private MainContract.OneView view;
    private DataSource dataRepository;

    public OnePresenter(DataSource dataRepository, MainContract.OneView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void banners(String sysAdvertiseLocation) {
        dataRepository.banners(sysAdvertiseLocation, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.bannersSuccess((List<BannerEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.bannersFail(code, toastMessage);

            }
        });
    }
}
