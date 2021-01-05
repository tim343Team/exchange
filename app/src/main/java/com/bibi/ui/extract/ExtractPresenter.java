package com.bibi.ui.extract;


import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import com.bibi.app.UrlFactory;
import com.bibi.data.DataSource;
import com.bibi.entity.CoinContract;
import com.bibi.entity.ExtractInfo;
import com.bibi.entity.RechargeSupportContract;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;
import okhttp3.Request;

import java.util.List;

import static com.bibi.app.GlobalConstant.JSON_ERROR;

/**
 * Created by Administrator on 2017/9/25.
 */

public class ExtractPresenter implements ExtractContract.Presenter {
    private final DataSource dataRepository;
    private final ExtractContract.View view;

    public ExtractPresenter(DataSource dataRepository, ExtractContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getRechargeSupport(String token, final String parentCoin) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getRechargeSupportedUrl())
                .addHeader("x-auth-token", token)
                .addParams("parentCoin", parentCoin).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.optInt("code") == 0) {
                                List<RechargeSupportContract> objs = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<RechargeSupportContract>>() {
                                }.getType());
                                view.getRechargeSupportSuccess(objs,parentCoin);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            view.errorMes(JSON_ERROR, null);
                        }
                    }
                });
    }

    @Override
    public void getUSDTWallet(String token) {
        view.displayLoadingPopup();
        dataRepository.getUSDTWallet(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.usdtInfoSuccess((CoinContract) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
//                view.extractinfoFail(code, toastMessage);
            }
        });
    }

    @Override
    public void extractinfo(String token,String parentCoin) {
        view.displayLoadingPopup();
        dataRepository.extractinfo(token,parentCoin, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.extractinfoSuccess((List<ExtractInfo>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.extractinfoFail(code, toastMessage);
            }
        });
    }

    @Override
    public void extract(String token, String unit, String amount, String remark, String jyPassword,String address,String code,String googleCode,String tag) {
        view.displayLoadingPopup();
        dataRepository.extract(token, unit, amount, remark, jyPassword,address, code,googleCode,tag,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.extractSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.extractFail(code, toastMessage);

            }
        });
    }
}
