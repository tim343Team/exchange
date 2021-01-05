package com.bibi.ui.main.market;

import com.bibi.data.DataSource;
import com.bibi.entity.Currency;
import com.bibi.ui.main.MainContract;

import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/28
 */
public class SearchSymbolPresenter implements SearchSymbolContract.Presenter {
    private SearchSymbolContract.View view;
    private DataSource dataRepository;

    public SearchSymbolPresenter(DataSource dataRepository, SearchSymbolContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
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

    @Override
    public void addCollection(final String symbol) {
        dataRepository.addCollection(symbol, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.addCollectionSuccess(symbol,(String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.addCollectionFail(code, toastMessage);

            }
        });
    }

    @Override
    public void cancleCollection(final String symbol) {
        dataRepository.cancleCollection(symbol, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.cancleCollectionSuccess(symbol,(String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.cancleCollectionFail(code, toastMessage);

            }
        });
    }

    @Override
    public void getFavorite(String token,String id) {
        dataRepository.getFavorite(token,id, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.getFavoriteSuccess((List<Currency>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.getFavoriteFail(code, toastMessage);
            }
        });
    }
}
