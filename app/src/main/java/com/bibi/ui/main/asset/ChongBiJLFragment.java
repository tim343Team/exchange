package com.bibi.ui.main.asset;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Request;
import com.bibi.R;
import com.bibi.adapter.ChongBiAdapter;
import com.bibi.app.UrlFactory;
import com.bibi.base.BaseFragment;
import com.bibi.base.LinListView;
import com.bibi.customview.DropdownLayout;
import com.bibi.entity.ChongBiBean;
import com.bibi.ui.wallet.ChongBiJLActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/19
 */
public class ChongBiJLFragment extends BaseFragment {
    @BindView(R.id.listview_1)
    LinListView listview_1;
    @BindView(R.id.tvMessage)
    LinearLayout tvMessage;

    private int page = 1;
    private List<ChongBiBean.ContentBean> beans = new ArrayList<>();
    private ChongBiAdapter adapter;
    private String bizhong = "";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chongbijilu;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void obtainData() {
        beans.clear();
        qingQiu("");
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
//        qingQiu("");
    }

    private void qingQiu(String symbol) {
        int id1 = SharedPreferenceInstance.getInstance().getID();
        WonderfulLogUtils.logi("miao", id1 + "id");
        WonderfulOkhttpUtils.post().url(UrlFactory.getCha())
                .addHeader("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN())
                .addParams("memberId", id1 + "")
                .addParams("pageNo", page + "")
                .addParams("pageSize", "40")
                .addParams("type", "0")
                .addParams("symbol", "" + symbol)
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
                WonderfulLogUtils.logi("miao", "充币记录：" + response);
                if (page == 1) {
                    beans.clear();
                }
                if (listview_1 == null) {
                    return;
                }
                listview_1.stopFreshing();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Gson gson = new Gson();
                    ChongBiBean chongBiBean = gson.fromJson(jsonObject.optString("data"), ChongBiBean.class);
                    List<ChongBiBean.ContentBean> contentBeanList = chongBiBean.getContent();
                    if (contentBeanList.size() == 0 && page == 1) {
                        listview_1.setVisibility(View.GONE);
                        tvMessage.setVisibility(View.VISIBLE);
                        return;
                    }
                    beans.addAll(contentBeanList);
                    tvMessage.setVisibility(View.GONE);
                    listview_1.setVisibility(View.VISIBLE);
                    adapter = new ChongBiAdapter(getActivity(), beans);
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
                            page = 1;
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
