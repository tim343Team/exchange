package com.bibi.ui.address;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import com.bibi.entity.ExtractInfo;
import okhttp3.Request;
import com.bibi.app.UrlFactory;
import com.bibi.data.DataSource;
import com.bibi.entity.HoldEntity;
import com.bibi.entity.SafeSetting;
import com.bibi.entity.TiBiAddress;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

import static com.bibi.app.GlobalConstant.JSON_ERROR;
import static com.bibi.app.GlobalConstant.OKHTTP_ERROR;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/27
 */
public class AddressPresenter implements AddressContract.Presenter {
    private AddressContract.View view;
    private DataSource dataRepository;

    public AddressPresenter(DataSource dataRepository, AddressContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getAddress(String token, int pageNo, int pageSize) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAddressUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("unit","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("获取提币地址回执", UrlFactory.getWalletUrl() + "  error " + " --" + e.toString());
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取提币地址回执：", "获取提币地址回执：" + response.toString());
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optInt("code") == 0) {
                            TiBiAddress obj = gson.fromJson(object.getJSONObject("data").toString(), TiBiAddress.class);
                            view.getAddressSuccess(obj);
                        } else {
                            view.getAddressFial(code, object.optString("message"));
                        }
                    } catch (JSONException e1) {
                        view.getAddressFial(code, null);
                    }
                }
            }
        });
    }

    @Override
    public void deleteAddress(String token, String id) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getDeleteAddressUrl())
                .addHeader("x-auth-token", token)
                .addParams("id", id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("删除地址回执", UrlFactory.getWalletUrl() + "  error " + " --" + e.toString());
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("删除地址回执：", "删除地址回执：" + response.toString());
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optInt("code") == 0) {
                            view.deleteAddressSuccess(object.optString("message"));
                        } else {
                            view.deleteAddressFial(code, object.optString("message"));
                        }
                    } catch (JSONException e1) {
                        view.getAddressFial(code, null);
                    }
                }
            }
        });
    }

    @Override
    public void sendCode(String token) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAddressCode())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("提币地址验证码回执", UrlFactory.getWalletUrl() + "  error " + " --" + e.toString());
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("提币地址验证码回执：", "提币地址验证码回执：" + response.toString());
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optInt("code") == 0) {
                            view.getAddressCodeSuccess( object.optString("message"));
                        } else {
                            view.getAddressCodeFial(code, object.optString("message"));
                        }
                    } catch (JSONException e1) {
                        view.getAddressFial(code, null);
                    }
                }
            }
        });
    }

    @Override
    public void addAddress(String token, String address, String unit, String aims, String code, String remark) {
        WonderfulOkhttpUtils.post().url(UrlFactory.addAddressUrl())
                .addHeader("x-auth-token", token)
                .addParams("address", address + "")
                .addParams("unit", unit + "")
                .addParams("aims", aims + "")
                .addParams("code", code + "")
                .addParams("remark", remark + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("添加提币地址回执", UrlFactory.getWalletUrl() + "  error " + " --" + e.toString());
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("添加提币地址回执：", "添加提币地址回执：" + response.toString());
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optInt("code") == 0) {
                            view.addAddressCodeSuccess( object.optString("message"));
                        } else {
                            view.addAddressCodeFial(code, object.optString("message"));
                        }
                    } catch (JSONException e1) {
                        view.getAddressFial(code, null);
                    }
                }
            }
        });
    }

    @Override
    public void getAllAddress(String token) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getExtractinfo2Url()).addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("获取提币信息出错", "获取提币信息出错：" + e.getMessage());
                view.getAllAddressFial(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("获取提币信息回执：", "获取提币信息回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<ExtractInfo> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<ExtractInfo>>() {
                        }.getType());
                        view.getAllAddressSuccess(objs);
                    } else {
                        view.getAllAddressFial(object.getInt("code"), object.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.getAllAddressFial(JSON_ERROR, null);
                }
            }
        });
    }
}
