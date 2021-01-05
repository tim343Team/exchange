package com.bibi.ui.main.asset;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


import com.bibi.app.UrlFactory;
import com.bibi.data.DataSource;
import com.bibi.entity.RechargeSupportContract;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;
import okhttp3.Request;

import static com.bibi.app.GlobalConstant.JSON_ERROR;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/1
 */
public class AssetPresenter implements AssetContract.Presenter {
    private final DataSource dataRepository;
    private final AssetContract.View view;

    public AssetPresenter(DataSource dataRepository, AssetContract.View view) {
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
}
