package com.bibi.ui.order;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Request;
import com.bibi.app.UrlFactory;
import com.bibi.data.DataSource;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.HoldEntity;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

import static com.bibi.app.GlobalConstant.OKHTTP_ERROR;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/25
 */
public class OrdersPresenter implements OrdersContract.OrdersPresenter  {
    private OrdersContract.OrdersView view;
    private DataSource dataRepository;

    public OrdersPresenter(DataSource dataRepository, OrdersContract.OrdersView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getHoldOrder(String token, int pageNo, int pageSize, String symbol, String holdStatus,String startTime,String endTime) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getPersonalHoldUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("symbol", symbol)
                .addParams("holdStatus", holdStatus)
                .addParams("startTime", startTime)
                .addParams("endTime", endTime)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("当前持仓回执", UrlFactory.getWalletUrl() + "  error " + " --" + e.toString());
                if (view != null) view.getHoldFial(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        code = object.optInt("code");
                        List<HoldEntity> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<HoldEntity>>() {
                        }.getType());
                        if(objs.size()>0){
                            WonderfulLogUtils.logi("当前持仓回执：", "当前持仓盈亏回执：" + objs.get(0).getProfitLost());
                        }
                        view.getHoldSuccess(objs);
                    } catch (JSONException e1) {
                        view.getHoldFial(code, null);
                    }
                }
            }
        });
    }

    @Override
    public void getHoldFinishOrder(String token, int pageNo, int pageSize, String symbol,String startTime,String endTime) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getHoldFinishUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("symbol", symbol)
                .addParams("startTime", startTime)
                .addParams("endTime", endTime)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("当前持仓完成回执", UrlFactory.getWalletUrl() + "  error " + " --" + e.toString());
                if (view != null) view.getHoldFial(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("当前持仓完成回执：", "当前持仓完成回执：" + response.toString());
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        code = object.optInt("code");
                        List<HoldEntity> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<HoldEntity>>() {
                        }.getType());
                        view.getHoldFinishSuccess(objs);
                    } catch (JSONException e1) {
                        view.getHoldFinishFial(code, null);
                    }
                }
            }
        });
    }

    @Override
    public void getHoldCurrentOrder(String token, int pageNo, int pageSize, String symbol,String startTime,String endTime) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getHoldCurrentUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("symbol", symbol)
                .addParams("startTime", startTime)
                .addParams("endTime", endTime)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("查询当前委托回执", UrlFactory.getWalletUrl() + "  error " + " --" + e.toString());
                if (view != null) view.getHoldFial(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("查询当前委托回执：", "查询当前委托回执：" + response.toString());
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        code = object.optInt("code");
                        List<HoldEntity> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<HoldEntity>>() {
                        }.getType());
                        view.getHoldCurrentSuccess(objs);
                    } catch (JSONException e1) {
                        view.getHoldCurrentFial(code, null);
                    }
                }
            }
        });
    }

    @Override
    public void getCancle(String token, String orderId) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCancleOrderUrl() + orderId)
                .addHeader("access-auth-token", token)
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                view.getCancleFial(OKHTTP_ERROR, null);
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
    public void getClose(String token, String orderId) {
        WonderfulLogUtils.logi("平仓回执：", UrlFactory.getCloseOrderUrl()+orderId);
        WonderfulOkhttpUtils.post().url(UrlFactory.getCloseOrderUrl() + orderId)
                .addHeader("access-auth-token", token)
                .addHeader("x-auth-token", token)
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
    public void getCloseAll(String token) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCloseAllOrderUrl())
                .addHeader("access-auth-token", token)
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("一键平仓回执", UrlFactory.getCloseAllOrderUrl() + "  error " + " --" + e.toString());
                view.getCloseFial(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("一键平仓回执：", "一键平仓回执：" + response.toString());
                if (view == null) return;

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        view.getCloseAllSuccess(object.getString("message"));
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
                WonderfulLogUtils.logi("盈利损失统计回执：", "盈利损失统计回执：" + response.toString());
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
