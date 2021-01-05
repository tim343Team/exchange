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

import com.bibi.ui.kline_spot.SKlineActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import butterknife.BindViews;
import butterknife.OnClick;

import com.bibi.R;
import com.bibi.app.Injection;
import com.bibi.customview.BottomSelectionFragment;
import com.bibi.data.DataSource;
import com.bibi.data.RemoteDataSource;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.HoldEntity;
import com.bibi.entity.PankouEntity;
import com.bibi.ui.asset_transfer.AssetTransferActivity;
import com.bibi.ui.common.ChooseCoinActivity;
import com.bibi.ui.dialog.ModifyProfitDialogFragment;
import com.bibi.ui.entrust.TrustListActivity;
import com.bibi.ui.main.MainActivity;
import com.bibi.ui.kline.KlineActivity;
import com.bibi.ui.login.LoginActivity;
import com.bibi.adapter.SellAdapter;
import com.bibi.adapter.TrustCurrentAdapter;
import com.bibi.adapter.TrustHistoryAdapter;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseTransFragment;
import com.bibi.ui.dialog.BBConfirmDialogFragment;
import com.bibi.ui.dialog.EntrustOperateDialogFragment;
import com.bibi.entity.Currency;
import com.bibi.entity.EntrustHistory;
import com.bibi.entity.Exchange;
import com.bibi.entity.Favorite;
import com.bibi.entity.Money;
import com.bibi.entity.TextItems;
import com.bibi.app.UrlFactory;
import com.bibi.socket.ISocket;
import com.bibi.ui.order.OrderActivity;
import com.bibi.ui.order.OrdersFragment;
import com.bibi.utils.PopupUtils;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.serivce.SocketMessage;
import com.bibi.serivce.SocketResponse;
import com.bibi.utils.IMyTextChange;
import com.bibi.utils.LoadDialog;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.xw.repo.BubbleSeekBar;

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
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import okhttp3.Request;


public class Coin2CoinFragment extends BaseTransFragment implements RadioGroup.OnCheckedChangeListener, Coin2CoinContract.View, View.OnClickListener {
    public static final String TAG = Coin2CoinFragment.class.getSimpleName();
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvMoreOrder)
    TextView tvMoreOrder;
    @BindView(R.id.ivKline)
    ImageView ivKline;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.mRadioGroupPrice)
    RadioGroup mRadioGroupPrice;
    @BindView(R.id.ll_risk)
    LinearLayout llRisk; // 风险率
    @BindView(R.id.ll_price_type_selector)
    LinearLayout ll_price_type_selector; // 下拉
    @BindView(R.id.text_to_all)
    RelativeLayout mToAllLayout; // 全部
    @BindView(R.id.recyclerThree)
    RecyclerView mThreeRecycler; // 当前委托
    @BindView(R.id.mTvThree)
    TextView mTvThree;
    @BindView(R.id.mOneLayout)
    LinearLayout mOneLayout; // 买入
    @BindView(R.id.mOneShi)
    TextView mOneShi;
    @BindView(R.id.mOneXian)
    LinearLayout mOneXian;
    @BindView(R.id.mOnePrice)
    EditText mOnePriceEdit;
    @BindView(R.id.mOneAdd)
    RelativeLayout mOneAdd; // +
    @BindView(R.id.mOneSub)
    RelativeLayout mOneSub; // -
    @BindView(R.id.mTvPanJia)
    TextView mPanJia;
    @BindView(R.id.imgChg)
    ImageView imgChg;
    @BindView(R.id.mTvMoney)
    TextView mPanMoney;
    @BindView(R.id.mOneYuE)
    TextView mOneYuE;
    @BindView(R.id.tvMarginMoney)
    TextView tvMarginMoney;
    @BindView(R.id.tv_risk_rate)
    TextView tvRiskRate;
    @BindView(R.id.tv_depth_type)
    TextView tvDepthType;
    @BindView(R.id.mProfitPrice)
    EditText mProfitPrice;
    @BindView(R.id.mLoosePrice)
    EditText mLoosePrice;
    @BindView(R.id.mTvOneBuy)
    TextView mOneBuy;
    @BindView(R.id.mOneTCP)
    EditText mOneTcpEdit;
    @BindView(R.id.mOneDeal)
    TextView mOneDeal; // 交易额
    @BindView(R.id.btn_toLogin)
    TextView btnLogin;
    @BindView(R.id.btnOneBuy)
    Button btnBuy;
    @BindView(R.id.mOneJiaoYi)
    LinearLayout mOneJiaoYi;
    Unbinder unbinder;
    @BindView(R.id.ivCompare)
    ImageView ivCompare;
    @BindView(R.id.mTabOne)
    RadioButton mTabOne;
    @BindView(R.id.mTabTwo)
    RadioButton mTabTwo;
    @BindView(R.id.mTabLimitPrice)
    RadioButton mTabLimitPrice;
    @BindView(R.id.mTabMarketPrice)
    RadioButton mTabMarketPrice;
    @BindView(R.id.ll_current_trust)
    LinearLayout llCurrentTrust;
    @BindView(R.id.tv_current_trust)
    TextView tvCurrentTrust;
    @BindView(R.id.current_trust_underline)
    View currentTrustUnderline;
    @BindView(R.id.mRadioGroupMargin)
    RadioGroup mRadioGroupMargin;
    @BindViews({R.id.mTabMarginOne, R.id.mTabMarginTwo, R.id.mTabMarginThree})
    RadioButton[] radioButtons;

    private String maichu;
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
    private Coin2CoinContract.Presenter mPresenter;
    private int oneAccuracy = 2; // 价格
    private int twoAccuracy = 2; // 数量
    private String oldSymbol; // 上个订阅的币种
    private Boolean isSeekBar = false; // 上个订阅的币种
    private boolean isFace = false;
    @BindView(R.id.tvchange)
    TextView tvchange;
    String buttonTextBuy = "";
    String buttonTextSell = "";
    private AssetEntity assetEntity;
    //做空/做多需要填写信息
    private String leverage = "1";
    private String price; // 价格
    private String touchPrice;//触发价
    private String amout; // 数量
    private String stopProfitPrice; //止盈价格
    private String stopLossPrice;  //止损价格
    private String priceType = "LIMIT_PRICE"; //LIMIT_PRICE限价   MARKET_PRICE 市7价
    private String direction = "BUY"; //BUY 做多，  SELL 做空
    private List<Currency> currencyMain = new ArrayList<>();
    private List<HoldEntity> mCurrentData = new ArrayList<>();
    private Gson gson = new Gson();

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
    protected int getLayoutId() {
        return R.layout.fragment_text_three_new;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvCurrentTrust.setSelected(true);
        mPresenter = new Coin2CoinPresenter(Injection.provideTasksRepository(getActivity()), this);
        // 打开左侧的滑动
//        ibOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) getActivity()).getDlRoot(MainActivity.MENU_TYPE_EXCHANGE).openDrawer(Gravity.LEFT);
//            }
//        });
        // 全部订单
        tvMoreOrder.setOnClickListener(this);
        for (int i = 0; i < 15; i++) {
            mOne.add(new Exchange(15 - i, "--", "--"));
            mTow.add(new Exchange(i, "--", "--"));
        }
        llRisk.setVisibility(View.VISIBLE);
        ll_price_type_selector.setOnClickListener(this);
        mToAllLayout.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioGroupPrice.setOnCheckedChangeListener(this);
        mRadioGroupMargin.setOnCheckedChangeListener(this);
        mOneAdd.setOnClickListener(this);
        mOneSub.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
        ivKline.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        llCurrentTrust.setOnClickListener(this);
//        llHistoryTrust.setOnClickListener(this);
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
                        mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                                + mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
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
                    mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                            + mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
                    if (!WonderfulStringUtils.isEmpty(mOneTcpEdit.getText().toString()) && !WonderfulStringUtils.isEmpty(mOnePriceEdit.getText().toString()) && mOneTcpEdit.isFocused()) {
                        try {
                            Double d = Double.valueOf(WonderfulMathUtils.getRundNumber(usdeBalance, 5, null)) / Double.valueOf(mOnePriceEdit.getText().toString());
                            Double d2 = Double.valueOf(mOneTcpEdit.getText().toString());
                            Float aFloat = Float.valueOf(WonderfulMathUtils.getRundNumber(d2 / d, 2, null)) * 100;
                            if (aFloat < 0) {
                                aFloat = 0f;
                            }
                            isSeekBar = false;
                            if (aFloat > 100) {
//                                WonderfulToastUtils.showToast(getResources().getString(R.string.input_number_over_limit1));
                            }
                        } catch (Exception e) {

                        }

                    }
                } else {
                    mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                            + mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
                    if (!WonderfulStringUtils.isEmpty(mOneTcpEdit.getText().toString())) {
                        try {
                            Double d = Double.valueOf(WonderfulMathUtils.getRundNumber(usdeBalance, 5, null));
                            Double d2 = Double.valueOf(mOneTcpEdit.getText().toString());
                            Float aFloat = Float.valueOf(WonderfulMathUtils.getRundNumber(d2 / d, 2, null)) * 100;
                            if (aFloat < 0) {
                                aFloat = 0f;
                            }
                            isSeekBar = false;
//                            mOneSeekBar.setProgress(aFloat >= 100 ? 100 : aFloat);
//                            text_one_jieshu.setText(mOneSeekBar.getProgress() + "%");
                        } catch (Exception e) {

                        }

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
        mPanJia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == 0) {
                    mOnePriceEdit.setText(mPanJia.getText());
                }
            }
        });

        ivCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mTabOne.setSelected(true);
        mTabLimitPrice.setSelected(true);
        initRecyclerView();
    }

    private TrustCurrentAdapter trustCurrentAdapter;

    private void initRecyclerView() {
        trustCurrentAdapter = new TrustCurrentAdapter(getActivity(), currencyMain, mCurrentData);
        mThreeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mThreeRecycler.setHasFixedSize(true);
        mThreeRecycler.setAdapter(trustCurrentAdapter);
//        trustCurrentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                showBottomFragment((EntrustHistory) adapter.getData().get(position));
//            }
//        });
        trustCurrentAdapter.setOnColseListener(new TrustCurrentAdapter.OnclickListenerColse() {
            @Override
            public void close(String orderId) {
                Log.e("token11: ", SharedPreferenceInstance.getInstance().getTOKEN());
                mPresenter.getClose(SharedPreferenceInstance.getInstance().getTOKEN(), orderId);
            }
        });
        trustCurrentAdapter.setOnModifyListener(new TrustCurrentAdapter.OnclickListenerModify() {
            @Override
            public void modify(String orderId, String stopProfitPrice, String stopLossPrice) {
                showBottomModify(orderId, stopProfitPrice, stopLossPrice);
            }
        });

    }

    private boolean addFace(String symbol) {
        for (Favorite favorite : MainActivity.mFavorte) {
            if (symbol.equals(favorite.getSymbol())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 删除收藏
     *
     * @param token
     * @param symbol
     */
    private void delete(String token, final String symbol) {
        if (!MyApplication.getApp().isLogin()) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
            return;
        }
        showDialog();
        WonderfulOkhttpUtils.post().url(UrlFactory.getDeleteUrl()).addHeader("x-auth-token", token)
                .addParams("symbol", symbol).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                hideDialog();
                WonderfulToastUtils.showToast(getResources().getString(R.string.text_cancel_fail));
            }

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        WonderfulToastUtils.showToast(getResources().getString(R.string.text_cancel_success));
                        isFace = false;
                        ((MainActivity) getmActivity()).Find();
                    } else {
                        WonderfulToastUtils.showToast(getResources().getString(R.string.text_cancel_fail));
                    }
                } catch (JSONException e) {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_cancel_fail));
                }
            }
        });
    }

    /**
     * 添加收藏
     *
     * @param token
     * @param symbol
     */
    private void getCollect(String token, final String symbol) {
        if (!MyApplication.getApp().isLogin()) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
            return;
        }
        showDialog();
        WonderfulOkhttpUtils.post().url(UrlFactory.getAddUrl()).addHeader("x-auth-token", token)
                .addParams("symbol", symbol).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                hideDialog();
                WonderfulToastUtils.showToast(getResources().getString(R.string.text_add_fail));
            }

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    WonderfulLogUtils.logi("jiejie", "  getCollect  " + response);
                    if (object.optInt("code") == 0) {
                        WonderfulToastUtils.showToast(getResources().getString(R.string.text_add_success));
                        isFace = true;
                        ((MainActivity) getmActivity()).Find();
                    } else {
                        WonderfulToastUtils.showToast(getResources().getString(R.string.text_add_fail));
                    }
                } catch (JSONException e) {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_add_fail));
                }
            }
        });
    }


    private void setPriceNull() {
        mOnePriceEdit.setText(null);
        mLoosePrice.setText(null);
        mOneTcpEdit.setText(null);
        mProfitPrice.setText(null);
        radioButtons[0].performClick();
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
                mPresenter.getModifyProfit(SharedPreferenceInstance.getInstance().getTOKEN(), orderId, stopProfitPrice, stopLossPrice);
            }
        });
    }


    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        // 获取盘口的信息
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
        Log.e("AgreementFragment:", "showPageFragment_2");
        this.mCurrency = currency;
        if (this.mCurrency != null) {
            Log.e("AgreementFragment:", "showPageFragment_4");
            setCurrentSelected();
            mOneDeal.setText("--");
            setPriceNull();
            String format2 = new DecimalFormat("#0.00000000").format(currency.getClose());
            BigDecimal bg2 = new BigDecimal(format2);
            String v2 = bg2.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
            mOnePriceEdit.setText(v2);
            // 获取盘口信息
            mPresenter.getExchange(this.mCurrency.getSymbol());
            Log.i("getExchange", "1" + currency.getSymbol());
//            Log.i("miao","币种："+currency.getSymbol());
            // 获取当前币种精确度
            mPresenter.getSymbolInfo(this.mCurrency.getSymbol());
            // 获取当前币种杠杆
            mPresenter.getLeverage(this.mCurrency.getSymbol());
            // 如果是登录状态
            if (!WonderfulStringUtils.isEmpty(SharedPreferenceInstance.getInstance().getTOKEN())) {
                Log.e("resetSymbol: ", "3");

                //盈利损失统计
                mPresenter.getAssetPNL(SharedPreferenceInstance.getInstance().getTOKEN());
                // 获取这个币的多少
                mPresenter.getWallet(1, SharedPreferenceInstance.getInstance().getTOKEN(), this.mCurrency.getSymbol());
                // 获取该币的委托信息
                currentPage = startPage;
                historyPage = startPage;
                //获取当前的持仓
                mPresenter.allCurrency();
//                mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), startPage, pageSize, this.mCurrency.getSymbol(), "HOLD");
//                mPresenter.getCurrentOrder(SharedPreferenceInstance.getInstance().getTOKEN(), startPage, pageSize, this.mCurrency.getSymbol(), "", "", "", "");
            }
            // 修改标题
            isFace = addFace(this.mCurrency.getSymbol());
            tvTitle.setText(this.mCurrency.getSymbol());
            if (priceType.equals("LIMIT_PRICE") || priceType.equals("CHECK_FULL_STOP")) {
//                mOneTextType.setText(this.mCurrency.getSymbol().substring(0, currency.getSymbol().indexOf("/")));
            }
//            mTwoTextType.setText(this.mCurrency.getSymbol().substring(0, currency.getSymbol().indexOf("/")));
            buttonTextBuy = String.valueOf(getResources().getString(R.string.text_duo));
            buttonTextSell = String.valueOf(getResources().getString(R.string.text_kong));
            if (check == 0) {
                btnBuy.setText(buttonTextBuy);
                btnBuy.setBackgroundResource(R.color.color_green);
            } else {
                btnBuy.setText(buttonTextSell);
                btnBuy.setBackgroundResource(R.color.typeRed);
            }
            //更新最新价格从盘口socket里拿
            String format = new DecimalFormat("#0.00000000").format(currency.getClose());
            BigDecimal bg = new BigDecimal(format);
            String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
            mPanJia.setText(String.valueOf(v));
            tvchange.setText((mCurrency.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(mCurrency.getChg() * 100, 2, "########0.") + "%");
            tvchange.setEnabled(mCurrency.getChg() >= 0);
            imgChg.setBackgroundResource(mCurrency.getChg() >= 0 ? R.drawable.icon_rise : R.drawable.icon_fall);
            mPanJia.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
            if ("CNY".equals(currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length()))) {
                mPanMoney.setText(String.valueOf("≈" + WonderfulMathUtils.getRundNumber(currency.getClose() * 1 * (mCurrency.getBaseUsdRate() == null ? 0 : mCurrency.getBaseUsdRate()),
                        2, null) + "CNY"));
            } else {
                mPanMoney.setText(String.valueOf("≈" + WonderfulMathUtils.getRundNumber(currency.getClose() * MainActivity.rate * (mCurrency.getBaseUsdRate() == null ? 0 : mCurrency.getBaseUsdRate()),
                        2, null) + "CNY"));
            }

            // 代表选择了哪个币种，需要重新订阅 应该先取消原来的再订阅现在的
            if (!TextUtils.isEmpty(oldSymbol)) {
                stop(oldSymbol, getmActivity().getId());
            }
//            Log.i("miao","币种："+ getmActivity().getId());
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
                mPresenter.getAssetPNL(SharedPreferenceInstance.getInstance().getTOKEN());
                mPresenter.allCurrency();
            }
            btnLogin.setVisibility(View.GONE);
            btnBuy.setVisibility(View.VISIBLE);
            mThreeRecycler.setVisibility(View.VISIBLE);
            mTvThree.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnBuy.setVisibility(View.GONE);
            mThreeRecycler.setVisibility(View.GONE);
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (MyApplication.getApp().isLogin() && mCurrency != null) {
                mPresenter.getWallet(0, getmActivity().getToken(), mCurrency.getSymbol());

            }
        }
    }

    public void tcpNotify() {
        if (mCurrency != null) {
//            String format = new DecimalFormat("#0.00").format(mCurrency.getClose());
//            BigDecimal bg = new BigDecimal(format);
//            String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
//            if (mPanJia == null) return;
//            mPanJia.setText(String.valueOf(v));
            tvchange.setText((mCurrency.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(mCurrency.getChg() * 100, 2, "########0.") + "%");
            tvchange.setEnabled(mCurrency.getChg() >= 0);
            imgChg.setBackgroundResource(mCurrency.getChg() >= 0 ? R.drawable.icon_rise : R.drawable.icon_fall);
            mPanJia.setTextColor(mCurrency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) :
                    ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
//            if ("CNY".equals(mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()))) {
//                mPanMoney.setText(String.valueOf("≈" + WonderfulMathUtils.getRundNumber(mCurrency.getClose() * 1 * mCurrency.getBaseUsdRate(),
//                        2, null) + "CNY"));
//            } else {
//                mPanMoney.setText(String.valueOf("≈" + WonderfulMathUtils.getRundNumber(mCurrency.getClose() * MainActivity.rate * mCurrency.getBaseUsdRate(),
//                        2, null) + "CNY"));
//            }
            mPresenter.getAssetPNL(SharedPreferenceInstance.getInstance().getTOKEN());
            mPresenter.allCurrency();
//            mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), startPage, pageSize, this.mCurrency.getSymbol(), "HOLD");
        }
    }

    private int check = 0;
    private int checkPrice = 0;

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.mTabOne: // 点击买入
                check = 0;
                direction = "BUY";
                mOneLayout.setVisibility(View.VISIBLE);
                mTabOne.setSelected(true);
                mTabTwo.setSelected(false);
                btnBuy.setText(getResources().getString(R.string.text_duo));
                btnBuy.setBackgroundResource(R.color.color_green);
                break;
            case R.id.mTabTwo: // 点击卖出
                check = 1;
                direction = "SELL";
                mTabOne.setSelected(false);
                mTabTwo.setSelected(true);
                btnBuy.setText(getResources().getString(R.string.text_kong));
                btnBuy.setBackgroundResource(R.color.typeRed);
                break;
            case R.id.mTabLimitPrice: // 点击限价交易
                checkPrice = 0;
                mTabMarketPrice.setSelected(false);
                mTabLimitPrice.setSelected(true);
                mOnePriceEdit.setFocusable(true);
                mOnePriceEdit.setFocusableInTouchMode(true);
                mOnePriceEdit.setEnabled(true);
                priceType = "LIMIT_PRICE";
                break;
            case R.id.mTabMarketPrice: // 点击市场交易
                checkPrice = 1;
                mTabLimitPrice.setSelected(false);
                mTabMarketPrice.setSelected(true);
                mOnePriceEdit.setFocusable(false);
                mOnePriceEdit.setFocusableInTouchMode(false);
                mOnePriceEdit.setEnabled(false);
                priceType = "MARKET_PRICE";

//                String format2 = new DecimalFormat("#0.00000000").format(mCurrency.getClose());
                try {
                    String format2 = new DecimalFormat("#0.00000000").format(Double.parseDouble(mPanJia.getText().toString()));
                    BigDecimal bg2 = new BigDecimal(format2);
                    String v2 = bg2.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                    mOnePriceEdit.setText(v2);
                } catch (Exception e) {
                }
                break;
            case R.id.mTabMarginOne: //
                radioButtons[0].setSelected(true);
                radioButtons[1].setSelected(false);
                radioButtons[2].setSelected(false);
                leverage = radioButtons[0].getText().toString();
                mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                        + mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
                break;
            case R.id.mTabMarginTwo: //
                radioButtons[0].setSelected(false);
                radioButtons[1].setSelected(true);
                radioButtons[2].setSelected(false);
                leverage = radioButtons[1].getText().toString();
                mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                        + mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
                break;
            case R.id.mTabMarginThree: //
                radioButtons[0].setSelected(false);
                radioButtons[1].setSelected(false);
                radioButtons[2].setSelected(true);
                leverage = radioButtons[2].getText().toString();
                mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                        + mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
                break;
            default:
        }
    }

    @Override
    public void errorMes(int e, String msg) {

        //mTvThree.setVisibility(View.INVISIBLE);
        if (e == 4000) {
            MyApplication.getApp().setCurrentUser(null);
            if (!MyApplication.getApp().isLogin()) {
                btnLogin.setVisibility(View.VISIBLE);
                btnBuy.setVisibility(View.GONE);
                mThreeRecycler.setVisibility(View.GONE);
                mTvThree.setVisibility(View.GONE);
//                mTwoYuE.setText("0.0");
//                mOneYuE.setText("0.0");
                SharedPreferenceInstance.getInstance().saveaToken("");
                SharedPreferenceInstance.getInstance().saveTOKEN("");

            } else {
                btnLogin.setVisibility(View.GONE);
                btnBuy.setVisibility(View.VISIBLE);
                mThreeRecycler.setVisibility(View.VISIBLE);
                mTvThree.setVisibility(View.VISIBLE);
                if (MyApplication.getApp().isLogin() && this.mCurrency != null) {
                    currentPage = startPage;
                    historyPage = startPage;
                    mPresenter.allCurrency();
//                    mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), startPage, pageSize, this.mCurrency.getSymbol(), "HOLD");
//                    mPresenter.getCurrentOrder(SharedPreferenceInstance.getInstance().getTOKEN(), startPage, pageSize, this.mCurrency.getSymbol(), "", "", "", "");
//                mPresenter.getOrderHistory(SharedPreferenceInstance.getInstance().getTOKEN(), startPage, pageSize, this.currency.getSymbol(), "", "", "", "");
                }
            }
        } else {
            WonderfulCodeUtils.checkedErrorCode(getmActivity(), e, msg);
        }

        //mTvThree.setVisibility(this.mHistoryData.size() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 提交委托后返回的信息(买入或者卖出成功)
     */
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
                mPresenter.allCurrency();
                // 重新刷新下盘口信息  也许是不用的
                mPresenter.getExchange(mCurrency.getSymbol());
                Log.i("getExchange", "5" + mCurrency.getSymbol());
                // 应该还需要刷新下钱包的接口
                mPresenter.getWallet(1, SharedPreferenceInstance.getInstance().getTOKEN(), this.mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")));
                mPresenter.getWallet(2, SharedPreferenceInstance.getInstance().getTOKEN(), mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()));
            }
        }
    }

    private LoadDialog mDialog;

    @Override
    public void showDialog() {
        if (mDialog == null) {
            mDialog = new LoadDialog(getmActivity());
        }
        mDialog.show();
    }

    @Override
    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void setDefaultSymbol(String symbol) {
        for (Currency currency : currencies) {
            if (currency.getSymbol().equals(symbol)) {
                this.mCurrency = currency;
                break;
            }
        }
        if (mCurrency == null) {
            mCurrency = currencies.get(0);
        }
        if (tvTitle != null && TextUtils.isEmpty(tvTitle.getText()) && mCurrency != null) {
            resetSymbol(mCurrency);
            Log.i("tvTitle1", "setDefaultSymbol");
        }
    }

    @Override
    public void getAssetPNLSuccess(AssetEntity assetEntity) {
        mOneYuE.setText(new DecimalFormat("#0.00").format(assetEntity.getAvailable()));
        tvRiskRate.setText(new DecimalFormat("#0.00").format(assetEntity.getRiskRate()) + "%");
        this.assetEntity = assetEntity;
    }

    @Override
    public void getLeverage(List<String> lergave) {
        for (int i = 0; i < lergave.size(); i++) {
            if (i > 2) {
                break;
            }
            if (i == 0) {
                leverage = lergave.get(i);
                radioButtons[0].setSelected(true);
                mOneDeal.setText(String.valueOf(WonderfulMathUtils.getRundNumber(mul(mOneTextPrice * mOneText, 1) / Double.parseDouble(leverage), 2, null)
                        + mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
            }
            radioButtons[i].setVisibility(View.VISIBLE);
            radioButtons[i].setText(lergave.get(i));
        }
    }

    @Override
    public void allCurrencySuccess(Object obj) {
        try {
            JsonObject object = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray array1 = object.getAsJsonArray("changeRank").getAsJsonArray();
            currencyMain = gson.fromJson(array1, new TypeToken<List<Currency>>() {
            }.getType());
            trustCurrentAdapter.setCurrencies(currencyMain);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO
        mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), 1, 10, this.mCurrency.getSymbol(), "HOLD");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hideDialog();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        EventBus.getDefault().register(this);
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
    public void getHoldSuccess(List<HoldEntity> holdEntityList) {
        btnLogin.setVisibility(View.GONE);
        btnBuy.setVisibility(View.VISIBLE);
        if (holdEntityList == null) {
            return;
        }
        setListData(holdEntityList);
        WonderfulLogUtils.logd("jiejie", "--当前持仓啊--" + holdEntityList.size());
    }

    @Override
    public void getHoldFial(int e, String meg) {
        //TODO
    }

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

    /**
     * 当前委托
     */
    @Override
    public void getDataLoaded(List<EntrustHistory> entrusts) {
//        btnLogin.setVisibility(View.GONE);
//        btnBuy.setVisibility(View.VISIBLE);
//        if (entrusts == null) {
//            return;
//        }
//        setListData(entrusts);
        //trustFragments.get(0).setListData(TrustFragment.TRUST_TYPE_CURRENT, entrusts);
    }

    /**
     * 历史委托
     */
    @Override
    public void getHistorySuccess(List<EntrustHistory> entrustHistories) {
        if (entrustHistories == null) {
            return;
        }
    }

    public void setListData(List<HoldEntity> list) {
        if (list != null && list.size() > 0) {
            WonderfulLogUtils.logd("jiejie", "--当前委托条数--" + list.size());
            mThreeRecycler.setVisibility(View.VISIBLE);
            mTvThree.setVisibility(View.GONE);

            mCurrentData.clear();
            mCurrentData.addAll(list);
            if (isRefresh) {
                trustCurrentAdapter.notifyDataSetChanged();
            } else {
                for (int i = 0; i < mCurrentData.size(); i++) {
                    trustCurrentAdapter.notifyItemChanged(i);
                }
                isRefresh = false;
            }
        } else {
            mCurrentData.clear();
            trustCurrentAdapter.notifyDataSetChanged();
            mThreeRecycler.setVisibility(View.GONE);
            mTvThree.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 取消某个委托成功
     */
    @Override
    public void getCancelSuccess(String success) {
        if (MyApplication.getApp().isLogin() && this.mCurrency != null) {
            // 重新获取当前订单
            currentPage = startPage;
            historyPage = startPage;
            mPresenter.getCurrentOrder(SharedPreferenceInstance.getInstance().getTOKEN(), currentPage, pageSize, this.mCurrency.getSymbol(), "", "", "", "");
            // 应该还需要刷新下钱包的接口
            mPresenter.getWallet(1, SharedPreferenceInstance.getInstance().getTOKEN(), this.mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")));
            mPresenter.getWallet(2, SharedPreferenceInstance.getInstance().getTOKEN(), mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()));
        }
    }

    @Override
    public void getModifySuccess(String success) {
        if (MyApplication.getApp().isLogin() && this.mCurrency != null) {
            // 重新获取当前订单
            currentPage = startPage;
            historyPage = startPage;
            mPresenter.allCurrency();
//            mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), currentPage, pageSize, this.mCurrency.getSymbol(), "HOLD");

            // 重新刷新下盘口信息  也许是不用的
            mPresenter.getExchange(mCurrency.getSymbol());
            Log.i("getExchange", "5" + mCurrency.getSymbol());
            // 应该还需要刷新下钱包的接口
            mPresenter.getWallet(1, SharedPreferenceInstance.getInstance().getTOKEN(), this.mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")));
            mPresenter.getWallet(2, SharedPreferenceInstance.getInstance().getTOKEN(), mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()));
        }
    }

    @Override
    public void getCloseSuccess(String success) {
        if (MyApplication.getApp().isLogin() && this.mCurrency != null) {
            //平仓成功，用于adapter刷新
            isRefresh = true;
            // 重新获取当前订单
            currentPage = startPage;
            historyPage = startPage;
            mPresenter.allCurrency();
//            mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), currentPage, pageSize, this.mCurrency.getSymbol(), "HOLD");

            // 重新刷新下盘口信息  也许是不用的
            mPresenter.getExchange(mCurrency.getSymbol());
            Log.i("getExchange", "5" + mCurrency.getSymbol());
            // 应该还需要刷新下钱包的接口
            mPresenter.getWallet(1, SharedPreferenceInstance.getInstance().getTOKEN(), this.mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")));
            mPresenter.getWallet(2, SharedPreferenceInstance.getInstance().getTOKEN(), mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()));
        }
    }

    @Override
    public void getCloseFial(int e, String meg) {

    }

    /**
     * 获取钱包成功
     */
    @Override
    public void getWalletSuccess(Object object, int type) {
//        Log.d("jiejie", type + "");
        Money obj = (Money) object;
        switch (type) {
            case 1: // 可卖
                if (obj.getCode() == 0 && obj.getData() != null) {
//                    if (mTwoYuE == null) {
//                        return;
//                    }
                    usdeBalance2 = Double.valueOf(WonderfulMathUtils.getRundNumber(obj.getData().getBalance(), 5, null));
//                    mTwoYuE.setText(String.valueOf(WonderfulMathUtils.getRundNumber(obj.getData().getBalance(), 8, null) +
//                            this.mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/"))));
                    maichu = String.valueOf(WonderfulMathUtils.getRundNumber(obj.getData().getBalance(), 8, null));
//                    text_two_jieshu.setText(mTwoYuE.getText().toString());
                } else {
//                    mTwoYuE.setText(String.valueOf("0.0" +
//                            this.mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/"))));
                    maichu = "0";
//                    text_two_jieshu.setText(mTwoYuE.getText().toString());
                }
                break;
            case 2: // 可用
                if (obj.getCode() == 0 && obj.getData() != null) {
//                    if ("USDT".equals(currency.getSymbol().substring(currency.getSymbol().indexOf("/") + 1, currency.getSymbol().length()))) {
//                        usdeBalance = obj.getData().getBalance();
//                    }
                    if (mOneYuE == null) {
                        return;
                    }
                    usdeBalance = Double.valueOf(WonderfulMathUtils.getRundNumber(obj.getData().getBalance(), 5, null));
//                    mOneYuE.setText(String.valueOf(WonderfulMathUtils.getRundNumber(obj.getData().getBalance(), 8, null) +
//                            mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
                    WonderfulLogUtils.logi("miao", "mOneYuE1" + mOneYuE.getText().toString());
//                    text_one_jieshu.setText(mOneYuE.getText().toString());
                } else {
//                    mOneYuE.setText(String.valueOf("0.0" + this.currency.getSymbol().substring(0, currency.getSymbol().indexOf("/"))));
//                    text_one_jieshu.setText(mOneYuE.getText().toString());

                }
                break;
            case 3:
//                if (mTwoYuE == null) {
//                    return;
//                }
//                mTwoYuE.setText(String.valueOf("0.0" +
//                        this.mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/"))));
//                mOneYuE.setText(String.valueOf("0.0" + mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length())));
                WonderfulLogUtils.logi("miao", "mOneYuE3" + mOneYuE.getText().toString());
//                text_one_jieshu.setText(mOneYuE.getText().toString());
//                text_two_jieshu.setText(mTwoYuE.getText().toString());
//                SharedPreferenceInstance.getInstance().saveIsNeedShowLock(false);
//                SharedPreferenceInstance.getInstance().saveLockPwd("");
//                toLoginOrCenter();
//                Log.i("miao","断开登录");
                break;
            default:
        }
    }

    private double usdeBalance = -1;
    private double usdeBalance2 = -1;

    private void toLoginOrCenter() {
        startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_price_type_selector:
                final LinkedList<String> linkedList = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.text_type)));
                PopupUtils.showListBottom(getActivity(), linkedList, view, view.getMeasuredWidth(), new PopupUtils.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int i) {
//                        tv_price_type.setText(linkedList.get(i));
                        if (i == 0) { // 深度 5
                            currentPlate = PLATE_DEFAULT;
                            mOneAdapter.setList(getDisplayPlateList(mOne, currentPlate, true));
                            mTwoAdapter.setList(getDisplayPlateList(mTow, currentPlate, false));
                            mOneAdapter.notifyDataSetChanged();
                            mTwoAdapter.notifyDataSetChanged();
                            tvDepthType.setText(getResources().getString(R.string.depth));
                        } else if (i == 1) { // 深度 10
                            currentPlate = PLATE_BUY;
                            mOneAdapter.setList(getDisplayPlateList(mOne, currentPlate, true));
                            mTwoAdapter.setList(getDisplayPlateList(mTow, currentPlate, false));
                            mOneAdapter.notifyDataSetChanged();
                            mTwoAdapter.notifyDataSetChanged();
                            tvDepthType.setText(getResources().getString(R.string.depth_10));
                        } else if (i == 2) { // 深度 15
                            currentPlate = PLATE_SELL;
                            mOneAdapter.setList(getDisplayPlateList(mOne, currentPlate, true));
                            mTwoAdapter.setList(getDisplayPlateList(mTow, currentPlate, false));
                            mOneAdapter.notifyDataSetChanged();
                            mTwoAdapter.notifyDataSetChanged();
                            tvDepthType.setText(getResources().getString(R.string.depth_15));
//                            priceType = "CHECK_FULL_STOP";
////                            ll_buy_section_touch.setVisibility(View.VISIBLE);
////                            ll_sell_section_touch.setVisibility(View.VISIBLE);
//                            mOneXian.setVisibility(View.VISIBLE);
////                            mTwoXian.setVisibility(View.VISIBLE);
////                            mTwoShi.setVisibility(View.GONE);
//                            mOneShi.setVisibility(View.GONE);
//                            mOneJiaoYi.setVisibility(View.VISIBLE);
////                            mTwoJiaoYi.setVisibility(View.VISIBLE);
//                            if (mCurrency != null) {
////                                mOneTextType.setText(mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")));
//                            }
////                            mTwoTcpEdit.setHint(getResources().getString(R.string.text_number));
//                            mOneTcpEdit.setHint(getResources().getString(R.string.text_number));
//                            mOneBuy.setVisibility(View.VISIBLE);
////                            mTwoBuy.setVisibility(View.VISIBLE);
                        }
//                        mOneAdapter.setList(getDisplayPlateList(mOne, currentPlate, true));
//                        mTwoAdapter.setList(getDisplayPlateList(mTow, currentPlate, false));
//                        mOneAdapter.notifyDataSetChanged();
//                        mTwoAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case R.id.tvMoreOrder:
                if (MyApplication.getApp().isLogin()) {
                    OrderActivity.actionStart(getActivity(), this.mCurrency.getSymbol(), assetEntity);
                } else {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
                }
                break;
            case R.id.ivKline:
                if (this.mCurrency != null) {
                    if (mCurrency.getExchangeable() == 1) {
                        SKlineActivity.actionStart(getActivity(), this.mCurrency.getSymbol());
                    } else {
                        WonderfulToastUtils.showToast("暂未开放");
                    }
                }
                break;
            case R.id.btn_toLogin: // 点击登录
                toLoginOrCenter();
                break;
            case R.id.mOneAdd: // +买入 价格
                try {
                    double num = mOneTcpEdit.getText().toString().equals("") ? 0.0 : Double.valueOf(mOneTcpEdit.getText().toString());
                    mOneTcpEdit.setText(String.valueOf(num + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.mOneSub: // -买入 价格
                try {
                    double num1 = Double.valueOf(mOneTcpEdit.getText().toString());
                    if ((num1 - 1) > 0) {
                        mOneTcpEdit.setText(String.valueOf(num1 - 1));
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
            case R.id.btnOneBuy: // 买入/卖出
                addOrder();
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
            mPresenter.allCurrency();
        } else {
//            WonderfulToastUtils.showToast("请去登录");
//            MyApplication.getApp().loginAgain(getmActivity());
//            mTwoYuE.setText("0.0");
            mOneYuE.setText("0.0");
            SharedPreferenceInstance.getInstance().saveaToken("");
            SharedPreferenceInstance.getInstance().saveTOKEN("");
        }

    }


    private void addOrder() {
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
        stopProfitPrice = mProfitPrice.getText().toString();
        stopLossPrice = mLoosePrice.getText().toString();
        mPresenter.getAddOrder(SharedPreferenceInstance.getInstance().getTOKEN(), mCurrency.getSymbol(), price, amout, direction, priceType, leverage, stopProfitPrice, stopLossPrice);
    }

    /**
     * 盘口信息的返回 和 当前委托的返回
     *
     * @param response SocketResponse
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(SocketResponse response) {
        Log.i("推送过来的信息： ThreeFragment", response.getCmd().toString());
        if (response.getCmd() == null) {
            return;
        }
        switch (response.getCmd()) {
            case PUSH_EXCHANGE_TRADE:
                try {
                    setResponse(response.getResponse());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case PUSH_EXCHANGE_ORDER_CANCELED:
            case PUSH_EXCHANGE_ORDER_COMPLETED:
            case PUSH_EXCHANGE_ORDER_TRADE:
                if (MyApplication.getApp().isLogin() && this.mCurrency != null) {
                    WonderfulLogUtils.logi("推送过来的信息", "当前委托改为当前持仓");
                    currentPage = startPage;
                    historyPage = startPage;
                    //TODO 当前委托改为当前持仓
                    mPresenter.allCurrency();
//                    mPresenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), currentPage, pageSize, this.mCurrency.getSymbol(), "HOLD");
//                    if (isCurrent) {
//                        mPresenter.getCurrentOrder(SharedPreferenceInstance.getInstance().getTOKEN(), currentPage, pageSize, this.mCurrency.getSymbol(), "", "", "", "");
//                    } else {
//                        mPresenter.getOrderHistory(SharedPreferenceInstance.getInstance().getTOKEN(), historyPage, pageSize, this.mCurrency.getSymbol(), "", "", "", "");
//                    }
                    mPresenter.getWallet(1, SharedPreferenceInstance.getInstance().getTOKEN(), this.mCurrency.getSymbol().substring(0, mCurrency.getSymbol().indexOf("/")));
                    mPresenter.getWallet(2, SharedPreferenceInstance.getInstance().getTOKEN(), mCurrency.getSymbol().substring(mCurrency.getSymbol().indexOf("/") + 1, mCurrency.getSymbol().length()));

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
        } catch (Exception e) {

        }
    }


    @Override
    public void setPresenter(Coin2CoinContract.Presenter presenter) {
        mPresenter = presenter;
    }


    public void asyncCollect(String symbol, boolean isFace) {
        if (symbol.equals(mCurrency.getSymbol())) {
            this.isFace = isFace;
        }
    }

    @BindView(R.id.recyclerSell)
    RecyclerView recyclerSell; // 卖出的
    @BindView(R.id.recyclerBuy)
    RecyclerView recyclerBuy; //买进

    final int PLATE_DEFAULT = 0;
    final int PLATE_BUY = 1;
    final int PLATE_SELL = 2;

    int currentPlate = PLATE_SELL;

}
