package com.bibi.ui.kline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import com.bibi.R;
import com.bibi.app.UrlFactory;
import com.bibi.base.BaseFragment;
import com.bibi.entity.CoinDetail;
import com.bibi.entity.SafeSetting;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/7
 */
public class IntroduceFragment extends BaseFragment {
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.tvPublishTime)
    TextView tvPublishTime;
    @BindView(R.id.tvTotalSupply)
    TextView tvTotalSupply;
    @BindView(R.id.tvCirculatingSupply)
    TextView tvCirculatingSupply;
    @BindView(R.id.tvIcoPrice)
    TextView tvIcoPrice;
    @BindView(R.id.tvWhitepaperUrl)
    TextView tvWhitepaperUrl;
    @BindView(R.id.tvWebsite)
    TextView tvWebsite;
    @BindView(R.id.tvlockExplore)
    TextView tvlockExplore;
    @BindView(R.id.tvCoinInfo)
    TextView tvCoinInfo;

    private Unbinder unbinder;
    private String symbol;
    private String name;

    public static IntroduceFragment getInstance(String symbol) {
        IntroduceFragment introduceFragment = new IntroduceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("symbol", symbol);
        introduceFragment.setArguments(bundle);
        return introduceFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_introduce;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void obtainData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            String[] strings = symbol.split("/");
            name = strings[0];
            getInfo();
        }
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    private void getInfo() {
        WonderfulOkhttpUtils.post().url(UrlFactory.getCoinDetail()).addParams("name", name).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        WonderfulLogUtils.logi("tag12345", "成交 返回数据==" + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.optInt("code") == 0) {
                                CoinDetail coinDetail = gson.fromJson(object.getJSONObject("data").toString(), CoinDetail.class);
                                tvName.setText(coinDetail.getNameCn());
                                tvPublishTime.setText(coinDetail.getPublishTime());
                                tvTotalSupply.setText(coinDetail.getTotalSupply());
                                tvCirculatingSupply.setText(coinDetail.getCirculatingSupply());
                                tvIcoPrice.setText(coinDetail.getIcoPrice());
                                tvWhitepaperUrl.setText(coinDetail.getWhitepaperUrl());
                                tvWebsite.setText(coinDetail.getWebsite());
                                tvlockExplore.setText(coinDetail.getBlockExplore());
                                tvCoinInfo.setText(coinDetail.getCoinInfo());
                            }
                        }catch (Exception e){

                        }

                    }
                });
    }
}
