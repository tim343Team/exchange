package com.bibi.ui.notice;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Request;
import com.bibi.app.UrlFactory;
import com.bibi.data.DataSource;
import com.bibi.entity.Ads;
import com.bibi.entity.AnnounceEntity;
import com.bibi.entity.HoldEntity;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

import static com.bibi.app.GlobalConstant.OKHTTP_ERROR;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/8
 */
public class AnnouncementPresenter implements AnnouncementContract.Presenter {
    private final DataSource dataRepository;
    private final AnnouncementContract.View view;

    public AnnouncementPresenter(DataSource dataRepository, AnnouncementContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getAnnouncement(String token, String cate, int pageNo, int pageSize) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getAnnouncementUrl())
                .addHeader("x-auth-token", token)
                .addParams("pageNo", pageNo + "")
                .addParams("pageSize", pageSize + "")
                .addParams("cate", cate)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Log.d("查询公告回执", UrlFactory.getWalletUrl() + "  error " + " --" + e.toString());
                if (view != null) view.getAnnounceFial(OKHTTP_ERROR, null);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("查询公告回执：", "查询公告回执：" + response.toString());
                int code = 4000;
                if (view != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optInt("code") == 0) {
                            List<AnnounceEntity> objs = gson.fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<AnnounceEntity>>() {
                            }.getType());
                            view.getAnnounceSuccess(objs);
                        }else {
                            view.getAnnounceFial(code, null);
                        }
                    } catch (JSONException e1) {
                        view.getAnnounceFial(code, null);
                    }
                }
            }
        });
    }
}
