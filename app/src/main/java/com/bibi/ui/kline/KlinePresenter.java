package com.bibi.ui.kline;


import android.util.Log;

import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.post.PostFormBuilder;
import com.google.gson.Gson;

import com.bibi.app.GlobalConstant;
import com.bibi.app.UrlFactory;
import com.bibi.data.DataSource;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.Currency;
import com.bibi.entity.CurrencyK;
import com.bibi.entity_remote.OptionsAddOrder;
import com.bibi.ui.mychart.PushLineDataNewBean;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;
import okhttp3.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class KlinePresenter implements KlineContract.Presenter {
    private DataSource dataRepository;
    private KlineContract.View view;

    public KlinePresenter(DataSource dataRepository, KlineContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void KData(String symbol, Long from, Long to, String resolution) {
        dataRepository.KData(symbol, from, to, resolution, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.KDataSuccess((JSONArray) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.KDataFail(code, toastMessage);
            }
        });
    }

    @Override
    public void allCurrency() {
        dataRepository.allCurrency(new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.allCurrencySuccess((List<Currency>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.allCurrencyFail(code, toastMessage);

            }
        });
    }

    @Override
    public void getAssetPNL(String token) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAssetPnl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(String response) {
                if (view == null) return;

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        AssetEntity obj = gson.fromJson(object.getJSONObject("data").toString(), AssetEntity.class);
                        view.getAssetPNLSuccess(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void addOrder(OptionsAddOrder params) {
        view.displayLoadingPopup();
        String periodS;
        int period = params.getPeriod();
        if (period == GlobalConstant.TAG_ONE_MINUTE) {
            periodS = "1min";
        } else if (period == GlobalConstant.TAG_FIVE_MINUTE) {
            periodS = "5min";
        } else if (period == GlobalConstant.TAG_FIFTEEN_MINUTE) {
            periodS = "15min";
        } else if (period == GlobalConstant.TAG_THIRTY_MINUTE) {
            periodS = "30min";
        } else if (period == GlobalConstant.TAG_AN_HOUR) {
            periodS = "60min";
        } else {
            periodS = "1min";
        }

        PostFormBuilder pfb = WonderfulOkhttpUtils.post().url(UrlFactory.getEryuantddOrderUrl())
                .addHeader("x-auth-token", params.getToken())
                .addParams("direction", params.getDirection())
                .addParams("amount", params.getAmount())
                .addParams("symbol", params.getSymbol())
                .addParams("price", params.getPrice())
                .addParams("coinId", params.getCoinId())
                .addParams("period", periodS);
        if(params.getLeverage()!=0){
            WonderfulLogUtils.logd("leverage","添加杠杆");
            pfb.addParams("leverage", String.valueOf(params.getLeverage()));
        }
        pfb.build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                view.hideLoadingPopup();
                super.onError(request, e);
            }

            @Override
            public void onResponse(String response) {
                view.hideLoadingPopup();
                if (view == null) return;

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.addOrderSuccess(object.optString("message"));
                    } else {
                        view.addOrderFail(object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void coinThumb(String token, String symbol, int period) {
        String periodS;
        if (period == GlobalConstant.TAG_ONE_MINUTE) {
            periodS = "1min";
        } else if (period == GlobalConstant.TAG_FIVE_MINUTE) {
            periodS = "5min";
        } else if (period == GlobalConstant.TAG_FIFTEEN_MINUTE) {
            periodS = "15min";
        } else if (period == GlobalConstant.TAG_THIRTY_MINUTE) {
            periodS = "30min";
        } else if (period == GlobalConstant.TAG_AN_HOUR) {
            periodS = "1hour";
        } else {
            periodS = "1min";
        }

        WonderfulOkhttpUtils.post().url(UrlFactory.getEryuanCoinThumbUrl())
                .addHeader("x-auth-token", token)
                .addParams("symbol", symbol)
                .addParams("period", periodS)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(String response) {
                if (view == null) return;

                try {
                    view.getCoinThumbSuccess(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getEryuanSymbol(String token) {
        dataRepository.getDrawleSymbol(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.allEryuanCurrencySuccess((List<Currency>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
            }
        });
    }
}
