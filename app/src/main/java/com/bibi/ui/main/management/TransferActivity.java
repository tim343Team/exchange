package com.bibi.ui.main.management;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.app.GlobalConstant;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.data.DataSource;
import com.bibi.data.RemoteDataSource;
import com.bibi.entity.Coin;
import com.bibi.entity.CoinContract;
import com.bibi.entity.FiatAssetBean;
import com.bibi.entity.ProfitEntity;
import com.bibi.ui.asset_transfer.AssetTransferActivity;
import com.bibi.ui.main.management.presenter.AssetWalletPresenter;
import com.bibi.ui.main.management.presenter.TransferPresenter;
import com.bibi.utils.AnimationUtil;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulToastUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class TransferActivity extends BaseActivity implements ManagementContract.TransferView{
    boolean isSwitch = false;
    int direction = 0;
    private String accountType = GlobalConstant.ACCOUNT_TYPE_FIAT;//钱包转资金
    @BindView(R.id.llTitle)
    ViewGroup llTitle;
    @BindView(R.id.ll_transfer_from)
    LinearLayout ll_transfer_from;
    @BindView(R.id.ll_transfer_to)
    LinearLayout ll_transfer_to;
    @BindView(R.id.et_transfer_amount)
    EditText et_transfer_amount;
    @BindView(R.id.tv_coin_name1)
    TextView tv_coin_name1;
    @BindView(R.id.tv_asset_to)
    TextView tv_asset_to;
    @BindView(R.id.tv_available_coin)
    TextView tv_available_coin;
    @BindView(R.id.tvFrom)
    TextView tvFrom;

    private ManagementContract.TransferPresenter presenter;
    private List<FiatAssetBean> fiatCoins = new ArrayList<>();//资金
    private List<Coin> exchangeCoins = new ArrayList<>(); //合约
    private List<CoinContract> coinContracts = new ArrayList<>();//币币
    private String coinName = "CUT";
    String balance = "0";
    //启动划转页面的类型：币币，杠杆，法币
    public static final String START_TRANSFER_TYPE_EXCHANGE = "START_TRANSFER_TYPE_EXCHANGE";
    public static final String START_TRANSFER_TYPE_FIAT = "START_TRANSFER_TYPE_FIAT";
    private String startType;
    double balanceUsd = 0;
    double releaseBalance = 0;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_transfer;
    }


    @OnClick(R.id.tv_commit)
    public void commitTransfer() {
        switch (accountType) {
            case GlobalConstant.ACCOUNT_TYPE_FIAT:
                transferFiat();
                break;
            case GlobalConstant.ACCOUNT_TYPE_OPTIONS:
                transferOptions();
                break;
            default:
        }
    }

    @OnClick(R.id.ibBack)
    public void onBackClick() {
        finish();
    }

    @OnClick(R.id.tv_transfer_all)
    public void transferAll() {
        et_transfer_amount.setText(balance);
    }




    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void obtainData() {
        presenter = new TransferPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        startType = getIntent().getStringExtra("startType");
        balanceUsd = getIntent().getDoubleExtra("balanceUsd",0.0);
        switch (startType) {
            case START_TRANSFER_TYPE_EXCHANGE:
                tvFrom.setText(getResources().getString(R.string.profit_wallet));
                accountType = GlobalConstant.ACCOUNT_TYPE_FIAT;
                break;
            case START_TRANSFER_TYPE_FIAT:
                tvFrom.setText(getResources().getString(R.string.team_profit));
                accountType = GlobalConstant.ACCOUNT_TYPE_OPTIONS;
                break;
            default:
        }
        tv_coin_name1.setText(coinName);
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        String text = getResources().getString(R.string.availableCoin) + new DecimalFormat("#0.00").format(balanceUsd) + coinName;
        balance = new DecimalFormat("#0.00").format(balanceUsd) + "";
        tv_available_coin.setText(text);
//        getAllAsset();
    }

    private void getAllAsset() {
        RemoteDataSource.getInstance().getFiatAsset(getToken(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                try {
                    fiatCoins.clear();
                    fiatCoins.addAll((List<FiatAssetBean>) obj);
                    if (START_TRANSFER_TYPE_FIAT.equals(startType)) {
                        for (FiatAssetBean coin : fiatCoins) {
                            if (coinName.equals(coin.getCoin().getUnit())) {
                                String text = getResources().getString(R.string.availableCoin) + new DecimalFormat("#0.00").format(coin.getBalance()) + coinName;
                                balance = new DecimalFormat("#0.00").format(coin.getBalance()) + "";
                                tv_available_coin.setText(text);
                            }
                        }
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                WonderfulCodeUtils.checkedErrorCode(TransferActivity.this, code, toastMessage);
            }
        });

        RemoteDataSource.getInstance().myWallet(getToken(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                try {
                    exchangeCoins.clear();
                    exchangeCoins.addAll((List<Coin>) obj);
                    if (START_TRANSFER_TYPE_EXCHANGE.equals(startType)) {
                        for (Coin coin : exchangeCoins) {
                            String coinUnit = coin.getCoin().getUnit();
                            if (coinName.equals(coinUnit)) {
                                String text = getResources().getString(R.string.availableCoin) + new DecimalFormat("#0.00").format(coin.getBalance()) + coinName;
                                balance = new DecimalFormat("#0.00").format(coin.getBalance()) + "";
                                tv_available_coin.setText(text);
                            }
                        }
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                WonderfulCodeUtils.checkedErrorCode(TransferActivity.this, code, toastMessage);
            }
        });

        RemoteDataSource.getInstance().getSpotWallet(getToken(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                coinContracts.clear();
                coinContracts.addAll((List<CoinContract>) obj);
                for (CoinContract coinContract : coinContracts) {
                    if(coinContract.getCoin().getName().equals(coinName)){
                        releaseBalance += coinContract.getBalance();
                    }
                }
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                WonderfulToastUtils.showToast(toastMessage);
            }
        });

    }

    public static void actionStart(Activity activity, String startType,double balanceUsd) {
        Intent intent = new Intent(activity, TransferActivity.class);
        intent.putExtra("startType", startType);
        intent.putExtra("balanceUsd", balanceUsd);
        activity.startActivity(intent);
    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }


    //钱包——资金划转
    public void transferFiat() {
        String amount = et_transfer_amount.getText().toString();
        if (TextUtils.isEmpty(amount)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.input_transfer_count));
            return;
        }
        direction = isSwitch ? 0 : 1;
        presenter.getStaticTransfer(getToken(),amount);
//        RemoteDataSource.getInstance().transferFiat(coinName, amount, direction + "", getToken(), new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                WonderfulToastUtils.showToast((String) obj);
//                finish();
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                WonderfulCodeUtils.checkedErrorCode(TransferActivity.this, code, toastMessage);
//            }
//        });

    }

    //团队——资金划转
    public void transferOptions() {
        String amount = et_transfer_amount.getText().toString();
        if (TextUtils.isEmpty(amount)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.input_transfer_count));
            return;
        }
        direction = isSwitch ? 0 : 1;
        presenter.getTeamTransfer(getToken(),amount);
//        RemoteDataSource.getInstance().transferOption(getToken(),coinName, amount, direction + "", new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                WonderfulToastUtils.showToast((String) obj);
//                finish();
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                WonderfulCodeUtils.checkedErrorCode(TransferActivity.this, code, toastMessage);
//            }
//        });
    }

    @Override
    public void setPresenter(ManagementContract.TransferPresenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void getDataSuccess(String datas) {
        WonderfulToastUtils.showToast(datas);
        finish();
    }
}
