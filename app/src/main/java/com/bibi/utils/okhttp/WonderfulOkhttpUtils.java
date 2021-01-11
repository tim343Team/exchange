package com.bibi.utils.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.bibi.app.MyApplication;
import com.bibi.ui.setting.SettingActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulStringUtils;

import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.okhttp.get.GetBuilder;
import com.bibi.utils.okhttp.post.PostFormBuilder;
import com.bibi.utils.okhttp.post.PostJsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WonderfulOkhttpUtils {
    private static WonderfulOkhttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler handler;

    public WonderfulOkhttpUtils() {
//        mOkHttpClient = new OkHttpClient();
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(50, TimeUnit.SECONDS).readTimeout(50, TimeUnit.SECONDS).cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                cookieStore.put(httpUrl.host(), list);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                List<Cookie> cookies = cookieStore.get(httpUrl.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).build();
        handler = new Handler(Looper.getMainLooper());
    }

    public static WonderfulOkhttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (WonderfulOkhttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new WonderfulOkhttpUtils();
                }
            }
        }
        return mInstance;
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostJsonBuilder postJson() {
        return new PostJsonBuilder();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    private String name;
    private int cishu = 0;

    public void execute(RequestCall requestCall, Callback callback) {
        if (callback == null) {
            Log.e("callback is null", "!!!");
            callback = Callback.CALLBACK_DEFAULT;
        }
        final String url = requestCall.getCall().request().url().toString();
        WonderfulLogUtils.logi("URL", url);
//        WonderfulLogUtils.logi("请求头", "233"+requestCall.getCall().request().headers().toString());
        name = requestCall.getCall().request().url().toString();
        final Callback finalCallback = callback;
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailResultCallback(call.request(), e, finalCallback);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.code() >= 400 && response.code() <= 599) {
//                    WonderfulLogUtils.logi("miao",response.code()+"-");
                    if (response.code() == 404) {
                        WonderfulLogUtils.logi("miao", name);
                    }
                    MyApplication.getApp().typeBiaoshi2 = response.code();
                    try {
                        sendFailResultCallback(call.request(), new RuntimeException(response.body().string()), finalCallback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                try {
                    String token = response.header("x-auth-token");
                    if (!WonderfulStringUtils.isEmpty(token)) {
                        MyApplication.getApp().getCurrentUser().setToken(token);
                        MyApplication.getApp().saveCurrentUser();
                    }
                    String result = response.body().string();
                    WonderfulLogUtils.logi("URL="+url, result);
                    Object o = result;///finalCallback.parseNetworkResponse(response);
                    sendSuccessResultCallback(o, finalCallback);
                } catch (IOException e) {
                    sendFailResultCallback(response.request(), e, finalCallback);
                }
            }
        });
    }

    public void sendFailResultCallback(final Request request, final Exception e, final Callback callback) {
        if (callback == null) return;
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(request, e);
                callback.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null) return;
        handler.post(new Runnable() {
            @Override
            public void run() {
//                try {
//                    String objectString = object.toString();
//                    JSONObject object = null;
//                    object = new JSONObject(objectString);
//                    if(object.optInt("code") == 4000){
//                        WonderfulToastUtils.showToast("账号失效，请重新登录");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public Handler getHandler() {
        return handler;
    }
}
