package com.bibi.ui.main.trade;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import com.bibi.data.DataSource;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.HoldEntity;
import com.bibi.entity.User;
import com.bibi.ui.main.trade.Coin2CoinContract;
import com.bibi.entity.EntrustHistory;
import com.bibi.entity.Exchange;
import com.bibi.entity.Money;
import com.bibi.entity.ThreeTextInfo;
import com.bibi.app.UrlFactory;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Request;

import static com.bibi.app.GlobalConstant.JSON_ERROR;
import static com.bibi.app.GlobalConstant.OKHTTP_ERROR;


public class Coin2CoinPresenter implements Coin2CoinContract.Presenter {
    Coin2CoinContract.View view;
    private DataSource dataRepository;

    public Coin2CoinPresenter(DataSource dataRepository, Coin2CoinContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    /**
     * 提交委托 卖出或是买入
     * 这里加个Dialog
     */
    @Override
    public void getAddOrder(String token, String symbol, String price, String amount,
                            String direction, String type, String leverage, String stopProfitPrice, String stopLossPrice) {
        if (view != null) view.showDialog();
        WonderfulOkhttpUtils.post().url(UrlFactory.getAddOrderUrl()).addHeader("x-auth-token", token).addParams("symbol", symbol)
                .addParams("price", price).addParams("amount", amount).addParams("direction", direction).addParams("type", type)
                .addParams("leverage", leverage)
                .addParams("stopProfitPrice", stopProfitPrice)
                .addParams("stopLossPrice", stopLossPrice)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                WonderfulLogUtils.logi("提交委托的回执：", "提交委托的回执：" + e.getMessage());
                super.onError(request, e);
                if (view != null) {
                    view.hideDialog();
                    view.errorMes(OKHTTP_ERROR, "提交失败");
                }
            }

            @Override
            public void onResponse(String response) {
                if (view == null) return;
                view.hideDialog();
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

    /**
     * 查询盘口的信息
     */
    @Override
    public void getExchange(final String symbol) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getPlateUrl())
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
                                JsonArray ask = object.getAsJsonArray("ask").getAsJsonArray();
                                List<Exchange> askList = new Gson().fromJson(ask, new TypeToken<List<Exchange>>() {
                                }.getType());
                                JsonArray bid = object.getAsJsonArray("bid").getAsJsonArray();
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
    public void getHoldOrder(String token, int pageNo, int pageSize, String symbol, String holdStatus) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getHoldUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("symbol", symbol)
                .addParams("holdStatus", holdStatus)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("当前持仓回执", UrlFactory.getWalletUrl() + "  error " + " --" + e.toString());
                if (view != null) view.errorMes(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
//                WonderfulLogUtils.logi("当前持仓回执：", "当前持仓回执：" + response.toString());
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

    /**
     * 获取当前委托
     *
     * @param token    token
     * @param pageNo   页码
     * @param pageSize 多少条
     * @param symbol   类型
     */
    @Override
    public void getCurrentOrder(String token, int pageNo, int pageSize, String symbol, String type, String direction, String startTime, String endTime) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getEntrustUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("type", type)
                .addParams("direction", direction)
                .addParams("startTime", startTime)
                .addParams("marginTrade", "0")
                .addParams("endTime", endTime)
                .addParams("symbol", symbol).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (view != null) view.errorMes(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                Log.d("trust", "当前委托add" + response);
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        code = object.optInt("code");
                        List<EntrustHistory> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<EntrustHistory>>() {
                        }.getType());
                        view.getDataLoaded(objs);
                    } catch (JSONException e1) {

                        view.errorMes(code, null);
                    }
                    /*try {
                        object = new JSONObject(response);
                        int o = object.has("code");
                        if (o==500){
                            SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
                            SharedPreferenceInstance.getInstance().saveLockPwd("");
                        }
                        //view.errorMes(object.getInt("code"), object.optString("message"));
                    } catch (JSONException e) {

                    }*/
                }
            }
        });
    }

    /**
     * 获取历史的订单
     */
    @Override
    public void getOrderHistory(String token, int pageNo, int pageSize, String symbol, String type, String direction, String startTime, String endTime) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getHistoryEntrus())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("type", type)
                .addParams("direction", direction)
                .addParams("startTime", startTime)
                .addParams("marginTrade", "0")
                .addParams("endTime", endTime)
                .addParams("symbol", symbol).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                if (view != null) view.errorMes(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                if (view == null) return;
                Log.d("trust", "-历史的订单--" + response);
                try {
                    JsonObject object1 = new JsonParser().parse(response).getAsJsonObject();
                    List<EntrustHistory> orders = gson.fromJson(object1.getAsJsonArray("content")
                            .getAsJsonArray(), new TypeToken<List<EntrustHistory>>() {
                    }.getType());
                    view.getHistorySuccess(orders);
                } catch (Exception e1) {
                    // view.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


    /**
     * 取消某个委托
     */
    @Override
    public void getCancelEntrust(String token, String orderId) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCancleEntrustUrl() + orderId)
                .addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                if (view != null) view.errorMes(OKHTTP_ERROR, "取消失败");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.getCancelSuccess(object.getString("message"));
                    } else {
                        view.errorMes(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.errorMes(JSON_ERROR, "取消失败");
                }
            }
        });
    }

    /**
     * 获取单个钱包
     */
    @Override
    public void getWallet(final int type, String token, String coinName) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getWalletUrl() + coinName)
                .addParams("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("获取单个钱包回调", UrlFactory.getWalletUrl() + "  error " + " --" + e.toString());
                view.getWalletSuccess(null, 3);
            }

            @Override
            public void onResponse(String response) {
                Log.d("获取单个钱包回调", UrlFactory.getWalletUrl() + "response " + " --" + response);
                WonderfulLogUtils.logi("jiejie", "获取单个钱包回调" + response);
                if (view == null) return;
                try {
                    Money money = new Gson().fromJson(response, Money.class);
                    view.getWalletSuccess(money, type);
                } catch (Exception e) {
                    view.getWalletSuccess(null, 3);
                }

            }
        });
    }

    @Override
    public void getSymbolInfo(String symbol) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSymbolInfo())
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
//                    WonderfulToastUtils.showToast(getResources().getString(R.string.parse_error));
                    return;
                }
                view.getAccuracy(info.getCoinScale(), info.getBaseCoinScale());
            }
        });
    }

    @Override
    public void getDefaultSymbol() {
        WonderfulOkhttpUtils.get().url(UrlFactory.getDefaultSymbol()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);

            }

            @Override
            public void onResponse(String response) {
                if (view == null) return;
                //{"data":{"app":"HT/ETH","web":"HT_ETH"},"code":0,"message":"SUCCESS","total":null}
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
    public void getAssetPNL(String token) {
        WonderfulLogUtils.logi("盈利损失统计URL：", UrlFactory.getAssetPnl());
        WonderfulOkhttpUtils.post().url(UrlFactory.getAssetPnl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(String response) {
//                WonderfulLogUtils.logi("盈利损失统计回执：", "盈利损失统计回执：" + response.toString());
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
    public void getLeverage(String symbol) {
        WonderfulLogUtils.logi("杠杆URL：", UrlFactory.getAssetPnl());
        WonderfulOkhttpUtils.post().url(UrlFactory.getLeverage())
                .addParams("symbol", symbol)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("杠杆回执：", "杠杆回执：" + response.toString());
                if (view == null) return;

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<String> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<String>>() {
                        }.getType());
                        view.getLeverage(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void getClose(String token, String orderId) {
        Log.e("token11: ", token);
        WonderfulLogUtils.logi("平仓回执：", UrlFactory.getCloseOrderUrl()+orderId);
        WonderfulOkhttpUtils.post().url(UrlFactory.getCloseOrderUrl() + orderId)
                .addHeader("access-auth-token", token)
                .addHeader("x-auth-token", token)
                .addHeader("Text1234", "Text1234")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                view.getCloseFial(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("平仓回执：", "平仓回执：" + response.toString());
                if (view == null) return;

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.getCloseSuccess(object.getString("message"));
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
    public void getModifyProfit(String token, String orderId,String stopProfitPrice,String stopLossPrice) {
        WonderfulLogUtils.logi("平仓URL：", UrlFactory.getAssetPnl());
        WonderfulOkhttpUtils.post().url(UrlFactory.getModifyProffitLoss())
                .addHeader("x-auth-token", token)
                .addParams("orderId",orderId)
                .addParams("stopProfitPrice",stopProfitPrice)
                .addParams("stopLossPrice",stopLossPrice)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("修改止赢回执：", "修改止赢回执：" + response.toString());
                if (view == null) return;

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.getModifySuccess(object.getString("message"));
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
    public void allCurrency() {
        dataRepository.allHomeCurrency(new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.allCurrencySuccess(obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
            }
        });
    }

}
