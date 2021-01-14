package com.bibi.ui.main.options;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bibi.entity.RechargeSupportContract;
import com.bibi.entity.SimpleListBean;
import com.bibi.entity.SimpleListItem;
import com.bibi.ui.dialog.ListDialogFragment;
import com.bibi.ui.main.asset.AssetActivity;
import com.bibi.ui.recharge.RechargeActivity;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.MinuteChartView;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.github.tifezh.kchartlib.chart.entity.IMinuteLine;
import com.github.tifezh.kchartlib.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bibi.R;
import com.bibi.adapter.KChartAdapter;
import com.bibi.adapter.MyPagerAdapter;
import com.bibi.adapter.PagerAdapter;
import com.bibi.app.GlobalConstant;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.app.UrlFactory;
import com.bibi.base.BaseTransFragment;
import com.bibi.customview.CustomViewPager;
import com.bibi.customview.MyViewPager;
import com.bibi.customview.intercept.WonderfulScrollView;
import com.bibi.data.DataHelper;
import com.bibi.data.DataSource;
import com.bibi.data.RemoteDataSource;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.CoinContract;
import com.bibi.entity.Currency;
import com.bibi.entity.CurrencyK;
import com.bibi.entity.KLineEntity;
import com.bibi.entity.MinuteLineEntity;
import com.bibi.entity.ThreeTextInfo;
import com.bibi.entity_remote.OptionsAddOrder;
import com.bibi.serivce.MyTextService;
import com.bibi.serivce.SocketMessage;
import com.bibi.serivce.SocketResponse;
import com.bibi.socket.ISocket;
import com.bibi.ui.kline.DepthFragment;
import com.bibi.ui.kline.IntroduceFragment;
import com.bibi.ui.kline.KlineContract;
import com.bibi.ui.kline.KlinePresenter;
import com.bibi.ui.main.MainActivity;
import com.bibi.ui.mychart.DataParse;
import com.bibi.ui.mychart.KLineBean;
import com.bibi.ui.mychart.MinutesBean;
import com.bibi.ui.mychart.PushLineDataBean;
import com.bibi.ui.mychart.PushLineDataNewBean;
import com.bibi.utils.ServiceUtil;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulDateUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Request;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/10
 */
public class OptionsFragment extends BaseTransFragment implements KlineContract.View, View.OnClickListener {
    public static final String TAG = OptionsFragment.class.getSimpleName();
    @BindView(R.id.ibOpen)
    ImageButton ibOpen;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tvCurrencyName)
    TextView tvCurrencyName;
    @BindView(R.id.kDataText)
    TextView mDataText;
    @BindView(R.id.kDataOne)
    TextView mDataOne;
    @BindView(R.id.kDataImg)
    ImageView kDataImg;
    @BindView(R.id.kUp)
    TextView kUp;
    @BindView(R.id.kLow)
    TextView kLow;
    @BindView(R.id.kRange)
    TextView kRange;
    @BindView(R.id.tab)
    LinearLayout tab;
    @BindView(R.id.viewPager)
    MyViewPager viewPager;
    @BindView(R.id.tvMore)
    TextView tvMore;
    @BindView(R.id.llAllTab)
    LinearLayout llAllTab;
    @BindView(R.id.llVertical)
    LinearLayout llVertical;
    @BindView(R.id.llState)
    LinearLayout llState;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.vpDepth)
    CustomViewPager depthPager;
    @BindView(R.id.scrollView)
    WonderfulScrollView scrollView;
    @BindView(R.id.tvPrice)
    EditText tvPrice;
    @BindView(R.id.tvLeverage)
    TextView tvLeverage;
    @BindArray(R.array.k_line_tab)
    String[] mTitles;

    private Intent intentTcp;
    private boolean isInitKChart=false;
    private KChartAdapter kChartAdapter;
    private ArrayList<TextView> textViews;
    private ArrayList<View> views; //k线图view
    int scale = 4;
    private String symbol = "BTC/USDT"; //当前货币名称
    private KlineContract.Presenter presenter;
    private List<Currency> currencies = new ArrayList<>(); //所有货币信息
    private Currency mCurrency; //当前货币信息
    private boolean isPopClick; //是否弹出"更多"选择框
    private TextView selectedTextView;
    private int type; //选择的时段
    private Date startDate;
    private Date endDate;
    private String resolution;
    private boolean isFirstLoad = true;
    private ArrayList<KLineBean> kLineDatas;     // K线图数据
    //加载菊花
    private ProgressBar mProgressBar;
    //定义pop里的控件
    private PopupWindow popupWindow;
    private LinearLayout moreTabLayout;
    private LinearLayout indexLayout;
    private TextView maView;
    private TextView bollView;
    private TextView macdView;
    private TextView kdjView;
    private TextView rsiView;
    private TextView hideChildView;
    private TextView hideMainView;
    private int childType = 0;//0:macd  1:kdj  2:rsi  -1:隐藏
    //定义k线图相关控件
    private KChartView kChartView;
    private MinuteChartView minuteChartView;
    private OptionOrderFragment fragment;
    private List<CoinContract> coinContracts = new ArrayList<>();
    private double balanceUsd = 0.0;
    private double releaseBalance = 0;
    private double presentBalance = 0;
    private long tiem1;

    private Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                //显示系统时间
                long sysTime = System.currentTimeMillis();//获取系统时间
                CharSequence sysTimeStr = DateFormat.format("hh:mm:ss", sysTime);//时间显示格式
                tvRecord.setText(sysTimeStr);
                //在这里执行定时需要的操作
                presenter.coinThumb(SharedPreferenceInstance.getInstance().getTOKEN(), symbol, type);
                mHandler.postDelayed(this, 1000);
            }catch (Exception e){
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_option;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        // 打开左侧的滑动
        ibOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).getDlRoot(MainActivity.MENU_TYPE_EXCHANGE).openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    protected void obtainData() {
        presenter = new KlinePresenter(Injection.provideTasksRepository(getActivity()), this);
        textViews = new ArrayList<>();
        views = new ArrayList<>();
        tvCurrencyName.setText(symbol);
        initRvData();
        getCurrent();
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        getOptionsAsset();
        mHandler.postDelayed(runnable, 1000);
    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.UNSUBSCRIBE_SYMBOL_THUMB, null)); //  取消订阅
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    /**
     * 副图的点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMA:
            case R.id.tvBOLL:
            case R.id.tvMainHide:
                if (view.getId() == R.id.tvMA) {
                    maView.setSelected(true);
                    bollView.setSelected(false);
                    hideMainView.setSelected(false);
                } else if (view.getId() == R.id.tvBOLL) {
                    maView.setSelected(false);
                    bollView.setSelected(true);
                    hideMainView.setSelected(false);
                } else {
                    maView.setSelected(false);
                    bollView.setSelected(false);
                    hideMainView.setSelected(true);
                }
                if (type == GlobalConstant.TAG_DIVIDE_TIME) {
                    minuteChartView.setMAandBOLL(maView.isSelected(), bollView.isSelected());
                } else {
                    kChartView.setMAandBOLL(maView.isSelected(), bollView.isSelected());
                }
                popupWindow.dismiss();
                break;
            case R.id.tvMACD:
            case R.id.tvRSI:
            case R.id.tvKDJ:
            case R.id.tvChildHide:
                if (view.getId() == R.id.tvMACD) {
                    childType = 0;
                    macdView.setSelected(true);
                    rsiView.setSelected(false);
                    kdjView.setSelected(false);
                    hideChildView.setSelected(false);
                } else if (view.getId() == R.id.tvKDJ) {
                    childType = 1;
                    macdView.setSelected(false);
                    rsiView.setSelected(false);
                    kdjView.setSelected(true);
                    hideChildView.setSelected(false);
                } else if (view.getId() == R.id.tvRSI) {
                    childType = 2;
                    macdView.setSelected(false);
                    rsiView.setSelected(true);
                    kdjView.setSelected(false);
                    hideChildView.setSelected(false);
                } else {
                    childType = -1;
                    macdView.setSelected(false);
                    rsiView.setSelected(false);
                    kdjView.setSelected(false);
                    hideChildView.setSelected(true);
                }
                if (type == GlobalConstant.TAG_DIVIDE_TIME) {
                } else {
                    kChartView.setChidType(childType);
                }
                popupWindow.dismiss();
                break;
            default:
        }
    }

    /**
     * 初始化下单数据
     */
    private void initRvData() {
        fragment = null;
        fragments.clear();
        fragment = OptionOrderFragment.getInstance(symbol);
        fragments.add(fragment);
        depthPager.setAdapter(new PagerAdapter(getmActivity().getSupportFragmentManager(), fragments, null));
        depthPager.setOffscreenPageLimit(fragments.size() - 1);
        depthPager.setCurrentItem(0);
    }

    /**
     * 获取头部信息
     */
    private void getCurrent() {
        tiem1 = System.currentTimeMillis();
//        if(currencies.size()>0){
//            getCurrencyInfo();
//        }else {
            WonderfulOkhttpUtils.get().addParams("symbol",symbol).url(UrlFactory.getEryuanSymbolInfo()).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response) {
                            WonderfulLogUtils.logd("response:",response);
                            /*List<Currency> obj = new Gson().fromJson(response, new TypeToken<List<Currency>>() {
                            }.getType());*/
                            Currency obj = new Gson().fromJson(response, new TypeToken<Currency>() {
                            }.getType());
                            currencies.clear();
                            currencies.add(obj);
                            getCurrencyInfo();
                        }
                    });
//        }
    }

    private void setDrawableRightIconSize(Context context, TextView compoundButton, int drawableId, int resourceIdW, int resourceIdH){
        int w = context.getResources().getDimensionPixelOffset(resourceIdW);
        int h = context.getResources().getDimensionPixelOffset(resourceIdH);
        Drawable fragmentBottomDrawable = context.getResources().getDrawable(drawableId);
        fragmentBottomDrawable.setBounds(0, 0, w, h);
        compoundButton.setCompoundDrawables(null, null, fragmentBottomDrawable, null);
    }

    private void setLeverageView(final List<Currency> currencyList){
        WonderfulLogUtils.logd("currencyList:",currencyList.size()+"");
        if(currencyList.size()>0){
            if(currencyList.get(0).getUseLeverage()==1){//使用杠杆
                tvLeverage.setVisibility(View.VISIBLE);
            }else{
                tvLeverage.setVisibility(View.GONE);
            }
            setDrawableRightIconSize(getActivity(),tvLeverage,R.drawable.icon_pull_down,R.dimen.dimen_20,R.dimen.dimen_20);
            tvLeverage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currencyList.size() > 0) {
                        String leverage = currencyList.get(0).getLeverage();
                        String array[] = leverage.split(",");
                        final SimpleListBean bean = new SimpleListBean();
                        List<SimpleListItem> simpleListItems = new ArrayList<>();
                        for (int i = 0; i < array.length; i++) {
                            SimpleListItem item = new SimpleListItem();
                            item.setContent(array[i] + "倍");
                            simpleListItems.add(item);
                        }
                        bean.setNewsItems(simpleListItems);
                        ListDialogFragment listDialogFragment = ListDialogFragment.getInstance(bean);
                        listDialogFragment.show(getActivity().getSupportFragmentManager(), "bottom");
                        listDialogFragment.setCallback(new ListDialogFragment.OperateCallback() {
                            @Override
                            public void ItemClick(int position) {
                                tvLeverage.setText("倍率："+bean.getNewsItems().get(position).getContent());
                            }
                        });
                    }
                }
            });
        }
    }

    public void getCurrencyInfo() {
        setLeverageView(currencies);
        setCurrentcy(currencies);
        List<String> titles = Arrays.asList(mTitles);
        if (viewPager != null){
            if(!isInitKChart){
                initViewpager(titles);
                initTextView(5);
                initPopWindow(5);
                isInitKChart=true;
            }else {
                initViewpager(titles);
            }
        }

        //默认选择第三个tab
        selectedTextView = textViews.get(2);
        //画线
        Drawable home_zhang_no = getResources().getDrawable(
                R.drawable.tag);
        selectedTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                null, null, home_zhang_no);
        type = (int) selectedTextView.getTag();
        viewPager.setCurrentItem(2);
    }

    /**
     * 初始化popwindow
     *
     * @param count
     */
    private void initPopWindow(int count) {
        View contentView = LayoutInflater.from(getmActivity()).inflate(R.layout.layout_kline_popwindow, null);
        initPopChidView(contentView);
        intMoreTab(count);
        popupWindow = new PopupWindow(getmActivity());
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
    }

    /**
     * 头部显示内容
     *
     * @param objs
     */
    private void setCurrentcy(List<Currency> objs) {
        try {
            for (Currency currency : objs) {
                if (symbol.equals(currency.getSymbol())) {
                    mCurrency = currency;
                    break;
                }
            }

            String strUp = String.valueOf(mCurrency.getHigh());
            String strLow = String.valueOf(mCurrency.getLow());
            //涨跌幅
            Double douChg = mCurrency.getChg();
            String strRang = WonderfulMathUtils.getRundNumber(mCurrency.getChg() * 100, 2, "########0.") + "%";
            String strDataText = "≈" + WonderfulMathUtils.getRundNumber(mCurrency.getClose() * MainActivity.rate * (mCurrency.getBaseUsdRate() == null ? 1 : mCurrency.getBaseUsdRate()),
                    2, null) + "CNY";
            String strDataOne = String.valueOf(mCurrency.getClose());

            BigDecimal bg3 = new BigDecimal(strUp);
            String v3 = bg3.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
            kUp.setText(v3);

            BigDecimal bg2 = new BigDecimal(strLow);
            String v2 = bg2.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
            kLow.setText(v2);

            kRange.setText(strRang);
            //现价
            BigDecimal bg1 = new BigDecimal(strDataOne);
            String v1 = bg1.toPlainString();
            mDataOne.setText(v1);
            if (fragment != null) {
                fragment.setCurrentPrice(v1);
            }
            //cny
            mDataText.setText(strDataText);
            //涨跌幅ui判断
            if (douChg < 0) {
                mDataOne.setTextColor(getResources().getColor(R.color.typeRed));
                kRange.setTextColor(getResources().getColor(R.color.typeRed));
                kDataImg.setBackgroundResource(R.drawable.icon_fall);
            } else {
                mDataOne.setTextColor(getResources().getColor(R.color.typeGreen));
                kRange.setTextColor(getResources().getColor(R.color.typeGreen));
                kDataImg.setBackgroundResource(R.drawable.icon_rise);
            }
            //是否休息
            if(mCurrency.isClosed()){
                tvBuy.setBackgroundResource(R.color.grey);
                tvSell.setBackgroundResource(R.color.grey);
                tvBuy.setEnabled(false);
                tvSell.setEnabled(false);
            } else {
                tvBuy.setBackgroundResource(R.color.color_green);
                tvSell.setBackgroundResource(R.color.typeRed);
                tvBuy.setEnabled(true);
                tvSell.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化viewpager
     *
     * @param titles
     */
    private void initViewpager(List<String> titles) {
        for (int i = 0; i < titles.size(); i++) {
            View view = LayoutInflater.from(getmActivity()).inflate(R.layout.layout_kchartview, null);
            if (i == 0) {
                minuteChartView = view.findViewById(R.id.minuteChartView);
                minuteChartView.setVisibility(View.VISIBLE);
                RelativeLayout mLayout = view.findViewById(R.id.mLayout);
                mProgressBar = new ProgressBar(getmActivity());
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewUtil.Dp2Px(getmActivity(), 50), ViewUtil.Dp2Px(getmActivity(), 50));
                lp.addRule(CENTER_IN_PARENT);
                mLayout.addView(mProgressBar, lp);
            } else {
                KChartView kChartView = view.findViewById(R.id.kchart_view);
                initKchartView(kChartView);
                kChartView.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String format(float value) {
                        return String.format("%." + scale + "f", value);
                    }
                });
                kChartView.setVisibility(View.VISIBLE);
                kChartView.setAdapter(new KChartAdapter());
            }
            views.add(view);
        }
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(views);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPagerView();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置tab栏显示内容
     *
     * @param count
     */
    private void initTextView(int count) {
        List<String> titles = Arrays.asList(this.mTitles);
        for (int i = 0; i < titles.size(); i++) {
            if (i < count) {
                TextView textView = (TextView) LayoutInflater.from(getmActivity()).inflate(R.layout.tab_kline_textview, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.weight = 1;
                textView.setLayoutParams(layoutParams);
                textView.setText(titles.get(i));
                textView.setTag(i);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isPopClick = false;
                        selectedTextView = (TextView) view;
                        int selectedTag = (int) selectedTextView.getTag();
                        type = selectedTag;
                        viewPager.setCurrentItem(selectedTag);
                        if (type == 0) {
                            tvBuy.setBackgroundResource(R.color.grey);
                            tvSell.setBackgroundResource(R.color.grey);
                            tvBuy.setEnabled(false);
                            tvSell.setEnabled(false);
                        } else {
                            tvBuy.setBackgroundResource(R.color.color_green);
                            tvSell.setBackgroundResource(R.color.typeRed);
                            tvBuy.setEnabled(true);
                            tvSell.setEnabled(true);
                        }
                    }
                });
                textViews.add(textView);
                tab.addView(textView);
            }
        }
    }

    /**
     * 设置more显示内容
     *
     * @param count
     */
    private void intMoreTab(int count) {
        List<String> titles = Arrays.asList(this.mTitles);
        for (int i = count; i < titles.size(); i++) {
            TextView textView = (TextView) LayoutInflater.from(getmActivity()).inflate(R.layout.tab_kline_textview, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(ViewUtil.Dp2Px(getmActivity(), 20), 0, 0, 0);
            textView.setText(titles.get(i));
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isPopClick = true;
                    selectedTextView = (TextView) view;
                    int selectedTag = (int) selectedTextView.getTag();
                    type = selectedTag;
                    viewPager.setCurrentItem(selectedTag);
                    popupWindow.dismiss();
                }
            });
            moreTabLayout.addView(textView);
            textViews.add(textView);

        }
    }

    /**
     * 初始化指标popwindow里的控件
     *
     * @param contentView
     */
    private void initPopChidView(View contentView) {
        moreTabLayout = contentView.findViewById(R.id.tabPop);
        indexLayout = contentView.findViewById(R.id.llIndex);
        maView = contentView.findViewById(R.id.tvMA);
        maView.setSelected(true);
        maView.setOnClickListener(this);
        bollView = contentView.findViewById(R.id.tvBOLL);
        bollView.setOnClickListener(this);
        hideMainView = contentView.findViewById(R.id.tvMainHide);
        hideMainView.setOnClickListener(this);
        macdView = contentView.findViewById(R.id.tvMACD);
        macdView.setSelected(true);
        macdView.setOnClickListener(this);
        kdjView = contentView.findViewById(R.id.tvKDJ);
        kdjView.setOnClickListener(this);
        rsiView = contentView.findViewById(R.id.tvRSI);
        rsiView.setOnClickListener(this);
        hideChildView = contentView.findViewById(R.id.tvChildHide);
        hideChildView.setSelected(false);
        hideChildView.setOnClickListener(this);
    }

    /**
     * 设置kchartview
     *
     * @param kChartView
     */
    private void initKchartView(final KChartView kChartView) {
        kChartView.setCandleSolid(true);
        kChartView.setGridRows(4);
        kChartView.setGridColumns(4);
        kChartView.setOverScrollRange(200);
        kChartView.setOnSelectedChangedListener(new BaseKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(BaseKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                WonderfulLogUtils.logi("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice());
            }
        });
    }

    /**
     * viewpager和textview的点击事件
     */
    private void setPagerView() {
        WonderfulLogUtils.logi("KLineActivitySize:", "begin");
        for (int j = 0; j < textViews.size(); j++) {
            textViews.get(j).setSelected(false);
            textViews.get(j).setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
            int tag = (int) textViews.get(j).getTag();
            if (tag == type) {
                if (isPopClick) {
                    tvMore.setText(selectedTextView.getText());
                    tvMore.setSelected(true);
                } else {
                    tvMore.setText(getString(R.string.more));
                    tvMore.setSelected(false);
                    textViews.get(j).setSelected(true);
                    Drawable home_zhang_no1 = getResources().getDrawable(
                            R.drawable.tag);
                    textViews.get(j).setCompoundDrawablesWithIntrinsicBounds(null,
                            null, null, home_zhang_no1);
                }
                View view = views.get(j);
                if (type != GlobalConstant.TAG_DIVIDE_TIME) {
                    kChartView = view.findViewById(R.id.kchart_view);
                    kChartView.setValueFormatter(new IValueFormatter() {
                        @Override
                        public String format(float value) {
                            return String.format("%." + scale + "f", value);
                        }
                    });
                    kChartView.setMAandBOLL(maView.isSelected(), bollView.isSelected());
                    kChartView.setChidType(childType);
                    kChartAdapter = (KChartAdapter) kChartView.getAdapter();
                    if (kChartAdapter.getDatas() == null || kChartAdapter.getDatas().size() == 0) {
                        loadData2();
                    }
                } else {
                    minuteChartView.setMAandBOLL(maView.isSelected(), bollView.isSelected());
                    if (isFirstLoad)
                        loadData2();
                }
            } else if (!isPopClick) {
                tvMore.setSelected(false);
            }
        }
    }

    private void loadData2() {
        if (type != GlobalConstant.TAG_DIVIDE_TIME)
            kChartView.showLoading();
        else
            mProgressBar.setVisibility(View.VISIBLE);
        Long to = System.currentTimeMillis();
        endDate = WonderfulDateUtils.getDate("HH:mm", to);
        Long from = to;
        WonderfulLogUtils.logi("miao", "type==" + type);
        switch (type) {
            case GlobalConstant.TAG_DIVIDE_TIME:
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY) - 1;
                c.set(Calendar.HOUR_OF_DAY, hour);
                String strDate = WonderfulDateUtils.getFormatTime("HH:mm", c.getTime());
                startDate = WonderfulDateUtils.getDateTransformString(strDate, "HH:mm");
                resolution = 1 + "";
                String str = WonderfulDateUtils.getFormatTime(null, c.getTime());
                from = WonderfulDateUtils.getTimeMillis(null, str);
                break;
            case GlobalConstant.TAG_ONE_MINUTE:
                from = to - 24L * 60 * 60 * 1000;//前一天数据
                resolution = 1 + "";
                break;
            case GlobalConstant.TAG_FIVE_MINUTE:
                from = to - 2 * 24L * 60 * 60 * 1000;//前两天数据
                resolution = 5 + "";
                break;
            case GlobalConstant.TAG_FIFTEEN_MINUTE:
                from = to - 6 * 24L * 60 * 60 * 1000; //前6天数据
                resolution = 15 + "";
                break;
            case GlobalConstant.TAG_THIRTY_MINUTE:
                from = to - 12 * 24L * 60 * 60 * 1000; //前12天数据
                resolution = 30 + "";
                break;
            case GlobalConstant.TAG_AN_HOUR:
                from = to - 24 * 24L * 60 * 60 * 1000;//前 24天数据
                resolution = 1 + "H";
                break;
            case GlobalConstant.TAG_DAY:
                from = to - 60 * 24L * 60 * 60 * 1000; //前60天数据
                resolution = 1 + "D";
                break;
            case GlobalConstant.TAG_WEEK:
                from = to - 730 * 24L * 60 * 60 * 1000; //前两年数据
                resolution = 1 + "W";
                break;
            case GlobalConstant.TAG_MONTH:
                from = to - 1095 * 24L * 60 * 60 * 1000; //前三年数据
                resolution = 1 + "M";
                break;
            default:
        }
        presenter.KData(symbol, from, to, resolution);
    }

    //订阅货币信息消息
    private void startTCP() {
//        EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.SUBSCRIBE_SYMBOL_THUMB, null));
    }

    public void tcpNotify() {
        //刷新类
    }

    public void resetSymbol(Currency currency) {
        isFirstLoad = true;
        views = new ArrayList<>();
//        textViews = new ArrayList<>();
//        tab.removeAllViews();
        symbol = currency.getSymbol();
        tvCurrencyName.setText(symbol);
        if (symbol != null) {
            String[] s = symbol.split("/");
            tvBuy.setText(getResources().getString(R.string.bullish));
            tvSell.setText(getResources().getString(R.string.bearish));
        }
        if (fragment != null) {
            fragment.setSymbol(symbol);
        } else {
            initRvData();
        }
        getCurrent();
    }

    @OnClick({R.id.tvSell, R.id.tvBuy, R.id.tvMore, R.id.tvIndex})
    void setListener(View view) {
        if (!MyApplication.getApp().isLogin()) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
            return;
        }
        switch (view.getId()) {
            case R.id.tvSell:
                if (mCurrency != null && !mDataOne.getText().toString().equals("")) {
                    if (tvPrice.getText().toString().equals("")) {
                        WonderfulToastUtils.showToast("请填写购买数量");
                        return;
                    }
                    int leverageInt = -1;
                    if (tvLeverage.getVisibility() == View.VISIBLE) {
                        String leverage = tvLeverage.getText().toString();
                        int fontIndex = leverage.lastIndexOf("：");
                        int lastIndex = leverage.lastIndexOf("倍");
                        leverageInt = Integer.parseInt(leverage.substring(fontIndex+1, lastIndex));
                    }
                    OptionsAddOrder optionsAddOrder;
                    if (leverageInt >= 0) {
                        optionsAddOrder = new OptionsAddOrder(SharedPreferenceInstance.getInstance().getTOKEN(),
                                "SELL", tvPrice.getText().toString(), mCurrency.getSymbol(), mDataOne.getText().toString(), "USDT", leverageInt);
                    } else {
                        optionsAddOrder = new OptionsAddOrder(SharedPreferenceInstance.getInstance().getTOKEN(),
                                "SELL", tvPrice.getText().toString(), mCurrency.getSymbol(), mDataOne.getText().toString(), "USDT");
                    }
                    optionsAddOrder.setPeriod(type);
                    presenter.addOrder(optionsAddOrder);
                }
                return;
            case R.id.tvBuy:
                if (mCurrency != null && !mDataOne.getText().toString().equals("")) {
                    if (tvPrice.getText().toString().equals("")) {
                        WonderfulToastUtils.showToast("请填写购买数量");
                        return;
                    }
                    int leverageInt = -1;
                    if (tvLeverage.getVisibility() == View.VISIBLE) {
                        String leverage = tvLeverage.getText().toString();
                        int fontIndex = leverage.lastIndexOf("：");
                        int lastIndex = leverage.lastIndexOf("倍");
                        leverageInt = Integer.parseInt(leverage.substring(fontIndex+1, lastIndex));
                    }
                    OptionsAddOrder optionsAddOrder;
                    if(leverageInt >= 0){
                        optionsAddOrder = new OptionsAddOrder(SharedPreferenceInstance.getInstance().getTOKEN(),
                                "BUY", tvPrice.getText().toString(), mCurrency.getSymbol(), mDataOne.getText().toString(), "USDT",leverageInt);
                    }else{
                        optionsAddOrder = new OptionsAddOrder(SharedPreferenceInstance.getInstance().getTOKEN(),
                                "BUY", tvPrice.getText().toString(), mCurrency.getSymbol(), mDataOne.getText().toString(), "USDT");
                    }
                    optionsAddOrder.setPeriod(type);
                    WonderfulLogUtils.logd("Coin2Coin:","Buy");
                    presenter.addOrder(optionsAddOrder);
                }
                return;
            case R.id.tvMore:
                moreTabLayout.setVisibility(View.VISIBLE);
                indexLayout.setVisibility(View.GONE);
                break;
            case R.id.tvIndex:
                moreTabLayout.setVisibility(View.GONE);
                indexLayout.setVisibility(View.VISIBLE);
                break;
            default:
        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAsDropDown(llAllTab);
        }
    }

    /**
     * socket 推送过来的信息
     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onSocketMessage(SocketResponse response) {
//
//    }

    @Override
    public void KDataFail(Integer code, String toastMessage) {

    }

    @Override
    public void KDataSuccess(JSONArray obj) {
        long tiem2 = System.currentTimeMillis();
        WonderfulLogUtils.logi("KLineActivitySize:", (tiem2 - tiem1) + " ");
        DataParse kData = new DataParse();
        switch (type) {
            case GlobalConstant.TAG_DIVIDE_TIME: // 分时图
                mProgressBar.setVisibility(View.GONE);
                try {
                    kData.parseMinutes(obj, (float) mCurrency.getLastDayClose());
                    ArrayList<MinutesBean> objList = kData.getDatas();
                    if (objList != null && objList.size() > 0) {
                        ArrayList<MinuteLineEntity> minuteLineEntities = new ArrayList<>();
                        for (int i = 0; i < objList.size(); i++) {
                            MinuteLineEntity minuteLineEntity = new MinuteLineEntity();
                            MinutesBean minutesBean = objList.get(i);
                            minuteLineEntity.setAvg(minutesBean.getAvprice()); // 成交价
                            minuteLineEntity.setPrice(minutesBean.getCjprice());
                            minuteLineEntity.setTime(WonderfulDateUtils.getDateTransformString(minutesBean.getTime(), "HH:mm"));
                            minuteLineEntity.setVolume(minutesBean.getCjnum());
                            minuteLineEntity.setClose(minutesBean.getClose());
                            minuteLineEntities.add(minuteLineEntity);
                        }
                        if (isFirstLoad) { // 避免界面重绘
                            DataHelper.calculateMA30andBOLL(minuteLineEntities);
                            minuteChartView.initData(minuteLineEntities,
                                    startDate,
                                    endDate,
                                    null,
                                    null,
                                    (float) mCurrency.getLow(), maView.isSelected());
                            isFirstLoad = false;
                        }
                    }
                } catch (Exception e) {
                    WonderfulToastUtils.showToast(getString(R.string.parse_error));
                }
                break;
            default:
                try {
                    kData.parseKLine(obj, type);
                    kLineDatas = kData.getKLineDatas();
                    if (kLineDatas != null && kLineDatas.size() > 0) {
                        ArrayList<KLineEntity> kLineEntities = new ArrayList<>();
                        kLineEntities.clear();
                        for (int i = 0; i < kLineDatas.size(); i++) {
                            KLineEntity lineEntity = new KLineEntity();
                            KLineBean kLineBean = kLineDatas.get(i);
                            lineEntity.setTime(kLineBean.getTime());
                            lineEntity.setDate(kLineBean.getDate());
                            lineEntity.setOpen(kLineBean.getOpen());
                            lineEntity.setClose(kLineBean.getClose());
                            lineEntity.setHigh(kLineBean.getHigh());
                            lineEntity.setLow(kLineBean.getLow());
                            lineEntity.setVolume(kLineBean.getVol());
                            kLineEntities.add(lineEntity);
                        }
                        WonderfulLogUtils.logi("miao", kLineDatas.get(0).getClose() + "--" + kLineDatas.get(0).getHigh() + "--" + kLineDatas.get(0).getLow() + "--" + kLineDatas.get(0).getOpen() + "--" + kLineDatas.get(0).getVol());
                        WonderfulLogUtils.logi("miao", kLineEntities.size() + "--");

                        kChartAdapter.addFooterData(DataHelper.getALL(getmActivity(), kLineEntities));
                        kChartView.refreshEnd();
                        kChartView.setScrollX(-200);
                    } else {
                        kChartView.refreshEnd();
                    }
                } catch (Exception e) {
                    WonderfulToastUtils.showToast(getString(R.string.parse_error));
                }
                break;
        }
    }

    @Override
    public void allCurrencySuccess(List<Currency> obj) {

    }

    @Override
    public void allCurrencyFail(Integer code, String toastMessage) {

    }

    @Override
    public void getAssetPNLSuccess(AssetEntity assetEntity) {
    }

    @Override
    public void addOrderSuccess(String message) {
        WonderfulLogUtils.logd("Coin2Coin:","交易成功");
        WonderfulToastUtils.showToast("交易成功");
        tvPrice.setText("");
        getOptionsAsset();
        fragment.refresh();
    }

    @Override
    public void addOrderFail(String message) {
        WonderfulToastUtils.showToast(message);
    }

    @Override
    public void getCoinThumbSuccess(String response) {
        if (type == GlobalConstant.TAG_DIVIDE_TIME) {
            //分时
            try {
                Currency temp = new Gson().fromJson(response, Currency.class);
                CurrencyK tempK = new Gson().fromJson(response, CurrencyK.class);

                for (Currency currency : currencies) {
                    if (temp.getSymbol().equals(currency.getSymbol())) {
                        temp.setLow(tempK.getLow24());
                        temp.setHigh(tempK.getHigh24());
                        temp.setChg(tempK.getChg24());
                        temp.setChange(tempK.getChange24());
                        temp.setOpen(tempK.getOpen24());
                        temp.setVolume(tempK.getVolume24());
                        Currency.shallowClone(currency, temp);
                        break;
                    }
                }
                setCurrentcy(currencies);
                //现价
                String strDataOne = String.valueOf(mCurrency.getClose());
                BigDecimal bg1 = new BigDecimal(strDataOne);
                String closeS = bg1.toPlainString();

                MinuteLineEntity minuteLineEntity = new MinuteLineEntity();
                minuteLineEntity = (MinuteLineEntity) minuteChartView.getItem(minuteChartView.getItemSize() - 1);
                minuteLineEntity.setClosePrices(Float.valueOf(closeS));
                minuteChartView.refreshLastPoint(minuteLineEntity);
            } catch (Exception e) {

            }
        } else {
            try {
                //k线图数据
                Currency temp = new Gson().fromJson(response, Currency.class);
                CurrencyK tempK = new Gson().fromJson(response, CurrencyK.class);

                for (Currency currency : currencies) {
                    if (temp.getSymbol().equals(currency.getSymbol())) {
                        temp.setLow(tempK.getLow24());
                        temp.setHigh(tempK.getHigh24());
                        temp.setChg(tempK.getChg24());
                        temp.setChange(tempK.getChange24());
                        temp.setOpen(tempK.getOpen24());
                        temp.setVolume(tempK.getVolume24());
                        Currency.shallowClone(currency, temp);
                        break;
                    }
                }
                setCurrentcy(currencies);
                PushLineDataNewBean kBean = new Gson().fromJson(response, PushLineDataNewBean.class);
                if (kBean != null) {
                    if (kBean.getSymbol().equals(symbol)) {
                        PushLineDataBean pushLineDataBean = new PushLineDataBean();
                        pushLineDataBean.setClosePrice(kBean.getClose());
                        pushLineDataBean.setHighestPrice(kBean.getHigh());
                        pushLineDataBean.setLowestPrice(kBean.getLow());
                        pushLineDataBean.setOpenPrice(kBean.getOpen());
                        pushLineDataBean.setTime(kBean.getTime());
                        pushLineDataBean.setSymbol(kBean.getSymbol());
                        pushLineDataBean.setTurnover(kBean.getTurnover());
                        pushLineDataBean.setVolume(kBean.getVolume());

                        KLineBean kLineBean = new DataParse().parseKLine(pushLineDataBean);
                        ArrayList<KLineEntity> kLineEntities = new ArrayList<>();
                        KLineEntity lineEntity = new KLineEntity();
                        lineEntity.setTime(kLineBean.getTime());
                        lineEntity.setDate(kLineBean.getDate());
                        lineEntity.setOpen(kLineBean.getOpen());
                        lineEntity.setClose(kLineBean.getClose());
                        lineEntity.setHigh(kLineBean.getHigh());
                        lineEntity.setLow(kLineBean.getLow());
                        lineEntity.setVolume(kLineBean.getVol());
                        int count = kChartAdapter.getCount();
                        kLineEntities.add(lineEntity);
                        if (count > 0) {
                            long time = kChartAdapter.getDatas().get(count - 1).getTime();
                            WonderfulLogUtils.logd("KTime:",time+"");
                            WonderfulLogUtils.logd("KTime:","2 "+kBean.getTime()+"");
                            if (kBean.getTime() > time) {
                                kChartAdapter.addFooterData(DataHelper.getALL(getmActivity(), kLineEntities));
                            } else {
                                kChartAdapter.updateFooterData(DataHelper.getALL(getmActivity(), kLineEntities));
                            }
                        }
                        Log.i("k线实时==", kChartAdapter.getCount() + "--" + count);
                        kChartView.refreshEnd();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
//            WonderfulToastUtils.showToast(getResources().getString(R.string.data_error));
            }
        }
    }

    @Override
    public void allEryuanCurrencySuccess(List<Currency> obj) {

    }

    @Override
    public void setPresenter(KlineContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private boolean screenTimeType(String period) {
        String typeName = "";
        switch (type) {
            case GlobalConstant.TAG_ONE_MINUTE:
                typeName = "1min";
                break;
            case GlobalConstant.TAG_FIVE_MINUTE:
                typeName = "5min";
                break;
            case GlobalConstant.TAG_AN_HOUR:
                typeName = "1hour";
                break;
            case GlobalConstant.TAG_DAY:
                typeName = "1day";
                break;
            case GlobalConstant.TAG_FIFTEEN_MINUTE:
                typeName = "15min";
                break;
            case GlobalConstant.TAG_THIRTY_MINUTE:
                typeName = "30min";
                break;
            case GlobalConstant.TAG_WEEK:
                typeName = "1week";
                break;
            case GlobalConstant.TAG_MONTH:
                typeName = "1month";
                break;
            default:
        }
        if (typeName.equals(period)) {
            return true;
        } else {
            return false;
        }
    }

    private void getOptionsAsset() {
        RemoteDataSource.getInstance().eryuanWalletWallet(SharedPreferenceInstance.getInstance().getTOKEN(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                coinContracts.clear();
                coinContracts.addAll((List<CoinContract>) obj);
                balanceUsd = 0.0;
                presentBalance = 0.0;
                for (CoinContract coinContract : coinContracts) {
                    balanceUsd += coinContract.getBalance() + coinContract.getFrozenBalance();
                    if(coinContract.getCoin().getName().equals("USDT")){
                        releaseBalance += coinContract.getBalance();
                    }
                    if(coinContract.getCoin().getName().equals("USDT(赠)")){
                        presentBalance += coinContract.getBalance();
                    }
                }
                tvPrice.setHint(getResources().getString(R.string.balances)+"：" + new DecimalFormat("#0.00").format(balanceUsd) + "  USDT");
//                tvPrice.setHint(getResources().getString(R.string.balances)+"：" + new DecimalFormat("#0.00").format(balanceUsd) + "  USDT ;  "
//                        + getResources().getString(R.string.present_balance)+"：" + new DecimalFormat("#0.00").format(presentBalance) + "  USDT");
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                WonderfulToastUtils.showToast(toastMessage);
            }
        });
    }

}
