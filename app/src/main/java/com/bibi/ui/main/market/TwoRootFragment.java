package com.bibi.ui.main.market;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bibi.R;
import com.bibi.adapter.PagerAdapter;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseFragment;
import com.bibi.base.BaseNestingTransFragment;
import com.bibi.base.BaseTransFragment;
import com.bibi.customview.CustomViewPager;
import com.bibi.customview.NoScrollViewPager;
import com.bibi.customview.intercept.WonderfulViewPager;
import com.bibi.entity.CoinInfo;
import com.bibi.entity.Currency;
import com.bibi.ui.address.AddressActivity;
import com.bibi.ui.address.AddressAddActivity;
import com.bibi.ui.main.MainContract;
import com.bibi.ui.main.trade.C2CFragment;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulToastUtils;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/21
 */
public class TwoRootFragment extends BaseTransFragment implements MainContract.TwoRootView {
    public static final String TAG = TwoRootFragment.class.getSimpleName();
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.market_list)
    NoScrollViewPager market_list;
    @BindViews({R.id.tvTab1, R.id.tvTab2, R.id.tvTab3, R.id.tvTab4})
    TextView[] tvTabs;

    private PagerAdapter adapter;
    private ArrayList<String> tabs = new ArrayList<>();
    private MainContract.TwoRootPresenter presenter;
    private FavoriteSubFragment favoriteSubFragment;
    private NormalSubFragment subFragment1;
    private NormalSubFragment subFragment2;
    private NormalSubFragment subFragment3;
    private String legalCurrency = "";
    private List<Currency> currenciesTag1 = new ArrayList<>(); //行情列表1
    private List<Currency> currenciesTag2 = new ArrayList<>(); //行情列表2
    private List<Currency> currenciesTag3 = new ArrayList<>(); //行情列表3

    @OnClick(R.id.ibSearch)
    public void sarch() {
        if (MyApplication.getApp().isLogin()) {
            SearchSymbolActivity.actionStart(getActivity());
        } else {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
        }
    }

    @OnClick(R.id.tvTab1)
    public void selectTab1() {
        showTab(0);
    }

    @OnClick(R.id.tvTab2)
    public void selectTab2() {
        showTab(1);
    }

    @OnClick(R.id.tvTab3)
    public void selectTab3() {
        showTab(2);
    }

    @OnClick(R.id.tvTab4)
    public void selectTab4() {
        showTab(3);
    }

    void showTab(int position) {
        for (int i = 0; i < tvTabs.length; i++) {
            if (i == position) {
                tvTabs[i].setTextColor(getResources().getColor(R.color.white));
                tvTabs[i].setBackground(getResources().getDrawable(R.drawable.shape_gradient_primary_right));
            } else {
                tvTabs[i].setTextColor(getResources().getColor(R.color.colorPrimary));
                tvTabs[i].setBackground(null);
            }
        }
        market_list.setCurrentItem(position);
    }


    @Override
    protected String getmTag() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_root_two;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setData();
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

    private void setData() {
        tabs.clear();
        fragments.clear();
        for (TextView textView : tvTabs) {
            tabs.add("");
        }
        fragments.add(favoriteSubFragment = FavoriteSubFragment.getInstance());
        fragments.add(subFragment1 = NormalSubFragment.getInstance("", legalCurrency));
        fragments.add(subFragment2 = NormalSubFragment.getInstance("MAIN", legalCurrency));
        fragments.add(subFragment3 = NormalSubFragment.getInstance("GEM", legalCurrency));
        if (adapter == null) {
            adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), fragments, tabs);
            market_list.setAdapter(adapter);
            market_list.setOffscreenPageLimit(fragments.size() - 1);
            market_list.setCurrentItem(1);
        } else {
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(getmActivity(), llTitle);
            isSetTitle = true;
        }
    }

    @Override
    public void setPresenter(MainContract.TwoRootPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getZoneFail(Integer code, String toastMessage) {

    }

    @Override
    public void getZoneSuccess(List<String> obj) {

    }

    @Override
    public void getTickersPageSuccess(List<Currency> obj) {

    }

    public void dataLoaded(Currency temp) {
        subFragment1.dataLoaded(temp);
        subFragment1.dataLoaded(temp);
        subFragment2.dataLoaded(temp);
        subFragment3.dataLoaded(temp);
    }

    public void tcpNotify() {

    }
}
