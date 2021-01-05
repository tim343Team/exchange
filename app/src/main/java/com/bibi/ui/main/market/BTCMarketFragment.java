package com.bibi.ui.main.market;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bibi.ui.kline_spot.SKlineActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.bibi.R;
import com.bibi.ui.kline.KlineActivity;
import com.bibi.ui.main.MarketBaseFragment;
import com.bibi.ui.main.presenter.CommonPresenter;
import com.bibi.ui.main.presenter.ICommonView;
import com.bibi.ui.login.LoginActivity;
import com.bibi.adapter.HomesAdapter;
import com.bibi.app.GlobalConstant;
import com.bibi.app.MyApplication;
import com.bibi.entity.Currency;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import com.bibi.app.Injection;

/**
 * Created by Administrator on 2018/1/29.
 */

public class BTCMarketFragment extends MarketBaseFragment implements ICommonView {

    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    private HomesAdapter adapter;
    private List<Currency> currencies = new ArrayList<>();
    private CommonPresenter commonPresenter;

    public static BTCMarketFragment getInstance( ) {
        BTCMarketFragment btcMarketFragment = new BTCMarketFragment();
        Bundle bundle = new Bundle();
        btcMarketFragment.setArguments(bundle);
        return btcMarketFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_btc_market;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new CommonPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {
        initRvContent();
    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new HomesAdapter(currencies, 1);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(currencies.get(position).getExchangeable()==1){
                    SKlineActivity.actionStart(getActivity(), currencies.get(position).getSymbol());
                }else {
                    WonderfulToastUtils.showToast("暂未开放");
                }
            }
        });
        adapter.isFirstOnly(true);
        rvContent.setAdapter(adapter);
        View head=View.inflate(getActivity(),R.layout.adapter_home_head,null);
        adapter.setHeaderView(head);
    }
    public void isChange(boolean isLoad){
        if(adapter!=null){
            adapter.setLoad(isLoad);
            adapter.notifyDataSetChanged();
        }
    }
    private void collectClick(int position) {
        if (!MyApplication.getApp().isLogin()) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
            return;
        }
        String symbol = currencies.get(position).getSymbol();
        if (currencies.get(position).isCollect()) commonPresenter.delete(getmActivity().getToken(), symbol, position);
        else commonPresenter.add(getmActivity().getToken(), symbol, position);
    }

    @Override
    protected void loadData() {
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    public void dataLoaded(List<Currency> baseBtc) {
        this.currencies.clear();
        this.currencies.addAll(baseBtc);
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(CommonPresenter presenter) {
        this.commonPresenter = presenter;
    }

    @Override
    public void deleteFail(Integer code, String toastMessage) {
        if (code == GlobalConstant.TOKEN_DISABLE1) LoginActivity.actionStart(getActivity());
        else WonderfulCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void deleteSuccess(String obj, int position) {
        this.currencies.get(position).setCollect(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addFail(Integer code, String toastMessage) {
        if (code == GlobalConstant.TOKEN_DISABLE1) LoginActivity.actionStart(getActivity());
        else WonderfulCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void getSymbolSuccess(List<Currency> cirrency) {

    }

    @Override
    public void addSuccess(String obj, int position) {
        this.currencies.get(position).setCollect(true);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getActivity().getSupportFragmentManager().putFragment(outState, "USDT", this);
    }

    public void tcpNotify() {
        if (getUserVisibleHint() && adapter != null) adapter.notifyDataSetChanged();
    }
}
