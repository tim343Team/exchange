package com.bibi.ui.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.base.BaseActivity;
import com.bibi.entity.AnnounceEntity;


/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/11
 */
public class AnnouncementDetailActivity extends BaseActivity {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.wb)
    WebView wb;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.text_time)
    TextView text_time;

    AnnounceEntity obj;

    @OnClick(R.id.ibBack)
    public void back() {
        finish();
    }

    public static void actionStart(Context context, AnnounceEntity item) {
        Intent intent = new Intent(context, AnnouncementDetailActivity.class);
        intent.putExtra("announceEntity", item);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_announcement_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        obj = (AnnounceEntity) getIntent().getSerializableExtra("announceEntity");
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
            ImmersionBar.setTitleBar(AnnouncementDetailActivity.this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected void onDestroy() {
        if (wb != null) {
            //加载null内容
            wb.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //清除历史记录
            wb.clearHistory();
            //移除WebView
            ((ViewGroup) wb.getParent()).removeView(wb);
            //销毁VebView
            wb.destroy();
            //WebView置为null
            wb = null;
        }
        super.onDestroy();
    }

    private void initWb() {
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setSupportZoom(false);
        wb.getSettings().setBuiltInZoomControls(false);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setDefaultFontSize(16);
        wb.setWebViewClient(new WebViewClient());
        wb.setBackgroundColor(0);
    }


    private void getMessage() {
        if (obj == null) return;
        if (text == null) {
            return;
        }
        text.setText(obj.getTitle());
        text_time.setText(obj.getCreateTime());
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
        wb.loadDataWithBaseURL(null, obj.getContent(), "text/html", "utf-8", null);
    }
}
