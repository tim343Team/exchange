package com.bibi.ui.main.home;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bibi.adapter.PagerSimpleAdapter;
import com.bibi.base.BaseFragment;
import com.bibi.customview.CustomViewPager;
import com.bibi.customview.NoScrollViewPager;
import com.bibi.customview.intercept.WonderfulViewPager;
import com.bibi.entity.User;
import com.bibi.ui.defi.DeFiActivity;
import com.bibi.ui.main.market.BTCMarketFragment;
import com.bibi.ui.main.market.ETHMarketFragment;
import com.bibi.ui.main.market.FavoriteFragment;
import com.bibi.ui.main.market.SearchSymbolActivity;
import com.bibi.ui.message.MessageActivity;
import com.bibi.ui.my_order.OrderFragment;
import com.bibi.ui.my_promotion.PromotionActivity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;

import com.bibi.ui.kline_spot.SKlineActivity;

import butterknife.OnClick;

import com.bibi.R;
import com.bibi.holder.BannerViewHolder;
import com.bibi.ui.dialog.GestureDialogFragment;
import com.bibi.ui.fiat_exchange.FiatExchangeActivity;
import com.bibi.ui.main.MainActivity;
import com.bibi.ui.main.MainContract;
import com.bibi.ui.main.presenter.CommonPresenter;
import com.bibi.ui.main.presenter.ICommonView;
import com.bibi.ui.kline.KlineActivity;
import com.bibi.ui.login.LoginActivity;
import com.bibi.ui.message_detail.MessageDetailActivity;
import com.bibi.ui.myinfo.MyInfoActivity;
import com.bibi.ui.notice.NoticeActivity;
import com.bibi.ui.setting.ContractActivity;
import com.bibi.ui.setting.HelpActivity;
import com.bibi.adapter.BannerImageLoader;
import com.bibi.adapter.HomeAdapter;
import com.bibi.app.GlobalConstant;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseTransFragment;
import com.bibi.entity.BannerEntity;
import com.bibi.entity.Currency;
import com.bibi.entity.LunBoBean;
import com.bibi.entity.Message;
import com.bibi.app.UrlFactory;
import com.bibi.utils.ScreenUtils;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

import com.hacknife.carouselbanner.CoolCarouselBanner;
import com.hacknife.carouselbanner.interfaces.CarouselImageFactory;
import com.hacknife.carouselbanner.interfaces.OnCarouselItemClickListener;
import com.m7.imkfsdk.utils.PermissionUtils;
import com.sunfusheng.marqueeview.MarqueeView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.view.BannerViewPager;

import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.bibi.app.Injection;

import okhttp3.Request;
import zendesk.core.AnonymousIdentity;
import zendesk.core.Identity;
import zendesk.core.Zendesk;
import zendesk.support.requestlist.RequestListActivity;

import static com.bibi.app.MyApplication.list;

/**
 * Created by Administrator on 2018/1/7.
 */

public class OneFragment extends BaseTransFragment implements com.bibi.ui.main.MainContract.OneView, ICommonView {
    public static final String TAG = OneFragment.class.getSimpleName();
    @BindView(R.id.banner)
    MZBannerView banner;
    @BindView(R.id.line_fabi)
    LinearLayout line_fabi;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.line_invite)
    LinearLayout line_invite;
    @BindView(R.id.line_defi)
    LinearLayout line_defi;
    @BindView(R.id.line_lock_up)
    LinearLayout line_lock_up;
    @BindView(R.id.line_service)
    LinearLayout line_service;
    @BindView(R.id.vContentPager)
    CustomViewPager vpPager;
    //    @BindView(R.id.mRecyclerView)
//    RecyclerView mRecyclerView; // 涨幅榜
    @BindView(R.id.item_home_coin_1)
    TextView itemHomeCoin1;
    @BindView(R.id.item_home_coin_2)
    TextView itemHomeCoin2;
    @BindView(R.id.item_home_coin_3)
    TextView itemHomeCoin3;
    @BindView(R.id.item_home_chg_1)
    TextView itemHomeChg1;
    @BindView(R.id.item_home_chg_2)
    TextView itemHomeChg2;
    @BindView(R.id.item_home_chg_3)
    TextView itemHomeChg3;
    @BindView(R.id.item_home_close_1)
    TextView itemHomeClose1;
    @BindView(R.id.item_home_close_2)
    TextView itemHomeClose2;
    @BindView(R.id.item_home_close_3)
    TextView itemHomeClose3;
    @BindView(R.id.kDataText_1)
    TextView kDataText1;
    @BindView(R.id.kDataText_2)
    TextView kDataText2;
    @BindView(R.id.kDataText_3)
    TextView kDataText3;
    @BindView(R.id.item_1)
    LinearLayout item1;
    @BindView(R.id.item_2)
    LinearLayout item2;
    @BindView(R.id.item_3)
    LinearLayout item3;
    @BindView(R.id.vTab1)
    View vTab1;
    @BindView(R.id.vTab2)
    View vTab2;
    @BindView(R.id.vTab3)
    View vTab3;

    Unbinder unbinder;
    private List<String> imageUrls = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private HomeFragment homeFragmentHighs;
    private HomeFragment homeFragmentBibi;
    private HomeFragment homeFragmentNew;

    private List<Currency> currencies = new ArrayList<>();//推荐
    private List<Currency> currenciesTwo = new ArrayList<>(); //主页涨幅榜列表
    private List<Currency> currenciesThree = new ArrayList<>(); //主页成交额列表
    private List<Currency> currenciesFour = new ArrayList<>(); //主页新币榜列表
    private MainContract.OnePresenter presenter;

    private CommonPresenter commonPresenter;
    private String sysAdvertiseLocation = "0";

    @OnClick(R.id.tvMessage)
    public void startMessage() {
        NoticeActivity.actionStart(getmActivity());
    }

    @OnClick(R.id.item_1)
    public void startItem1() {
        try {
            if (currencies.size() > 0) {
                if (currencies.get(0).getExchangeable() == 1) {
                    SKlineActivity.actionStart(getActivity(), currencies.get(0).getSymbol());
                } else {
                    WonderfulToastUtils.showToast("暂未开放");
                }
            }
        } catch (Exception e) {

        }
    }

    @OnClick(R.id.etSearch)
    public void search() {
        if (MyApplication.getApp().isLogin()) {
            SearchSymbolActivity.actionStart(getActivity());
        } else {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
        }
    }

    @OnClick(R.id.item_2)
    public void startItem2() {
        try {
            if (currencies.size() > 1) {
                if (currencies.get(1).getExchangeable() == 1) {
                    SKlineActivity.actionStart(getActivity(), currencies.get(1).getSymbol());
                } else {
                    WonderfulToastUtils.showToast("暂未开放");
                }
            }
        } catch (Exception e) {

        }
    }

    @OnClick(R.id.item_3)
    public void startItem3() {
        try {
            if (currencies.size() > 2) {
                if (currencies.get(2).getExchangeable() == 1) {
                    SKlineActivity.actionStart(getActivity(), currencies.get(2).getSymbol());
                } else {
                    WonderfulToastUtils.showToast("暂未开放");
                }
            }
        } catch (Exception e) {

        }
    }

    @OnClick({R.id.llTab1, R.id.llTab2, R.id.llTab3})
    public void selectTab(View view) {
        switch (view.getId()) {
            case R.id.llTab1:
                vTab1.setVisibility(View.VISIBLE);
                vTab2.setVisibility(View.GONE);
                vTab3.setVisibility(View.GONE);
                vpPager.setCurrentItem(0);
                break;
            /*case R.id.llTab2:
                vTab1.setVisibility(View.GONE);
                vTab2.setVisibility(View.VISIBLE);
                vTab3.setVisibility(View.GONE);
                vpPager.setCurrentItem(1);
                break;*/
            case R.id.llTab3:
                vTab1.setVisibility(View.GONE);
                vTab2.setVisibility(View.GONE);
                vTab3.setVisibility(View.VISIBLE);
                vpPager.setCurrentItem(1);
                break;
            default:
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_one_new;
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.start();
//        banner.startAutoPlay();
        /*if (MyApplication.getApp().isLogin() && !TextUtils.isEmpty(MyApplication.getApp().getCurrentUser().getAvatar())) {
            Glide.with(getActivity()).load(MyApplication.getApp().getCurrentUser().getAvatar()).into(iv_user_info);
        } else {
//            Glide.with(getActivity()).load(R.mipmap.icon_default_header).into(iv_user_info);
        }*/
    }

    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.pause();
//        banner.stopAutoPlay();
    }

    private void dialogShow2() {
        GestureDialogFragment dialogFragment = new GestureDialogFragment();
        dialogFragment.setCallBack(new GestureDialogFragment.CallBack() {
            @Override
            public void callSure(boolean isCheck) {
                if (MyApplication.getApp().isLogin()) {
                    if (isCheck) {
                        SharedPreferenceInstance.getInstance().saveTishi(isCheck);
                    }
                    MyInfoActivity.actionStart(getActivity());
                } else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });
        dialogFragment.show(getmActivity().getSupportFragmentManager(), "");

    }

    //TODO 拉出侧边栏
//    @OnClick(R.id.iv_user_info)
//    public void showDrawerLayout() {
//        ((MainActivity) getActivity()).getDlRoot(MainActivity.MENU_TYPE_USER).openDrawer(Gravity.LEFT);
//    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
//        com.hacknife.carouselbanner.Banner.init(new CarouselImageFactory() {
//            @Override
//            public void onLoadFactory(String url, ImageView view) {
//                Glide.with(getmActivity()).load(url).centerCrop().into(view);
//            }
//        });
        new CommonPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        line_fabi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WonderfulToastUtils.showToast("暂未开放,敬请期待");
                startActivity(new Intent(getActivity(), FiatExchangeActivity.class));
            }
        });
        line_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromotionActivity.actionStart(getActivity());
            }
        });
        line_defi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeFiActivity.actionStart(getActivity());
            }
        });
        line_lock_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WonderfulToastUtils.showToast("暂未开放");
//                MainActivity parentActivity = (MainActivity) getActivity();
//                parentActivity.selecte(parentActivity.llThree, 3);
            }
        });
        line_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 新模块
                handlePermission();
            }
        });
        if (WonderfulStringUtils.isEmpty(SharedPreferenceInstance.getInstance().getLockPwd())) {
            if (SharedPreferenceInstance.getInstance().getTishi()) {
                return;
            }
            dialogShow2();
        }
    }

    @Override
    protected void obtainData() {
        presenter.banners(sysAdvertiseLocation);
    }

    @Override
    protected void fillWidget() {
        initRvContent();
    }

    class MyAdapter extends PagerAdapter {
        private List<Currency> lists = new ArrayList<>();

        @Override
        public int getCount() {
            int size = currenciesTwo.size();
            if (size == 0) {
                return 0;
            }
            int i = size % 3;
            int a = size / 3;
            if (i > 0) {
                a = a + 1;
            }
            return a;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_home_viewpage, null, false);
            //第一组
            LinearLayout line_one = inflate.findViewById(R.id.line_one);
            TextView tvCurrencyName = inflate.findViewById(R.id.tvCurrencyName);
            ImageView ivCollect = inflate.findViewById(R.id.ivCollect);
            TextView tvClose = inflate.findViewById(R.id.tvClose);
            TextView tvAddPercent = inflate.findViewById(R.id.tvAddPercent);
            TextView tvVol = inflate.findViewById(R.id.tvVol);

            //第二组
            LinearLayout line_two = inflate.findViewById(R.id.line_two);
            TextView tvCurrencyName1 = inflate.findViewById(R.id.tvCurrencyName1);
            ImageView ivCollect1 = inflate.findViewById(R.id.ivCollect1);
            TextView tvClose1 = inflate.findViewById(R.id.tvClose1);
            TextView tvAddPercent1 = inflate.findViewById(R.id.tvAddPercent1);
            TextView tvVol1 = inflate.findViewById(R.id.tvVol1);
            //第三组
            LinearLayout line_three = inflate.findViewById(R.id.line_three);
            TextView tvCurrencyName2 = inflate.findViewById(R.id.tvCurrencyName2);
            ImageView ivCollect2 = inflate.findViewById(R.id.ivCollect2);
            TextView tvClose2 = inflate.findViewById(R.id.tvClose2);
            TextView tvAddPercent2 = inflate.findViewById(R.id.tvAddPercent2);
            TextView tvVol2 = inflate.findViewById(R.id.tvVol2);
            int star = position * 3;
            int end = (position + 1) * 3;
            lists.clear();
            for (int i = 0; i <= currenciesTwo.size(); i++) {
                if (i >= star && i < end && i < currenciesTwo.size()) {
                    lists.add(currenciesTwo.get(i));
                }
            }
            for (int i = 0; i < lists.size(); i++) {
                if (i == 0) {
                    final Currency currency = lists.get(i);
                    line_one.setVisibility(View.VISIBLE);
                    ivCollect.setImageResource(currency.isCollect() ? R.mipmap.icon_collect_yes : R.mipmap.icon_collect_no);
                    tvCurrencyName.setText(currency.getSymbol());
                    tvClose.setText(WonderfulMathUtils.getRundNumber(currency.getClose(), 2, null));
                    tvAddPercent.setText((currency.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(currency.getChg() * 100, 2, "########0.") + "%");
                    tvVol.setText("≈" + WonderfulMathUtils.getRundNumber(currency.getClose() * (currency.getBaseUsdRate() == null ? 0 : currency.getBaseUsdRate()) * MainActivity.rate, 2, null) + "CNY");
                    tvClose.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    tvAddPercent.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    line_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currency.getExchangeable() == 1) {
                                SKlineActivity.actionStart(getActivity(), currency.getSymbol());
                            } else {
                                WonderfulToastUtils.showToast("暂未开放");
                            }
                        }
                    });
                }
                if (i == 1) {
                    final Currency currency = lists.get(i);
                    line_two.setVisibility(View.VISIBLE);
                    ivCollect1.setImageResource(currency.isCollect() ? R.mipmap.icon_collect_yes : R.mipmap.icon_collect_no);
                    tvCurrencyName1.setText(currency.getSymbol());
                    tvClose1.setText(WonderfulMathUtils.getRundNumber(currency.getClose(), 2, null));
                    tvAddPercent1.setText((currency.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(currency.getChg() * 100, 2, "########0.") + "%");
                    tvVol1.setText("≈" + WonderfulMathUtils.getRundNumber(currency.getClose() * (currency.getBaseUsdRate() == null ? 0 : currency.getBaseUsdRate()) * MainActivity.rate, 2, null) + "CNY");
                    tvClose1.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    tvAddPercent1.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    line_two.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currency.getExchangeable() == 1) {
                                SKlineActivity.actionStart(getActivity(), currency.getSymbol());
                            } else {
                                WonderfulToastUtils.showToast("暂未开放");
                            }
                        }
                    });
                }
                if (i == 2) {
                    final Currency currency = lists.get(i);
                    line_three.setVisibility(View.VISIBLE);
                    ivCollect2.setImageResource(currency.isCollect() ? R.mipmap.icon_collect_yes : R.mipmap.icon_collect_no);
                    tvCurrencyName2.setText(currency.getSymbol());
                    tvClose2.setText(WonderfulMathUtils.getRundNumber(currency.getClose(), 2, null));
                    tvAddPercent2.setText((currency.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(currency.getChg() * 100, 2, "########0.") + "%");
                    tvVol2.setText("≈" + WonderfulMathUtils.getRundNumber(currency.getClose() * (currency.getBaseUsdRate() == null ? 0 : currency.getBaseUsdRate()) * MainActivity.rate, 2, null) + "CNY");
                    tvClose2.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    tvAddPercent2.setTextColor(currency.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(), R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
                    line_three.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currency.getExchangeable() == 1) {
                                SKlineActivity.actionStart(getActivity(), currency.getSymbol());
                            } else {
                                WonderfulToastUtils.showToast("暂未开放");
                            }
                        }
                    });
                }
            }
            container.addView(inflate);
            return inflate;
        }

    }

    private void initRvContent() {
        addFragments();
        vpPager.setOffscreenPageLimit(0);
        vpPager.setAdapter(new PagerSimpleAdapter(getmActivity().getSupportFragmentManager(), fragments));
    }

    private void addFragments() {
        fragments.clear();
        fragments.add(homeFragmentHighs = HomeFragment.getInstance(1));
        fragments.add(homeFragmentBibi = HomeFragment.getInstance(2));
        fragments.add(homeFragmentNew = HomeFragment.getInstance(3));
    }

    @Override
    protected void loadData() {
        notifyData();
//        presenter.banners(sysAdvertiseLocation);
        getMessage();

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    @Override
    public void setPresenter(MainContract.OnePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setPresenter(CommonPresenter presenter) {
        this.commonPresenter = presenter;
    }

    private MyAdapter adapter2;

    public void dataLoadedFour(List<Currency> currencie) {
        adapter2 = new MyAdapter();
        this.currenciesFour.clear();
        this.currenciesFour.addAll(currencie);
        if (homeFragmentNew != null) {
            homeFragmentNew.dataLoaded(currenciesFour);
        }
    }

    public void dataLoadedThree(List<Currency> currencie) {
        adapter2 = new MyAdapter();
        this.currenciesThree.clear();
        this.currenciesThree.addAll(currencie);
        if (homeFragmentBibi != null) {
            homeFragmentBibi.dataLoaded(currenciesThree);
        }
    }

    public void dataLoadedTwo(List<Currency> currencie) {
        adapter2 = new MyAdapter();
        this.currenciesTwo.clear();
        this.currenciesTwo.addAll(currencie);
        if (homeFragmentHighs != null) {
            homeFragmentHighs.dataLoaded(currenciesTwo);
        }
    }

    public void dataLoaded(List<Currency> currencies) {
        this.currencies.clear();
        this.currencies.addAll(currencies);
//        this.currenciesTwo.clear();
//        this.currenciesTwo.addAll(changeRank);
//        adapter2 = new MyAdapter();
//        //刷新列表
//        if (homeFragmentHighs != null) {
//            homeFragmentHighs.dataLoaded(currenciesTwo);
//        }
//        if (homeFragmentBibi != null) {
//            homeFragmentBibi.dataLoaded(currenciesThree);
//        }
//        if (homeFragmentNew != null) {
//            homeFragmentNew.dataLoaded(currenciesFour);
//        }
        //推荐数据
        for (int i = 0; i < currencies.size(); i++) {
            if (i == 0) {
                Currency item = currencies.get(i);
                item1.setVisibility(View.VISIBLE);
                itemHomeCoin1.setText(item.getSymbol());
                itemHomeChg1.setText((item.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(item.getChg() * 100, 2, "########0.") + "%");
                String format = new DecimalFormat("#0.00").format(item.getClose());
                BigDecimal bg = new BigDecimal(format);
                String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                itemHomeClose1.setText(item.getCloseStr());
                kDataText1.setText("≈" + item.getCnyPrice() + "CNY");
            } else if (i == 1) {
                Currency item = currencies.get(i);
                itemHomeCoin2.setText(item.getSymbol());
                itemHomeChg2.setText((item.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(item.getChg() * 100, 2, "########0.") + "%");
                String format = new DecimalFormat("#0.00").format(item.getClose());
                BigDecimal bg = new BigDecimal(format);
                String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                itemHomeClose2.setText(item.getCloseStr());
                item2.setVisibility(View.VISIBLE);
                kDataText2.setText("≈" + item.getCnyPrice() + "CNY");
            } else if (i == 2) {
                Currency item = currencies.get(i);
                itemHomeCoin3.setText(item.getSymbol());
                itemHomeChg3.setText((item.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(item.getChg() * 100, 2, "########0.") + "%");
                String format = new DecimalFormat("#0.00").format(item.getClose());
                BigDecimal bg = new BigDecimal(format);
                String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                itemHomeClose3.setText(item.getCloseStr());
                item3.setVisibility(View.VISIBLE);
                kDataText3.setText("≈" + item.getCnyPrice() + "CNY");
            } else {
                break;
            }
        }
    }

    private int mNum = 0;


    @Override
    public void deleteFail(Integer code, String toastMessage) {
        if (code == GlobalConstant.TOKEN_DISABLE1) {
            LoginActivity.actionStart(getActivity());
        } else {
            WonderfulCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
        }
    }

    @Override
    public void deleteSuccess(String obj, int position) {
        this.currencies.get(position).setCollect(false);
        if (adapter2 != null) {
            adapter2.notifyDataSetChanged();
        }
    }

    @Override
    public void addFail(Integer code, String toastMessage) {
        if (code == GlobalConstant.TOKEN_DISABLE1) {
            LoginActivity.actionStart(getActivity());
        } else {
            WonderfulCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
        }
    }

    @Override
    public void getSymbolSuccess(List<Currency> cirrency) {

    }

    @Override
    public void addSuccess(String obj, int position) {
        this.currencies.get(position).setCollect(true);
        if (adapter2 != null) {
            adapter2.notifyDataSetChanged();
        }
    }

    public void notifyData() {
        if (adapter2 != null) {
            adapter2.notifyDataSetChanged();
        }
    }

    @Override
    public void bannersSuccess(final List<BannerEntity> obj) {
        //轮播图回执
        if (obj == null) {
            return;
        }
        imageUrls.clear();
        for (BannerEntity bannerEntity : obj) {
            imageUrls.add(bannerEntity.getUrl());
        }
        if (imageUrls.size() > 0) {
            banner.setPages(imageUrls, new MZHolderCreator<BannerViewHolder>() {
                @Override
                public BannerViewHolder createViewHolder() {
                    return new BannerViewHolder();
                }
            });
            banner.start();
        }
    }

    @Override
    public void bannersFail(Integer code, String toastMessage) {
        //do nothing
    }

    public void tcpNotify() {
        //刷新列表
        if (homeFragmentHighs != null) {
            homeFragmentHighs.dataLoaded(currenciesTwo);
        }
        if (homeFragmentBibi != null) {
            homeFragmentBibi.dataLoaded(currenciesThree);
        }
        if (homeFragmentNew != null) {
            homeFragmentNew.dataLoaded(currenciesFour);
        }
        if (adapter2 != null) {
            adapter2.notifyDataSetChanged();
        }
        if (currencies != null && currencies.size() > 0) {
            for (int i = 0; i < currencies.size(); i++) {
                String format = new DecimalFormat("#0.00").format(currencies.get(i).getClose());
                BigDecimal bg = new BigDecimal(format);
                String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
                if (i == 0) {
                    itemHomeClose1.setText(currencies.get(i).getCloseStr());
                    kDataText1.setText("≈" + currencies.get(i).getCnyPrice() + "CNY");
                    itemHomeChg1.setText((currencies.get(i).getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(currencies.get(i).getChg() * 100, 2, "########0.") + "%");
                } else if (i == 1) {
                    itemHomeClose2.setText(currencies.get(i).getCloseStr());
                    kDataText2.setText("≈" + currencies.get(i).getCnyPrice() + "CNY");
                    itemHomeChg2.setText((currencies.get(i).getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(currencies.get(i).getChg() * 100, 2, "########0.") + "%");
                } else if (i == 2) {
                    itemHomeClose3.setText(currencies.get(i).getCloseStr());
                    kDataText3.setText("≈" + currencies.get(i).getCnyPrice() + "CNY");
                    itemHomeChg3.setText((currencies.get(i).getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(currencies.get(i).getChg() * 100, 2, "########0.") + "%");
                } else {
                    break;
                }
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (marqueeView != null) {
                marqueeView.stopFlipping();
            }
        } else {
            if (marqueeView != null) {
                if (!marqueeView.isFlipping()) {
                    marqueeView.startFlipping();
                }
            }
        }
    }

    /**
     * 文件写入权限 （初始化需要写入文件，点击在线客服按钮之前需打开文件写入权限）
     * 读取设备 ID 权限 （初始化需要获取用户的设备 ID）
     */
    private void handlePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.hasAlwaysDeniedPermission(getmActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                PermissionUtils.requestPermissions(getmActivity(), 0x11, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        User user = MyApplication.getApp().getCurrentUser();
                        Identity identity = new AnonymousIdentity.Builder()
                                .withNameIdentifier(user.getId() + "")
                                .build();
                        Zendesk.INSTANCE.setIdentity(identity);
                        Intent requestActivityIntent = RequestListActivity.builder()
                                .intent(getmActivity());
                        startActivity(requestActivityIntent);
                        //7room客服
//                        initSdk();
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        Toast.makeText(getmActivity(), R.string.notpermession, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    }
                });
            }
        }
    }

    private void getMessage() {
        WonderfulOkhttpUtils.post().url(UrlFactory.getMessageUrl())
                .addParams("pageNo", 1 + "").addParams("pageSize", "100")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        List<Message> messages = new Gson().fromJson(object.getJSONObject("data").getJSONArray("content").toString(), new TypeToken<List<Message>>() {
                        }.getType());
                        messageList.clear();
                        messageList.addAll(messages);
                        setMarqueeView(messageList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                MessageActivity.actionStart(getmActivity());
            }
        });
    }

    private List<Message> messageList = new ArrayList<>();
    private List<String> info = new ArrayList<>();
    private List<Integer> infoss = new ArrayList<>();

    private void setMarqueeView(List<Message> messageList) {
        info.clear();
        int code = SharedPreferenceInstance.getInstance().getLanguageCode();
        if (code == 1) {
            //中文
            for (int i = 0; i < messageList.size(); i++) {
                Message message = messageList.get(i);
                /* if (isContainChinese(message.getTitle())) {*/
                String str = "";
                if (message.getTitle().length() > 15) {
                    str = message.getTitle();
                    str = str.substring(0, 15);
                    info.add(str + "...");
                } else {
                    info.add(message.getTitle());
                }

                infoss.add(i);
                /*}*/
            }

        } else {
            for (int i = 0; i < messageList.size(); i++) {
                Message message = messageList.get(i);
                if (!isContainChinese(message.getTitle())) {
                    info.add(message.getTitle());
                    infoss.add(i);
                }
            }
        }
        if (info.size() > 0) {
            marqueeView.startWithList(info);
        }
    }

    static Pattern p = Pattern.compile("[\u4e00-\u9fa5]");

    public static boolean isContainChinese(String str) {
        Matcher m = p.matcher(str);
        return m.find();
    }


}
