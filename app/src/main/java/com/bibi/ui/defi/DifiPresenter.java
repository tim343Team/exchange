package com.bibi.ui.defi;

import com.bibi.data.DataSource;
import com.bibi.ui.my_promotion.PromotionRewardContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/23
 */
public class DifiPresenter implements DeFiContract.Presenter {

    private final DataSource dataRepository;
    private final DeFiContract.View view;

    public DifiPresenter(DataSource dataRepository, DeFiContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getDeFiList(String token, String page) {
        dataRepository.newTickerCurrency("nav","DEFI",new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.newTickerSuccess(obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.newTickerFail(code, toastMessage);
            }
        });
    }
}
