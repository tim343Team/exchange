package com.bibi.ui.main.asset;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.adapter.ChargeAdapter;
import com.bibi.adapter.TiBiAdapter;
import com.bibi.app.UrlFactory;
import com.bibi.base.BaseFragment;
import com.bibi.base.LinListView;
import com.bibi.entity.ChargeHisBean;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;
import butterknife.BindView;
import okhttp3.Request;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/16
 */
public class DealFragment extends BaseFragment{
    @BindView(R.id.listview_1)
    LinListView listview_1;
    @BindView(R.id.tvMessage)
    LinearLayout tvMessage;

    private List<ChargeHisBean> beans = new ArrayList<>();
    private int page = 1;
    private ChargeAdapter adapter;
    private String bizhong = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tibijilu;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void obtainData() {
        qingQiu("");
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    private void qingQiu(String symbol) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getChargeHis())
                .addHeader("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN())
                .addParams("pageNo", page + "")
                .addParams("pageSize", "40")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                hideLoadingPopup();
                listview_1.stopFreshing();
                listview_1.setVisibility(View.GONE);
                tvMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("miao", "交易记录：" + response);
                if (page == 1) {
                    beans.clear();
                }
                if (listview_1 == null) {

                    return;
                }
                listview_1.stopFreshing();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                    JSONArray content = jsonObject1.optJSONArray("content");

                    if (content.length() == 0 && page == 0) {
                        listview_1.setVisibility(View.GONE);
                        tvMessage.setVisibility(View.VISIBLE);
                        return;
                    }
                    for (int i = 0; i < content.length(); i++) {
                        JSONObject jsonObject3 = content.optJSONObject(i);
                        ChargeHisBean bean = new ChargeHisBean();
                        bean.name = jsonObject3.optString("type");
                        bean.time = jsonObject3.optString("createTime");
                        bean.number = jsonObject3.optString("amount");
                        beans.add(bean);
                    }

                    tvMessage.setVisibility(View.GONE);
                    listview_1.setVisibility(View.VISIBLE);
                    adapter = new ChargeAdapter(getActivity(), beans);
                    listview_1.setEveryPageItemCount(40);
                    listview_1.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listview_1.setOnRefreshListener(new LinListView.OnRefreshListener() {
                        @Override
                        public void onLoadMore() {
                            page = page + 1;
                            qingQiu("");
                        }

                        @Override
                        public void onRefresh() {
                            page = 1;
                            beans.clear();
                            qingQiu("");
                        }
                    });

                } catch (Exception e) {
                    listview_1.setVisibility(View.GONE);
                    tvMessage.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }

            }
        });
    }
}
