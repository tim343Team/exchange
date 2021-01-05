//package com.bibi.ui.main.asset;
//
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.gyf.barlibrary.ImmersionBar;
//
//import org.xutils.common.util.LogUtil;
//
//import java.math.BigDecimal;
//import java.text.DecimalFormat;
//import java.text.DecimalFormatSymbols;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import com.bibi.R;
//import com.bibi.app.MyApplication;
//import com.bibi.base.BaseFragment;
//import com.bibi.base.BaseTransFragment;
//import com.bibi.customview.CircleImageView;
//import com.bibi.data.DataSource;
//import com.bibi.data.RemoteDataSource;
//import com.bibi.entity.Coin;
//import com.bibi.entity.CoinContract;
//import com.bibi.entity.FiatAssetBean;
//import com.bibi.entity.MarginAssetBean;
//import com.bibi.entity.User;
//import com.bibi.ui.address.AddressActivity;
//import com.bibi.ui.asset_transfer.AssetTransferActivity;
//import com.bibi.ui.extract.ExtractActivity;
//import com.bibi.ui.recharge.RechargeActivity;
//import com.bibi.ui.recharge.RechargeDialogFragment;
//import com.bibi.ui.recharge.ReportDialogFragment;
//import com.bibi.utils.SharedPreferenceInstance;
//import com.bibi.utils.WonderfulMathUtils;
//import com.bibi.utils.WonderfulStringUtils;
//import com.bibi.utils.WonderfulToastUtils;
//
//public class AssetFragment extends BaseTransFragment {
//    public static final String TAG = AssetFragment.class.getSimpleName();
//    public static final String CONTRACT = "CONTRACT";
//    public static final String OPTIONS = "OPTIONS";
//    public static final String FIAT = "FIAT";
//    //    private List<Coin> exchangeCoins = new ArrayList<>();
//    private List<MarginAssetBean> marginCoins = new ArrayList<>();
//    private List<FiatAssetBean> fiatCoins = new ArrayList<>();
//
//    @Override
//    protected String getmTag() {
//        return TAG;
//    }
//
//    @BindView(R.id.llTitle)
//    RelativeLayout llTitle;
//    @BindView(R.id.tvAmount)
//    TextView tvAmount;
//    @BindView(R.id.tvCnyAmount)
//    TextView tvCnyAmount;
//    @BindView(R.id.tvReleaseBalance)
//    TextView tvReleaseBalance;
//    @BindView(R.id.tvFrozenBalance)
//    TextView tvFrozenBalance;
//    @BindView(R.id.tv_asset_charge)
//    TextView tvCharge;
//    @BindView(R.id.tv_asset_withdraw)
//    TextView tvWithdraw;
//    @BindView(R.id.tv_asset_transfer)
//    TextView tvTransfer;
//    @BindView(R.id.tabContract)
//    TextView tabContract;
//    @BindView(R.id.tabOptions)
//    TextView tabOptions;
//    @BindView(R.id.tabFiat)
//    TextView tabFiat;
//    @BindView(R.id.tvPresentBalance)
//    TextView tvPresentBalance;
//    @BindView(R.id.ivSee)
//    ImageView ivSee;
//    @BindView(R.id.ivHeader)
//    CircleImageView ivHeader;
//    @BindView(R.id.llPresent)
//    View llPresent;
//    double balanceUsd = 0;
//    double frozenBalance = 0;
//    double releaseBalance = 0;
//    double balanceCny = 0;
//    double rate = 0.0;
//
//    private List<BaseFragment> fragments;
//    private List<CoinContract> coinContracts = new ArrayList<>();
//    private String type = CONTRACT;
//    private boolean isShow = true;
//    private CoinContract coin;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.asset_layout_new;
//    }
//
//    @Override
//    protected void initViews(Bundle savedInstanceState) {
//        ivSee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchSee();
//            }
//        });
//    }
//
//    private void switchSee() {
//        if (!"*****".equals(tvCnyAmount.getText())) {
//            tvCnyAmount.setText("*****");
//            tvAmount.setText("********");
//            Drawable drawable = getResources().getDrawable(R.drawable.icon_eye_guan);
//            ivSee.setImageDrawable(drawable);
//            SharedPreferenceInstance.getInstance().saveMoneyShowtype(2);
//            isShow = false;
//        } else {
//            tvAmount.setText(new DecimalFormat("#0.00").format(balanceUsd));
//            tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(balanceCny, 2, null) + " CNY");
//            Drawable drawable = getResources().getDrawable(R.drawable.icon_eye_open);
//            ivSee.setImageDrawable(drawable);
//            SharedPreferenceInstance.getInstance().saveMoneyShowtype(1);
//            isShow = true;
//        }
//    }
//
//
//    @Override
//    protected void obtainData() {
////        getExchangeAsset();
//    }
//
//    @Override
//    protected void fillWidget() {
//    }
//
//
//    @Override
//    protected void loadData() {
//        getRate();
//    }
//
//    private void getData(int pagePosition) {
//        balanceUsd = 0;
//        frozenBalance = 0;
//        releaseBalance = 0;
//        balanceCny = 0;
//        switch (pagePosition) {
//            case 0:
//                getContractAsset();
//                break;
//            case 1:
//                getFiatAsset();
//                break;
//            case 2:
//                getOptionsAsset();
//                break;
//            default:
//        }
//    }
//
//    private void getRate() {
//        RemoteDataSource.getInstance().getRate(new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                try {
//                    rate = (double) obj;
//                    balanceCny = balanceUsd * rate;
//                    if (isShow) {
//                        tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(balanceCny, 2, null) + " CNY");
//
//                    } else if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2) {
//                        tvCnyAmount.setText("*****");
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                WonderfulToastUtils.showToast(toastMessage);
//            }
//        });
//    }
//
//    private void getFiatAsset() {
//        RemoteDataSource.getInstance().myFiatWallet(getmActivity().getToken(), new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                Log.i("资产明细回执：",obj.toString());
//                coinContracts.clear();
//                coinContracts.addAll((List<CoinContract>) obj);
//
//                if (coinContracts.size() > 0) {
//                    coin = coinContracts.get(0);
//                }
//                for (CoinContract coinContract : coinContracts) {
//                    balanceUsd += coinContract.getBalance() + coinContract.getFrozenBalance();
//                    balanceCny += (coinContract.getBalance() + coinContract.getFrozenBalance()) * rate;
//                    frozenBalance += coinContract.getFrozenBalance();
//                    releaseBalance += coinContract.getBalance();
//                }
//                if (isShow) {
//                    tvAmount.setText(new DecimalFormat("#0.00").format(balanceUsd));
//                    tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(balanceCny, 2, null) + " CNY");
//
//                } else if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2) {
//                    tvAmount.setText("********");
//                    tvCnyAmount.setText("*****");
//                }
//                tvReleaseBalance.setText(new DecimalFormat("#0.00").format(releaseBalance));
//                tvFrozenBalance.setText(new BigDecimal(frozenBalance).setScale(2,BigDecimal.ROUND_DOWN).toPlainString());
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                WonderfulToastUtils.showToast(toastMessage);
//            }
//        });
//    }
//
//    private void getContractAsset() {
//        RemoteDataSource.getInstance().myContractWallet(getmActivity().getToken(), new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                coinContracts.clear();
//                coinContracts.addAll((List<CoinContract>) obj);
//
//                for (CoinContract coinContract : coinContracts) {
//                    balanceUsd += coinContract.getBalance() + coinContract.getFrozenBalance();
//                    balanceCny += (coinContract.getBalance() + coinContract.getFrozenBalance()) * rate;
//                    frozenBalance += coinContract.getFrozenBalance();
//                    releaseBalance += coinContract.getBalance();
//                }
//                if (isShow) {
//                    tvAmount.setText(new DecimalFormat("#0.00").format(balanceUsd));
//                    tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(balanceCny, 2, null) + " CNY");
//
//                } else if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2) {
//                    tvAmount.setText("********");
//                    tvCnyAmount.setText("*****");
//                }
//                tvReleaseBalance.setText(new DecimalFormat("#0.00").format(releaseBalance));
//                tvFrozenBalance.setText(new DecimalFormat("#0.00").format(frozenBalance));
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                WonderfulToastUtils.showToast(toastMessage);
//            }
//        });
//    }
//
//    private void getOptionsAsset() {
//        RemoteDataSource.getInstance().eryuanWalletWallet(getmActivity().getToken(), new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                coinContracts.clear();
//                coinContracts.addAll((List<CoinContract>) obj);
//
//                for (CoinContract coinContract : coinContracts) {
//                    balanceUsd += coinContract.getBalance() + coinContract.getFrozenBalance();
//                    balanceCny += (coinContract.getBalance() + coinContract.getFrozenBalance()) * rate;
//                    frozenBalance += coinContract.getFrozenBalance();
//                    releaseBalance += coinContract.getBalance();
//                }
//                if (isShow) {
//                    tvAmount.setText(new DecimalFormat("#0.00").format(balanceUsd));
//                    tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(balanceCny, 2, null) + " CNY");
//
//                } else if (SharedPreferenceInstance.getInstance().getMoneyShowType() == 2) {
//                    tvAmount.setText("********");
//                    tvCnyAmount.setText("*****");
//                }
//                tvReleaseBalance.setText(new DecimalFormat("#0.00").format(releaseBalance));
//                tvFrozenBalance.setText(new DecimalFormat("#0.00").format(frozenBalance));
//                tvPresentBalance.setText(new DecimalFormat("#0.00").format(frozenBalance));//TODO 赠金是哪个字段
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                WonderfulToastUtils.showToast(toastMessage);
//            }
//        });
//    }
//
//    @Override
//    protected void initImmersionBar() {
//        super.initImmersionBar();
//        if (!isSetTitle) {
//            ImmersionBar.setTitleBar(getActivity(), llTitle);
//            isSetTitle = true;
//        }
//    }
//
//    @OnClick(R.id.tv_asset_charge)
//    public void startCharge() {
//        ReportDialogFragment dialogFragment = ReportDialogFragment.getInstance();
//        dialogFragment.show(getActivity().getSupportFragmentManager(), "");
//        dialogFragment.setOperateCallback(new ReportDialogFragment.OperateCallback() {
//            @Override
//            public void toERC20() {
//                if (MyApplication.getApp().isLogin()) {
//                    RechargeActivity.actionStart(getActivity(),"USDT(ERC20)");
//                } else {
//                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
//                }
//            }
//
//            @Override
//            public void toOmni() {
//                if (MyApplication.getApp().isLogin()) {
//                    RechargeActivity.actionStart(getActivity(),"USDT(Omni)");
//                } else {
//                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
//                }
//            }
//        });
//    }
//
//    @OnClick(R.id.tabContract)
//    public void showwContract() {
//        tabFiat.setTextColor(getResources().getColor(R.color.primaryTextGray));
//        tabOptions.setTextColor(getResources().getColor(R.color.primaryTextGray));
//        tabContract.setTextColor(getResources().getColor(R.color.white));
//        tvCharge.setVisibility(View.GONE);
//        tvWithdraw.setVisibility(View.GONE);
//        tvTransfer.setVisibility(View.VISIBLE);
//        llPresent.setVisibility(View.GONE);
//        type = CONTRACT;
//        getData(0);
//    }
//
//    @OnClick(R.id.tabOptions)
//    public void showwOptions() {
//        tabContract.setTextColor(getResources().getColor(R.color.primaryTextGray));
//        tabFiat.setTextColor(getResources().getColor(R.color.primaryTextGray));
//        tabOptions.setTextColor(getResources().getColor(R.color.white));
//        tvCharge.setVisibility(View.GONE);
//        tvWithdraw.setVisibility(View.GONE);
//        tvTransfer.setVisibility(View.VISIBLE);
//        llPresent.setVisibility(View.VISIBLE);
//        type = OPTIONS;
//        getData(2);
//    }
//
//    @OnClick(R.id.tabFiat)
//    public void showFiat() {
//        tabFiat.setTextColor(getResources().getColor(R.color.white));
//        tabOptions.setTextColor(getResources().getColor(R.color.primaryTextGray));
//        tabContract.setTextColor(getResources().getColor(R.color.primaryTextGray));
//        tvCharge.setVisibility(View.VISIBLE);
//        tvWithdraw.setVisibility(View.VISIBLE);
//        tvTransfer.setVisibility(View.VISIBLE);
//        llPresent.setVisibility(View.GONE);
//        type = FIAT;
//        getData(1);
//    }
//
//    @OnClick(R.id.tv_asset_withdraw)
//    public void startWithdraw() {
//        if (MyApplication.getApp().isLogin()) {
//            ExtractActivity.actionStart(getActivity(), coin);
//        } else {
//            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
//        }
//    }
//
//    @OnClick(R.id.tv_asset_transfer)
//    public void startTransfer() {
//        if (MyApplication.getApp().isLogin()) {
//            AssetTransferActivity.actionStart(getActivity(), AssetTransferActivity.START_TRANSFER_TYPE_EXCHANGE, "USDT");
////            AssetTransferActivity.actionStart(getActivity(), AssetTransferActivity.START_TRANSFER_TYPE_OPTIONS, "USDT");
//        } else {
//            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
//        }
//    }
//
//    @OnClick(R.id.ivSetting)
//    public void startReport() {
//        ReportActivity.actionStart(getActivity(), "");
//
////        ReportDialogFragment dialogFragment = ReportDialogFragment.getInstance();
////        dialogFragment.show(getActivity().getSupportFragmentManager(), "");
////        dialogFragment.setOperateCallback(new ReportDialogFragment.OperateCallback() {
////            @Override
////            public void toERC20() {
////                if (MyApplication.getApp().isLogin()) {
////                    ReportActivity.actionStart(getActivity(), "USDT(ERC20)");
////                } else {
////                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
////                }
////            }
////
////            @Override
////            public void toOmni() {
////                if (MyApplication.getApp().isLogin()) {
////                    ReportActivity.actionStart(getActivity(), "USDT(Omni)");
////                } else {
////                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
////                }
////            }
////        });
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
////        getTotalAssets();
//        if (type.equals(CONTRACT)) {
//            getData(0);
//        } else if(type.equals(OPTIONS)){
//            getData(2);
//        }else if (type.equals(FIAT)) {
//            getData(1);
//        }
//        if (MyApplication.getApp().isLogin()) {
//            loginingViewText();
//        }
//    }
//
//    private void loginingViewText() {
//        User user = MyApplication.getApp().getCurrentUser();
//        String url = user.getAvatar();
//        if (WonderfulStringUtils.isEmpty(url))
//            Glide.with(getmActivity()).load(R.mipmap.icon_default_header).centerCrop().into(ivHeader);
//        else Glide.with(getmActivity()).load(url).centerCrop().into(ivHeader);
//    }
//
////    private void getTotalAssets() {
////        RemoteDataSource.getInstance().getTotalAssets(getmActivity().getToken(), new DataSource.DataCallback() {
////            @Override
////            public void onDataLoaded(Object obj) {
////                List<Double> list = (List<Double>) obj;
////                sumUsd = list.get(0);
////                sumCny = list.get(1);
////                if ("*****".equals(tvCnyAmount.getText())) {
////                    return;
////                }
////                tvAmount.setText(WonderfulStringUtils.getLongFloatString(sumUsd, 8));
////                tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(sumCny, 2, null) + "CNY");
////            }
////
////            @Override
////            public void onDataNotAvailable(Integer code, String toastMessage) {
////                LogUtil.i(toastMessage);
////            }
////        });
////    }
//}
