package com.bibi.ui.main.asset;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.adapter.AssetAdapter;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.customview.CircleImageView;
import com.bibi.data.DataSource;
import com.bibi.data.RemoteDataSource;
import com.bibi.entity.CoinContract;
import com.bibi.entity.FiatAssetBean;
import com.bibi.entity.MarginAssetBean;
import com.bibi.entity.RechargeSupportContract;
import com.bibi.entity.SimpleListBean;
import com.bibi.entity.SimpleListItem;
import com.bibi.entity.User;
import com.bibi.ui.asset_transfer.AssetTransferActivity;
import com.bibi.ui.dialog.AssetDialog;
import com.bibi.ui.dialog.ListDialogFragment;
import com.bibi.ui.extract.ExtractActivity;
import com.bibi.ui.recharge.RechargeActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/19
 */
public class AssetActivity extends BaseActivity implements AssetContract.View {
    public static final String CONTRACT = "CONTRACT";
    public static final String FIAT = "FIAT";
    public static final String OPTIONS = "OPTIONS";
    private List<MarginAssetBean> marginCoins = new ArrayList<>();
    private List<FiatAssetBean> fiatCoins = new ArrayList<>();
    private AssetContract.Presenter presenter;

    @BindView(R.id.llTitle)
    RelativeLayout llTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvCnyAmount)
    TextView tvCnyAmount;
    @BindView(R.id.tabContract)
    TextView tabContract;
    @BindView(R.id.tabOptions)
    TextView tabOptions;
    @BindView(R.id.tabFiat)
    TextView tabFiat;
    @BindView(R.id.ivSee)
    ImageView ivSee;
    @BindView(R.id.ivHeader)
    CircleImageView ivHeader;

    @OnClick(R.id.ibBack)
    public void back() {
        finish();
    }

    double balanceUsd = 0;
    double frozenBalance = 0;
    double releaseBalance = 0;
    double presentBalance = 0;
    double balanceCny = 0;
    double rate = 0.0;

    private AssetAdapter adapter;
    private List<CoinContract> coinContracts = new ArrayList<>();
    private String type = OPTIONS;
    private boolean isShow = true;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AssetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.asset_layout_new;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new AssetPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        ivSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchSee();
            }
        });
        initRv();
    }

    private void switchSee() {
        if (!"*****".equals(tvCnyAmount.getText())) {
            tvCnyAmount.setText("*****");
            tvAmount.setText("********");
            Drawable drawable = getResources().getDrawable(R.drawable.icon_eye_guan);
            ivSee.setImageDrawable(drawable);
            SharedPreferenceInstance.getInstance().saveMoneyShowtype(2);
            isShow = false;
        } else {
            tvAmount.setText(new DecimalFormat("#0.00").format(balanceUsd));
            tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(balanceCny, 2, null) + " CNY");
            Drawable drawable = getResources().getDrawable(R.drawable.icon_eye_open);
            ivSee.setImageDrawable(drawable);
            SharedPreferenceInstance.getInstance().saveMoneyShowtype(1);
            isShow = true;
        }
    }

    @Override
    protected void obtainData() {
//        getExchangeAsset();
    }

    @Override
    protected void fillWidget() {
    }

    @Override
    protected void loadData() {
        getOptionsAsset();
    }

    private void initRv() {
        adapter = new AssetAdapter(this, coinContracts);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);
        adapter.setOnListener(new AssetAdapter.OnclickListener() {
            @Override
            public void onClick(CoinContract content) {
                showAmountDialog(content, type);
            }
        });
    }

    private void restRv() {
        adapter.notifyDataSetChanged();
    }

    private void getData(int pagePosition) {
        balanceUsd = 0;
        frozenBalance = 0;
        releaseBalance = 0;
        balanceCny = 0;
        presentBalance = 0;
        switch (pagePosition) {
            case 0:
                //合约账户
                tvAmount.setVisibility(View.VISIBLE);
                tvCnyAmount.setVisibility(View.VISIBLE);
                getContractAsset();
                break;
            case 1:
                //资金账户
                tvAmount.setVisibility(View.VISIBLE);
                tvCnyAmount.setVisibility(View.VISIBLE);
                getFiatAsset();
                break;
            case 2:
                //币币账户
                tvAmount.setVisibility(View.GONE);
                tvCnyAmount.setVisibility(View.GONE);
                getOptionsAsset();
                break;
            default:
        }
    }

    private void getRate() {
        RemoteDataSource.getInstance().getRate(new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                try {
                    rate = (double) obj;
                    balanceCny = balanceUsd * rate;
                    if (isShow) {
                        tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(balanceCny, 2, null) + " CNY");

                    } else if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2) {
                        tvCnyAmount.setText("*****");
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                WonderfulToastUtils.showToast(toastMessage);
            }
        });
    }

    private void getFiatAsset() {
        RemoteDataSource.getInstance().myFiatWallet(getToken(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                Log.i("资产明细回执：", obj.toString());
                coinContracts.clear();
                coinContracts.addAll((List<CoinContract>) obj);

                for (CoinContract coinContract : coinContracts) {
                    balanceUsd += coinContract.getBalance() + coinContract.getFrozenBalance();
                    balanceCny += (coinContract.getBalance() + coinContract.getFrozenBalance()) * rate;
                    frozenBalance += coinContract.getFrozenBalance();
                    releaseBalance += coinContract.getBalance();
                }
                if (isShow) {
                    tvAmount.setText(new DecimalFormat("#0.00").format(balanceUsd));
                    tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(balanceCny, 2, null) + " CNY");

                } else if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2) {
                    tvAmount.setText("********");
                    tvCnyAmount.setText("*****");
                }
                restRv();
//                tvReleaseBalance.setText(new DecimalFormat("#0.00").format(releaseBalance));
//                tvFrozenBalance.setText(new BigDecimal(frozenBalance).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                WonderfulToastUtils.showToast(toastMessage);
            }
        });
    }

    private void getContractAsset() {
        RemoteDataSource.getInstance().myContractWallet(getToken(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                coinContracts.clear();
                coinContracts.addAll((List<CoinContract>) obj);

                for (CoinContract coinContract : coinContracts) {
                    balanceUsd += coinContract.getBalance() + coinContract.getFrozenBalance();
                    balanceCny += (coinContract.getBalance() + coinContract.getFrozenBalance()) * rate;
                    frozenBalance += coinContract.getFrozenBalance();
                    releaseBalance += coinContract.getBalance();
                }
                if (isShow) {
                    tvAmount.setText(new DecimalFormat("#0.00").format(balanceUsd));
                    tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(balanceCny, 2, null) + " CNY");

                } else if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2) {
                    tvAmount.setText("********");
                    tvCnyAmount.setText("*****");
                }
                restRv();
//                tvReleaseBalance.setText(new DecimalFormat("#0.00").format(releaseBalance));
//                tvFrozenBalance.setText(new DecimalFormat("#0.00").format(frozenBalance));
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                WonderfulToastUtils.showToast(toastMessage);
            }
        });
    }

    private void getOptionsAsset() {
        RemoteDataSource.getInstance().getSpotWallet(getToken(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                coinContracts.clear();
                coinContracts.addAll((List<CoinContract>) obj);

                for (CoinContract coinContract : coinContracts) {
                    balanceUsd += coinContract.getBalance() + coinContract.getFrozenBalance();
                    balanceCny += (coinContract.getBalance() + coinContract.getFrozenBalance()) * rate;
                    frozenBalance += coinContract.getFrozenBalance();
                    if (coinContract.getCoin().getName().equals("USDT")) {
                        releaseBalance += coinContract.getBalance();
                    }
                    if (coinContract.getCoin().getName().equals("USDT(赠)")) {
                        presentBalance += coinContract.getBalance();
                    }
                }
                try {
                    if (isShow) {
                        tvAmount.setText(new DecimalFormat("#0.00").format(balanceUsd));
                        tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(balanceCny, 2, null) + " CNY");

                    } else if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2) {
                        tvAmount.setText("********");
                        tvCnyAmount.setText("*****");
                    }
                    restRv();
                } catch (Exception e) {

                }

            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                WonderfulToastUtils.showToast(toastMessage);
            }
        });
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    @OnClick(R.id.tabContract)
    public void showwContract() {
        tabFiat.setTextColor(getResources().getColor(R.color.primaryTextGray));
        tabOptions.setTextColor(getResources().getColor(R.color.primaryTextGray));
        tabContract.setTextColor(getResources().getColor(R.color.white));
        type = CONTRACT;
        getData(0);
    }

    @OnClick(R.id.tabFiat)
    public void showFiat() {
        tabFiat.setTextColor(getResources().getColor(R.color.white));
        tabOptions.setTextColor(getResources().getColor(R.color.primaryTextGray));
        tabContract.setTextColor(getResources().getColor(R.color.primaryTextGray));
        type = FIAT;
        getData(1);
    }

    @OnClick(R.id.tabOptions)
    public void showwOptions() {
        tabContract.setTextColor(getResources().getColor(R.color.primaryTextGray));
        tabFiat.setTextColor(getResources().getColor(R.color.primaryTextGray));
        tabOptions.setTextColor(getResources().getColor(R.color.white));
        type = OPTIONS;
        getData(2);
    }

    @OnClick(R.id.ivSetting)
    public void startReport() {
        ReportActivity.actionStart(this, "");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (type.equals(CONTRACT)) {
            getData(0);
        } else if (type.equals(OPTIONS)) {
            getData(2);
        } else if (type.equals(FIAT)) {
            getData(1);
        }
        if (MyApplication.getApp().isLogin()) {
            loginingViewText();
        }
    }

    private void loginingViewText() {
        User user = MyApplication.getApp().getCurrentUser();
        String url = user.getAvatar();
        if (WonderfulStringUtils.isEmpty(url))
            Glide.with(this).load(R.mipmap.icon_default_header).centerCrop().into(ivHeader);
        else Glide.with(this).load(url).centerCrop().into(ivHeader);
    }

    private void showAmountDialog(final CoinContract content, String type) {
        AssetDialog dialog = AssetDialog.newInstance(content.getCoin().getCanRecharge(), content.getCoin().getCanWithdraw()
                , content.getCoin().getCanTransfer(), type);
        dialog.show(getSupportFragmentManager(), "bottom");
        dialog.setCallback(new AssetDialog.OperateCallback() {
            @Override
            public void ItemClick(int type) {
                if (!MyApplication.getApp().isLogin()) {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
                    return;
                }
                if (type == 0) {
                    presenter.getRechargeSupport(getToken(), content.getCoin().getName());
                } else if (type == 1) {
                    ExtractActivity.actionStart(AssetActivity.this, content);
                } else if (type == 2) {
                    AssetTransferActivity.actionStart(AssetActivity.this, AssetTransferActivity.START_TRANSFER_TYPE_EXCHANGE, content.getCoin().getName());
                }
            }
        });
    }

    @Override
    public void errorMes(int e, String meg) {

    }

    @Override
    public void getRechargeSupportSuccess(List<RechargeSupportContract> objs, String parentCoin) {
        if (objs.size() == 0) {
            if (MyApplication.getApp().isLogin()) {
                RechargeActivity.actionStart(AssetActivity.this, parentCoin);
            } else {
                WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
            }
        } else {
            final SimpleListBean bean = new SimpleListBean();
            List<SimpleListItem> simpleListItems = new ArrayList<>();
            for (RechargeSupportContract contract : objs) {
                SimpleListItem item = new SimpleListItem();
                item.setContent(contract.getName());
                simpleListItems.add(item);
            }
            bean.setNewsItems(simpleListItems);
            ListDialogFragment listDialogFragment = ListDialogFragment.getInstance(bean);
            listDialogFragment.show(getSupportFragmentManager(), "bottom");
            listDialogFragment.setCallback(new ListDialogFragment.OperateCallback() {
                @Override
                public void ItemClick(int position) {
                    if (MyApplication.getApp().isLogin()) {
                        RechargeActivity.actionStart(AssetActivity.this, bean.getNewsItems().get(position).getContent());
                    } else {
                        WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
                    }
                }
            });
        }
    }

    @Override
    public void setPresenter(AssetContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
