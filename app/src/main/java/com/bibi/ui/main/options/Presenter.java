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
public class Presenter implements OptionsContract.Presenter {
    private OptionsContract.View view;
    private DataSource dataRepository;

    public Presenter(DataSource dataRepository, OptionsContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getCurrentOrder(String token, int pageNo, String symbol) {
        dataRepository.getCurrentOrder(token,pageNo,symbol,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getCurrentSuccess((List<OptionEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }
}
