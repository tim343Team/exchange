package com.bibi.ui.main.drawer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.bibi.R;
import com.bibi.ui.main.MarketBaseFragment;
import com.bibi.ui.main.presenter.CommonPresenter;
import com.bibi.ui.main.presenter.ICommonView;
import com.bibi.ui.login.LoginActivity;
import com.bibi.adapter.Homes2Adapter;
import com.bibi.app.GlobalConstant;
import com.bibi.app.MyApplication;
import com.bibi.entity.Currency;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import com.bibi.app.Injection;

/**
 * Created by Administrator on 2018/1/29.
 * USDTMarketFragment
 */

public class USDTMarket2Fragment extends MarketBaseFragment implements ICommonView {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
   // private HomeTwoAdapter adapter;
    private Homes2Adapter mAdapter;
    private List<Currency> currencies = new ArrayList<>();
    private CommonPresenter commonPresenter;

    public static USDTMarket2Fragment getInstance( ) {
        USDTMarket2Fragment usdtMarketFragment = new USDTMarket2Fragment();
        Bundle bundle = new Bundle();
        usdtMarketFragment.setArguments(bundle);
        return usdtMarketFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_usdt_market;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        WonderfulLogUtils.loge("INFO", "执行了" + isVisibleToUser);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new CommonPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
    }

    @Override
    protected void obtainData() {
//        commonPresenter.getDrawleSymbol(getmActivity().getToken());
    }

    @Override
    protected void fillWidget() {
        initRvContent();
    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        mAdapter = new Homes2Adapter( currencies, 1);
        View head = View.inflate(getActivity(), R.layout.adapter_home_head, null);
        mAdapter.addHeaderView(head);
//        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                collectClick(position);
//            }
//        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((MarketOperateCallback) getActivity()).itemClick(USDTMarket2Fragment.this.mAdapter.getItem(position),0);
            }
        });
        mAdapter.isFirstOnly(true);
        mAdapter.setLoad(true);
        rvContent.setAdapter(mAdapter);
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
        notifyData();
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    public void dataLoaded(List<Currency> baseUsdt) {
        this.currencies.clear();
        for(Currency currency:baseUsdt){
            if (currency.getExchangeable()==1) this.currencies.add(currency);
        }
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }
    public void isChange(boolean isLoad){
        if(mAdapter!=null){
            mAdapter.setLoad(isLoad);
            mAdapter.notifyDataSetChanged();
        }
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
        mAdapter.notifyDataSetChanged();
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
        mAdapter.notifyDataSetChanged();
    }

    public void notifyData() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public void tcpNotify() {
        if (getUserVisibleHint() && mAdapter != null) mAdapter.notifyDataSetChanged();
    }
}
