package com.bibi.ui.asset_transfer;

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

import com.bibi.base.BaseFragment;
import com.bibi.entity.CoinContract;
import com.bibi.ui.dialog.AmountDialog;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulMathUtils;
import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.app.GlobalConstant;
import com.bibi.base.BaseActivity;
import com.bibi.data.DataSource;
import com.bibi.data.RemoteDataSource;
import com.bibi.entity.Coin;
import com.bibi.entity.FiatAssetBean;
import com.bibi.utils.AnimationUtil;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulToastUtils;

public class AssetTransferActivity extends BaseActivity {
    boolean isSwitch = false;
    int direction = 0;
//    private String accountType = GlobalConstant.ACCOUNT_TYPE_FIAT;//默认币币转法币
    private String accountType = GlobalConstant.ACCOUNT_TYPE_OPTIONS;//默认币币转资金账户
    int REQUEST_CODE_SELECT_ACCOUNT = 666;
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

    private String symbol;
    private List<FiatAssetBean> fiatCoins = new ArrayList<>();//资金
    private List<Coin> exchangeCoins = new ArrayList<>(); //合约
    private List<CoinContract> coinContracts = new ArrayList<>();//币币
    private String coinName = "USDT";
    String balance = "0";
    //启动划转页面的类型：币币，杠杆，法币
    public static final String START_TRANSFER_TYPE_EXCHANGE = "START_TRANSFER_TYPE_EXCHANGE";
    public static final String START_TRANSFER_TYPE_OPTIONS = "START_TRANSFER_TYPE_OPTIONS";
    public static final String START_TRANSFER_TYPE_FIAT = "START_TRANSFER_TYPE_FIAT";
    private String symbolOrCoin;
    private String startType;
    double releaseBalance = 0;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_asset_transfer;
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

    @OnClick(R.id.ll_transfer_from)
    public void transFrom() {
        if (isSwitch) {
            return;
        }
        showAmountDialog();
    }

    @OnClick(R.id.ll_transfer_to)
    public void transTo() {
        if (!isSwitch) {
            return;
        }
        showAmountDialog();
    }

    private String getAvailableCoin(String coinName) {
        String text = "";
        if (!isSwitch) {//未切换，显示合约/资金账户对应币种的余额
            switch (accountType) {
                case GlobalConstant.ACCOUNT_TYPE_FIAT:
                    for (Coin coin : exchangeCoins) {
                        String coinUnit = coin.getCoin().getUnit();
                        if (coinName.equals(coinUnit)) {

                            text = getResources().getString(R.string.availableCoin) + new DecimalFormat("#0.00").format(coin.getBalance()) + coinName;
                            balance = new DecimalFormat("#0.00").format(coin.getBalance()) + "";
                        }
                    }
                    break;
                case GlobalConstant.ACCOUNT_TYPE_OPTIONS:
                    for (FiatAssetBean coin : fiatCoins) {
                        if (coinName.equals(coin.getCoin().getUnit())) {
                            text = getResources().getString(R.string.availableCoin) + new DecimalFormat("#0.00").format(coin.getBalance()) + coinName;
                            balance = new DecimalFormat("#0.00").format(coin.getBalance()) + "";
                        }
                    }

                    break;
                default:
            }
        } else {//已切换
            text = getResources().getString(R.string.availableCoin) + new DecimalFormat("#0.00").format(releaseBalance) + coinName;
            balance=new DecimalFormat("#0.00").format(releaseBalance)+ "";
        }
        return text;
    }

    private void getSupportBeansToSelect() {
        ArrayList<String> beans = new ArrayList<>();
        switch (accountType) {
            case GlobalConstant.ACCOUNT_TYPE_FIAT:
                if (!isSwitch) {//合约和币币互转时，未切换，显示币币的币种
                    for (Coin coin : exchangeCoins) {
                        beans.add(coin.getCoin().getUnit());
                    }
                } else {
                    //币币和法币互转时，已切换，接口获取法币支持的币种
                    for (FiatAssetBean fiatAssetBean : fiatCoins) {
                        beans.add(fiatAssetBean.getCoin().getUnit());
                    }
                }
                break;
            default:
        }
    }

    @OnClick(R.id.iv_switch)
    public void switchDirection() {
        switchDirectionView();
        tv_available_coin.setText(getAvailableCoin(coinName));
    }

    private void switchDirectionView() {
        AnimationUtil.upToDown(ll_transfer_from, !isSwitch);
        AnimationUtil.downToUp(ll_transfer_to, !isSwitch);
        isSwitch = !isSwitch;
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void obtainData() {
        symbolOrCoin = getIntent().getStringExtra("symbolOrCoin");
        startType = getIntent().getStringExtra("startType");
        switch (startType) {
            case START_TRANSFER_TYPE_EXCHANGE:
                coinName = symbolOrCoin;
                accountType = GlobalConstant.ACCOUNT_TYPE_OPTIONS;
                break;
            case START_TRANSFER_TYPE_FIAT:
                switchDirectionView();
                coinName = symbolOrCoin;
                accountType = GlobalConstant.ACCOUNT_TYPE_OPTIONS;
                break;
            default:
        }
        tv_coin_name1.setText(coinName);
        getSupportBeansToSelect();
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        getAllAsset();
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
                WonderfulCodeUtils.checkedErrorCode(AssetTransferActivity.this, code, toastMessage);
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
                WonderfulCodeUtils.checkedErrorCode(AssetTransferActivity.this, code, toastMessage);
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

    public static void actionStart(Activity activity, String startType, String symbolOrCoin) {
        Intent intent = new Intent(activity, AssetTransferActivity.class);
        intent.putExtra("startType", startType);
        intent.putExtra("symbolOrCoin", symbolOrCoin);
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


    //合约——币币划转
    public void transferFiat() {
        String amount = et_transfer_amount.getText().toString();
        if (TextUtils.isEmpty(amount)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.input_transfer_count));
            return;
        }
        direction = isSwitch ? 0 : 1;
        RemoteDataSource.getInstance().transferFiat(coinName, amount, direction + "", getToken(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                WonderfulToastUtils.showToast((String) obj);
                finish();
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                WonderfulCodeUtils.checkedErrorCode(AssetTransferActivity.this, code, toastMessage);
            }
        });

    }

    //资金-币币
    public void transferOptions() {
        String amount = et_transfer_amount.getText().toString();
        if (TextUtils.isEmpty(amount)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.input_transfer_count));
            return;
        }
        direction = isSwitch ? 0 : 1;
        RemoteDataSource.getInstance().transferOption(getToken(),coinName, amount, direction + "", new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                WonderfulToastUtils.showToast((String) obj);
                finish();
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                WonderfulCodeUtils.checkedErrorCode(AssetTransferActivity.this, code, toastMessage);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_ACCOUNT && resultCode == RESULT_OK) {
            accountType = data.getStringExtra("accountType");
            if (accountType.equals(GlobalConstant.ACCOUNT_TYPE_FIAT)) {
                tv_asset_to.setText(getString(R.string.fiat_asset));
                tv_available_coin.setText(getAvailableCoin(coinName));
            } else {
                symbol = data.getStringExtra("coinUnit");
                tv_asset_to.setText(symbol + "  " + getString(R.string.margin_asset));
                tv_available_coin.setText(getAvailableCoin(coinName));
            }
        }
    }

    private void showAmountDialog() {
        AmountDialog dialog = AmountDialog.newInstance();
        dialog.show(getSupportFragmentManager(), "bottom");
        dialog.setCallback(new AmountDialog.OperateCallback() {
            @Override
            public void ItemClick(int type) {
                if (type == 0) {//OTC
                    accountType = GlobalConstant.ACCOUNT_TYPE_FIAT;
                    tvFrom.setText(R.string.account_contract);
                } else if (type == 1) {//期权
                    accountType = GlobalConstant.ACCOUNT_TYPE_OPTIONS;
                    tvFrom.setText(R.string.account_options);
                }
                tv_available_coin.setText(getAvailableCoin(coinName));
            }
        });
    }

}
