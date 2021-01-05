package com.bibi.ui.buy_or_sell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.OnClick;
import com.bibi.R;
import com.bibi.ui.login.LoginActivity;
import com.bibi.ui.my_order.MyOrderActivity;
import com.bibi.adapter.TextWatcher;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.C2C;
import com.bibi.entity.C2CExchangeInfo;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import com.bibi.app.Injection;

public class C2CBuyOrSellActivity extends BaseActivity implements C2CBuyOrSellContract.View, OrderConfirmDialogFragment.OperateCallback {

    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.llTitle)
    RelativeLayout llTitle;
    @BindView(R.id.ivHeader)
    ImageView ivHeader;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    //    @BindView(R.id.tvExchangeCount)
//    TextView tvExchangeCount;
//    @BindView(R.id.tvCommentPercent)
//    TextView tvCommentPercent;
//    @BindView(R.id.tvDoneHistory)
//    TextView tvDoneHistory;
//    @BindView(R.id.tvMessage)
//    TextView tvMessage;
    @BindView(R.id.tvLocalCoinText)
    TextView tvLocalCoinText;
    @BindView(R.id.etLocalCoin)
    EditText etLocalCoin;
    @BindView(R.id.tvOtherCoinText)
    TextView tvOtherCoinText;
    @BindView(R.id.etOtherCoin)
    EditText etOtherCoin;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvFee)
    TextView tvFee;
    //    @BindView(R.id.tvInfo)
//    TextView tvInfo;
//    @BindView(R.id.tvMyOrder)
//    TextView tvMyOrder;
    @BindView(R.id.ivZhifubao)
    ImageView ivZhifubao;
    @BindView(R.id.ivWeChat)
    ImageView ivWeChat;
    @BindView(R.id.ivUnionPay)
    ImageView ivUnionPay;
    @BindView(R.id.ivGuoji)
    ImageView ivGuoji;
    @BindView(R.id.tvRemainAmount)
    TextView tvRemainAmount;
    @BindView(R.id.tvTradeAmount)
    TextView tvTradeAmount;
    //    @BindView(R.id.view_back)
//    View view_back;
    /*@BindView(R.id.tvContact)
    TextView tvContact;*/
    private C2C.C2CBean c2cBean;
    private C2CExchangeInfo c2CExchangeInfo;
    private C2CBuyOrSellContract.Presenter presenter;
    private String mode = "0";
    private CountDownTimer timer;
    private Double rate = 0.0;
    private Double fee = 0.0;
    private String realPrice="0";
    private String realNumber="0";

    private TextWatcher localChangeWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            localCoinChange();
        }
    };
    private TextWatcher otherChangeWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            numberChange(s.toString());
            otherCoinChange();
        }
    };

    public static void actionStart(Context context, C2C.C2CBean c2CBean) {
        Intent intent = new Intent(context, C2CBuyOrSellActivity.class);
        intent.putExtra("c2cBean", c2CBean);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_c2_cbuy_new;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new C2CBuyOrSellPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        view_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        etLocalCoin.addTextChangedListener(localChangeWatcher);
        etOtherCoin.addTextChangedListener(otherChangeWatcher);
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });
//        tvMyOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MyOrderActivity.actionStart(C2CBuyOrSellActivity.this, 0);
//            }
//        });
    }

    private void showConfirmDialog() {
        WonderfulLogUtils.logi("C2CBuyOrSellActivity", "   mode    =   " + mode);
        if (!MyApplication.getApp().isLogin()) {
            startActivityForResult(new Intent(this, LoginActivity.class), LoginActivity.RETURN_LOGIN);
            return;
        }
        String countStr = etOtherCoin.getText().toString();
        if (WonderfulStringUtils.isEmpty(countStr)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.input_count));
            return;
        }
        double count = Double.parseDouble(countStr);
        String totalStr = etLocalCoin.getText().toString();
        if (WonderfulStringUtils.isEmpty(totalStr)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.input_count));
            return;
        }
        double total = Double.parseDouble(totalStr);
        OrderConfirmDialogFragment orderConfirmDialogFragment = OrderConfirmDialogFragment.getInstance(c2cBean.getAdvertiseType(), c2CExchangeInfo.getUnit(), c2CExchangeInfo.getPrice(), count, total);
        orderConfirmDialogFragment.show(getSupportFragmentManager(), "orderConfirm");
    }

    @Override
    public void buyOrSell() {
        if (c2cBean == null || c2CExchangeInfo == null) return;
        String id = c2cBean.getAdvertiseId() + "";
        String coinId = c2CExchangeInfo.getOtcCoinId() + "";
        String price = c2CExchangeInfo.getPrice() + "";
//        String money = etLocalCoin.getText().toString();
        String money = realPrice;
//        String amount = etOtherCoin.getText().toString();
        String amount = realNumber;
        String remark = "";
        if ("0".equals(c2cBean.getAdvertiseType()))
            //TODO 传递的price，应该是扣除手续费后的
//            presenter.c2cSell(getToken(), id, coinId, price, money, amount, remark, mode);
            presenter.c2cSell(getToken(), id, coinId, price, realPrice, realNumber, remark, mode);
        else presenter.c2cBuy(getToken(), id, coinId, price, money, amount, remark, mode);
    }

    private void otherCoinChange() {
        mode = "1";
        etLocalCoin.removeTextChangedListener(localChangeWatcher);
        String otherStr = etOtherCoin.getText().toString();
        if (WonderfulStringUtils.isEmpty(otherStr) || c2cBean.getPrice() == 0) {
            etLocalCoin.setText("0");
            realPrice="0";
            realNumber="0";
        }
        else {
            etLocalCoin.setText(WonderfulMathUtils.getRundNumber((Double.parseDouble(otherStr) - fee) * c2CExchangeInfo.getPrice(), 2, null));
            realPrice= String.valueOf(Double.parseDouble(otherStr) * c2CExchangeInfo.getPrice());
            realNumber=otherStr;
        }
        etLocalCoin.addTextChangedListener(localChangeWatcher);
    }

    private void localCoinChange() {
        if (c2CExchangeInfo == null) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.No_ads));
            presenter.c2cInfo(c2cBean.getAdvertiseId());
            return;
        }
        etOtherCoin.removeTextChangedListener(otherChangeWatcher);
        String localStr = etLocalCoin.getText().toString();
        mode = "0";
        if (WonderfulStringUtils.isEmpty(localStr) || c2cBean.getPrice() == 0) {
            etOtherCoin.setText("0");
            numberChange(etOtherCoin.getText().toString());
            realNumber="0";
            realPrice= "0";
        } else {
            etOtherCoin.setText(WonderfulMathUtils.getRundNumber(Double.parseDouble(localStr) / (c2CExchangeInfo.getPrice()*(1-rate)), 8, null));
            numberChange(etOtherCoin.getText().toString());
            realNumber= WonderfulMathUtils.getRundNumber(Double.parseDouble(localStr) / (c2CExchangeInfo.getPrice()*(1-rate)), 8, null);
            BigDecimal a1 =new BigDecimal(localStr);
            BigDecimal a2 =new BigDecimal(1-rate);
            Double dd = a1.divide(a2, 6, BigDecimal.ROUND_HALF_UP).doubleValue();
            realPrice= String.valueOf(dd);
        }
        etOtherCoin.addTextChangedListener(otherChangeWatcher);
    }

    @Override
    protected void obtainData() {
        c2cBean = (C2C.C2CBean) getIntent().getSerializableExtra("c2cBean");
    }

    @Override
    protected void fillWidget() {
        if ("0".equals(c2cBean.getAdvertiseType())) {
            tvTitle.setText(getResources().getString(R.string.text_chu_sho));
            tvFee.setVisibility(View.VISIBLE);
//            tvInfo.setText(getResources().getString(R.string.text_much_sale));
        } else {
            tvTitle.setText(getResources().getString(R.string.text_gou_mai));
            tvFee.setVisibility(View.INVISIBLE);
//            tvInfo.setText(getResources().getString(R.string.text_much_buy));
        }
        setButtonText();
    }

    private void setButtonText() {
        if ("0".equals(c2cBean.getAdvertiseType())) {
            if (MyApplication.getApp().isLogin()) {
                //tvContact.setVisibility(View.VISIBLE);
                tvBuy.setText(getResources().getString(R.string.text_chu_sho));
            } else {
                //tvContact.setVisibility(View.GONE);
                tvBuy.setText(getResources().getString(R.string.text_to_login));
            }
        } else {
            if (MyApplication.getApp().isLogin()) {
                //tvContact.setVisibility(View.VISIBLE);
                tvBuy.setText(getResources().getString(R.string.text_gou_mai));
            } else {
                //tvContact.setVisibility(View.GONE);
                tvBuy.setText(getResources().getString(R.string.text_to_login));
            }
        }

    }

    @Override
    protected void loadData() {
        presenter.c2cInfo(c2cBean.getAdvertiseId());
        if ("0".equals(c2cBean.getAdvertiseType())) {
            presenter.getFeeRate();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LoginActivity.RETURN_LOGIN:
                setButtonText();
                break;
            default:
        }
    }

    @Override
    public void setPresenter(C2CBuyOrSellContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void c2cInfoSuccess(C2CExchangeInfo obj) {
        if (obj == null) return;
        this.c2CExchangeInfo = obj;
        fillViews();
    }

    private void fillViews() {
        tvLimit.setText(c2CExchangeInfo.getMinLimit() + "~" + c2CExchangeInfo.getMaxLimit() + "CNY");
        tvOtherCoinText.setText(c2CExchangeInfo.getUnit());
        if (!WonderfulStringUtils.isEmpty(c2cBean.getAvatar()))
            Glide.with(this).load(c2cBean.getAvatar()).into(ivHeader);
        else Glide.with(this).load(R.mipmap.icon_default_header).into(ivHeader);
        String name = c2CExchangeInfo.getUsername();
        if (name.length() > 1) {
            name = name.substring(0, 1);
        }
        tvName.setText(name + "**");
        tvTradeAmount.setText("成交量： " + c2CExchangeInfo.getTransactions() + "");
//        item.getTransactions()
        List<String> pays = Arrays.asList(c2CExchangeInfo.getPayMode().split(","));
        if (pays.contains("支付宝")) ivZhifubao.setVisibility(View.VISIBLE);
        else ivZhifubao.setVisibility(View.GONE);
        if (pays.contains("微信")) ivWeChat.setVisibility(View.VISIBLE);
        else ivWeChat.setVisibility(View.GONE);
        if (pays.contains("银联") || (pays.contains("银行卡"))) ivUnionPay.setVisibility(View.VISIBLE);
        else ivUnionPay.setVisibility(View.GONE);
        if (pays.contains("SWIFT")) ivGuoji.setVisibility(View.VISIBLE);
        else ivGuoji.setVisibility(View.GONE);
        tvPrice.setText(c2CExchangeInfo.getPrice() + "CNY");
//        tvMessage.setText(c2CExchangeInfo.getRemark());
//        tvExchangeCount.setText(getResources().getString(R.string.text_deal_num) + c2CExchangeInfo.getTransactions());
//        tvRemainAmount.setText(getResources().getString(R.string.text_surplus_num)+new BigDecimal(c2CExchangeInfo.getMaxTradableAmount()));
        tvRemainAmount.setText(getResources().getString(R.string.text_surplus_num) + new BigDecimal(c2CExchangeInfo.getNumber()).setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
    }

    @Override
    public void c2cInfoFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void c2cBuySuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
        MyOrderActivity.actionStart(C2CBuyOrSellActivity.this, 0);
    }

    @Override
    public void c2cBuyFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void c2cSellSuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
        MyOrderActivity.actionStart(C2CBuyOrSellActivity.this, 0);
    }

    @Override
    public void c2cSellFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void getFeeSuccess(String obj) {
        rate = Double.valueOf(obj);
        if (!etOtherCoin.getText().toString().equals("")) {
            try {
                BigDecimal a1 = new BigDecimal(etOtherCoin.getText().toString());
                BigDecimal aa = new BigDecimal(obj);
                fee = a1.multiply(aa).doubleValue();
                tvFee.setText("交易手续费(" + rate * 100 + "%): " + new BigDecimal(fee).setScale(4, BigDecimal.ROUND_DOWN).toPlainString() + "USDT");
            } catch (Exception e) {
                tvFee.setText("交易手续费(" + rate * 100 + "%): ");
            }
        } else {
            tvFee.setText("交易手续费(" + rate * 100 + "%): ");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.tvAllNu, R.id.tvAllPrice})
    public void getAll() {
//        double number = c2CExchangeInfo.getNumber();
//        double maxLimit = c2CExchangeInfo.getMaxLimit();
//        double price = c2CExchangeInfo.getPrice();
//
//        BigDecimal a1 = new BigDecimal(number);
//        BigDecimal aa = new BigDecimal(String.valueOf(rate));
//        fee = a1.multiply(aa).doubleValue();
//
//        if ((number - fee) * price < maxLimit) {
//            double localNum = number - fee;
//            etLocalCoin.setText(localNum * price + "");
//        } else {
//            double localNum = number - fee;
//            etLocalCoin.setText(localNum * price + "");
//        }
    }

    private void numberChange(String orgNumber) {
        if (!orgNumber.equals("")) {
            try {
                BigDecimal a1 = new BigDecimal(orgNumber);
                BigDecimal aa = new BigDecimal(rate.toString());
                fee = a1.multiply(aa).doubleValue();
                tvFee.setText("交易手续费(" + rate * 100 + "%): " + new BigDecimal(fee).setScale(4, BigDecimal.ROUND_DOWN).toPlainString() + "USDT");
            } catch (Exception e) {
                tvFee.setText("交易手续费(" + rate * 100 + "%): ");
            }
        } else {
            tvFee.setText("交易手续费(" + rate * 100 + "%): ");
        }
    }
}