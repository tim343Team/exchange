package com.bibi.ui.main.asset;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Request;
import com.bibi.R;
import com.bibi.adapter.TiBiAdapter;
import com.bibi.app.UrlFactory;
import com.bibi.base.BaseFragment;
import com.bibi.base.LinListView;
import com.bibi.customview.DropdownLayout;
import com.bibi.entity.TiBiBean;
import com.bibi.ui.wallet.TiBiJLActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/20
 */
public class TiBiJLAFragment extends BaseFragment {
    @BindView(R.id.listview_1)
    LinListView listview_1;
    @BindView(R.id.tvMessage)
    LinearLayout tvMessage;

    private List<TiBiBean> beans = new ArrayList<>();
    private int page = 0;
    private TiBiAdapter adapter;
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

        WonderfulOkhttpUtils.post().url(UrlFactory.getChaTiBi())
                .addHeader("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN())
                .addParams("page", page + "")
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
                WonderfulLogUtils.logi("miao", "提币记录：" + response);
                if (page == 0) {
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
                        JSONObject jsonObject2 = jsonObject3.optJSONObject("coin");
                        TiBiBean bean = new TiBiBean();
                        bean.name = jsonObject2.optString("unit");
                        bean.dizhi = jsonObject3.optString("address");
                        bean.time = jsonObject3.optString("createTime");
                        bean.number = new BigDecimal(jsonObject3.optDouble("totalAmount")).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                        bean.shouxufei = new BigDecimal(jsonObject3.optDouble("fee")).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                        int status = jsonObject3.optInt("status");
                        if (status == 0) {
                            bean.zhuangtai = getResources().getString(R.string.creditting);
                        } else if (status == 1) {
                            bean.zhuangtai = getResources().getString(R.string.waiting_for_release);
                        } else if (status == 2) {
                            bean.zhuangtai = getResources().getString(R.string.withdraw_fail);
                        } else if (status == 3) {
                            bean.zhuangtai = getResources().getString(R.string.withdraw_success);
                        }
                        beans.add(bean);
                    }

                    tvMessage.setVisibility(View.GONE);
                    listview_1.setVisibility(View.VISIBLE);
                    adapter = new TiBiAdapter(getActivity(), beans);
                    listview_1.setEveryPageItemCount(40);
                    listview_1.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    listview_1.setOnRefreshListener(new LinListView.OnRefreshListener() {
                        @Override
                        public void onLoadMore() {
                            page = page + 1;
                            qingQiu(bizhong);
                        }

                        @Override
                        public void onRefresh() {
                            page = 0;
                            beans.clear();
                            qingQiu(bizhong);
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
