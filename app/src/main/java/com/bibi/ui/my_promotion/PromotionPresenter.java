package com.bibi.ui.my_promotion;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;
import com.bibi.app.UrlFactory;
import com.bibi.data.DataSource;
import com.bibi.entity.SummaryEntity;
import com.bibi.entity.TiBiAddress;
import com.bibi.ui.address.AddressContract;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/27
 */
public class PromotionPresenter  implements PromotionContract.Presenter {
    private PromotionContract.View view;
    private DataSource dataRepository;

    public PromotionPresenter(DataSource dataRepository, PromotionContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void summary(String token) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getSummary())
                .addHeader("x-auth-token", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("推广统计回执", UrlFactory.getWalletUrl() + "  error " + " --" + e.toString());
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("推广统计回执：", "推广统计回执：" + response.toString());
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optInt("code") == 0) {
                            SummaryEntity obj = gson.fromJson(object.getJSONObject("data").toString(), SummaryEntity.class);
                            view.getSummarySuccess(obj);
                        } else {
//                            view.getSummaryFial(code, object.optString("message"));
                        }
                    } catch (JSONException e1) {
//                        view.getSummary(code, null);
                    }
                }
            }
        });
    }
}
