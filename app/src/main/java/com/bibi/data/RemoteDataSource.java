package com.bibi.data;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bibi.R;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import com.bibi.app.MyApplication;
import com.bibi.entity.*;
import com.bibi.app.UrlFactory;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.okhttp.RequestCall;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

import okhttp3.Request;

import static com.bibi.app.GlobalConstant.JSON_ERROR;
import static com.bibi.app.GlobalConstant.OKHTTP_ERROR;
import static com.bibi.utils.okhttp.WonderfulOkhttpUtils.post;

/**
 * Created by Administrator on 2017/9/25.
 */
public class RemoteDataSource implements DataSource {
    private static RemoteDataSource INSTANCE;

    public static RemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDataSource();
        }
        return INSTANCE;
    }

    private RemoteDataSource() {

    }

    public void loadDayK(int count, DataCallback loadDataCallback) {

    }

    @Override
    public void phoneCode(String phone, String country, final DataCallback dataCallback) {
        RequestCall call = post().url(UrlFactory.getPhoneCodeUrl())
                .addParams("phone", phone).addParams("country", country)
                .build();
        call.execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取手机验证码出错", "获取手机验证码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取手机验证码回执：", "获取手机验证码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("获取成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void signUpByPhone(String phone, String username, String password, String country, String code, String tuijianma, String challenge, String validate, String seccode, final DataCallback dataCallback) {
        if (country == null || "".equals(country)) {
            WonderfulToastUtils.showToast("请填写完整信息");
            return;
        }
        post().url(UrlFactory.getSignUpByPhone())
                .addParams("phone", phone).addParams("code", code)
                .addParams("promotion", tuijianma + "")
                .addParams("username", username)
                .addParams("password", password)
                .addParams("country", country)
                .addParams("ticket", "1234")
                .addParams("randStr", "1234").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("手机号码注册出错", "手机号码注册出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("手机号码注册回执：", "手机号码注册回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("注册成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void signUpByEmail(String email, String username, String password, String country, String ticket, String randstr, String promotion, final DataCallback dataCallback) {
        Log.i("signUpByEmail", "email-" + email + "-username-" + username + "-password-" + password + "-country-" + country + "-ticket-" + ticket
                + "-randstr-" + randstr + "-tuijian2-" + promotion);
        post().url(UrlFactory.getSignUpByEmail())
                .addParams("email", email).addParams("promotion", promotion).addParams("username", username)
                .addParams("password", password).addParams("country", country).addParams("ticket", ticket)
                .addParams("randStr", randstr).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("邮箱注册出错", "邮箱注册出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("邮箱注册回执：", "邮箱注册回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void login(String username, String password, String code, String ticket,
                      String randStr, boolean isPass, final DataCallback dataCallback) {
        post().url(isPass ? UrlFactory.getLoginUrl() : UrlFactory.getLoginCodeUrl()).addParams(isPass ? "password" : "msgCode", password)
                .addParams(isPass ? "username" : "phone", username)
                .addParams("code", code).addParams("ticket", ticket).addParams("randStr", randStr).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("登录出错", "登录出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("登录回执：", "登录回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        User obj = gson.fromJson(object.getJSONObject("data").toString(), User.class);
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


    @Override
    public void KData(String symbol, Long from, Long to, String resolution, final DataCallback dataCallback) {
        WonderfulLogUtils.loge("INFO", from + "   " + to);
        post().url(UrlFactory.getKDataUrl())
                .addParams("symbol", symbol).addParams("from", from + "").addParams("to", to + "").addParams("resolution", resolution + "").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("历史K线数据出错", "历史K线数据出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("历史K线数据回执：", "历史K线数据回执：" + response.toString());
//                LogUtils.e("miao历史",response.toString());
                try {
                    dataCallback.onDataLoaded(new JSONArray(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


    @Override
    public void SpotKData(String symbol, Long from, Long to, String resolution, final DataCallback dataCallback) {
        WonderfulLogUtils.loge("INFO", from + "   " + to);
        WonderfulOkhttpUtils.post().url(UrlFactory.getSpotKDataUrl())
                .addParams("symbol", symbol).addParams("from", from + "").addParams("to", to + "").addParams("resolution", resolution + "").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("历史K线数据出错", "历史K线数据出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("历史K线数据回执：", "历史K线数据回执：" + response.toString());
//                LogUtils.e("miao历史",response.toString());
                try {
                    dataCallback.onDataLoaded(new JSONArray(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    /**
     * 首页获取所有的币种
     */
    @Override
    public void allHomeCurrency(final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAllCurrencys()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取所有币种出错", "获取所有币种出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
//                Log.d("jiejie","home " + response);
//                WonderfulLogUtils.logi("获取所有币种回执：", "获取所有币种回执：" + response.toString());
                dataCallback.onDataLoaded(response);
            }
        });
    }

    @Override
    public void newTickerCurrency(String nav, String content, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getNewTickersCurrencys())
                .addParams(nav, content)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("首页获取DeFi等新币所有的币种 出错", "首页获取DeFi等新币所有的币种 出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                dataCallback.onDataLoaded(response);
            }
        });
    }

    /**
     * 首页获取所有的币种
     */
    @Override
    public void allCurrency(final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAllCurrency())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
//                Log.d("jiejie","111111" + e);
                WonderfulLogUtils.logi("获取所有币种出错", "获取所有币种出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
//                Log.d("jiejie","----" + response);
                WonderfulLogUtils.logi("获取所有币种回执：", "获取所有币种回执：" + response.toString());
//                JsonObject object = new JsonParser().parse(response).getAsJsonObject();
//                JsonArray array = object.getAsJsonArray("changeRank").getAsJsonArray();
//                List<Currency> objs = gson.fromJson(array,new TypeToken<List<Currency>>() {
//                    }.getType());
                //              dataCallback.onDataLoaded(response);
                // List<Currency> objs = new
                try {
                    JSONObject object = new JSONObject(response);
                    dataCallback.onDataNotAvailable(object.optInt("code"), object.optString("message"));
                } catch (Exception ex) {
                    List<Currency> objs = gson.fromJson(response, new TypeToken<List<Currency>>() {
                    }.getType());
                    dataCallback.onDataLoaded(objs);
                }
            }
        });
    }

    @Override
    public void find(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getFindUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("查询自选出错", "查询自选出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    dataCallback.onDataNotAvailable(obj.getInt("code"), obj.getString("message"));
                } catch (JSONException e) {
                    WonderfulLogUtils.logi("查询自选回执：", "查询自选回执：" + response.toString());
                    List<Favorite> objs = gson.fromJson(response, new TypeToken<List<Favorite>>() {
                    }.getType());
                    dataCallback.onDataLoaded(objs);
                }
            }
        });
    }

    @Override
    public void delete(String token, String symbol, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getDeleteUrl()).addHeader("x-auth-token", token)
                .addParams("symbol", symbol).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("删除自选出错", "删除自选出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("删除自选回执：", "删除自选回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("删除成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void add(String token, String symbol, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAddUrl()).addHeader("x-auth-token", token)
                .addParams("symbol", symbol).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("添加自选出错", "添加自选出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("添加自选回执：", "添加自选回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("添加成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void exChange(String token, String symbol, String price, String amount, String direction, String type, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getExChangeUrl()).addHeader("x-auth-token", token).addParams("symbol", symbol)
                .addParams("price", price).addParams("amount", amount).addParams("direction", direction).addParams("type", type).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("提交委托出错", "提交委托出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);

            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("提交委托回执：", "提交委托回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("提交成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void wallet(String token, String coinName, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getWalletUrl() + coinName).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取单个钱包出错", "获取单个钱包出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取单个钱包回执：", "获取单个钱包回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        Coin obj = gson.fromJson(object.getJSONObject("data").toString(), Coin.class);
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message") + "请重新登录");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void all(final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAllUrl()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取支持的币种出错", "获取支持的币种出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取支持的币种回执：", "获取支持的币种回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<CoinInfo> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<CoinInfo>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getFeeRate(final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getFeeRateUrl()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取手续费比例出错", "获取手续费比例出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取手续费比例回执：", "获取手续费比例回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        String obj = object.optString("data");
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void advertise(int pageNo, int pageSize, String advertiseType, String unit, String id, final DataCallback dataCallback) {
        String payModes;
        if (unit.equals("USD")) {
            payModes = "SWIFT";
        } else {
            payModes = "SWIFT,支付宝,微信,银联";
        }
        WonderfulOkhttpUtils.post().url(UrlFactory.getAdvertiseUrl())
                .addParams("pageNo", pageNo + "").addParams("pageSize", pageSize + "")
                .addParams("advertiseType", advertiseType)
                .addParams("unit", "usdt")
                .addParams("payModes[]", payModes)
                .addParams("id", id).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("广告查询出错", "广告查询出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("广告查询回执：", "广告查询回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        C2C obj = gson.fromJson(object.getJSONObject("data").toString(), C2C.class);
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void country(final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCountryUrl()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取所有国家出错", "获取所有国家出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取所有国家回执：", "获取所有国家回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Country> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Country>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void create(String token, final String price, String advertiseType, String coinId, String minLimit, String maxLimit,
                       int timeLimit, String countryZhName, String priceType, String premiseRate, String remark,
                       String number, String pay, String jyPassword, String auto, String autoword, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getReleaseAdUrl()).addHeader("x-auth-token", token)
                .addParams("price", price).addParams("advertiseType", advertiseType).addParams("coin.id", coinId).addParams("minLimit", minLimit).addParams("maxLimit", maxLimit)
                .addParams("timeLimit", timeLimit + "").addParams("country.ZhName", countryZhName).addParams("priceType", priceType).addParams("premiseRate", premiseRate).addParams("remark", remark)
                .addParams("number", number).addParams("pay[]", pay).addParams("jyPassword", jyPassword).addParams("auto", auto).addParams("autoword", autoword)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("发布广告出错", "发布广告出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("发布广告回执：", "发布广告回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    WonderfulLogUtils.logi("releaseOrEditAd", "price  create  " + price);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("发布成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void uploadBase64Pic(String token, String base64Data, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getUploadPicUrl()).addHeader("x-auth-token", token)
                .addParams("oss", "false")
                .addParams("base64Data", base64Data).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        WonderfulLogUtils.logi("上传图片出错", "上传图片出错：" + e.getMessage());
                        dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
                    }

                    @Override
                    public void onResponse(String response) {
                        WonderfulLogUtils.logi("上传图片回执：", "上传图片回执：" + response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.optInt("code") == 0) {
                                dataCallback.onDataLoaded(object.optString("data"));
                            } else {
                                dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dataCallback.onDataNotAvailable(JSON_ERROR, null);
                        }
                    }
                });
    }

    @Override
    public void changeAvatar(String token, String url, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.changeAvatarUrl()).addHeader("x-auth-token", token)
                .addParams("url", url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        WonderfulLogUtils.logi("修改头像出错", "修改头像出错：" + e.getMessage());
                        dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
                    }

                    @Override
                    public void onResponse(String response) {
                        WonderfulLogUtils.logi("修改头像回执：", "修改头像回执：" + response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.optInt("code") == 0) {
                                dataCallback.onDataLoaded(object.optString("message"));
                            } else {
                                dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dataCallback.onDataNotAvailable(JSON_ERROR, null);
                        }
                    }
                });
    }

    @Override
    public void name(String token, String realName, String idCard, String idCardFront, String idCardBack, String handHeldIdCard, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getNameUrl()).addHeader("x-auth-token", token)
                .addParams("realName", realName).addParams("idCard", idCard).addParams("idCardFront", idCardFront)
                .addParams("idCardBack", idCardBack).addParams("handHeldIdCard", handHeldIdCard).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("实名认证出错", "实名认证出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("实名认证回执：", "实名认证回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void accountPwd(String token, String jyPassword, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAccountPwdUrl()).addHeader("x-auth-token", token)
                .addParams("jyPassword", jyPassword).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("设置资金密码出错", "设置资金密码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("设置资金密码回执：", "设置资金密码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("设置成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void allAds(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAllAdsUrl()).addHeader("x-auth-token", token).addParams("pageSize", "50").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取我的所有广告出错", "获取我的所有广告出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取我的所有广告回执：", "获取我的所有广告回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Ads> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<Ads>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void release(String token, int id, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getReleaseUrl())
                .addHeader("x-auth-token", token).addParams("id", id + "").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("上架广告出错", "上架广告出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("上架广告回执：", "上架广告回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("上架成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void deleteAds(String token, int id, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getDeleteAdsUrl())
                .addHeader("x-auth-token", token).addParams("id", id + "").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("删除广告出错", "删除广告出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("删除广告回执：", "删除广告回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("删除成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void offShelf(String token, int id, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getOffShelfUrl())
                .addHeader("x-auth-token", token).addParams("id", id + "").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("下架广告出错", "下架广告出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("下架广告回执：", "下架广告回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("下架成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void adDetail(String token, int id, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAdDetailUrl()).addHeader("x-auth-token", token).addParams("id", id + "").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("广告详情出错", "广告详情出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("广告详情回执：", "广告详情回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        Ads obj = gson.fromJson(object.getJSONObject("data").toString(), Ads.class);
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void updateAd(String token, int id, String price, String advertiseType, String coinId, String minLimit, String maxLimit, int timeLimit, String countryZhName, String priceType, String premiseRate, String remark, String number, String pay, String jyPassword, String auto, String autoword, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getUpdateAdUrl()).addHeader("x-auth-token", token).addParams("id", id + "")
                .addParams("price", price).addParams("advertiseType", advertiseType).addParams("coin.id", coinId).addParams("minLimit", minLimit).addParams("maxLimit", maxLimit)
                .addParams("timeLimit", timeLimit + "").addParams("country.ZhName", countryZhName).addParams("priceType", priceType).addParams("premiseRate", premiseRate).addParams("remark", remark)
                .addParams("number", number).addParams("pay[]", pay).addParams("jyPassword", jyPassword).addParams("auto", auto).addParams("autoword", autoword)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("修改广告出错", "修改广告出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("修改广告回执：", "修改广告回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("修改成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void c2cInfo(int id, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getC2CInfoUrl()).addParams("id", id + "").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取买入卖出信息出错", "获取买入卖出信息出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取买入卖出信息回执：", "获取买入卖出信息回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        C2CExchangeInfo obj = gson.fromJson(object.getJSONObject("data").toString(), C2CExchangeInfo.class);
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void c2cBuy(String token, String id, String coinId, String price, String money, String amount, String remark, String mode, final DataCallback dataCallback) {
        WonderfulLogUtils.logi(UrlFactory.getC2CSellUrl(), "price：" + price);
        WonderfulLogUtils.logi(UrlFactory.getC2CSellUrl(), "money：" + money);
        WonderfulLogUtils.logi(UrlFactory.getC2CSellUrl(), "amount：" + amount);
        WonderfulOkhttpUtils.post().url(UrlFactory.getC2CBuyUrl()).addHeader("x-auth-token", token)
                .addParams("id", id).addParams("coinId", coinId).addParams("price", price)
                .addParams("money", money).addParams("amount", amount).addParams("remark", remark)
                .addParams("mode", mode)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("C2C买入出错", "C2C买入出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("C2C买入回执：", "C2C买入回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("成功买入");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void c2cSell(String token, String id, String coinId, String price, String money, String amount, String remark, String mode, final DataCallback dataCallback) {
        WonderfulLogUtils.logi(UrlFactory.getC2CSellUrl(), "price：" + price);
        WonderfulLogUtils.logi(UrlFactory.getC2CSellUrl(), "money：" + money);
        WonderfulLogUtils.logi(UrlFactory.getC2CSellUrl(), "amount：" + amount);
        WonderfulOkhttpUtils.post().url(UrlFactory.getC2CSellUrl()).addHeader("x-auth-token", token)
                .addParams("id", id).addParams("coinId", coinId).addParams("price", price)
                .addParams("money", money).addParams("amount", amount).addParams("remark", remark)
                .addParams("mode", mode)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("C2C卖出出错", "C2C卖出出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("C2C卖出回执：", "C2C卖出回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("成功卖出");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void myOrder(String token, int status, int pageNo, int pageSize, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getMyOrderUrl()).addHeader("x-auth-token", token)
                .addParams("status", status + "").addParams("pageNo", pageNo + "").addParams("pageSize", pageSize + "").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("我的订单出错", "我的订单出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("我的订单回执：", "我的订单回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Order> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<Order>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void myWallet(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getWalletUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取所有钱包出错0", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取所有钱包回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Coin> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Coin>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void myContractWallet(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getContractWalletUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取合约信息出错0", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取合约信息回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<CoinContract> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<CoinContract>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getSpotWallet(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSpotWalletUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("币币资产列表 出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("币币资产列表 回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<CoinContract> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<CoinContract>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void eryuanWalletWallet(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getEryuanWalletUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("期权资产列表 出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("期权资产列表 回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<CoinContract> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<CoinContract>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getRate(final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getExchangeRate()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取实时汇率出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
//                WonderfulLogUtils.logi("获取实时汇率回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        Log.e("balanceCny: ", "1");
                        double obj = object.optDouble("data");
//                        String obj = gson.fromJson(object.getJSONObject("data").toString(), String.class);
                        Log.e("balanceCny: ", "2");
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void myFiatWallet(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getFiatWalletUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取法币信息出错0", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取法币信息回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<CoinContract> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<CoinContract>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void extractinfo(String token, String parentCoin, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getExtractinfo2Url()).addHeader("x-auth-token", token)
                .addParams("parentCoin", parentCoin)
                .addParams("coinName", parentCoin)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取提币信息出错", "获取提币信息出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取提币信息回执：", "获取提币信息回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<ExtractInfo> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<ExtractInfo>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getUSDTWallet(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getOTCWalletUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取USDT信息出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取USDT信息回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        CoinContract obj = gson.fromJson(object.getJSONObject("data").toString(), CoinContract.class);
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void extract(String token, String unit, String amount, String remark, String jyPassword, String address, String code, String googleCode, String tag, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getTIBi()).addHeader("x-auth-token", token).addParams("jyPassword", jyPassword)
                .addParams("unit", unit).addParams("amount", amount).addParams("address", address)
                .addParams("remark", remark).addParams("code", code).addParams("googleCode", googleCode)
                .addParams("tag", tag)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("提币出错", "提币出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("提币回执：", "提币回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("提币成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void allTransaction(String token, int page, int limit, int memberId, String startTime, String endTime, String symbol, String type, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAllTransactionUrl2()).addHeader("x-auth-token", token)
                .addParams("pageNo", page + "")
                .addParams("pageSize", limit + "")
                .addParams("memberId", memberId + "")
                .addParams("startTime", startTime + "")
                .addParams("endTime", endTime + "")
                .addParams("symbol", symbol + "")
                .addParams("type", type + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取资产流水出错", "获取资产流水出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取资产流水回执：", "获取资产流水回执：" + response.toString());
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    if (0 == object.getInt("code")) {
                        WalletDetailNew objs = gson.fromJson(object.get("data").toString(), WalletDetailNew.class);
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.getString("message"));
                    }
                } catch (JSONException e) {
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void safeSetting(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSafeSettingUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("账户设置出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("账户设置回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        SafeSetting obj = gson.fromJson(object.getJSONObject("data").toString(), SafeSetting.class);
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message") + "请重新登录");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void avatar(String token, String url, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAvatarUrl()).addHeader("x-auth-token", token).addParams("url", url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("设置头像出错", "设置头像出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("设置头像回执：", "设置头像回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("设置成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void bindPhone(String token, String phone, String code, String password, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getBindPhoneUrl()).addHeader("x-auth-token", token)
                .addParams("phone", phone).addParams("code", code).addParams("password", password).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("绑定手机号出错", "绑定手机号出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("绑定手机号回执：", "绑定手机号回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("成功绑定");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void sendCode(String token, String phone, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSendCodeUrl()).addHeader("x-auth-token", token).addParams("phone", phone).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("发送绑定手机的验证码出错", "发送绑定手机的验证码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("发送绑定手机的验证码回执：", "发送绑定手机的验证码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("发送成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void bindEmail(String token, String email, String code, String password, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getBindEmailUrl()).addHeader("x-auth-token", token)
                .addParams("email", email).addParams("code", code).addParams("password", password).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("绑定邮箱出错", "绑定邮箱出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("绑定邮箱回执：", "绑定邮箱回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("绑定成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void bindUsername(String token, String username, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getBindUserNameUrl()).addHeader("x-auth-token", token)
                .addParams("nickname", username).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("编辑姓名出错", "编辑姓名出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("编辑姓名回执：", "编辑姓名回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("绑定成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void sendEmailCode(String token, String email, final DataCallback dataCallback) {
//        Log.i("miao","x-auth-token : "+token);
//        Log.i("miao","email : "+email);
        WonderfulOkhttpUtils.post().url(UrlFactory.getSendEmailCodeUrl()).addHeader("x-auth-token", token).addParams("email", email).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("发送绑定邮箱的验证码出错", "发送绑定邮箱的验证码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("发送绑定邮箱的验证码回执：", "发送绑定邮箱的验证码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("发送成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void sendEditLoginPwdCode(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getEditLoginPwdUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("发送修改登录密码出错", "发送修改登录密码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("发送修改登录密码回执：", "发送修改登录密码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("发送成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void editPwd(String token, String oldPassword, String newPassword, String code, String googleCode, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getEditPwdUrl()).addHeader("x-auth-token", token)
                .addParams("oldPassword", oldPassword).addParams("newPassword", newPassword).addParams("code", code)
                .addParams("googleCode", googleCode).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("修改登录密码出错", "修改登录密码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("修改登录密码回执：", "修改登录密码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("修改成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    /**
     * 盘口查询
     */
    @Override
    public void plate(String symbol, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getPlateUrl()).addParams("symbol", symbol).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("盘口信息出错", "盘口信息出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("盘口信息回执：", "盘口信息回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    dataCallback.onDataNotAvailable(object.getInt("code"), object.getString("message"));
                } catch (JSONException e) {
                    Plate obj = gson.fromJson(response.toString(), Plate.class);
                    dataCallback.onDataLoaded(obj);
                }
            }
        });
    }

    @Override
    public void entrust(String token, int pageSize, int pageNo, String symbol, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getEntrustUrl()).addHeader("x-auth-token", token)
                .addParams("pageSize", pageSize + "").addParams("pageNo", pageNo + "").addParams("symbol", symbol + "").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("委托出错", "委托出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("委托回执：", "委托回执：" + response.toString());
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                } catch (JSONException e) {
                    try {
                        List<EntrustHistory> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<EntrustHistory>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
//                        dataCallback.onDataNotAvailable(GlobalConstant.TOKEN_DISABLE1, object.optString("message"));
                    }
                }
            }
        });

    }

    @Override
    public void cancleEntrust(String token, String orderId, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCancleEntrustUrl() + orderId).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("取消委托出错", "取消委托出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("取消委托回执：", "取消委托回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("取消成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void phoneForgotCode(String account, String challenge, String validate, String seccode, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getPhoneForgotPwdCodeUrl()).addParams("account", account).addParams("challenge", challenge)
                .addParams("validate", validate).addParams("seccode", seccode).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("忘记密码手机验证码出错", "忘记密码手机验证码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("忘记密码手机验证码回执：", "忘记密码手机验证码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("发送成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void forgotPwd(String account, String code, String mode, String password, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getForgotPwdUrl())
                .addParams("account", account).addParams("password", password).addParams("code", code).addParams("mode", mode).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("忘记密码手机重置出错", "忘记密码手机重置出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("忘记密码手机重置回执：", "忘记密码手机重置回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("重置成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void emailForgotCode(String account, String challenge, String validate, String seccode, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getEmailForgotPwdCodeUrl()).addParams("account", account).addParams("challenge", challenge)
                .addParams("validate", validate).addParams("seccode", seccode).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("忘记密码邮箱验证码出错", "忘记密码邮箱验证码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("忘记密码邮箱验证码回执：", "忘记密码邮箱验证码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("发送成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void captch(final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCaptchaUrl()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("api1出错", "api1出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("api1回执：", "api1回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    dataCallback.onDataLoaded(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void sendChangePhoneCode(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSendChangePhoneCodeUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("更改绑定手机号验证码出错", "更改绑定手机号验证码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("更改绑定手机号验证码回执：", "更改绑定手机号验证码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("发送成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void changePhone(String token, String password, String phone, String code, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getChangePhoneUrl())
                .addHeader("x-auth-token", token).addParams("password", password).addParams("phone", phone).addParams("code", code).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("更改绑定手机号出错", "更改绑定手机号出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("更改绑定手机号回执：", "更改绑定手机号回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("修改成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void message(int pageNo, int pageSize, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getMessageUrl())
                .addParams("pageNo", pageNo + "").addParams("pageSize", pageSize + "").build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取公告出错", "获取公告出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取公告回执：", "获取公告回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Message> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<Message>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void messageDetail(String id, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.get().url(UrlFactory.getMessageDetailUrl() + id).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("公告详情出错", "公告详情出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("公告详情回执：", "公告详情回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        Message obj = gson.fromJson(object.getJSONObject("data").toString(), Message.class);
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void remark(String token, String remark, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getRemarkUrl()).addHeader("x-auth-token", token).addParams("remark", remark).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("意见反馈出错", "意见反馈出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("意见反馈回执：", "意见反馈回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("提交成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void appInfo(final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAppInfoUrl()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("关于我们出错", "关于我们出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("关于我们回执：", "关于我们回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        AppInfo obj = gson.fromJson(object.getJSONObject("data").toString(), AppInfo.class);
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void banners(String sysAdvertiseLocation, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getBannersUrl()).addParams("sysAdvertiseLocation", sysAdvertiseLocation).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("首页轮播出错", "首页轮播出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
//                WonderfulLogUtils.logi("首页轮播回执：", "首页轮播回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<BannerEntity> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<BannerEntity>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void orderDetail(String token, String orderSn, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getOrderDetailUrl()).addHeader("x-auth-token", token).addParams("orderSn", orderSn).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("订单详情出错", "订单详情出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("订单详情回执：", "订单详情回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        OrderDetial obj = gson.fromJson(object.getJSONObject("data").toString(), OrderDetial.class);
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void cancle(String token, String orderSn, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCancleUrl()).addHeader("x-auth-token", token).addParams("orderSn", orderSn).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("取消订单出错", "取消订单出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("取消订单回执：", "取消订单回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("取消成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void payDone(String token, String orderSn, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getpayDoneUrl()).addHeader("x-auth-token", token)
                .addParams("orderSn", orderSn).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("确认付款完成出错", "确认付款完成出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("确认付款完成回执：", "确认付款完成回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void releaseOrder(String token, String orderSn, String jyPassword, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getReleaseOrderUrl())
                .addHeader("x-auth-token", token).addParams("orderSn", orderSn).addParams("jyPassword", jyPassword).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("放行订单出错", "放行订单出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("放行订单回执：", "放行订单回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void appeal(String token, String remark, String orderSn, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAppealUrl()).addHeader("x-auth-token", token)
                .addParams("remark", remark).addParams("orderSn", orderSn).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("订单申诉出错", "订单申诉出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("订单申诉回执：", "订单申诉回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("提交成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void editAccountPed(String token, String newPassword, String oldPassword, String msgCode, String googleCode, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getEditAccountPwdUrl())
                .addHeader("x-auth-token", token).addParams("newPassword", newPassword).addParams("oldPassword", oldPassword).addParams("msgCode", msgCode)
                .addParams("googleCode", googleCode)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("修改账户密码出错", "修改账户密码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("修改账户密码回执：", "修改账户密码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("修改成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void resetAccountPwd(String token, String newPassword, String code, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getResetAccountPwdUrl()).addHeader("x-auth-token", token)
                .addParams("newPassword", newPassword).addParams("code", code).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("重置密码出错", "重置密码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("重置密码回执：", "重置密码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("重置成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void resetAccountPwdCode(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getResetAccountPwdCodeUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("重置密码的验证码出错", "重置密码的验证码出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("重置密码的验证码回执：", "重置密码的验证码回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("发送成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getHistoryMessage(String token, String orderId, final int pageNo, int pageSize, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getHistoryMessageUrl()).addHeader("x-auth-token", token).
                addParams("orderId", orderId).addParams("page", String.valueOf(pageNo))
                .addParams("limit", String.valueOf(pageSize)).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取历史聊天记录出错", "获取历史聊天记录出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取历史聊天记录回执：", "pageNo     " + pageNo + "      获取历史聊天记录回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    /*if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.getString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }*/
                    List<ChatEntity> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<ChatEntity>>() {
                    }.getType());
                    dataCallback.onDataLoaded(objs);
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getEntrustHistory(String token, String symbol, final int pageNo, int pageSize, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getHistoryEntrus()).addHeader("x-auth-token", token)
                .addParams("symbol", symbol)
                .addParams("pageNo", String.valueOf(pageNo))
                .addParams("pageSize", String.valueOf(pageSize)).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取委托记录出错", "获取委托记录出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取委托记录回执：", "pageNo     " + pageNo + "      获取委托记录回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    /*if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.getString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }*/
                    WonderfulLogUtils.logi("获取委托记录回执：", "pageNo     " + pageNo + "      获取委托记录回执：" + object.getJSONArray("content").toString());
                    List<EntrustHistory> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<EntrustHistory>>() {
                    }.getType());
                    dataCallback.onDataLoaded(objs);
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getCreditInfo(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCreditInfo()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取认证信息出错", "获取认证信息出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取认证信息回执：", "获取认证信息回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    Credit.DataBean objs = gson.fromJson(object.getJSONObject("data").toString(), new TypeToken<Credit.DataBean>() {
                    }.getType());
                    dataCallback.onDataLoaded(objs);
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getNewVision(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getNewVision()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取版本信息出错", "获取版本信息出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取版本信息回执：", "获取版本信息回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        Vision objs = gson.fromJson(object.toString(), new TypeToken<Vision>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), MyApplication.getApp().getString(R.string.versionUpdateTip));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getSymbol(final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSymbolUrl()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取交易对信息出错", "获取交易对信息出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取交易对信息回执：", "获取交易对信息回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONArray array = new JSONArray(response);
                    if (array.length() != 0) {
                        List<MarketSymbol> objs = gson.fromJson(array.toString(), new TypeToken<List<MarketSymbol>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        //dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getAccountSetting(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAccountSettingUrl())
                .addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取收款账户信息出错", "获取收款账户信息出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取收款账户信息回执：", "获取收款账户信息回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        AccountSetting objs = gson.fromJson(object.getJSONObject("data").toString(), new TypeToken<AccountSetting>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getBindBank(String token, String bank, String branch, String jyPassword, String realName, String cardNo, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getBindBankUrl())
                .addHeader("x-auth-token", token)
                .addParams("bank", bank)
                .addParams("branch", branch)
                .addParams("jyPassword", jyPassword)
                .addParams("realName", realName)
                .addParams("cardNo", cardNo).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("绑定银行卡出错", "绑定银行卡出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("绑定银行卡回执：", "绑定银行卡回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("绑定成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getUpdateBank(String token, String bank, String branch, String jyPassword, String realName, String cardNo, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getUpdateBankUrl())
                .addHeader("x-auth-token", token)
                .addParams("bank", bank)
                .addParams("branch", branch)
                .addParams("jyPassword", jyPassword)
                .addParams("realName", realName)
                .addParams("cardNo", cardNo).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("更改银行卡出错", "更改银行卡出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("更改银行卡回执：", "更改银行卡回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("更改成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getBindWeiChat(String token, String wechat, String jyPassword, String realName, String qrCodeUrl, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getBindWechatUrl())
                .addHeader("x-auth-token", token)
                .addParams("wechat", wechat).addParams("jyPassword", jyPassword)
                .addParams("realName", realName)
                .addParams("qrCodeUrl", qrCodeUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("绑定微信出错", "绑定微信出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("绑定微信回执：", "绑定微信回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("绑定成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getUpdateWeiChat(String token, String wechat, String jyPassword, String realName, String qrCodeUrl, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getUpdateWechatUrl())
                .addHeader("x-auth-token", token)
                .addParams("wechat", wechat).addParams("jyPassword", jyPassword)
                .addParams("realName", realName)
                .addParams("qrCodeUrl", qrCodeUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("更改微信出错", "更改微信出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("更改微信回执：", "更改微信回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("更改成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getBindAli(String token, String ali, String jyPassword, String realName, String qrCodeUrl, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getBindAliUrl())
                .addHeader("x-auth-token", token)
                .addParams("ali", ali).addParams("jyPassword", jyPassword)
                .addParams("realName", realName)
                .addParams("qrCodeUrl", qrCodeUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("绑定支付宝出错", "绑定支付宝出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("绑定支付宝回执：", "绑定支付宝回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("绑定成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getUpdateAli(String token, String ali, String jyPassword, String realName, String qrCodeUrl, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getUpdateAliUrl())
                .addHeader("x-auth-token", token)
                .addParams("ali", ali).addParams("jyPassword", jyPassword)
                .addParams("realName", realName)
                .addParams("qrCodeUrl", qrCodeUrl).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("更改支付宝出错", "更改支付宝出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("更改支付宝回执：", "更改支付宝回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("更改成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getUpdateInter(String token, String name, String address, String jyPassword, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getUpdateInterUrl())
                .addHeader("x-auth-token", token)
                .addParams("name", name).addParams("jyPassword", jyPassword)
                .addParams("swiftAddress", address).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("更改switft出错", "更改switft出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("更改switft回执：", "更改switft回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void updateInter(String token, String name, String address, String jyPassword, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.updateInterUrl())
                .addHeader("x-auth-token", token)
                .addParams("name", name).addParams("jyPassword", jyPassword)
                .addParams("swiftAddress", address).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("更改switft出错", "更改switft出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("更改switft回执：", "更改switft回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("更改成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getCheckMatch(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCheckMatchUrl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("检查配对出错", "检查配对出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("检查配对回执：", "检查配对回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        GccMatch objs = gson.fromJson(object.toString(), new TypeToken<GccMatch>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getStartMatch(String token, String amount, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getStartMatchUrl())
                .addHeader("x-auth-token", token)
                .addParams("amount", amount)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("开始配对出错", "开始配对出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("开始配对回执：", "开始配对回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getPromotion(String token, String page, String number, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getPromotionUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", page).addParams("pageSize", number)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("推广好友记录出错", "推广好友记录出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("推广好友记录回执：", "推广好友记录回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<PromotionRecord> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<PromotionRecord>>() {
                        }.getType());
                        //WonderfulLogUtils.logi("推广好友记录回执：", "推广好友记录回执：" + objs.size());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getPromotionByMember(String token, String pageNo, String inviteId, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getPromotionByMemberUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo)
                .addParams("pageSize", "20")
                .addParams("inviteId", inviteId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("推广好友记录 出错", "推广好友记录 出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("推广好友记录 回执：", "推广好友记录 回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<PromotionRecord> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<PromotionRecord>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getInvestPromotion(String token, String page, String number, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestPromotionUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", page).addParams("pageSize", number)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("接点好友记录出错", "接点好友记录出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("接点好友记录回执：", "接点好友记录回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<PromotionRecord> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<PromotionRecord>>() {
                        }.getType());
                        //WonderfulLogUtils.logi("推广好友记录回执：", "推广好友记录回执：" + objs.size());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getInvestPromotionByMember(String token, String pageNo, String pointId, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestPromotionByMemberUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo)
                .addParams("pageSize", "20")
                .addParams("pointId", pointId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("接点好友记录 出错", "接点好友记录 出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("接点好友记录 回执：", "接点好友记录 回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<PromotionRecord> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<PromotionRecord>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getPromotionReward(String token, String page, String number, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getPromotionRewardUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", page).addParams("pageSize", number)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("推广佣金记录出错", "推广佣金记录出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("推广佣金记录回执：", "推广佣金记录回执：" + response.toString());
                try {
                    //JSONObject object = new JSONObject(response);
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<PromotionReward> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<PromotionReward>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getDepositList(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getDepositList())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取保证金币种出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("保证金币种：", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<DepositInfo> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<DepositInfo>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void commitSellerApply(String token, String coinId, String json, final DataCallback dataCallback) {

        WonderfulOkhttpUtils.post().url(UrlFactory.getSellerApply())
                .addHeader("x-auth-token", token)
                .addParams("json", json)
                .addParams("businessAuthDepositId", coinId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("申请成为商家出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("申请成为商家：", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getLoginAuthType(String mobile, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getLoginAuthType())
                .addParams("mobile", mobile)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取用户是否绑定谷歌认证出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取用户是否绑定谷歌认证：", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.getString("data"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    public void getServiceFeeAll(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.get().url(UrlFactory.getServiceFeeAll()).addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取所有等级的手续费出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取所有等级的手续费：", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.getString("data"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


    public void getUserWithdrawLimit(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.get().url(UrlFactory.getUserWithdrawLimit()).addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取用户当日提币数量和次数出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取用户当日提币数量和次数：", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.getString("data"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    // pageNum,pageSize,type(积分类型 PROMOTION_GIVING 推广 LEGAL_RECHARGE_GIVING 法币充值赠送 COIN_RECHARGE_GIVING("币币充值赠送"))
    //                createStartTime 开始时间 createStartTime 结束时间 (时间格式 yyyy-MM-dd HH:mm:ss)
    @Override
    public void getScoreRecord(String token, String pageNum, String pageSize, String type, String createStartTime, String createEndTime, final DataCallback dataCallback) {
        post().url(UrlFactory.getScoreRecord())
                .addParams("pageNum", pageNum)
                .addParams("pageSize", pageSize)
//                .addParams("type", type)
//                .addParams("createStartTime", createStartTime)
//                .addParams("createEndTime", createEndTime)
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("用户积分查询出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("用户积分查询：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        RecordEntity obj = gson.fromJson(object.getJSONObject("data").toString(), RecordEntity.class);
//                        List<ScoreRecordBean> scoreRecordBeans = new Gson().fromJson(object.getString("data"), new TypeToken<List<ScoreRecordBean>>() {
//                        }.getType());
                        dataCallback.onDataLoaded(obj.getContent());
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void sendEditAccountPwdCode(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSendEditAccountPwdCodeUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("发送修改资金密码出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("发送修改资金密码回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("发送成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


    public void getCandyRecord(String token, String pageNum, String pageSize, String startTime, String endTime, final DataCallback dataCallback) {
        post().url(UrlFactory.getCandyRecord())
                .addParams("pageNum", pageNum)
                .addParams("pageSize", pageSize)
                .addParams("startTime", startTime)
                .addParams("endTime", endTime)
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("用户糖果查询出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("用户糖果查询：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.getString("data"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    public void getRandom(String token, final DataCallback dataCallback) {
        post().url(UrlFactory.getRandom())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取随机数出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取随机数：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.getString("data"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    public void uploadVideo(String token, String fileName, File file, final DataCallback dataCallback) {
        Log.i("uploadVideo", fileName + "--" + file.getAbsolutePath());
        post().url(UrlFactory.uploadVideo())
                .addHeader("x-auth-token", token)
                //.addHeader("Content-Type","video/*")
                .addFile("file", fileName, file)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("上传视频出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("上传视频：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("data"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }
    /*public void uploadVideo(String token, String fileName, File file, final DataCallback dataCallback) {
        OkHttpUtils
                .post()
                .url(UrlFactory.uploadVideo())
                .addHeader("x-auth-token", token)
                .addHeader("Content-Type","media/mp4")*//*audio/mp4*//*
                .addFile("file", fileName, file)
                .build()
                .execute(new MyStringCallback<String>() {
                    @Override
                    public void onContinue(String entity, int id) {
                        Log.i("entity",entity);
                        dataCallback.onDataLoaded(entity);
                    }

                    @Override
                    public String parseNetworkResponse(Response response, int id) throws Exception {
                        return null;
                    }

                });
    }*/


    public void creditVideo(String token, String videoStr, String random, final DataCallback dataCallback) {
        Log.i("creditVideo", videoStr + "---" + random);
        post().url(UrlFactory.creditVideo())
                .addHeader("x-auth-token", token)
                .addParams("videoStr", videoStr)
                .addParams("random", random)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("提交视频认证出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("提交视频认证：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


    public void getFiatAsset(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getFiatAssetUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取法币资产 出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取法币资产 回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<FiatAssetBean> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<FiatAssetBean>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    public void getMarginAsset(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getMarginAssetUrl()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取杠杆资产出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取杠杆资产回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<MarginAssetBean> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<MarginAssetBean>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    //币币和法币划转
    public void transferFiat(String coinName, String amount, String direction, String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.transferFiat())
                .addParams("coinName", coinName).addParams("amount", amount).addParams("direction", direction)
                .addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("币币和法币划转出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("币币和法币划转回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    //期权和法币划转
    public void transferOption(String token, String coinName, String amount, String direction, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.transferOptions())
                .addHeader("x-auth-token", token)
                .addParams("coinName", coinName).addParams("amount", amount).addParams("direction", direction)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("期权和法币划转 出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("期权和法币划转 回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    //获取支持划转币种-杠杆交易对和法币币种
    public void getTransferSupportCoin(String token, final boolean isFiat, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getTransferSupportCoin()).addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取支持划转币种出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取支持划转币种回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        if (isFiat) {//法币支持币种
                            List<CoinTransferSupportBean> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("supportOtcCoins").toString(), new TypeToken<List<CoinTransferSupportBean>>() {
                            }.getType());
                            dataCallback.onDataLoaded(objs);
                        } else {//杠杆支持的交易对
                            List<SymbolSupportTransferBean> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("supportLeverCoins").toString(), new TypeToken<List<SymbolSupportTransferBean>>() {
                            }.getType());
                            dataCallback.onDataLoaded(objs);
                        }
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    public void transferMargin(boolean intoMargin, String coinUnit, String amount, String leverCoinSymbol, String token, final DataCallback dataCallback) {
        String url = intoMargin ? UrlFactory.transferIntoMargin() : UrlFactory.transferOutMargin();
        WonderfulOkhttpUtils.post().url(url)
                .addParams("coinUnit", coinUnit).addParams("amount", amount).addParams("leverCoinSymbol", leverCoinSymbol)
                .addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("币币和法币划转出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("币币和法币划转回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    public void borrowCoin(String coinUnit, String amount, String symbol, String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.borrowCoin())
                .addParams("coinUnit", coinUnit).addParams("amount", amount).addParams("symbol", symbol)
                .addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("借贷出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("借贷回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    /*       pageNo:1
            pageSize:10
            symbol:交易对 ETH/USDT 非必传
            repayment:是否归还 0未归还 1已归还*/
    public void getBorrowRecord(String pageNo, String pageSize, String symbol, String repayment, String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.borrowRecord())
                .addParams("pageNo", pageNo).addParams("pageSize", pageSize).addParams("symbol", symbol).addParams("repayment", repayment)
                .addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("借贷记录出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("借贷记录回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<BorrowRecordBean.ContentBean> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<BorrowRecordBean.ContentBean>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    public void giveBackCoin(String loanRecordId, String amount, String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.giveBackCoin())
                .addParams("amount", amount)
                .addParams("loanRecordId", loanRecordId)
                .addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("还币出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("还币回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


    public void getIeoList(String pageNum, String pageSize, String status, String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getIeo())
                .addParams("pageNum", pageNum)
                .addParams("pageSize", pageSize)
                .addParams("status", status)
                .addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取ieo出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取ieo回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<IeoBean> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<IeoBean>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    public void takeOrderIeo(String ieoId, String amount, String jyPassword, String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.takeOrderIeo())
                .addParams("id", ieoId)
                .addParams("amount", amount)
                .addParams("jyPassword", jyPassword)
                .addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("ieo下单出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("ieo下单回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


    public void getIeoOrderRecoed(String pageNum, String pageSize, String userName,
                                  String userMobile, String ieoName, String status, String startTime, String endTime,
                                  String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getIeoRecord())
                .addParams("pageNum", pageNum)
                .addParams("pageSize", pageSize)
                .addParams("userName", userName)
                .addParams("userMobile", userMobile)
                .addParams("ieoName", ieoName)
                .addParams("status", status)
                .addParams("startTime", startTime)
                .addParams("endTime", endTime)
                .addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取ieo订单记录出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取ieo订单记录回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<IeoOrderRecordBean> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<IeoOrderRecordBean>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    public void getRepaymentRecord(String pageNo, String pageSize, String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getRepaymentRecord())
                .addParams("pageNo", pageNo).addParams("pageSize", pageSize).addParams("paymentType", "3")//3是还款记录
                .addHeader("x-auth-token", token).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("还款记录出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("还款记录回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<GiveBackRecordBean> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<GiveBackRecordBean>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


    public void getTransactionType(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.get().url(UrlFactory.getTransactionType()).addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取资产流水交易类型出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取资产流水交易类型回执：", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<TransactionTypeBean> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<TransactionTypeBean>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    public void getFeeRate(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.get().url(UrlFactory.getFeeRate()).addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取费率出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取费率回执：", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Double> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Double>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    public void sendLoginCode(String phone, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.sendLoginCode()).addParams("phone", phone)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("发送登录验证码出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("发送登录验证码回执：", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        String objs = object.optString("message");
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


    public void getTotalAssets(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.get().url(UrlFactory.getTotalAssets()).addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取总资产出错", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取总资产回执：", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Double> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Double>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getNiurenRank(String token, String time, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.get().url(UrlFactory.getNiurenRankUrl() + time)
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取牛人榜榜单出错", "获取牛人榜榜单出错：" + e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取牛人榜榜单回执：", "获取牛人榜榜单回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<NiurenEntity> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("niurenRank").toString(), new TypeToken<List<NiurenEntity>>() {
                        }.getType());
                        List<MyNiurenEntity> objsMy = gson.fromJson(object.getJSONObject("data").getJSONArray("myRank").toString(), new TypeToken<List<MyNiurenEntity>>() {
                        }.getType());
                        NiurenArrayEntity niurenArrayEntity = new NiurenArrayEntity();
                        niurenArrayEntity.setObjs(objs);
                        niurenArrayEntity.setObjsMy(objsMy);
                        dataCallback.onDataLoaded(niurenArrayEntity);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getCoinType(final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCoinList())
                .addParams("symbol", "USDT")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取跟单资产类型", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
//                WonderfulLogUtils.logi("获取跟单资产类型回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<CoinTypeEntity> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<CoinTypeEntity>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getLeverage(final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getLeverage())
                .addParams("symbol", "EOS/USDT")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取杠杆", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
//                WonderfulLogUtils.logi("获取杠杆回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<String> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<String>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getFollowHistory(String token, String pageNo, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getFollowHistory())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo)
                .addParams("pageSize", "20")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取跟单记录", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取跟单记录回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<FollowHistoryContent> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<FollowHistoryContent>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getCancelFollow(String token, String followId, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCancelFollow())
                .addHeader("x-auth-token", token)
                .addParams("followId", followId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("删除跟单记录", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("删除跟单记录回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded("取消成功");
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getApplyNiuren(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getApplyNiuRen())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("申请", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("申请回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void addFollow(String token, String symbol, String leverage, String amount, String jyPassword, String fMemberId, final DataCallback dataCallback) {
        Log.e("fMemberId:", fMemberId);
        WonderfulOkhttpUtils.post().url(UrlFactory.addFollow())
                .addHeader("x-auth-token", token)
                .addParams("symbol", symbol)
                .addParams("leverage", leverage)
                .addParams("amount", amount)
                .addParams("jyPassword", jyPassword)
                .addParams("fMemberId", fMemberId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("跟单", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("跟单回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getLockUSDT(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getLockUSDTUrl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("申请", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("申请回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("data"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getGoogleCode(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.get().url(UrlFactory.googleCodeUrl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取googleCode失败", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取googleCode回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        GoogleCodeEntity obj = gson.fromJson(object.getJSONObject("data").toString(), GoogleCodeEntity.class);
                        dataCallback.onDataLoaded(obj);
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void bindGoogle(String token, String secret, String code, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.bindGoogleUrl())
                .addHeader("x-auth-token", token)
                .addParams("secret", secret)
                .addParams("codes", code)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("绑定google错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("绑定google回执：", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getDrawleSymbol(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getEryuanSymbolUrl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("含有价格趋势的交易对 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("含有价格趋势的交易对 回执：", response.toString());
                try {
                    List<Currency> objs = gson.fromJson(response, new TypeToken<List<Currency>>() {
                    }.getType());
                    dataCallback.onDataLoaded(objs);
                } catch (Exception e) {
                    WonderfulLogUtils.logi("含有价格趋势的交易对 错误解析", e.getMessage());
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getCurrentOrder(String token, int pageNo, final String symbol, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getEryuantCurrentUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", "30")
                .addParams("symbol", symbol)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取当前期权委托 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取当前期权委托获取当前期权委托", symbol + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<OptionEntity> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<OptionEntity>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getEryuanHistory(String token, int pageNo, final String symbol, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getEryuantHistoryUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", "30")
                .addParams("symbol", "")
                .addParams("startTime", "")
                .addParams("endTime", "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取当前完成期权 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取当前完成期权 回执", symbol + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<OptionEntity> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<OptionEntity>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getErryuanHold(String token, int pageNo, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getEryuantCurrentUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", "30")
                .addParams("symbol", "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取当前期权持仓 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取当前期权持仓 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<OptionEntity> objs = gson.fromJson(object.getJSONArray("content").toString(), new TypeToken<List<OptionEntity>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getInvestDetail(String token, int pageNo, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestDetailUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", "30")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("合约明细 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("合约明细 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<ContractDetailEntity> objs = gson.fromJson(object.getJSONObject("data").getJSONObject("detail").getJSONArray("content").toString(), new TypeToken<List<ContractDetailEntity>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void trusteeshipSubmit(String token, String amount, String password, String type, String coinName, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestSubmitUrl())
                .addHeader("x-auth-token", token)
                .addParams("amount", amount)
                .addParams("term", type)
                .addParams("password", password)
                .addParams("coinName", coinName)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("确认交易 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("确认交易 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataLoaded(object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getInvestQuota(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestQuotaUrl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("仓位上下限 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("仓位上下限 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        InvestQuotEntity obj = gson.fromJson(object.getJSONObject("data").toString(), InvestQuotEntity.class);
                        dataCallback.onDataLoaded(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getDayRate(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getDayRateUrl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("日利率 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("日利率 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        InvestDatRateEntity obj = gson.fromJson(object.getJSONObject("data").toString(), InvestDatRateEntity.class);
                        dataCallback.onDataLoaded(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getInvestRule(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestRuleUrl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("规则 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("规则 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        String obj = object.getJSONObject("data").optString("content");
//                        String obj = gson.fromJson(object.getJSONObject("data").getJSONObject("content").toString(), String.class);
                        dataCallback.onDataLoaded(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getInvestFinish(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestFinishUrl())
                .addHeader("x-auth-token", token)
                .addParams("result", "true")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("确认本轮结束 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("确认本轮结束 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        String obj = object.getJSONObject("data").optString("profit");
                        dataCallback.onDataLoaded(obj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getInvestEarning(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestEarning())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("确认本轮收益 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("确认本轮收益 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<ProfitManageEntity> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<ProfitManageEntity>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getInvestType(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestTypeUrl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("所有可投资项的详情 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("所有可投资项的详情 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<InvestTypeEntity> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<InvestTypeEntity>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


    @Override
    public void getInvestProfit(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestProfitStaticUrl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("收益详情总览 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("收益详情总览 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<InvestProfitEntity> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<InvestProfitEntity>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getInvestProfitDetail(String token, String type, int pageNo, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestProfitDetailUrl())
                .addHeader("x-auth-token", token)
                .addParams("type", type)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", "30")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("收益详情 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("收益详情 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<ProfitDetailEntity> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<ProfitDetailEntity>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getStaticProfit(String token, int page, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestStaticProfitUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", page + "")
                .addParams("pageSize", "30")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("查询收益钱包 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("查询收益钱包 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<ProfitListEntity> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<ProfitListEntity>>() {
                        }.getType());
//                        ProfitEntity obj = gson.fromJson(object.getJSONObject("data").toString(), ProfitEntity.class);
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getTeamProfit(String token, int page, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestTeamProfitUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", page + "")
                .addParams("pageSize", "30")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("查询团队奖励 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("查询团队奖励 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<ProfitListEntity> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<ProfitListEntity>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getStaticTransfer(String token, String amount, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestStaticTransferUrl())
                .addHeader("x-auth-token", token)
                .addParams("amount", amount)
                .addParams("coinName", "CUT")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("收益钱包划转 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("收益钱包划转 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataLoaded(object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getTeamTransfer(String token, String amount, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getInvestTeamTransferUrl())
                .addHeader("x-auth-token", token)
                .addParams("amount", amount)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("团队奖励划转 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("团队奖励划转 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataLoaded(object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void setTickersZone(String token, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.get().url(UrlFactory.getMarketZoneUrl())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("交易区间接口 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("交易区间接口 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<String> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<String>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void setTickersPage(String token, String sort, String direction, String nav, String legalCurrency, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.setTickersPageUrl())
                .addHeader("x-auth-token", token)
                .addParams("sort", sort)
                .addParams("direction", direction)
                .addParams("nav", nav)
                .addParams("legalCurrency", legalCurrency)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("行情页面接口 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("行情页面接口 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Currency> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Currency>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void addCollection(String symbol, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.addCollectionUrl())
                .addParams("symbol", symbol)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("添加自选 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("添加自选 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(JSON_ERROR, object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void cancleCollection(String symbol, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.cancleCollectionUrl())
                .addParams("symbol", symbol)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("删除自选 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("删除自选 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        dataCallback.onDataLoaded(object.optString("message"));
                    } else {
                        dataCallback.onDataNotAvailable(JSON_ERROR, object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }

    @Override
    public void getFavorite(String token, String memberId, final DataCallback dataCallback) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCollectionUrl())
                .addHeader("x-auth-token", token)
                .addParams("memberId", memberId)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取自选 错误", e.getMessage());
                dataCallback.onDataNotAvailable(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取自选 回执", response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Currency> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Currency>>() {
                        }.getType());
                        dataCallback.onDataLoaded(objs);
                    } else {
                        dataCallback.onDataNotAvailable(JSON_ERROR, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dataCallback.onDataNotAvailable(JSON_ERROR, null);
                }
            }
        });
    }


}
