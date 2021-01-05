package com.bibi.ui.order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.adapter.PagerAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.base.BaseFragment;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.C2C;
import com.bibi.entity.CoinInfo;
import com.bibi.entity.Currency;
import com.bibi.entity.HoldEntity;
import com.bibi.ui.buy_or_sell.C2CBuyOrSellActivity;
import com.bibi.ui.main.trade.C2CFragment;
import com.bibi.ui.order_detail.OrderDetailActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulStringUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/24
 */
public class OrderActivity extends BaseActivity implements OrdersContract.OrdersView {
    @BindView(R.id.llTitle)
    RelativeLayout llTitle;
    @BindView(R.id.tab_name)
    TabLayout tab_name;
    @BindView(R.id.vp_fiat_list)
    ViewPager vp_fiat_list;
    @BindView(R.id.tvProfit)
    TextView tvProfit;
    @BindView(R.id.tvAvailable)
    TextView tvAvailable;
    @BindView(R.id.tvFrozon)
    TextView tvFrozon;
    @BindView(R.id.tvFloatPnL)
    TextView tvFloatPnL;
    @BindView(R.id.tvRiskRate)
    TextView tvRiskRate;
    @BindView(R.id.tv_start_time)
    TextView tv_start_time;
    @BindView(R.id.tv_end_time)
    TextView tv_end_time;

    private ArrayList<String> tabs = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private PagerAdapter adapter;
    private String symbol;
    private AssetEntity assetEntity;
    private OrdersContract.OrdersPresenter presenter;
    private Gson gson = new Gson();
    private List<Currency> currency = new ArrayList<>();
    String startTime = "";
    String endTime = "";

    private Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //在这里执行定时需要的操作
            presenter.getAssetPNL(SharedPreferenceInstance.getInstance().getTOKEN());
            presenter.allCurrency();
            mHandler.postDelayed(this, 8000);
        }
    };

    public static void actionStart(Context context, String symbol, AssetEntity assetEntity) {
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra("symbol", symbol);
        intent.putExtra("assetEntity", assetEntity);
        context.startActivity(intent);
    }

    @OnClick(R.id.llStartTime)
    public void showStartTime() {
        showTimePickerView(tv_start_time, true);
    }

    @OnClick(R.id.llEndTime)
    public void showEndTime() {
        showTimePickerView(tv_end_time, false);
    }

    @OnClick(R.id.ibBack)
    public void back() {
        finish();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new OrdersPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        symbol = getIntent().getStringExtra("symbol");
        assetEntity = (AssetEntity) getIntent().getSerializableExtra("assetEntity");
        if (assetEntity != null) {
            tvProfit.setText(new DecimalFormat("#0.00").format(assetEntity.getDynamicProfit()));
            tvAvailable.setText(new DecimalFormat("#0.00").format(assetEntity.getAvailable()));
            tvFrozon.setText(new DecimalFormat("#0.00").format(assetEntity.getFrozon()));
            if (assetEntity.getFloatPnL() >= 0) {
                tvFloatPnL.setTextColor(getResources().getColor(R.color.color_00b274));
            } else {
                tvFloatPnL.setTextColor(getResources().getColor(R.color.typeRed));
            }
            tvFloatPnL.setText(new DecimalFormat("#0.00").format(assetEntity.getFloatPnL()));
            tvRiskRate.setText(new DecimalFormat("#0.00").format(assetEntity.getRiskRate()) + "%");
        }
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        presenter.allCurrency();
        setData();
        mHandler.postDelayed(runnable, 5000);
    }

    private void setData() {
        tabs.clear();
        fragments.clear();
        tabs.add(getResources().getString(R.string.position));
        tabs.add(getResources().getString(R.string.commission));
        tabs.add(getResources().getString(R.string.deal));
        tab_name.setTabMode(TabLayout.MODE_FIXED);

        fragments.add(OrdersFragment.getInstance(symbol, OrdersFragment.HOLD));
        fragments.add(OrdersFragment.getInstance(symbol, OrdersFragment.ENTRUST));
        fragments.add(OrdersFragment.getInstance(symbol, OrdersFragment.FINISH));

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

    public void showTimePickerView(final TextView tvTime, final boolean isStart) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(2019, 5, 1, 0, 0, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(System.currentTimeMillis());
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:00");
                tvTime.setText(df.format(date));
                if (isStart) {
                    startTime = WonderfulStringUtils.dateToLong(date) + "";
                } else {
                    endTime = WonderfulStringUtils.dateToLong(date) + "";
                }
                for (int i = 0; i < fragments.size(); i++) {
                    ((OrdersFragment) fragments.get(i)).dataLoaded(startTime, endTime);
                }
            }
        }).setType(new boolean[]{true, true, true, true, false, false})
                .setCancelText(getResources().getString(R.string.cancle))//取消按钮文字
                .setSubmitText(getResources().getString(R.string.confirm))//确认按钮文字
                .setSubmitColor(Color.parseColor("#0b0b0b"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#787878"))//取消按钮文字颜色
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .build();
        pvTime.show();
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
    public void getHoldFinishSuccess(List<HoldEntity> holdEntityList) {

    }

    @Override
    public void getHoldFinishFial(int e, String meg) {

    }

    @Override
    public void getHoldCurrentSuccess(List<HoldEntity> holdEntityList) {

    }

    @Override
    public void getHoldCurrentFial(int e, String meg) {

    }

    @Override
    public void getCancleSuccess(String success) {

    }

    @Override
    public void getCancleFial(int e, String meg) {

    }

    @Override
    public void getCloseAllSuccess(String success) {

    }

    @Override
    public void getCloseSuccess(String success) {

    }

    @Override
    public void getCloseFial(int e, String meg) {

    }

    @Override
    public void getModifySuccess(String success) {

    }

    @Override
    public void allCurrencySuccess(Object obj) {
        try {
            JsonObject object = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray array1 = object.getAsJsonArray("changeRank").getAsJsonArray();
            currency = gson.fromJson(array1, new TypeToken<List<Currency>>() {
            }.getType());
            if (fragments.get(0) != null) {
                ((OrdersFragment) fragments.get(0)).dataLoaded(currency);
            }
            if (fragments.get(1) != null) {
                ((OrdersFragment) fragments.get(1)).dataLoaded(currency);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAssetPNLSuccess(AssetEntity assetEntity) {
        if (assetEntity != null) {
            try {
                tvProfit.setText(new DecimalFormat("#0.00").format(assetEntity.getDynamicProfit()));
                tvAvailable.setText(new DecimalFormat("#0.00").format(assetEntity.getAvailable()));
                tvFrozon.setText(new DecimalFormat("#0.00").format(assetEntity.getFrozon()));
                if (assetEntity.getFloatPnL() >= 0) {
                    tvFloatPnL.setTextColor(getResources().getColor(R.color.color_00b274));
                } else {
                    tvFloatPnL.setTextColor(getResources().getColor(R.color.typeRed));
                }
                tvFloatPnL.setText(new DecimalFormat("#0.00").format(assetEntity.getFloatPnL()));
                tvRiskRate.setText(new DecimalFormat("#0.00").format(assetEntity.getRiskRate()) + "%");
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void setPresenter(OrdersContract.OrdersPresenter presenter) {
        this.presenter = presenter;
    }
}
