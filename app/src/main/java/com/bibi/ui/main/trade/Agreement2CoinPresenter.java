package com.bibi.ui.main.trade;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import com.bibi.app.UrlFactory;
import com.bibi.data.DataSource;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.Exchange;
import com.bibi.entity.HoldEntity;
import com.bibi.entity.ThreeTextInfo;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;
import okhttp3.Request;

import static com.bibi.app.GlobalConstant.JSON_ERROR;
import static com.bibi.app.GlobalConstant.OKHTTP_ERROR;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/7/27
 */
public class Agreement2CoinPresenter implements Agreement2CoinContract.Presenter {
    Agreement2CoinContract.View view;
    private DataSource dataRepository;

    public Agreement2CoinPresenter(DataSource dataRepository, Agreement2CoinContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getAsset(String token,final String type) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSpotURL()+"/"+type)
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
                        WonderfulLogUtils.logi("USDT_URL：", UrlFactory.getSpotURL()+"/"+type+"/"+obj.getBalance());
                        view.getAssetUSDTSuccess(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getDefaultSymbol() {
        WonderfulOkhttpUtils.get().url(UrlFactory.getDefaultCoinSymbol()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                WonderfulLogUtils.logi("币币默认交易对 回执：", "币币默认交易对 回执：" + e.getMessage());
                super.onError(request, e);

            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("币币默认交易对 回执：", "币币默认交易对 回执：" + response.toString());
                if (view == null) return;
                String symbol = null;
                try {
                    symbol = new JSONObject(response).getJSONObject("data").getString("app");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                view.setDefaultSymbol(symbol);
            }
        });
    }

    @Override
    public void getExchange(final String symbol) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSpotPlateUrl())
                .addParams("symbol", symbol).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        //if (view != null) view.errorMes(OKHTTP_ERROR);
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("Plate", UrlFactory.getPlateUrl() + "查询盘信息 " + symbol + " --" + response);
                        if (view != null) {
                            try {
                                JsonObject object = new JsonParser().parse(response).getAsJsonObject();
                                JsonArray ask = object.getAsJsonObject("ask").getAsJsonArray("items").getAsJsonArray();
                                List<Exchange> askList = new Gson().fromJson(ask, new TypeToken<List<Exchange>>() {
                                }.getType());
                                JsonArray bid = object.getAsJsonObject("bid").getAsJsonArray("items").getAsJsonArray();
                                List<Exchange> bidList = new Gson().fromJson(bid, new TypeToken<List<Exchange>>() {
                                }.getType());
                                view.getSuccess(askList, bidList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    @Override
    public void getSymbolInfo(String symbol) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSpotSymbolInfo())
                .addParams("symbol", symbol).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (view == null) return;
                view.getAccuracy(4, 4);
            }

            @Override
            public void onResponse(String response) {
                if (view == null) return;
                ThreeTextInfo info = gson.fromJson(response, ThreeTextInfo.class);
                if (info == null) {
                    return;
                }
                view.getAccuracy(info.getCoinScale(), info.getBaseCoinScale());
            }
        });
    }

    @Override
    public void getAddOrder(String token, String symbol, String price, String amount, String direction, String type, String useDiscount) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAddSpotOrderUrl())
                .addHeader("x-auth-token", token)
                .addParams("symbol", symbol)
                .addParams("price", price)
                .addParams("amount", amount)
                .addParams("direction", direction)
                .addParams("type", type)
                .addParams("useDiscount", useDiscount)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                WonderfulLogUtils.logi("提交委托的回执：", "提交委托的回执：" + e.getMessage());
                super.onError(request, e);
                if (view != null) {
                    view.errorMes(OKHTTP_ERROR, "提交失败");
                }
            }

            @Override
            public void onResponse(String response) {
                if (view == null) return;
                WonderfulLogUtils.logd("提交委托的回执", "提交委托的" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.getDataLoad(0, object.optString("message"));
                    } else {
                        view.errorMes(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.errorMes(JSON_ERROR, "提交失败");
                }
            }
        });
    }

    @Override
    public void getHoldOrder(String token, int pageNo, int pageSize, String symbol) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSpotEntrustUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("symbol", symbol)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("当前委托回执", UrlFactory.getSpotEntrustUrl() + "  error " + " --" + e.toString());
                if (view != null) view.errorMes(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
//                WonderfulLogUtils.logi("当前委托回执：", "当前持仓回执：" + response.toString());
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
        view.displayLoadingPopup();
        WonderfulOkhttpUtils.post().url(UrlFactory.getCancleSpotOrderUrl()+orderId)
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                view.hideLoadingPopup();
                if (view == null) return;
            }

            @Override
            public void onResponse(String response) {
                view.hideLoadingPopup();
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

}
