package com.bibi.ui.main.asset;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Request;
import com.bibi.R;
import com.bibi.adapter.TextWatcher;
import com.bibi.adapter.WalletAdapter;
import com.bibi.app.UrlFactory;
import com.bibi.base.BaseFragment;
import com.bibi.customview.BottomSelectionFragment;
import com.bibi.entity.Coin;
import com.bibi.ui.asset_transfer.AssetTransferActivity;
import com.bibi.ui.extract.ExtractActivity;
import com.bibi.ui.recharge.RechargeActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

/**
 * @author 明哥
 * 币币资产
 * 参考火币命名
 */

public class AssetExchangeFragment extends BaseFragment {
    @BindView(R.id.rc_asset)
    RecyclerView recyclerView;
    List<Coin> exchangeCoins = new ArrayList<>();
    private WalletAdapter adapter;

    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.cbHide)
    CheckBox cbHide;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_asset_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initRecyclerView();
        etSearch.addTextChangedListener(localChangeWatcher);
        cbHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.setHideZero(isChecked);
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new WalletAdapter(R.layout.adapter_wallet, exchangeCoins);
        adapter.isFirstOnly(true);
        recyclerView.setAdapter(adapter);
    }

    public void getAddress(final Coin coin) {
        WonderfulOkhttpUtils.post().url(UrlFactory.getChongbi())
                .addHeader("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN())
                .addParams("unit", coin.getCoin().getUnit())
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(String response) {
                // WonderfulToastUtils.showToast(WonderfulToastUtils.getString(R.string.noAddAddressTip));
                WonderfulLogUtils.logi("miao", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int code = jsonObject.optInt("code");
                    if (code == 0) {
                        RechargeActivity.actionStart(getActivity(), coin.getCoin().getUnit());
                    } else {
                        WonderfulToastUtils.showToast("" + jsonObject.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }


    private TextWatcher localChangeWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            adapter.setSearchKey(s.toString());
        }
    };

    public void setData(List<Coin> exchangeCoins) {
        this.exchangeCoins.clear();
        this.exchangeCoins.addAll(exchangeCoins);
        adapter.notifyDataSetChanged();
        getExchangeTotalAsset();
    }

    public void hideAmount(boolean isShowAmount){
        if(adapter!=null) {
            adapter.setHideAmount(isShowAmount);
            adapter.notifyDataSetChanged();
        }
    }

    double sumUsd = 0;
    double sumCny = 0;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvCnyAmount)
    TextView tvCnyAmount;


    private void getExchangeTotalAsset() {
        sumCny=0;
        sumUsd=0;
        if(exchangeCoins!=null&&exchangeCoins.size()!=0){
            for (Coin coin : exchangeCoins) {
                sumUsd += (coin.getBalance() * coin.getCoin().getUsdRate());
                sumCny += (coin.getBalance() * coin.getCoin().getCnyRate());
            }
        }
        if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 1) {
            tvAmount.setText(WonderfulMathUtils.getRundNumber(sumUsd, 8, null));
            tvCnyAmount.setText("≈"+WonderfulMathUtils.getRundNumber(sumCny, 2, null) + " CNY");
        } else if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2) {
            tvAmount.setText("********");
            tvCnyAmount.setText("*****");
        }
    }


}
