package com.bibi.ui.coin;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import com.bibi.app.UrlFactory;
import com.bibi.data.DataSource;
import com.bibi.entity.Currency;
import com.bibi.entity.HoldEntity;
import com.bibi.ui.order.OrdersContract;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;
import okhttp3.Request;

import static com.bibi.app.GlobalConstant.OKHTTP_ERROR;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/7/28
 */
public class CoinsPresenter implements CoinsContract.Presenter  {
    private CoinsContract.CoinsView view;
    private DataSource dataRepository;

    public CoinsPresenter(DataSource dataRepository, CoinsContract.CoinsView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }


    @Override
    public void getHoldOrder(String token, int pageNo, int pageSize, String startTime, String endTime) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSpotAllEntrustUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("startTime", "")
                .addParams("endTime", "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("当前委托回执", UrlFactory.getSpotEntrustUrl() + "  error " + " --" + e.toString());
                if (view != null) view.errorMes(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("当前委托回执：", "当前持仓回执：" + response.toString());
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        code = object.optInt("code");
                        List<HoldEntity> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<HoldEntity>>() {
                        }.getType());
                        view.getHoldSuccess(objs);
                    } catch (JSONException e1) {
                        view.getHoldFial(code, null);
                    }
                }
            }
        });
    }

    @Override
    public void cancle(String token, String orderId) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCancleSpotOrderUrl()+orderId)
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (view == null) return;
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("取消订单回执：", "取消订单回执：" + response.toString());
                if (view == null) return;

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.getCancleSuccess(object.getString("message"));
                    } else {
                        view.errorMes(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getHoldFinishOrder(String token, int pageNo, int pageSize, String startTime, String endTime) {
        view.displayLoadingPopup();
        WonderfulOkhttpUtils.post().url(UrlFactory.getSpotAllHistoryUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("startTime", "")
                .addParams("endTime", "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                view.hideLoadingPopup();
                Log.d("当前委托回执", UrlFactory.getSpotEntrustUrl() + "  error " + " --" + e.toString());
                if (view != null) view.errorMes(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
//                WonderfulLogUtils.logi("当前委托回执：", "当前持仓回执：" + response.toString());
                view.hideLoadingPopup();
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        code = object.optInt("code");
                        List<HoldEntity> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<HoldEntity>>() {
                        }.getType());
                        view.getHoldSuccess(objs);
                    } catch (JSONException e1) {
                        view.getHoldFial(code, null);
                    }
                }
            }
        });
    }

    @Override
    public void allSpotCurrency() {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAllSpotCurrency()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        WonderfulLogUtils.logi("miao", "所有币种-币币交易" + e);
                    }

                    @Override
                    public void onResponse(String response) {
                        WonderfulLogUtils.logi("miao", "所有币种-币币交易" + response.toString());
                        // 获取所有币种
                        try {
                            List<Currency> obj = new Gson().fromJson(response, new TypeToken<List<Currency>>() {
                            }.getType());
                            view.setSpotCurrency(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
