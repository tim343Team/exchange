package com.bibi.ui.main.presenter;

import java.util.List;

import com.bibi.data.DataSource;
import com.bibi.entity.Currency;
import com.bibi.utils.WonderfulLogUtils;

/**
 * Created by Administrator on 2018/2/25.
 */

public class CommonPresenter {
    private ICommonView view;
    private DataSource dataRepository;

    public CommonPresenter(DataSource dataRepository, ICommonView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    public void delete(String token, String symbol, final int position) {
        dataRepository.delete(token, symbol, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.deleteSuccess((String) obj, position);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.deleteFail(code, toastMessage);
            }
        });
    }

    public void add(String token, String symbol, final int position) {
        dataRepository.add(token, symbol, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.addSuccess((String) obj, position);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.addFail(code, toastMessage);
            }
        });
    }

    public void getDrawleSymbol(String token) {
        dataRepository.getDrawleSymbol(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                WonderfulLogUtils.logi("含有价格趋势的交易对 chengg：", "");
                view.getSymbolSuccess((List<Currency>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.getSymbolFail(code, toastMessage);
            }
        });
    }
}
