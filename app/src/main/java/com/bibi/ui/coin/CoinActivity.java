package com.bibi.ui.coin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.adapter.PagerAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.base.BaseFragment;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.Currency;
import com.bibi.entity.HoldEntity;
import com.bibi.ui.order.OrderActivity;
import com.bibi.ui.order.OrdersContract;
import com.bibi.ui.order.OrdersFragment;
import com.bibi.ui.order.OrdersPresenter;
import com.bibi.utils.SharedPreferenceInstance;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/7/27
 */
public class CoinActivity extends BaseActivity implements CoinsContract.CoinsView {
    @BindView(R.id.llTitle)
    RelativeLayout llTitle;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tab_name)
    TabLayout tab_name;
    @BindView(R.id.vp_fiat_list)
    ViewPager vp_fiat_list;

    private CoinsContract.Presenter presenter;
    private ArrayList<String> tabs = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private String symbol;
    private AssetEntity assetEntity;
    private PagerAdapter adapter;

    private Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //在这里执行定时需要的操作
            presenter.allSpotCurrency();
            mHandler.postDelayed(this, 8000);
        }
    };

    @OnClick(R.id.ibBack)
    public void back() {
        finish();
    }

    public static void actionStart(Context context, String symbol, AssetEntity assetEntity) {
        Intent intent = new Intent(context, CoinActivity.class);
        intent.putExtra("symbol", symbol);
        intent.putExtra("assetEntity", assetEntity);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_coin;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new CoinsPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        symbol = getIntent().getStringExtra("symbol");
        assetEntity = (AssetEntity) getIntent().getSerializableExtra("assetEntity");
        tvTitle.setText(getResources().getString(R.string.coin_2_coin_all_orders));
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        setData();
        mHandler.postDelayed(runnable, 2000);
    }

    private void setData() {
        tabs.clear();
        fragments.clear();
        tabs.add(getResources().getString(R.string.commission));
        tabs.add(getResources().getString(R.string.history_entrust));
        tab_name.setTabMode(TabLayout.MODE_FIXED);

        fragments.add(CoinsFragment.getInstance(symbol, OrdersFragment.HOLD));
        fragments.add(CoinsFragment.getInstance(symbol, CoinsFragment.FINISH));

        if (adapter == null) {
            adapter = new PagerAdapter(getSupportFragmentManager(), fragments, tabs);
            vp_fiat_list.setAdapter(adapter);
            tab_name.setupWithViewPager(vp_fiat_list);
            vp_fiat_list.setOffscreenPageLimit(fragments.size() - 1);
        } else {
            adapter.notifyDataSetChanged();
        }

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
    public void errorMes(int e, String meg) {

    }

    @Override
    public void getHoldSuccess(List<HoldEntity> holdEntityList) {

    }

    @Override
    public void getHoldFial(int e, String meg) {

    }

    @Override
    public void getCancleSuccess(String success) {

    }

    @Override
    public void setSpotCurrency(List<Currency> obj) {
        if (fragments.get(0) != null) {
            ((CoinsFragment) fragments.get(0)).dataLoaded(obj);
        }
    }

    @Override
    public void setPresenter(CoinsContract.Presenter presenter) {
        this.presenter=presenter;
    }
}
