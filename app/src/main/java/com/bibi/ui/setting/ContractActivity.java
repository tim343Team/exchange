package com.bibi.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.adapter.HelpNewAdapter;
import com.bibi.app.UrlFactory;
import com.bibi.base.BaseActivity;
import com.bibi.entity.ContractEntity;
import com.bibi.entity.HelpBean;
import com.bibi.entity.User;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/30
 */
public class ContractActivity extends BaseActivity {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
//    @BindView(R.id.ibDetail)
//    TextView ibDetail;
    @BindView(R.id.wb)
    WebView wb;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.text_time)
    TextView text_time;
    public final static String CSS_STYLE = "<style>* {font-size:16px;line-height:20px;}p {color:#FFFFFF;}</style>";
    private String id;

    @OnClick(R.id.ibBack)
    public void back() {
        finish();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ContractActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_contract;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWb();
    }

    @Override
    protected void obtainData() {
        getMessage();
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(ContractActivity.this, llTitle);
            isSetTitle = true;
        }
    }

    private void initWb() {
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setSupportZoom(false);
        wb.getSettings().setBuiltInZoomControls(false);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setDefaultFontSize(16);
        wb.setWebViewClient(new WebViewClient());
        wb.setBackgroundColor(getResources().getColor(R.color.white));
    }


    private void getMessage() {
        wb.getSettings().setSupportZoom(true);
        wb.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:function modifyTextColor(){" +
                        "document.getElementsByTagName('body')[0].style.webkitTextFillColor='#FFff0000'" +
                        "};modifyTextColor();");
            }
        });
        wb.loadDataWithBaseURL(null, "https://pdf.maitube.com/pdf/?e=lagPRWVSeo7YNIa", "text/html", "utf-8", null);

//        WonderfulOkhttpUtils.post().url(UrlFactory.getContractUrl())
//                .addParams("id", "1").build().execute(new StringCallback() {
//            @Override
//            public void onResponse(String response) {
//                WonderfulLogUtils.logi("合约指南回执：", "合约指南回执：" + response.toString());
//                try {
//                    JSONObject object = new JSONObject(response);
//                    if (object.optInt("code") == 0) {
//                        ContractEntity obj = gson.fromJson(object.getJSONObject("data").toString(), ContractEntity.class);
//                        if (obj == null) return;
//                        if (text == null) {
//                            return;
//                        }
//                        text.setText(obj.getTitle());
//                        text_time.setText(obj.getCreateTime());
//                        wb.getSettings().setSupportZoom(true);
//                        wb.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
//                        wb.setWebViewClient(new WebViewClient() {
//                            @Override
//                            public void onPageFinished(WebView view, String url) {
//                                super.onPageFinished(view, url);
//                                view.loadUrl("javascript:function modifyTextColor(){" +
//                                        "document.getElementsByTagName('body')[0].style.webkitTextFillColor='#FFff0000'" +
//                                        "};modifyTextColor();");
//                            }
//                        });
//                        wb.loadDataWithBaseURL(null, obj.getContent(), "text/html", "utf-8", null);
//                    }else {
//                        WonderfulToastUtils.showToast(object.optString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
}
