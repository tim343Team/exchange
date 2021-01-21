package com.bibi.ui.kline;


import com.bibi.base.Contract;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.Currency;
import com.bibi.entity.CurrencyK;
import com.bibi.entity_remote.OptionsAddOrder;
import com.bibi.socket.ISocket;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public interface KlineContract {

    interface View extends Contract.BaseView<Presenter> {

        void KDataFail(Integer code, String toastMessage);

        void KDataSuccess(JSONArray obj);

        void allCurrencySuccess(List<Currency> obj);

        void allCurrencyFail(Integer code, String toastMessage);

        void getAssetPNLSuccess(AssetEntity assetEntity);

        void addOrderSuccess(String message);

        void addOrderFail(String message);

        void getCoinThumbSuccess(String response, int cmd);

        void allEryuanCurrencySuccess(List<Currency> obj);
    }

    interface Presenter extends Contract.BasePresenter {

        void KData(String symbol, Long from, Long to, String resolution);

        void allCurrency();

        void getAssetPNL(String token);

        void addOrder(OptionsAddOrder params);

        void coinThumb(String token, String symbol, int type);

        void getEryuanSymbol(String token);
    }

    interface MinuteView extends Contract.BaseView<MinutePresenter> {

        void allCurrencySuccess(List<Currency> obj);

        void allCurrencyFail(Integer code, String toastMessage);

        void KDataSuccess(JSONArray obj);

        void KDataFail(Integer code, String toastMessage);
    }

    interface MinutePresenter extends Contract.BasePresenter {

        void allCurrency();

        void KData(String symbol, long from, Long to, String resolution);
    }
}
