package com.bibi.ui.releaseAd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import com.bibi.R;
import com.bibi.entity.SimpleListBean;
import com.bibi.entity.SimpleListItem;
import com.bibi.ui.country.CountryActivity;
import com.bibi.ui.dialog.ListDialogFragment;
import com.bibi.ui.dialog.ModifyProfitDialogFragment;
import com.bibi.ui.my_ads.AdsActivity;
import com.bibi.adapter.DialogCoinInfoAdapter;
import com.bibi.adapter.DialogTimeLimitAdapter;
import com.bibi.adapter.PayWayAdapter;
import com.bibi.adapter.TextWatcher;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.Ads;
import com.bibi.entity.CoinInfo;
import com.bibi.entity.Country;
import com.bibi.entity.PayWay;
import com.bibi.entity.TimeLimitBean;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import com.bibi.app.Injection;

public class ReleaseAdsActivity extends BaseActivity implements ReleaseAdContract.View {
    public static final int REQUEST_COUNTRY = 0;
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibHelp)
    ImageButton ibHelp;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tvCoin)
    TextView tvCoin;
    @BindView(R.id.llCoin)
    LinearLayout llCoin;
    @BindView(R.id.llCountry)
    LinearLayout llCountry;
    @BindView(R.id.llPayWay)
    LinearLayout llPayWay;
    @BindView(R.id.tvCountry)
    TextView tvCountry;
    @BindView(R.id.tvCoinKind)
    TextView tvCoinKind;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvOverflow)
    EditText tvOverflow;
    @BindView(R.id.etMin)
    EditText etMin;
    @BindView(R.id.etMax)
    EditText etMax;
    @BindView(R.id.tvCountText)
    TextView tvCountText;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.etCount)
    EditText etCount;
    @BindView(R.id.tvPayWayText)
    TextView tvPayWayText;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.tvPayTime)
    TextView tvPayTime;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.tvRelease)
    TextView tvRelease;
    @BindView(R.id.llTime)
    LinearLayout llTime;
    @BindView(R.id.line0)
    View line0;
    @BindView(R.id.rbFixed)
    RadioButton rbFixed;
    @BindView(R.id.rbChange)
    RadioButton rbChange;
    @BindView(R.id.rgPriceType)
    RadioGroup rgPriceType;
    @BindView(R.id.rbYes)
    RadioButton rbYes;
    @BindView(R.id.rbNo)
    RadioButton rbNo;
    @BindView(R.id.rgReply)
    RadioGroup rgReply;
    @BindView(R.id.etReplyContent)
    EditText etReplyContent;
    @BindView(R.id.etjyPrice)
    EditText etjyPrice;
    private String advertiseType = "";
    private CoinInfo coinInfo;
    private double minSellCount;
    private double minBuyCount;
    private View coinInfoView;
    private View timeLimitView;
    private List<CoinInfo> coinInfos = new ArrayList<>();
    @BindView(R.id.view_back)
    View view_back;

    private View payWayView;
    private List<PayWay> payWays = new ArrayList<PayWay>();

    private ReleaseAdContract.Presenter presenter;

    private RecyclerView rvCoinInfo;
    private DialogCoinInfoAdapter adapter;

    private RecyclerView rvpayWay;
    private PayWayAdapter payWayAdapter;
    private AlertDialog payWayDialog;
    private AlertDialog dialog;
    private AlertDialog timedialog;

    private Country country;
    private Ads ads;
    private RecyclerView rvTimeList;
    private DialogTimeLimitAdapter timeadapter;
    private List<TimeLimitBean> timeLimit = new ArrayList<>();
    private String[] list;
    private Double rate = 0.0;

    public static void actionStart(Context context, String type, Ads ads) {
        Intent intent = new Intent(context, ReleaseAdsActivity.class);
        intent.putExtra("type", type);
        if (ads != null) intent.putExtra("ads", ads);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_release_ads;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new ReleasePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        payWays.add(new PayWay(getString(R.string.alipay)));
        payWays.add(new PayWay(getString(R.string.weChat)));
        payWays.add(new PayWay(getString(R.string.unionPay)));
        payWays.add(new PayWay(getString(R.string.swift)));
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCoinInfoDialog();
            }
        });
        llCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReleaseAdsActivity.this.startActivityForResult(new Intent(ReleaseAdsActivity.this, CountryActivity.class), REQUEST_COUNTRY);
            }
        });
        llPayWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayWayDialog();
            }
        });
        tvRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseOrEditAd();
            }
        });
        rgPriceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                priceTypeChange(checkedId);
            }
        });
        rgReply.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                replyChanged(checkedId);
            }
        });
        tvOverflow.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                overFlowTextChange();
            }
        });
        etMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                minChange();
            }
        });
        etMax.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                maxChange();
            }
        });
        etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                numberChange(s.toString());
            }
        });
        list = getResources().getStringArray(R.array.timeLimit);
        WonderfulLogUtils.logi("ReleaseAdsActivity", "  list.length   " + list.length);
        for (int i = 0; i < list.length; i++) {
            TimeLimitBean limitBean = new TimeLimitBean();
            limitBean.setTime(list[i]);
            //limitBean.setSelected(false);
            timeLimit.add(limitBean);
        }

        tvPayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeLimitDialog();
            }
        });
        fix();
        etReplyContent.setEnabled(false);
    }

    private void maxChange() {
        String maxLimit = etMax.getText().toString();
        String price = etjyPrice.getText().toString();
        String number = etCount.getText().toString();
        if (!WonderfulStringUtils.isEmpty(maxLimit, price, number)) {
            BigDecimal a1 = new BigDecimal(number);
            BigDecimal aa = new BigDecimal(rate.toString());
            Double dd = a1.multiply(aa).doubleValue();
            //TODO
            Double max = 0.0;
            if (advertiseType.equals("BUY")) {
                max = Double.valueOf(price) * Double.valueOf(number);
            } else if (advertiseType.equals("SELL")) {
                max = Double.valueOf(price) * (Double.valueOf(number) - dd);
            }

            if (Double.valueOf(maxLimit) > max) {
                WonderfulToastUtils.showToast(getResources().getString(R.string.maxLimitTip) + max + "CNY");
            }
        }
    }

    private void minChange() {
        String minLimit = etMin.getText().toString();
        if (!WonderfulStringUtils.isEmpty(minLimit)) {
            if (Double.valueOf(minLimit) < 100) {
                WonderfulToastUtils.showToast(getResources().getString(R.string.minLimitTip));
            }
        }

    }

    private void numberChange(String orgNumber) {
        if (!orgNumber.equals("")) {
            try {
                BigDecimal a1 = new BigDecimal(orgNumber);
                BigDecimal aa = new BigDecimal(rate.toString());
                Double dd = a1.multiply(aa).doubleValue();
                tvFee.setText("交易手续费(" + rate * 100 + "%): " + dd + "USDT");
            } catch (Exception e) {
                tvFee.setText("交易手续费(" + rate * 100 + "%): ");
            }
        } else {
            tvFee.setText("交易手续费(" + rate * 100 + "%): ");
        }
    }

    private void showTimeLimitDialog() {
        if (timeLimitView == null) {
//            timeLimitView = getLayoutInflater().inflate(R.layout.dialog_timelimit_view, null);
//            rvTimeList = timeLimitView.findViewById(R.id.rvTimeList);
//            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//            rvTimeList.setLayoutManager(manager);
//            WonderfulLogUtils.logi("ReleaseAdsActivity", "  timeLimit.size()   " + timeLimit.size());
//            timeadapter = new DialogTimeLimitAdapter(R.layout.adapter_dialog_coininfo, timeLimit);
//            timeadapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    timeLimitItemClick(position);
//                }
//            });
//            rvTimeList.setAdapter(timeadapter);
//            timedialog = new AlertDialog.Builder(this, R.style.custom_dialog).setTitle(getResources().getString(R.string.text_ad_fu_time)).setView(timeLimitView)
//                    .setPositiveButton(getResources().getString(R.string.dialog_sure), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            timeConfirm();
//                        }
//                    }).setNegativeButton(getResources().getString(R.string.dialog_one_cancel), null).create();
//        }
//        timedialog.show();

            SimpleListBean bean = new SimpleListBean();
            for (TimeLimitBean timeLimitBean : timeLimit) {
                SimpleListItem item = new SimpleListItem();
                item.setContent(timeLimitBean.getTime());
                item.setSelected(timeLimitBean.isSelected());
                bean.addItem(item);
            }
            ListDialogFragment listDialogFragment = ListDialogFragment.getInstance(bean);
            listDialogFragment.show(getSupportFragmentManager(), "bottom");
            listDialogFragment.setCallback(new ListDialogFragment.OperateCallback() {
                @Override
                public void ItemClick(int position) {
                    timeLimitItemClick(position);
                    timeConfirm();
                }
            });
        }
    }

    private void timeConfirm() {
        for (TimeLimitBean limitBean : timeLimit) {
            if (limitBean.isSelected()) {
                tvPayTime.setText(limitBean.getTime());
                //WonderfulToastUtils.showToast(tvPayTime.getText().toString().split(" ")[0]);
                break;
            }
        }
    }

    private void timeLimitItemClick(int position) {
        for (int i = 0, len = timeLimit.size(); i < len; i++) {
            timeLimit.get(i).setSelected(false);
        }
        timeLimit.get(position).setSelected(true);

//        timeadapter.notifyDataSetChanged();
    }


    private void replyChanged(int checkedId) {
        switch (checkedId) {
            case R.id.rbYes:
                etReplyContent.setEnabled(true);
                break;
            case R.id.rbNo:
                etReplyContent.setEnabled(false);
                break;
            default:
        }
    }

    private void overFlowTextChange() {
        String priceStr = tvPrice.getText().toString();
        String overStr = tvOverflow.getText().toString();
        if (WonderfulStringUtils.isEmpty(priceStr, overStr)) return;
        double price = Double.parseDouble(priceStr);
        double over = Double.parseDouble(overStr);
        double finalPrice = price * (1 + 0.01 * over);
        etjyPrice.setText(WonderfulMathUtils.getRundNumber(finalPrice, 2, null));
    }

    private void priceTypeChange(int checkedId) {
        switch (checkedId) {
            case R.id.rbFixed:
                fix();
                break;
            case R.id.rbChange:
                change();
                break;
            default:
        }
    }

    private void change() {
        tvOverflow.setEnabled(true);
        tvOverflow.setHint(getResources().getString(R.string.text_ad_yi_two));
        etjyPrice.setText("");
        etjyPrice.setHint(getResources().getString(R.string.text_ad_jiao_two));
        etjyPrice.setEnabled(false);
    }

    private void fix() {
        tvOverflow.setText("");
        tvOverflow.setHint(getResources().getString(R.string.text_ad_yi_one));
        tvOverflow.setEnabled(false);
        etjyPrice.setText("");
        etjyPrice.setHint(getResources().getString(R.string.text_ad_jiao_one));
        etjyPrice.setEnabled(true);
    }

    private void releaseOrEditAd() {
        String price = etjyPrice.getText().toString();
        String coinId = "";
        if (coinInfo == null && ads == null) return;
        if (coinInfo != null) coinId = coinInfo.getId();
        else coinId = ads.getCoinId() + "";
        String minLimit = etMin.getText().toString();
        String maxLimit = etMax.getText().toString();
        String timeLimit = tvPayTime.getText().toString().split(" ")[0];
        String countryZhName = country.getZhName();
        String priceType = rbFixed.isChecked() ? "0" : "1";
        String premiseRate = "0";
        if (priceType.equals("1")) premiseRate = tvOverflow.getText().toString();
        String remark = etMessage.getText().toString();
        String number = etCount.getText().toString();
        String pay = tvPayWay.getText().toString();
        String jyPassword = etPassword.getText().toString();
        String auto = rbYes.isChecked() ? "1" : "0";
        String autoword = etReplyContent.getText().toString();
        if (WonderfulStringUtils.isEmpty(price, coinId, minLimit, maxLimit, timeLimit, countryZhName,
                priceType, premiseRate, number, pay, jyPassword)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_ad_entity));
//            WonderfulToastUtils.showToast("advertiseType=" + advertiseType + "  auto=" + auto + "  autoword=" + autoword);
            return;
        }

        if (Double.valueOf(maxLimit) < 100) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.minLimitTip));
            return;
        }

        Double max = Double.valueOf(price) * Double.valueOf(number);
        if (Double.valueOf(maxLimit) > max) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.maxLimitTip) + max + "CNY");
            return;
        }
        if (ads == null) {
            WonderfulLogUtils.logi("releaseOrEditAd", "timeLimit   " + timeLimit);
            presenter.create(getToken(), price, advertiseType, coinId, minLimit, maxLimit, Integer.valueOf(timeLimit), countryZhName
                    , priceType, premiseRate, remark, number, pay, jyPassword, auto, autoword);
        } else
            presenter.updateAd(getToken(), ads.getId(), price, advertiseType, coinId, minLimit, maxLimit, Integer.valueOf(timeLimit), countryZhName
                    , priceType, premiseRate, remark, number, pay, jyPassword, auto, autoword);
    }

    private void showPayWayDialog() {
        if (payWayView == null) {
//            payWayView = getLayoutInflater().inflate(R.layout.dialog_coins_view, null);
//            rvpayWay = payWayView.findViewById(R.id.rvCoinList);
//            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//            rvpayWay.setLayoutManager(manager);
//            payWayAdapter = new PayWayAdapter(R.layout.adapter_dialog_coininfo, payWays);
//            payWayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    payWayItemClick(position);
//                }
//            });
//            rvpayWay.setAdapter(payWayAdapter);
//            payWayDialog = new AlertDialog.Builder(this, R.style.custom_dialog).setTitle(getResources().getString(R.string.text_ad_fu_kuan)).setView(payWayView)
//                    .setPositiveButton(getResources().getString(R.string.dialog_sure), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            payWayConfirm();
//                        }
//                    }).setNegativeButton(getResources().getString(R.string.dialog_one_cancel), null).create();
//        }
//        payWayDialog.show();

            SimpleListBean bean = new SimpleListBean();
            for (PayWay payWay : payWays) {
                SimpleListItem item = new SimpleListItem();
                item.setContent(payWay.getName());
                item.setSelected(payWay.isSelect());
                bean.addItem(item);
            }
            ListDialogFragment listDialogFragment = ListDialogFragment.getInstance(bean);
            listDialogFragment.show(getSupportFragmentManager(), "bottom");
            listDialogFragment.setCallback(new ListDialogFragment.OperateCallback() {
                @Override
                public void ItemClick(int position) {
                    payWayItemClick(position);
                    payWayConfirm();
                }
            });
        }
    }

    private void payWayConfirm() {
        String content = "";
        for (PayWay payWay : payWays) {
            if (payWay.isSelect()) content = content + "," + payWay.getName();
        }
        if (WonderfulStringUtils.isEmpty(content)) tvPayWay.setText("");
        else
            tvPayWay.setText(content.length() > 1 ? content.substring(content.indexOf(",") + 1) : content);
    }

    private void payWayItemClick(int position) {
        payWays.get(position).setSelect(!payWays.get(position).isSelect());
//        payWayAdapter.notifyDataSetChanged();
    }

    private void showCoinInfoDialog() {
        if (coinInfoView == null) {
//            coinInfoView = getLayoutInflater().inflate(R.layout.dialog_coins_view, null);
//            rvCoinInfo = coinInfoView.findViewById(R.id.rvCoinList);
//            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//            rvCoinInfo.setLayoutManager(manager);
//            adapter = new DialogCoinInfoAdapter(R.layout.adapter_dialog_coininfo, coinInfos);
//            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    coinInfoItemClick(position);
//                }
//            });
//            rvCoinInfo.setAdapter(adapter);
//            dialog = new AlertDialog.Builder(this, R.style.custom_dialog).setTitle(getResources().getString(R.string.text_ad_bi)).setView(coinInfoView)
//                    .setPositiveButton(getResources().getString(R.string.dialog_sure), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            coinInfoConfirm();
//                        }
//                    }).setNegativeButton(getResources().getString(R.string.dialog_one_cancel), null).create();
//        dialog.show();

            SimpleListBean bean = new SimpleListBean();
            for (CoinInfo coinInfo : coinInfos) {
                SimpleListItem item = new SimpleListItem();
                item.setContent(coinInfo.getUnit());
                item.setSelected(coinInfo.isSelected());
                bean.addItem(item);
            }
            ListDialogFragment listDialogFragment = ListDialogFragment.getInstance(bean);
            listDialogFragment.show(getSupportFragmentManager(), "bottom");
            listDialogFragment.setCallback(new ListDialogFragment.OperateCallback() {
                @Override
                public void ItemClick(int position) {
                    coinInfoItemClick(position);
                    coinInfoConfirm();
                }
            });
//            listDialogFragment.setCallback(new ModifyProfitDialogFragment.OperateCallback() {
//                @Override
//                public void cancleOrder(String orderId, String stopProfitPrice, String stopLossPrice) {
//                    //TODO 1.调用修改接口 2.刷新数据
//                    mPresenter.getModifyProfit(SharedPreferenceInstance.getInstance().getTOKEN(), orderId, stopProfitPrice, stopLossPrice);
//                }
//            });
        }
    }

    private void coinInfoItemClick(int position) {
        for (int i = 0, len = coinInfos.size(); i < len; i++) {
            coinInfos.get(i).setSelected(false);
        }
        coinInfos.get(position).setSelected(true);
//        adapter.notifyDataSetChanged();
    }

    private void coinInfoConfirm() {
        for (CoinInfo coinInfo : coinInfos) {
            if (coinInfo.isSelected()) {
                this.coinInfo = coinInfo;
                tvCoin.setText(coinInfo.getUnit());
                tvPrice.setText(coinInfo.getMarketPrice());
                break;
            }
        }
    }

    @Override
    protected void obtainData() {
        advertiseType = getIntent().getStringExtra("type");
        ads = (Ads) getIntent().getSerializableExtra("ads");
    }

    @Override
    protected void fillWidget() {
        if (advertiseType.equals("BUY")) {
            tvTitle.setText(getResources().getString(R.string.text_ad_title));
            tvCountText.setText(getResources().getString(R.string.text_ad_buy_num));
            tvFee.setVisibility(View.GONE);
        } else if (advertiseType.equals("SELL")) {
            tvTitle.setText(getResources().getString(R.string.text_ad_titles));
            tvCountText.setText(getResources().getString(R.string.text_ad_sell_num));
            tvFee.setVisibility(View.VISIBLE);
        }
        if (ads == null) {
            tvRelease.setText(getResources().getString(R.string.text_fa_bu));
        } else {
            tvTitle.setText(getResources().getString(R.string.text_ad_change));
            tvRelease.setText(getResources().getString(R.string.text_change));
        }
    }

    private void fillViews(Ads ads) {
        tvCoin.setText(ads.getCoinUnit());
        country = ads.getCountry();
        tvCountry.setText(country.getZhName());
        tvCoinKind.setText(country.getLocalCurrency());
        tvPrice.setText(ads.getMarketPrice() + "");
        rbFixed.setChecked(ads.getPriceType() == 0);
        rbChange.setChecked(ads.getPriceType() != 0);
        tvOverflow.setText(ads.getPremiseRate() + "");
        etjyPrice.setText(ads.getPrice() + "");
        etMin.setText(ads.getMinLimit() + "");
        etMax.setText(ads.getMaxLimit() + "");
        etCount.setText(ads.getNumber() + "");
        tvPayWay.setText(ads.getPayMode());
        tvPayTime.setText(ads.getTimeLimit() + " " + getResources().getString(R.string.text_ad_minu));
        rbYes.setChecked(ads.getAuto() == 0);
        rbNo.setChecked(ads.getAuto() != 0);
        etReplyContent.setText(WonderfulStringUtils.isEmpty(ads.getAutoword()) ? "" : ads.getAutoword());
        etMessage.setText(WonderfulStringUtils.isEmpty(ads.getRemark()) ? "" : ads.getRemark());
    }

    @Override
    protected void loadData() {
        presenter.all();
        presenter.getFeeRate();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_COUNTRY) {
            if (resultCode == RESULT_OK) {
                Country country = (Country) data.getSerializableExtra("country");
                this.country = country;
                tvCountry.setText(country.getZhName());
                tvCoinKind.setText(country.getLocalCurrency());
            }
        }
    }

    @Override
    public void setPresenter(ReleaseAdContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void allSuccess(List<CoinInfo> obj) {
        if (obj == null) return;
        this.coinInfos.clear();
        this.coinInfos.addAll(obj);
        if (ads != null) presenter.adDetail(getToken(), ads.getId());
    }

    @Override
    public void allFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void createSuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
        AdsActivity.actionStart(ReleaseAdsActivity.this, MyApplication.getApp().getCurrentUser().getUsername(), MyApplication.getApp().getCurrentUser().getAvatar());
    }

    @Override
    public void createFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void adDetailSuccess(Ads obj) {
        if (obj == null) return;
        this.ads = obj;
        fillViews(ads);
    }

    @Override
    public void adDetailFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void getFeeSuccess(String obj) {
        rate = Double.valueOf(obj);
        if (!etCount.getText().toString().equals("")) {
            try {
//                Double number= Double.valueOf(etCount.getText().toString());
                BigDecimal a1 = new BigDecimal(etCount.getText().toString());
                BigDecimal aa = new BigDecimal(obj);
                Double dd = a1.multiply(aa).doubleValue();
                tvFee.setText("交易手续费(" + rate * 100 + "%): " + dd);
            } catch (Exception e) {
                tvFee.setText("交易手续费(" + rate * 100 + "%): ");
            }
        } else {
            tvFee.setText("交易手续费(" + rate * 100 + "%): ");
        }
    }

    @Override
    public void updateSuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void updateFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    protected void onDestroy() {
        if (payWayDialog != null && payWayDialog.isShowing()) {
            payWayDialog.dismiss();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (timedialog != null && timedialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }
}
