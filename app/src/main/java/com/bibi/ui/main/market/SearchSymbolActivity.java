package com.bibi.ui.main.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bibi.R;
import com.bibi.adapter.HomesAdapter;
import com.bibi.adapter.SearchSymbolAdapter;
import com.bibi.adapter.TextWatcher;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.Currency;
import com.bibi.entity.User;
import com.bibi.ui.aboutus.AboutUsActivity;
import com.bibi.ui.kline.KlineActivity;
import com.bibi.ui.main.MainContract;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/27
 */
public class SearchSymbolActivity extends BaseActivity implements SearchSymbolContract.View {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.rvContent)
    RecyclerView rvContent;

    private SearchSymbolContract.Presenter presenter;
    private SearchSymbolAdapter mAdapter;
    private List<Currency> currenciesAll = new ArrayList<>();
    private List<Currency> currencies = new ArrayList<>();
    private List<Currency> currencieFavorite = new ArrayList<>();

    @OnClick(R.id.ibBack)
    void onBack() {
        finish();
    }

    @OnClick(R.id.tvCancle)
    void onCancle() {
        etSearch.setText("");
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchSymbolActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_search_symbol;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new SearchSymbolPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                currencies.clear();
                if (s.toString().equals("")) {
                    currencies.addAll(currenciesAll);
                    mAdapter.notifyDataSetChanged();
                    return;
                }
                for (Currency currency : currenciesAll) {
                    if (currency.getSymbol().toLowerCase().contains(s.toString().toLowerCase())) {
                        currencies.add(currency);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        initRvContent();
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        User user = MyApplication.getApp().getCurrentUser();
        if (user == null) {
            return;
        }
        presenter.getFavorite(SharedPreferenceInstance.getInstance().getTOKEN(), user.getId() + "");
    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        mAdapter = new SearchSymbolAdapter(currencies);
        mAdapter.isFirstOnly(true);
        mAdapter.setOnCollectListener(new SearchSymbolAdapter.OnclickListenerCollect() {
            @Override
            public void collect(Currency item) {
                    if (item.isCollect()) {
                        //收藏
                        presenter.addCollection(item.getSymbol());
                    } else {
                        //取消收藏
                        presenter.cancleCollection(item.getSymbol());
                    }
            }
        });
        rvContent.setAdapter(mAdapter);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    public void setPresenter(SearchSymbolContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getTickersPageSuccess(List<Currency> obj) {
        currenciesAll.clear();
        currencies.clear();
        currenciesAll.addAll(obj);
        for (Currency currencyFavorite : currencieFavorite) {
            for (Currency currency : currenciesAll) {
                if (currencyFavorite.getSymbol().equals(currency.getSymbol())) {
                    currency.setCollect(true);
                    break;
                }
            }
        }
        currencies.addAll(obj);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getZoneFail(Integer code, String toastMessage) {

    }

    @Override
    public void getColletSuccess(String symbol, boolean isCollect) {
        for (Currency currency : currenciesAll) {
            if (currency.getSymbol().equals(symbol)) {
                currency.setCollect(isCollect);
                break;
            }
        }
    }

    @Override
    public void addCollectionSuccess(String symbol, String message) {
        WonderfulToastUtils.showToast("添加成功");
        for (Currency currency : currenciesAll) {
            if (symbol.equals(currency.getSymbol())) {
                currency.setCollect(true);
                break;
            }
        }
    }

    @Override
    public void cancleCollectionSuccess(String symbol, String message) {
        WonderfulToastUtils.showToast("取消自选成功");
        for (Currency currency : currenciesAll) {
            if (symbol.equals(currency.getSymbol())) {
                currency.setCollect(false);
                break;
            }
        }
    }

    @Override
    public void addCollectionFail(int code, String message) {
        WonderfulToastUtils.showToast(message);
    }

    @Override
    public void cancleCollectionFail(int code, String message) {
        WonderfulToastUtils.showToast(message);
    }

    @Override
    public void getFavoriteSuccess(List<Currency> obj) {
        currencieFavorite.clear();
        currencieFavorite.addAll(obj);
        presenter.setTickersPage(SharedPreferenceInstance.getInstance().getTOKEN(), "", "", "", "");
    }

    @Override
    public void getFavoriteFail(int code, String message) {
        currencieFavorite.clear();
        presenter.setTickersPage(SharedPreferenceInstance.getInstance().getTOKEN(), "", "", "", "");
    }

}
