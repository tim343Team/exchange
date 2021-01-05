package com.bibi.ui.main.market;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.bibi.R;
import com.bibi.adapter.HomesAdapter;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseLazyFragment;
import com.bibi.entity.Currency;
import com.bibi.entity.User;
import com.bibi.ui.kline_spot.SKlineActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/10/9
 */
public class FavoriteSubFragment extends BaseLazyFragment implements SearchSymbolContract.View {
    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    @BindView(R.id.llAllTab)
    HorizontalScrollView llAllTab;

    private SearchSymbolContract.Presenter presenter;
    private LinearLayout llAdd;
    private HomesAdapter mAdapter;
    private List<Currency> currencies = new ArrayList<>();

    public static FavoriteSubFragment getInstance() {
        FavoriteSubFragment fragment = new FavoriteSubFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_market_normal;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new SearchSymbolPresenter(Injection.provideTasksRepository(getActivity()), this);
        llAllTab.setVisibility(View.GONE);
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {
        initRvContent();
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        User user = MyApplication.getApp().getCurrentUser();
        if (user == null) {
            return;
        }
        presenter.getFavorite(SharedPreferenceInstance.getInstance().getTOKEN(), user.getId() + "");
    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        mAdapter = new HomesAdapter(currencies, 2);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (currencies.get(position).getExchangeable() == 1) {
                    SKlineActivity.actionStart(getActivity(), currencies.get(position).getSymbol());
                } else {
                    WonderfulToastUtils.showToast("暂未开放");
                }
            }
        });
        View emptyView = getLayoutInflater().inflate(R.layout.empty_no_favorite, null);
        llAdd = emptyView.findViewById(R.id.llAdd);
        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    SearchSymbolActivity.actionStart(getActivity());
                } else {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
                }
            }
        });
        mAdapter.setEmptyView(emptyView);
        mAdapter.isFirstOnly(true);
        mAdapter.setLoad(true);
        rvContent.setAdapter(mAdapter);
    }

    @Override
    public void getTickersPageSuccess(List<Currency> obj) {

    }

    @Override
    public void getZoneFail(Integer code, String toastMessage) {

    }

    @Override
    public void getColletSuccess(String symbol, boolean isCollet) {

    }

    @Override
    public void addCollectionSuccess(String symbol, String message) {

    }

    @Override
    public void cancleCollectionSuccess(String symbol, String message) {

    }

    @Override
    public void addCollectionFail(int code, String message) {

    }

    @Override
    public void cancleCollectionFail(int code, String message) {

    }

    @Override
    public void getFavoriteSuccess(List<Currency> obj) {
        currencies.clear();
        currencies.addAll(obj);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getFavoriteFail(int code, String message) {

    }

    @Override
    public void setPresenter(SearchSymbolContract.Presenter presenter) {
        this.presenter = presenter;
    }

    public void dataLoaded(Currency temp) {
        for (Currency currency : currencies) {
            if (temp.getSymbol().equals(currency.getSymbol())) {
                Currency.shallowClone(currency, temp);
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
