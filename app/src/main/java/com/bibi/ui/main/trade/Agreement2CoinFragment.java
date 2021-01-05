package com.bibi.ui.main.trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bibi.adapter.TextWatcher;
import com.bibi.entity.SimpleListBean;
import com.bibi.entity.SimpleListItem;
import com.bibi.ui.dialog.ListDialogFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bibi.R;
import com.bibi.adapter.Coin2CoinAdapter;
import com.bibi.adapter.SellAdapter;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseTransFragment;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.Currency;
import com.bibi.entity.Exchange;
import com.bibi.entity.Favorite;
import com.bibi.entity.HoldEntity;
import com.bibi.entity.PankouEntity;
import com.bibi.entity.TextItems;
import com.bibi.serivce.SocketMessage;
import com.bibi.serivce.SocketResponse;
import com.bibi.socket.ISocket;
import com.bibi.ui.coin.CoinActivity;
import com.bibi.ui.dialog.ModifyProfitDialogFragment;
import com.bibi.ui.entrust.TrustListActivity;
import com.bibi.ui.kline_spot.SKlineActivity;
import com.bibi.ui.login.LoginActivity;
import com.bibi.ui.main.MainActivity;
import com.bibi.ui.order.OrderActivity;
import com.bibi.utils.IMyTextChange;
import com.bibi.utils.LoadDialog;
import com.bibi.utils.PopupUtils;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.xw.repo.BubbleSeekBar;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.Unbinder;


/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/7/26
 */
public class Agreement2CoinFragment extends BaseTransFragment implements RadioGroup.OnCheckedChangeListener, Agreement2CoinContract.View, View.OnClickListener {
    public static final String TAG = Agreement2CoinFragment.class.getSimpleName();
    @BindView(R.id.ibOpen)
    ImageButton ibOpen;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tvMoreOrder)
    TextView tvMoreOrder;
    @BindView(R.id.ivKline)
    ImageView ivKline;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.ll_price_type_selector)
    LinearLayout ll_price_type_selector; // 交易类型下拉
    @BindView(R.id.ll_depth_type_selector)
    TextView ll_depth_type_selector; // 下拉
    @BindView(R.id.text_to_all)
    RelativeLayout mToAllLayout; // 全部
    @BindView(R.id.recyclerThree)
    RecyclerView mThreeRecycler; // 当前委托
    @BindView(R.id.mTvThree)
    TextView mTvThree;
    @BindView(R.id.mOneShi)
    TextView mOneShi;
    @BindView(R.id.mOneXian)
    LinearLayout mOneXian;
    @BindView(R.id.mOnePrice)
    EditText mOnePriceEdit;
    @BindView(R.id.mTwoPrice)
    EditText mTwoPriceEdit;
    @BindView(R.id.mOneAdd)
    TextView mOneAdd; // 买+
    @BindView(R.id.mOneSub)
    TextView mOneSub; // 买-
    @BindView(R.id.mTwoAdd)
    TextView mTwoAdd; // 卖+
    @BindView(R.id.mTwoSub)
    TextView mTwoSub; // 卖-
    @BindView(R.id.mTvPanJia)
    TextView mPanJia;
    @BindView(R.id.imgChg)
    ImageView imgChg;
    @BindView(R.id.mTvMoney)
    TextView mPanMoney;
    @BindView(R.id.mOneYuE)
    TextView mOneYuE;
    @BindView(R.id.mTwoYuE)
    TextView mTwoYuE;
    @BindView(R.id.tv_price_type)
    TextView tvPriceType;
    @BindView(R.id.mTwoLayout)
    LinearLayout mTwoLayout;    // 卖出
    @BindView(R.id.mOneLayout)
    LinearLayout mOneLayout;    // 买入
    @BindView(R.id.mTvOneBuy)
    TextView mOneBuy;
    @BindView(R.id.mTvTwoBuy)
    TextView mTwoBuy;
    @BindView(R.id.mOneTCP)
    EditText mOneTcpEdit;
    @BindView(R.id.mTwoTCP)
    EditText mTwoTcpEdit;
    @BindView(R.id.mOneDeal)
    TextView mOneDeal; // 交易额
    @BindView(R.id.mTwoDeal)
    TextView mTwoDeal; // 交易额
    @BindView(R.id.btn_toLogin)
    TextView btnLogin;
    @BindView(R.id.btnOneBuy)
    Button btnBuy;
    @BindView(R.id.btnTwoPost)
    Button btnSell;
    @BindView(R.id.mOneJiaoYi)
    LinearLayout mOneJiaoYi;
    Unbinder unbinder;
    @BindView(R.id.mTabOne)
    RadioButton mTabOne;
    @BindView(R.id.mTabTwo)
    RadioButton mTabTwo;
    @BindView(R.id.mOneSeekBar)
    BubbleSeekBar mOneSeekBar;
    @BindView(R.id.mTwoSeekBar)
    BubbleSeekBar mTwoSeekBar;
    @BindView(R.id.text_one_jieshu)
    TextView text_one_jieshu;
    @BindView(R.id.text_two_jieshu)
    TextView text_two_jieshu;
    @BindView(R.id.iv_switch_plate)
    ImageView ivSwitchPlate;
    @BindView(R.id.ll_current_trust)
    LinearLayout llCurrentTrust;
    @BindView(R.id.tv_current_trust)
    TextView tvCurrentTrust;
    @BindView(R.id.current_trust_underline)
    View currentTrustUnderline;
    @BindView(R.id.tvchange)
    TextView tvchange;
    @BindView(R.id.mOneTextType)
    TextView mOneTextType;
    @BindView(R.id.mTwoTextType)
    TextView mTwoTextType;
    @BindArray(R.array.switch_plate)
    String[] mSwitchPlate;

    int pageNo = 1;
    int pageSize = 6;
    int currentPage = 1;
    int historyPage = 1;
    int startPage = 1;
    private boolean isRefresh = false;
    private List<Currency> currencies = new ArrayList<>();
    private Currency mCurrency;
    private List<Exchange> mOne = new ArrayList<>();
    private List<Exchange> mTow = new ArrayList<>();
    private SellAdapter mOneAdapter; // 买入适配器
    private SellAdapter mTwoAdapter; // 卖出适配器
    public Agreement2CoinContract.Presenter mPresenter;
    private int oneAccuracy = 2; // 价格
    private int twoAccuracy = 2; // 数量
    private String oldSymbol; // 上个订阅的币种
    private Boolean isSeekBar = false; // 上个订阅的币种
    String buttonTextBuy = "";
    String buttonTextSell = "";
    private AssetEntity assetEntity;
    //做空/做多需要填写信息
    private String leverage = "1";
    private String price; // 价格
    private String amout; // 数量
    private String priceType = "LIMIT_PRICE"; //LIMIT_PRICE限价   MARKET_PRICE 市价
    private String direction = "BUY"; //BUY 做多，  SELL 做空
    private List<Currency> currencyMain = new ArrayList<>();
    private List<HoldEntity> mCurrentData = new ArrayList<>();
    private SimpleListBean coinListbean = new SimpleListBean();


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_agreement_2_coin;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvCurrentTrust.setSelected(true);
        mPresenter = new Agreement2CoinPresenter(Injection.provideTasksRepository(getActivity()), this);
        // 打开左侧的滑动
        ibOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).getDlRoot(MainActivity.MENU_TYPE_USER).openDrawer(Gravity.LEFT);
            }
        });
        // 全部订单
        tvMoreOrder.setOnClickListener(this);
        for (int i = 0; i < 15; i++) {
            mOne.add(new Exchange(15 - i, "--", "--"));
            mTow.add(new Exchange(i, "--", "--"));
        }
        ll_price_type_selector.setOnClickListener(this);
        ll_depth_type_selector.setOnClickListener(this);
        mToAllLayout.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        mOneAdd.setOnClickListener(this);
        mOneSub.setOnClickListener(this);
        mTwoAdd.setOnClickListener(this);
        mTwoSub.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
        btnSell.setOnClickListener(this);
        ivKline.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        llCurrentTrust.setOnClickListener(this);
        // 买入价格的变化
        mOnePriceEdit.addTextChangedListener(new IMyTextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if (mCurrency == null) {
                    return;
                }
                try {
                    mOneTextPrice = Double.valueOf(mOnePriceEdit.getText().toString());
                } catch (Exception e) {
                    mOneTextPrice = 0;
                }
                if (priceType.equals("LIMIT_PRICE") || priceType.equals("CHECK_FULL_STOP")) { // 限价或止盈
                    if ("CNY".equals(mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()))) {
                        mOneMyText = mOneText * mOneTextPrice;
                        mOneBuy.setText(String.valueOf("≈" + WonderfulMathUtils.getRundNumber(mOneTextPrice * 1,
                                2, null) + "CNY"));
                    } else {
                        mOneMyText = mOneText * mOneTextPrice * MainActivity.rate;
                        mOneBuy.setText(String.valueOf("≈" + WonderfulMathUtils.getRundNumber(mOneTextPrice * 1 * MainActivity.rate * (mCurrency.getBaseUsdRate() == null ? 0 : mCurrency.getBaseUsdRate()),
                                2, null) + "CNY"));
                    }
                    if (!TextUtils.isEmpty(mOneTcpEdit.getText())) {
                        if (priceType.equals("LIMIT_PRICE")) {
                            mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                                    + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                    mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")))));
                        } else if (priceType.equals("MARKET_PRICE")) {
                            mOneDeal.setText(mOneText + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                    mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/"))));
                        }
                    }
                }
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) {
                    return;
                }
                if (temp.length() - (posDot + 1) > oneAccuracy) {
                    editable.delete(posDot + 1 + oneAccuracy, posDot + 2 + oneAccuracy);
                }
            }
        });
        // 卖出价格的变化
        mTwoPriceEdit.addTextChangedListener(new IMyTextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if (mCurrency == null) {
                    return;
                }
                try {
                    mTwoTextPrice = Double.valueOf(mTwoPriceEdit.getText().toString());
                } catch (Exception e) {
                    mTwoTextPrice = 0;
                }
                if (priceType.equals("LIMIT_PRICE") || priceType.equals("CHECK_FULL_STOP")) { // 限价或止盈
                    if ("CNY".equals(mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()))) {
                        mTwoMyText = mTwoText * mTwoTextPrice;
                        mTwoBuy.setText(String.valueOf("≈" + WonderfulMathUtils.getRundNumber(mTwoTextPrice * 1,
                                2, null) + "CNY"));
                    } else {
                        mTwoMyText = mTwoText * mTwoTextPrice * MainActivity.rate;
                        mTwoBuy.setText(String.valueOf("≈" + WonderfulMathUtils.getRundNumber(mTwoTextPrice * 1 * MainActivity.rate * (mCurrency.getBaseUsdRate() == null ? 0 : mCurrency.getBaseUsdRate()),
                                2, null) + "CNY"));
                    }
                    if (!TextUtils.isEmpty(mTwoTcpEdit.getText())) {
                        if (priceType.equals("LIMIT_PRICE")) {
                            mTwoDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mTwoTextPrice * mTwoText, 1) / Double.parseDouble(leverage), 2, null)
                                    + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                    mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()))));
                        } else if (priceType.equals("MARKET_PRICE")) {
                            mTwoDeal.setText(mTwoText + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                    mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
                        }
                    }
                }
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) {
                    return;
                }
                if (temp.length() - (posDot + 1) > oneAccuracy) {
                    editable.delete(posDot + 1 + oneAccuracy, posDot + 2 + oneAccuracy);
                }
            }
        });
        //买入数量变化
        mOneTcpEdit.addTextChangedListener(new IMyTextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if (mCurrency == null) {
                    return;
                }
                try {
                    mOneText = Double.valueOf(mOneTcpEdit.getText().toString());
                } catch (Exception e) {
                    mOneText = 0;
                }
                if (priceType.equals("LIMIT_PRICE") || priceType.equals("CHECK_FULL_STOP")) { // 限价或止盈
                    if ("CNY".equals(mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()))) {
                        mOneMyText = mOneText * mOneTextPrice * (mCurrency.getBaseUsdRate() == null ? 0 : mCurrency.getBaseUsdRate());
                    } else {
                        mOneMyText = mOneText * mOneTextPrice * MainActivity.rate * (mCurrency.getBaseUsdRate() == null ? 0 : mCurrency.getBaseUsdRate());
                    }
                    if (mCurrency != null) {
                        if (priceType.equals("LIMIT_PRICE")) {
                            mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                                    + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                    mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")))));
                        } else if (priceType.equals("MARKET_PRICE")) {
                            mOneDeal.setText(mOneText + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                    mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/"))));
                        }
                    }
                } else {
                    if (priceType.equals("LIMIT_PRICE")) {
                        mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                                + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")))));
                    } else if (priceType.equals("MARKET_PRICE")) {
                        mOneDeal.setText(mOneText + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/"))));
                    }
                }
                String temp = editable.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) {
                    return;
                }
                if (temp.length() - (posDot + 1) > twoAccuracy) {
                    editable.delete(posDot + 1 + twoAccuracy, posDot + 2 + twoAccuracy);
                }
            }
        });
        //卖出数量变化
        mTwoTcpEdit.addTextChangedListener(new IMyTextChange() {
            @Override
            public void afterTextChanged(Editable editable) {
                super.afterTextChanged(editable);
                if (mCurrency == null) {
                    return;
                }
                try {
                    mTwoText = Double.valueOf(mTwoTcpEdit.getText().toString());
                } catch (Exception e) {
                    mTwoText = 0;
                }
                if (priceType.equals("LIMIT_PRICE") || priceType.equals("CHECK_FULL_STOP")) { // 限价或止盈
                    if ("CNY".equals(mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()))) {
                        mTwoMyText = mTwoText * mTwoTextPrice * (mCurrency.getBaseUsdRate() == null ? 0 : mCurrency.getBaseUsdRate());
                    } else {
                        mTwoMyText = mTwoText * mTwoTextPrice * MainActivity.rate * (mCurrency.getBaseUsdRate() == null ? 0 : mCurrency.getBaseUsdRate());
                    }
                    if (mCurrency != null) {
                        if (priceType.equals("LIMIT_PRICE")) {
                            mTwoDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mTwoTextPrice * mTwoText, 1) / Double.parseDouble(leverage), 2, null)
                                    + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                    mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()))));
                        } else if (priceType.equals("MARKET_PRICE")) {
                            mTwoDeal.setText(mTwoText + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                    mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
                        }
                    }
//                    if (!WonderfulStringUtils.isEmpty(mTwoTcpEdit.getText().toString())) {
//                        try {
//                            Double d = Double.valueOf(WonderfulMathUtils.getRundNumber(sellBalance, 5, null));
//                            Double d2 = Double.valueOf(mTwoTcpEdit.getText().toString());
//                            Float aFloat = Float.valueOf(WonderfulMathUtils.getRundNumber(d2 / d, 1, null)) * 100;
//                            if (aFloat < 0) {
//                                aFloat = 0f;
//                            }
//                            isSeekBar = false;
//                            mTwoSeekBar.setProgress(aFloat >= 100 ? 100 : aFloat);
//                            text_two_jieshu.setText(mTwoSeekBar.getProgress() + "%");
//                        } catch (Exception e) {
//
//                        }
//
//                    }
                }
            }
        });
        mPanJia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == 0) {
                    mOnePriceEdit.setText(mPanJia.getText());
                }
            }
        });
        ivSwitchPlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogFragment listDialogFragment = ListDialogFragment.getInstance(coinListbean);
                listDialogFragment.show(getmActivity().getSupportFragmentManager(), "bottom");
                listDialogFragment.setCallback(new ListDialogFragment.OperateCallback() {
                    @Override
                    public void ItemClick(int position) {
                        if (position == 0) {
                            recyclerSell.setVisibility(View.VISIBLE);
                            recyclerBuy.setVisibility(View.VISIBLE);
                        } else if (position == 1) {
                            recyclerSell.setVisibility(View.GONE);
                            recyclerBuy.setVisibility(View.VISIBLE);
                        } else if (position == 2) {
                            recyclerSell.setVisibility(View.VISIBLE);
                            recyclerBuy.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
        mOneSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                if (priceType.equals("LIMIT_PRICE")) {
                    if (mOnePriceEdit.getText().toString().equals("")) {
                        return;
                    }
                    Double price = Double.parseDouble(mOnePriceEdit.getText().toString());
                    if (price <= 0) {
                        return;
                    }
                    Double d = Double.valueOf(WonderfulMathUtils.getRundNumber(buyBalance / price, 5, null));
                    Double d2 = Double.valueOf(progress) / 100;
                    Float aFloat = Float.valueOf(WonderfulMathUtils.getRundNumber(d2 * d, 2, null));
                    if (aFloat < 0) {
                        aFloat = 0f;
                    }
                    mOneTcpEdit.setText(aFloat + "");
                    text_one_jieshu.setText(progress + "%");
                } else if (priceType.equals("MARKET_PRICE")) {
                    Double d = Double.valueOf(WonderfulMathUtils.getRundNumber(buyBalance, 5, null));
                    Double d2 = Double.valueOf(progress) / 100;
                    Float aFloat = Float.valueOf(WonderfulMathUtils.getRundNumber(d2 * d, 2, null));
                    if (aFloat < 0) {
                        aFloat = 0f;
                    }
                    mOneTcpEdit.setText(aFloat + "");
                    text_one_jieshu.setText(progress + "%");
                }
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }
        });
        mTwoSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                if (priceType.equals("LIMIT_PRICE")) {
                    Double d = Double.valueOf(WonderfulMathUtils.getRundNumber(sellBalance, 5, null));
                    Double d2 = Double.valueOf(progress) / 100;
                    Float aFloat = Float.valueOf(WonderfulMathUtils.getRundNumber(d2 * d, 2, null));
                    if (aFloat < 0) {
                        aFloat = 0f;
                    }
                    mTwoTcpEdit.setText(aFloat + "");
                    text_two_jieshu.setText(progress + "%");
                } else if (priceType.equals("MARKET_PRICE")) {
                    if (mPanJia.getText().toString().equals("") || mPanJia.getText().toString().equals("--")) {
                        return;
                    }
                    Double price = Double.parseDouble(mPanJia.getText().toString());
                    if (price <= 0) {
                        return;
                    }
                    Double d = Double.valueOf(WonderfulMathUtils.getRundNumber(sellBalance * price, 5, null));
                    Double d2 = Double.valueOf(progress) / 100;
                    Float aFloat = Float.valueOf(WonderfulMathUtils.getRundNumber(d2 * d, 2, null));
                    if (aFloat < 0) {
                        aFloat = 0f;
                    }
                    mTwoTcpEdit.setText(aFloat + "");
                    text_two_jieshu.setText(progress + "%");
                }
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }
        });
        mTabOne.setSelected(true);
        initRecyclerView();
        initSwitchPlate();
    }

    private Coin2CoinAdapter coin2CoinAdapter;

    private void initRecyclerView() {
        coin2CoinAdapter = new Coin2CoinAdapter(getActivity(), mCurrentData);
        mThreeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mThreeRecycler.setHasFixedSize(true);
        mThreeRecycler.setAdapter(coin2CoinAdapter);
        coin2CoinAdapter.setOnColseListener(new Coin2CoinAdapter.OnclickListenerColse() {
            @Override
            public void close(String orderId, int position) {
                mCurrentData.remove(position);
                coin2CoinAdapter.notifyDataSetChanged();
                mPresenter.cancle(SharedPreferenceInstance.getInstance().getTOKEN(), orderId);
            }
        });
    }

    private void initSwitchPlate() {
        for (int i = 0; i < mSwitchPlate.length; i++) {
            SimpleListItem item = new SimpleListItem();
            item.setContent(mSwitchPlate[i]);
            if (i == 0) {
                item.setSelected(true);
            }
            coinListbean.addItem(item);
        }
    }

    private boolean addFace(String symbol) {
        for (Favorite favorite : MainActivity.mFavorte) {
            if (symbol.equals(favorite.getSymbol())) {
                return true;
            }
        }
        return false;
    }


    private void setPriceNull() {
        mOnePriceEdit.setText(null);
        mOneTcpEdit.setText(null);
        mTwoPriceEdit.setText(null);
        mTwoTcpEdit.setText(null);
    }

    double mOneText, mTwoText, mOneTextPrice, mTwoTextPrice, mOneMyText, mTwoMyText, mOneTextPriceTouch, mTwoTextPriceTouch;

    @Override
    protected void obtainData() {
        recyclerSell.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerBuy.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOneAdapter = new SellAdapter(new ArrayList<Exchange>(), 0);
        mTwoAdapter = new SellAdapter(new ArrayList<Exchange>(), 1);
        recyclerSell.setAdapter(mOneAdapter);
        recyclerBuy.setAdapter(mTwoAdapter);
        mOneAdapter.setOnItemClickListener(listenerOne);
        mTwoAdapter.setOnItemClickListener(listenerTwo);
        // 获取盘口的信息
        getDefaultSymbol();
    }

    // 第一个Item的点击事件
    BaseQuickAdapter.OnItemClickListener listenerOne = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Exchange e = (Exchange) adapter.getData().get(position);
            if (priceType.equals("LIMIT_PRICE") || priceType.equals("CHECK_FULL_STOP")) { // 限价或止盈
                if (!"--".equals(e.getPriceStr())) {
                    mOnePriceEdit.setText(String.valueOf(WonderfulMathUtils.getRundNumber(Double.valueOf(e.getPriceStr()), oneAccuracy, null)));
                }
            }
        }
    };
    // 第二个Item的点击事件
    BaseQuickAdapter.OnItemClickListener listenerTwo = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Exchange e = (Exchange) adapter.getData().get(position);
            if (priceType.equals("LIMIT_PRICE") || priceType.equals("CHECK_FULL_STOP")) { // 限价或止盈
                if (!"--".equals(e.getPriceStr())) {
                    mOnePriceEdit.setText(String.valueOf(WonderfulMathUtils.getRundNumber(Double.valueOf(e.getPriceStr()), oneAccuracy, null)));
                }
            }
        }
    };

    private void showBottomModify(String orderId, String stopProfitPrice, String stopLossPrice) {
        ModifyProfitDialogFragment modifyProfitFragment = ModifyProfitDialogFragment.getInstance(orderId, stopProfitPrice, stopLossPrice);
        modifyProfitFragment.show(getActivity().getSupportFragmentManager(), "bottom");
        modifyProfitFragment.setCallback(new ModifyProfitDialogFragment.OperateCallback() {
            @Override
            public void cancleOrder(String orderId, String stopProfitPrice, String stopLossPrice) {
                //TODO 1.调用修改接口 2.刷新数据
//                mPresenter.getModifyProfit(SharedPreferenceInstance.getInstance().getTOKEN(), orderId, stopProfitPrice, stopLossPrice);
            }
        });
    }


    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }


    private double mul(double num1, double num2) {
        BigDecimal b1 = new BigDecimal(num1);
        BigDecimal b2 = new BigDecimal(num2);
        return b1.multiply(b2).doubleValue();
    }

    private JSONObject buildGetBodyJson(String symbol) {
        JSONObject obj = new JSONObject();
//        Log.i("miao","TCP："+symbol);
        try {
            obj.put("symbol", symbol);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    private JSONObject buildGetBodyJson(String symbol, int id) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("symbol", symbol);
            obj.put("uid", id);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 这里我开始订阅某个币盘口的信息
     */
    private void startTCP(String symbol, int id) {
        if (id == 0) {
            // 不需要iD
            EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.SUBSCRIBE_EXCHANGE_TRADE,
                    buildGetBodyJson(symbol).toString().getBytes()));
//            Log.i("miao","startTCP："+symbol);
        } else {
            // 需要ID
            EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.SUBSCRIBE_EXCHANGE_TRADE,
                    buildGetBodyJson(symbol, id).toString().getBytes()));
//            Log.i("miao","startTCPid："+symbol);
        }
        oldSymbol = symbol;
    }

    /**
     * 这里我取消某个币盘口的信息
     */
    private void stop(String symbol, int id) {
        if (id == 0) {
            EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.UNSUBSCRIBE_EXCHANGE_TRADE,
                    buildGetBodyJson(symbol).toString().getBytes()));
        } else {
            EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.UNSUBSCRIBE_EXCHANGE_TRADE,
                    buildGetBodyJson(symbol, id).toString().getBytes()));
        }

    }

    @Override
    protected String getmTag() {
        return TAG;
    }


    public void resetSymbol(Currency currency, int position) {
        Log.e("AgreementFragment:", "resetSymbol");
        this.mCurrency = currency;
        if (mRadioGroup != null) {
            if (position == 0) { // 买入
                direction = "BUY";
                mRadioGroup.check(R.id.mTabOne);
            } else { //卖出
                direction = "SELL";
                mRadioGroup.check(R.id.mTabTwo);
            }
            resetSymbol(currency);
        }
    }

    /**
     * 这个是从主界面来的，表示选择了某个币种
     */
    public void resetSymbol(Currency currency) {
        this.mCurrency = currency;
        if (this.mCurrency != null) {
            buyBalance = -1;
            sellBalance = -1;
            setCurrentSelected();
            mOneDeal.setText("--");
            setPriceNull();
            String format2 = new DecimalFormat("#0.00000000").format(currency.getClose());
            BigDecimal bg2 = new BigDecimal(format2);
            String v2 = bg2.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
            mOnePriceEdit.setText(v2);
            mTwoPriceEdit.setText(v2);
            // 获取盘口信息
            mPresenter.getExchange(this.mCurrency.getSymbol());
            // 获取当前币种精确度
            mPresenter.getSymbolInfo(this.mCurrency.getSymbol());
            // 如果是登录状态
            if (!WonderfulStringUtils.isEmpty(SharedPreferenceInstance.getInstance().getTOKEN())) {
                //盈利损失统计
                getAsset();
                // 获取该币的委托信息
                currentPage = startPage;
                historyPage = startPage;
                //获取当前的持仓
                mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), 1, 10, this.mCurrency.getSymbol());
            }
            //设置单位
            if (priceType.equals("LIMIT_PRICE")) {
                mOneTextType.setText(this.mCurrency.getSymbol().split("/")[0]);
                mTwoTextType.setText(this.mCurrency.getSymbol().split("/")[0]);
            } else {
                mOneTextType.setText(this.mCurrency.getSymbol().split("/")[1]);
                mTwoTextType.setText(this.mCurrency.getSymbol().split("/")[1]);
            }
            // 修改标题
            tvTitle.setText(this.mCurrency.getSymbol());
            buttonTextBuy = String.valueOf(getResources().getString(R.string.text_buy));
            buttonTextSell = String.valueOf(getResources().getString(R.string.text_sale));
            //更新最新价格从盘口socket里拿
            String format = new DecimalFormat("#0.00000000").format(currency.getClose());
            BigDecimal bg = new BigDecimal(format);
            String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
            mPanJia.setText(String.valueOf(v));
            setCurrentPrice(v);
            tvchange.setText((mCurrency.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(mCurrency.getChg() * 100, 2, "########0.") + "%");
            tvchange.setEnabled(mCurrency.getChg() >= 0);
            imgChg.setBackgroundResource(mCurrency.getChg() >= 0 ? R.drawable.icon_rise : R.drawable.icon_fall);
            mPanJia.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
            if ("CNY".equals(currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length()))) {
                mPanMoney.setText(String.valueOf("≈" + WonderfulMathUtils.getRundNumber(currency.getClose() * 1 * (mCurrency.getBaseUsdRate() == null ? 0 : mCurrency.getBaseUsdRate()),
                        4, null) + "CNY"));
            } else {
                mPanMoney.setText(String.valueOf("≈" + WonderfulMathUtils.getRundNumber(currency.getClose() * MainActivity.rate * (mCurrency.getBaseUsdRate() == null ? 0 : mCurrency.getBaseUsdRate()),
                        4, null) + "CNY"));
            }

            // 代表选择了哪个币种，需要重新订阅 应该先取消原来的再订阅现在的
            if (!TextUtils.isEmpty(oldSymbol)) {
                stop(oldSymbol, getmActivity().getId());
            }
            startTCP(currency.getSymbol(), getmActivity().getId());
        }
    }

    /**
     * 这个是刚开始
     */
    public void setViewContent(List<Currency> currencies) {
        this.currencies.clear();
        this.currencies.addAll(currencies);
        mPresenter.getDefaultSymbol();
    }

    private boolean isFirstLoad = true;

    public void getDefaultSymbol() {
        if (isFirstLoad) {
            mPresenter.getDefaultSymbol();
            isFirstLoad = false;
        }
    }

    boolean firstLoad = true;
    String symbolJson;

    @Override
    public void onResume() {
        super.onResume();
        if (firstLoad) {
            if (!TextUtils.isEmpty(oldSymbol)) {
                stop(oldSymbol, getmActivity().getId());
            }
            resetSymbol(mCurrency);
            firstLoad = false;
        }
        if (MyApplication.getApp().isLogin()) {
            if (mCurrency != null) {
                getAsset();
                WonderfulLogUtils.logd("jiejie", "获取委托");
                mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, 20, this.mCurrency.getSymbol());
            }
            btnLogin.setVisibility(View.GONE);
            btnBuy.setVisibility(View.VISIBLE);
            btnSell.setVisibility(View.VISIBLE);
            mThreeRecycler.setVisibility(View.VISIBLE);
            mTvThree.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnBuy.setVisibility(View.GONE);
            btnSell.setVisibility(View.GONE);
//            mThreeRecycler.setVisibility(View.GONE);
            mTvThree.setVisibility(View.GONE);
        }
        if (this.mCurrency != null) {
            try {
                symbolJson = new JSONObject().put("symbol", this.mCurrency.getSymbol()).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.SUBSCRIBE_EXCHANGE_TRADE, symbolJson.getBytes()));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        hideDialog();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    public void tcpNotify() {
        if (mCurrency != null) {
            tvchange.setText((mCurrency.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(mCurrency.getChg() * 100, 2, "########0.") + "%");
            tvchange.setEnabled(mCurrency.getChg() >= 0);
            imgChg.setBackgroundResource(mCurrency.getChg() >= 0 ? R.drawable.icon_rise : R.drawable.icon_fall);
            mPanJia.setTextColor(mCurrency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
            getAsset();
//            mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), 1, 10, this.mCurrency.getSymbol());
        }
    }

    private int check = 0;

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.mTabOne: // 点击买入
                check = 0;
                direction = "BUY";
                mOneLayout.setVisibility(View.VISIBLE);
                mTwoLayout.setVisibility(View.GONE);
                mTabOne.setSelected(true);
                mTabTwo.setSelected(false);
                if (mCurrency != null) {
                    if (priceType.equals("LIMIT_PRICE")) {
                        mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                                + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")))));
                    } else if (priceType.equals("MARKET_PRICE")) {
                        mOneDeal.setText(mOneText + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/"))));
                    }
                    getAsset();
                }
                break;
            case R.id.mTabTwo: // 点击卖出
                check = 1;
                direction = "SELL";
                mOneLayout.setVisibility(View.GONE);
                mTwoLayout.setVisibility(View.VISIBLE);
                mTabOne.setSelected(false);
                mTabTwo.setSelected(true);
                mTwoPriceEdit.setText(mPanJia.getText());
                if (mCurrency != null) {
                    if (priceType.equals("LIMIT_PRICE")) {
                        mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                                + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")))));
                    } else if (priceType.equals("MARKET_PRICE")) {
                        mOneDeal.setText(mOneText + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/"))));
                    }
                    getAsset();
                }
                break;
            default:
        }
    }

    void getAsset() {
        String[] str = this.mCurrency.getSymbol().split("/");
        if (str.length > 1) {
            if (direction.equals("BUY")) {
                mPresenter.getAsset(SharedPreferenceInstance.getInstance().getTOKEN(), str[1]);
            } else if (direction.equals("SELL")) {
                mPresenter.getAsset(SharedPreferenceInstance.getInstance().getTOKEN(), str[0]);
            }
        }
    }


//    @Override
//    public void allCurrencySuccess(Object obj) {
//        try {
//            JsonObject object = new JsonParser().parse((String) obj).getAsJsonObject();
//            JsonArray array1 = object.getAsJsonArray("changeRank").getAsJsonArray();
//            currencyMain = gson.fromJson(array1, new TypeToken<List<Currency>>() {
//            }.getType());
//            trustCurrentAdapter.setCurrencies(currencyMain);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //TODO
//        mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), 1, 10, this.mCurrency.getSymbol(), "HOLD");
//    }


    private List<Exchange> getPlateList(List<Exchange> list) {
        WonderfulLogUtils.logi("盘口11", list.size() + "");
        List<Exchange> result = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if (i < list.size()) {
                result.add(list.get(i));
            } else {
                result.add(new Exchange(i, "--", "--"));
            }
        }
        return result;

    }


    private List<Exchange> getDisplayPlateList(List<Exchange> list, int plateType, boolean reverse) {
        List<Exchange> result = new ArrayList<>();
        int size = 0;
        switch (plateType) {
            case PLATE_DEFAULT:
                size = 5;
                break;
            case PLATE_BUY:
                size = 10;
                break;
            case PLATE_SELL:
                size = 15;
                break;
            default:
        }
        for (int i = 0; i < size; i++) {
            result.add(list.get(i));
        }

        if (reverse) {
            Collections.reverse(result);
        }
        return result;
    }


    static int TYPE_CURRENT = 1;
    static int TYPE_HISTORY = 2;

    public void setListData(List<HoldEntity> list) {
        if (list != null && list.size() > 0) {
            WonderfulLogUtils.logd("jiejie", "当前委托条数" + list.size());
            mThreeRecycler.setVisibility(View.VISIBLE);
            mTvThree.setVisibility(View.GONE);

            mCurrentData.clear();
            mCurrentData.addAll(list);
            WonderfulLogUtils.logd("jiejie", "当前委托条数" + isRefresh);
            coin2CoinAdapter.notifyDataSetChanged();
//            if (isRefresh) {
//                coin2CoinAdapter.notifyDataSetChanged();
//            } else {
//                for (int i = 0; i < mCurrentData.size(); i++) {
//                    coin2CoinAdapter.notifyItemChanged(i);
//                }
//                isRefresh = false;
//            }
        } else {
            mCurrentData.clear();
            coin2CoinAdapter.notifyDataSetChanged();
//            mThreeRecycler.setVisibility(View.GONE);
            mTvThree.setVisibility(View.VISIBLE);
        }
    }

//    /**
//     * 取消某个委托成功
//     */
//    @Override
//    public void getCancelSuccess(String success) {
//        if (MyApplication.getApp().isLogin() && this.mCurrency != null) {
//            // 重新获取当前订单
//            currentPage = startPage;
//            historyPage = startPage;
//            mPresenter.getCurrentOrder(SharedPreferenceInstance.getInstance().getTOKEN(), currentPage, pageSize, this.mCurrency.getSymbol(), "", "", "", "");
//            // 应该还需要刷新下钱包的接口
//            mPresenter.getWallet(1, SharedPreferenceInstance.getInstance().getTOKEN(), this.mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")));
//            mPresenter.getWallet(2, SharedPreferenceInstance.getInstance().getTOKEN(), mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()));
//        }
//    }


//    @Override
//    public void getCloseSuccess(String success) {
//        if (MyApplication.getApp().isLogin() && this.mCurrency != null) {
//            //平仓成功，用于adapter刷新
//            isRefresh = true;
//            // 重新获取当前订单
//            currentPage = startPage;
//            historyPage = startPage;
//            mPresenter.allCurrency();
////            mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), currentPage, pageSize, this.mCurrency.getSymbol(), "HOLD");
//
//            // 重新刷新下盘口信息  也许是不用的
//            mPresenter.getExchange(mCurrency.getSymbol());
//            Log.i("getExchange", "5" + mCurrency.getSymbol());
//            // 应该还需要刷新下钱包的接口
//            mPresenter.getWallet(1, SharedPreferenceInstance.getInstance().getTOKEN(), this.mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")));
//            mPresenter.getWallet(2, SharedPreferenceInstance.getInstance().getTOKEN(), mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()));
//        }
//    }

    private double buyBalance = -1;
    private double sellBalance = -1;

    private void toLoginOrCenter() {
        startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_depth_type_selector:
                final LinkedList<String> linkedList = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.text_type)));
                PopupUtils.showListBottom(getActivity(), linkedList, view, view.getMeasuredWidth(), new PopupUtils.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int i) {
                        if (i == 0) { // 深度 5
                            currentPlate = PLATE_DEFAULT;
                            mOneAdapter.setList(getDisplayPlateList(mOne, currentPlate, true));
                            mTwoAdapter.setList(getDisplayPlateList(mTow, currentPlate, false));
                            mOneAdapter.notifyDataSetChanged();
                            mTwoAdapter.notifyDataSetChanged();
                            ll_depth_type_selector.setText(getResources().getString(R.string.depth));
                        } else if (i == 1) { // 深度 10
                            currentPlate = PLATE_BUY;
                            mOneAdapter.setList(getDisplayPlateList(mOne, currentPlate, true));
                            mTwoAdapter.setList(getDisplayPlateList(mTow, currentPlate, false));
                            mOneAdapter.notifyDataSetChanged();
                            mTwoAdapter.notifyDataSetChanged();
                            ll_depth_type_selector.setText(getResources().getString(R.string.depth_10));
                        } else if (i == 2) { // 深度 15
                            currentPlate = PLATE_SELL;
                            mOneAdapter.setList(getDisplayPlateList(mOne, currentPlate, true));
                            mTwoAdapter.setList(getDisplayPlateList(mTow, currentPlate, false));
                            mOneAdapter.notifyDataSetChanged();
                            mTwoAdapter.notifyDataSetChanged();
                            ll_depth_type_selector.setText(getResources().getString(R.string.depth_15));
                        }
                    }
                });
                break;
            case R.id.ll_price_type_selector:
                //TODO 交易类型
                final LinkedList<String> tradedList = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.text_trade_type)));
                PopupUtils.showListBottom(getActivity(), tradedList, view, view.getMeasuredWidth(), new PopupUtils.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int i) {
                        tvPriceType.setText(tradedList.get(i));
                        if (i == 0) { // 限价
                            priceType = "LIMIT_PRICE";
                            mOnePriceEdit.setFocusable(true);
                            mOnePriceEdit.setFocusableInTouchMode(true);
                            mOnePriceEdit.setEnabled(true);
                            mOneTcpEdit.setHint(getResources().getString(R.string.text_number));
                            mTwoPriceEdit.setFocusable(true);
                            mTwoPriceEdit.setFocusableInTouchMode(true);
                            mTwoPriceEdit.setEnabled(true);
                            mTwoTcpEdit.setHint(getResources().getString(R.string.text_number));
                            if (!TextUtils.isEmpty(mOneTcpEdit.getText())) {
                                if (priceType.equals("LIMIT_PRICE")) {
                                    mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                                            + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                            mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")))));
                                } else if (priceType.equals("MARKET_PRICE")) {
                                    mOneDeal.setText(mOneText + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                            mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/"))));
                                }
                            }
                            if (!TextUtils.isEmpty(mTwoTcpEdit.getText())) {
                                if (priceType.equals("LIMIT_PRICE")) {
                                    mTwoDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mTwoTextPrice * mTwoText, 1) / Double.parseDouble(leverage), 2, null)
                                            + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                            mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()))));
                                } else if (priceType.equals("MARKET_PRICE")) {
                                    mTwoDeal.setText(mTwoText + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                            mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
                                }
                            }
                        } else if (i == 1) { // 市价委托
                            priceType = "MARKET_PRICE";
                            mOnePriceEdit.setFocusable(false);
                            mOnePriceEdit.setFocusableInTouchMode(false);
                            mOnePriceEdit.setEnabled(false);
                            mOneTcpEdit.setHint(getResources().getString(R.string.text_turnover));
                            mTwoPriceEdit.setFocusable(false);
                            mTwoPriceEdit.setFocusableInTouchMode(false);
                            mTwoPriceEdit.setEnabled(false);
                            mTwoTcpEdit.setHint(getResources().getString(R.string.text_turnover));
                            if (!TextUtils.isEmpty(mOneTcpEdit.getText())) {
                                if (priceType.equals("LIMIT_PRICE")) {
                                    mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                                            + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                            mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")))));
                                } else if (priceType.equals("MARKET_PRICE")) {
                                    mOneDeal.setText(mOneText + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                            mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/"))));
                                }
                            }
                            if (!TextUtils.isEmpty(mTwoTcpEdit.getText())) {
                                if (priceType.equals("LIMIT_PRICE")) {
                                    mTwoDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mTwoTextPrice * mTwoText, 1) / Double.parseDouble(leverage), 2, null)
                                            + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                            mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()))));
                                } else if (priceType.equals("MARKET_PRICE")) {
                                    mTwoDeal.setText(mTwoText + (direction.equals("BUY") ? mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()) :
                                            mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
                                }
                            }
                            try {
                                String format2 = new DecimalFormat("#0.00000000").format(Double.parseDouble(mPanJia.getText().toString()));
                                BigDecimal bg2 = new BigDecimal(format2);
                                String v2 = bg2.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                                mTwoPriceEdit.setText(v2);
                                mOnePriceEdit.setText(v2);
                            } catch (Exception e) {
                            }
                        } else if (i == 2) { // 止盈止损

                        }
                        if (priceType.equals("LIMIT_PRICE")) {
                            mOneTextType.setText(mCurrency.getSymbol().split("/")[0]);
                            mTwoTextType.setText(mCurrency.getSymbol().split("/")[0]);
                        } else {
                            mOneTextType.setText(mCurrency.getSymbol().split("/")[1]);
                            mTwoTextType.setText(mCurrency.getSymbol().split("/")[1]);
                        }
                    }
                });
                break;
            case R.id.tvMoreOrder:
                if (MyApplication.getApp().isLogin()) {
                    CoinActivity.actionStart(getActivity(), this.mCurrency.getSymbol(), assetEntity);
                } else {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
                }
                break;
            case R.id.ivKline:
                if (this.mCurrency != null) {
                    SKlineActivity.actionStart(getActivity(), this.mCurrency.getSymbol());
                }
                break;
            case R.id.btn_toLogin: // 点击登录
                toLoginOrCenter();
                break;
            case R.id.mOneAdd: // +买入 价格
                try {
                    double num = mOnePriceEdit.getText().toString().equals("") ? 0.0 : Double.valueOf(mOnePriceEdit.getText().toString());
                    mOnePriceEdit.setText(String.valueOf(num + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.mOneSub: // -买入 价格
                try {
                    double num1 = Double.valueOf(mOnePriceEdit.getText().toString());
                    if ((num1 - 1) >= 0) {
                        mOnePriceEdit.setText(String.valueOf(num1 - 1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.mTwoAdd: // +卖出 价格
                try {
                    double num = mTwoPriceEdit.getText().toString().equals("") ? 0.0 : Double.valueOf(mTwoPriceEdit.getText().toString());
                    mTwoPriceEdit.setText(String.valueOf(num + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.mTwoSub: // -卖出 价格
                try {
                    double num1 = Double.valueOf(mTwoPriceEdit.getText().toString());
                    if ((num1 - 1) >= 0) {
                        mTwoPriceEdit.setText(String.valueOf(num1 - 1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.text_to_all: // 查看更多委托
                if (MyApplication.getApp().isLogin()) {
                    TrustListActivity.show(getActivity(), false);
                } else {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
                }
                break;
            case R.id.btnOneBuy: // 买入
                addBuyOrder();
                break;
            case R.id.btnTwoPost: // 卖出
                addSellOrder();
                break;
            default:
        }
    }

    boolean isCurrent = true;

    private void setCurrentSelected() {
        if (MyApplication.getApp().isLogin()) {
            mTvThree.setVisibility(View.GONE);
            isCurrent = true;
            tvCurrentTrust.setSelected(true);
            currentTrustUnderline.setVisibility(View.VISIBLE);
            currentPage = startPage;
            if (mCurrency == null) {
                return;
            }
            mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, 20, this.mCurrency.getSymbol());
        } else {
            mOneYuE.setText("0.0");
            mTwoYuE.setText("0.0");
            SharedPreferenceInstance.getInstance().saveaToken("");
            SharedPreferenceInstance.getInstance().saveTOKEN("");
        }

    }


    private void addBuyOrder() {
        if (mCurrency == null) {
            return;
        }
        if (!MyApplication.getApp().isLogin()) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
            return;
        }
        price = mOnePriceEdit.getText().toString();
        amout = mOneTcpEdit.getText().toString();
        if (WonderfulStringUtils.isEmpty(price, amout)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.Incomplete_information));
            return;
        }
        mPresenter.getAddOrder(SharedPreferenceInstance.getInstance().getTOKEN(), mCurrency.getSymbol(), price, amout, direction, priceType, "0");
    }


    private void addSellOrder() {
        if (mCurrency == null) {
            return;
        }
        if (!MyApplication.getApp().isLogin()) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
            return;
        }
        price = mTwoPriceEdit.getText().toString();
        amout = mTwoTcpEdit.getText().toString();
        if (WonderfulStringUtils.isEmpty(price, amout)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.Incomplete_information));
            return;
        }
        mPresenter.getAddOrder(SharedPreferenceInstance.getInstance().getTOKEN(), mCurrency.getSymbol(), price, amout, direction, priceType, "0");
    }

    /**
     * 盘口信息的返回 和 当前委托的返回
     *
     * @param response SocketResponse
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(SocketResponse response) {
        Log.i("推送过来的信息： Agreement", response.getCmd().toString());
        if (response.getCmd() == null) {
            return;
        }
        switch (response.getCmd()) {
            case PUSH_EXCHANGE_SPOT_TRADE:
                try {
                    setResponse(response.getResponse());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PUSH_EXCHANGE_ORDER_TRADE:
                if (MyApplication.getApp().isLogin() && this.mCurrency != null) {
                    WonderfulLogUtils.logi("推送过来的信息", "当前委托改为当前持仓");
                    currentPage = startPage;
                    historyPage = startPage;
                    //TODO 当前委托改为当前持仓
//                    mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), 1, 10, this.mCurrency.getSymbol());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 更新盘口买卖数据列表
     */
    private void setResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return;
        }
        PankouEntity entity = new Gson().fromJson(response, PankouEntity.class);
        String symbol = mCurrency.getSymbol();
        if (!symbol.equals(entity.getSymbol())) {
            return;
        }

        TextItems items = entity.getSellTradePlate();
        this.mOne.clear();
        this.mOne.addAll(getPlateList(items.getItems()));
        mOneAdapter.setList(getDisplayPlateList(mOne, currentPlate, true));
        mOneAdapter.notifyDataSetChanged();

        TextItems items2 = entity.getBuyTradePlate();
        this.mTow.clear();
        this.mTow.addAll(getPlateList(items2.getItems()));
        mTwoAdapter.setList(getDisplayPlateList(mTow, currentPlate, false));
        mTwoAdapter.notifyDataSetChanged();

        try {
            String format = new DecimalFormat("#0.00000000").format(entity.getPrice());
            BigDecimal bg = new BigDecimal(format);
            String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
            WonderfulLogUtils.logd("Coin2CoinView:", v);
            mPanJia.setText(String.valueOf(v));
            String format2 = new DecimalFormat("#0.00000000").format(entity.getCny());
            BigDecimal bg2 = new BigDecimal(format2);
            String v2 = bg2.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
            mPanMoney.setText("≈" + v2 + "CNY");
            setCurrentPrice(v);
        } catch (Exception e) {

        }
    }

    public void setCurrentPrice(String price) {
        if (coin2CoinAdapter != null) {
            Map<String, String> priceMap = new HashMap<>();
            priceMap.put(this.mCurrency.getSymbol(), price);
            coin2CoinAdapter.setPriceMap(priceMap);
        }
    }

    @Override
    public void setPresenter(Agreement2CoinContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @BindView(R.id.recyclerSell)
    RecyclerView recyclerSell; // 卖出的
    @BindView(R.id.recyclerBuy)
    RecyclerView recyclerBuy; //买进

    final int PLATE_DEFAULT = 0;
    final int PLATE_BUY = 1;
    final int PLATE_SELL = 2;

    int currentPlate = PLATE_SELL;

    @Override
    public void errorMes(int e, String msg) {
        if (e == 4000) {
            MyApplication.getApp().setCurrentUser(null);
            if (!MyApplication.getApp().isLogin()) {
                btnLogin.setVisibility(View.VISIBLE);
                btnBuy.setVisibility(View.GONE);
                btnSell.setVisibility(View.GONE);
//                mThreeRecycler.setVisibility(View.GONE);
                mTvThree.setVisibility(View.GONE);
                SharedPreferenceInstance.getInstance().saveaToken("");
                SharedPreferenceInstance.getInstance().saveTOKEN("");

            } else {
                btnLogin.setVisibility(View.GONE);
                btnBuy.setVisibility(View.VISIBLE);
                btnSell.setVisibility(View.VISIBLE);
                mThreeRecycler.setVisibility(View.VISIBLE);
                mTvThree.setVisibility(View.VISIBLE);
                if (MyApplication.getApp().isLogin() && this.mCurrency != null) {
                    currentPage = startPage;
                    historyPage = startPage;
                    mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, 20, this.mCurrency.getSymbol());
                }
            }
        } else {
            WonderfulCodeUtils.checkedErrorCode(getmActivity(), e, msg);
        }
    }

    @Override
    public void getDataLoad(int code, String message) {
        WonderfulToastUtils.showToast(getResources().getString(R.string.success));
        if (code == 0) {
            mOneTcpEdit.setText(null);
            // 提交委托成功
            if (MyApplication.getApp().isLogin() && this.mCurrency != null) {
                //平仓成功，用于adapter刷新
                isRefresh = true;
                // 重新获取当前订单
                currentPage = startPage;
                historyPage = startPage;
                mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, 20, this.mCurrency.getSymbol());
                // 重新刷新下盘口信息  也许是不用的
                mPresenter.getExchange(mCurrency.getSymbol());
                getAsset();
                Log.i("getExchange", "5" + mCurrency.getSymbol());
                // 应该还需要刷新下钱包的接口
            }
        }
    }

    /**
     * 盘口的数据
     */
    @Override
    public void getSuccess(List<Exchange> ask, List<Exchange> bid) {
        this.mOne.clear();
        this.mTow.clear();
        this.mOne.addAll(getPlateList(ask));
        mOneAdapter.setList(getDisplayPlateList(mOne, currentPlate, true));
        recyclerSell.scrollToPosition(mOneAdapter.getItemCount() - 1);
        mOneAdapter.notifyDataSetChanged();
        this.mTow.addAll(getPlateList(bid));
        mTwoAdapter.setList(getDisplayPlateList(mTow, currentPlate, false));
        mTwoAdapter.notifyDataSetChanged();
    }

    @Override
    public void getAssetUSDTSuccess(AssetEntity assetEntity) {
        String[] str = this.mCurrency.getSymbol().split("/");
        if (str.length > 1) {
            if (direction.equals("BUY")) {
                buyBalance = assetEntity.getBalance();
                mOneYuE.setText(new DecimalFormat("#0.00").format(assetEntity.getBalance()) + str[1]);
            } else if (direction.equals("SELL")) {
                sellBalance = assetEntity.getBalance();
                mTwoYuE.setText(new DecimalFormat("#0.00").format(assetEntity.getBalance()) + str[0]);
            }
        }
        this.assetEntity = assetEntity;
    }


    @Override
    public void setDefaultSymbol(String symbol) {
        if (mCurrency == null) {
            for (Currency currency : currencies) {
                if (currency.getSymbol().equals(symbol)) {
                    this.mCurrency = currency;
                    break;
                }
            }
        }

        if (mCurrency == null) {
            return;
        }
        if (tvTitle != null && TextUtils.isEmpty(tvTitle.getText()) && mCurrency != null) {
            resetSymbol(mCurrency);
        }
    }

    @Override
    public void getAccuracy(final int one, final int two) {
        twoAccuracy = one; // 数量
        oneAccuracy = two; // 价格
        if (mOneAdapter == null || mTwoAdapter == null) {
            return;
        }
        mOneAdapter.setText(new SellAdapter.myText() {
            @Override
            public int one() {
                return one;
            }

            @Override
            public int two() {
                return two;
            }
        });
        mOneAdapter.notifyDataSetChanged();
        mTwoAdapter.setText(new SellAdapter.myText() {
            @Override
            public int one() {
                return one;
            }

            @Override
            public int two() {
                return two;
            }
        });
        mTwoAdapter.notifyDataSetChanged();
    }

    @Override
    public void getHoldSuccess(List<HoldEntity> holdEntityList) {
        btnLogin.setVisibility(View.GONE);
        btnBuy.setVisibility(View.VISIBLE);
        btnSell.setVisibility(View.VISIBLE);
        if (holdEntityList == null) {
            return;
        }
        setListData(holdEntityList);
        WonderfulLogUtils.logd("jiejie", "--当前持仓啊--" + holdEntityList.size());
    }

    @Override
    public void getHoldFial(int e, String meg) {

    }

    @Override
    public void getCancleSuccess(String success) {
        WonderfulToastUtils.showToast("撤销成功");
        mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, 20, this.mCurrency.getSymbol());
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }
}
