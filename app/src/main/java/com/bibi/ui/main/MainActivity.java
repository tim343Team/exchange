package com.bibi.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bibi.entity.User;
import com.bibi.ui.dialog.CommonDialogFragment;
import com.bibi.ui.main.drawer.Favorite2Fragment;
import com.bibi.ui.main.market.TwoRootFragment;
import com.bibi.ui.main.market.TwoRootPresenter;
import com.bibi.ui.main.options.OptionsFragment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import com.bibi.R;
import com.bibi.ui.main.drawer.BTCMarket2Fragment;
import com.bibi.ui.main.drawer.ETHMarket2Fragment;
import com.bibi.ui.main.drawer.USDTMarket2Fragment;
import com.bibi.ui.main.home.OneFragment;
import com.bibi.ui.main.management.ManagementFragment;
import com.bibi.ui.main.mine.MineFragment;
import com.bibi.ui.main.mine.MinePresenter;
import com.bibi.ui.main.presenter.MainPresenter;
import com.bibi.ui.main.home.OnePresenter;
import com.bibi.adapter.DrawerListener;
import com.bibi.adapter.PagerAdapter;
import com.bibi.app.GlobalConstant;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseFragment;
import com.bibi.base.BaseTransFragmentActivity;
import com.bibi.entity.ChatTable;
import com.bibi.entity.Currency;
import com.bibi.entity.Favorite;
import com.bibi.entity.Vision;
import com.bibi.app.UrlFactory;
import com.bibi.socket.ISocket;
import com.bibi.ui.login.LoginActivity;
import com.bibi.ui.main.trade.ThreeFragment;
import com.bibi.ui.main.user.UserFragment;
import com.bibi.serivce.MyService;
import com.bibi.serivce.MyTextService;
import com.bibi.serivce.SocketMessage;
import com.bibi.serivce.SocketResponse;
import com.bibi.utils.LoadDialog;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulDatabaseUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulPermissionUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

import com.bibi.app.Injection;

import okhttp3.Request;

import com.bibi.utils.sysinfo.QMUIStatusBarHelper;

import static com.bibi.app.GlobalConstant.JSON_ERROR;

public class MainActivity extends BaseTransFragmentActivity implements MainContract.View, MarketBaseFragment.MarketOperateCallback {

    @BindView(R.id.flContainer)
    FrameLayout flContainer;
    @BindView(R.id.llOne)
    LinearLayout llOne;
    @BindView(R.id.llTwo)
    LinearLayout llTwo;
    @BindView(R.id.llThree)
    public LinearLayout llThree;
    @BindView(R.id.llOption)
    LinearLayout llOption;
    @BindView(R.id.llFive)
    LinearLayout llFive;
    @BindView(R.id.llTab)
    LinearLayout llTab;
    @BindView(R.id.ibClose)
    ImageButton ibClose;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vpMenu)
    ViewPager vpMenu;
    @BindView(R.id.dlRoot)
    DrawerLayout dlRoot;
    @BindArray(R.array.home_two_top_tab)
    String[] titles;
    @BindView(R.id.ll_drawer_symbol)
    LinearLayout ll_drawer_symbol;
    @BindView(R.id.fl_drawer_user)
    FrameLayout fl_drawer_user;

    private int currentPage;
    private List<Currency> currencies = new ArrayList<>(); //主页推荐的
    private List<Currency> currenciesTwo = new ArrayList<>(); //主页涨幅榜列表
    private List<Currency> currenciesThree = new ArrayList<>(); //主页成交额列表
    private List<Currency> currenciesFour = new ArrayList<>(); //主页新币榜列表

    private List<Currency> currencyListAllExchange = new ArrayList<>();
    private List<Currency> currencySpotListAllExchange = new ArrayList<>();
    private List<Currency> currencyFavoriteAllExchange = new ArrayList<>();
    private List<Currency> baseFavorite = new ArrayList<>();
    private List<Currency> baseUsdt = new ArrayList<>();
    private List<Currency> baseBtc = new ArrayList<>();
    private List<Currency> baseEth = new ArrayList<>();
    private List<BaseFragment> menusFragments2 = new ArrayList<>();

    private OneFragment oneFragment; //首页
    private OptionsFragment optionsFragment;//合约模块
    //    private TwoRootFragment twoFragment; //行情模块
//    private ThreeFragment threeFragment;//交易模块
//    private ManagementFragment managementFragment; //理财模块
    private MineFragment fiveFragment; //我的模块

    private UserFragment userFragment;//侧边栏"我的"模块
    //private Favorite2Fragment favoriteFragment2;
    private USDTMarket2Fragment usdtMarketFragment2;
    private BTCMarket2Fragment btcMarketFragment2;
    private ETHMarket2Fragment ethMarketFragment2;


    private MainContract.Presenter presenter;
    private long lastPressTime = 0;
    private int type;// 1 去买币  2 去卖币
    private LinearLayout[] lls;

    private Gson gson = new Gson();
    private boolean hasNew = false;
    private byte[] groupBody;
    //private byte[] groupBody;
    private WonderfulDatabaseUtils databaseUtils;
    private List<ChatTable> list;
    private List<ChatTable> findByOrderList;
    private Vision vision;
    private ProgressDialog progressDialog;
    private Intent intentTcp;

    private Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //在这里执行定时需要的操作
            try {
                Log.e("MainActivity", "服务重启");
                intentTcp = new Intent(getApplicationContext(), MyTextService.class);
                startService(intentTcp); // 开启服务
                mHandler.postDelayed(this, 10000);
            } catch (Exception e) {
                Log.e("MainActivity", "服务重启失败");
                mHandler.postDelayed(this, 10000);
            }
        }
    };

    private void tcpNotify(Currency temp) {
        oneFragment.tcpNotify();
//        twoFragment.dataLoaded(temp);
//        twoFragment.tcpNotify();
//        threeFragment.tcpNotify();
        optionsFragment.tcpNotify();
        usdtMarketFragment2.tcpNotify();
//        btcMarketFragment2.tcpNotify();
//        ethMarketFragment2.tcpNotify();
//        favoriteFragment2.tcpNotify();
    }

    /**
     * socket 推送过来的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSocketMessage(SocketResponse response) {
        Log.i("推送过来的信息： MainActivity", response.getCmd().toString());
        try {
            if (response.getCmd() == ISocket.CMD.PUSH_SYMBOL_THUMB) {
                // 如果是盘口返回的信息
                Currency temp = gson.fromJson(response.getResponse(), Currency.class);
                if (temp == null) {
                    return;
                }
                for (Currency currency : currencies) {
                    if (temp.getSymbol().equals(currency.getSymbol())) {
                        Currency.shallowClone(currency, temp);
                        break;
                    }
                }
                for (Currency currency : currenciesTwo) {
                    if (temp.getSymbol().equals(currency.getSymbol())) {
                        Currency.shallowClone(currency, temp);
                        break;
                    }
                }
                for (Currency currency : currenciesThree) {
                    if (temp.getSymbol().equals(currency.getSymbol())) {
                        Currency.shallowClone(currency, temp);
                        break;
                    }
                }
                for (Currency currency : currenciesFour) {
                    if (temp.getSymbol().equals(currency.getSymbol())) {
                        Currency.shallowClone(currency, temp);
                        break;
                    }
                }
                for (Currency currency : currencyListAllExchange) {
                    if (temp.getSymbol().equals(currency.getSymbol())) {
                        Currency.shallowClone(currency, temp);
                        break;
                    }
                }
                for (Currency currency : currencySpotListAllExchange) {
                    if (temp.getSymbol().equals(currency.getSymbol())) {
                        Currency.shallowClone(currency, temp);
                        break;
                    }
                }
                for (Currency currency : currencyFavoriteAllExchange) {
                    if (temp.getSymbol().equals(currency.getSymbol())) {
                        Currency.shallowClone(currency, temp);
                        break;
                    }
                }
                tcpNotify(temp);

            } else if (response.getCmd() == ISocket.CMD.PUSH_SYMBOL_1_k) {
                if (optionsFragment == null) {
                    return;
                }
                optionsFragment.getCoinThumbSuccess(response.getResponse());
            } else if (response.getCmd() == ISocket.CMD.PUSH_SYMBOL_5_k) {
                if (optionsFragment == null) {
                    return;
                }
                optionsFragment.getCoinThumbSuccess(response.getResponse());
            } else if (response.getCmd() == ISocket.CMD.PUSH_SYMBOL_15_k) {
                if (optionsFragment == null) {
                    return;
                }
                optionsFragment.getCoinThumbSuccess(response.getResponse());
            } else if (response.getCmd() == ISocket.CMD.PUSH_SYMBOL_30_k) {
                if (optionsFragment == null) {
                    return;
                }
                optionsFragment.getCoinThumbSuccess(response.getResponse());
            } else if (response.getCmd() == ISocket.CMD.PUSH_SYMBOL_60_k) {
                if (optionsFragment == null) {
                    return;
                }
                optionsFragment.getCoinThumbSuccess(response.getResponse());
            } else if (response.getCmd() == ISocket.CMD.SUBSCRIBE_SYMBOL_1d_k) {
                if (optionsFragment == null) {
                    return;
                }
                optionsFragment.getCoinThumbSuccess(response.getResponse());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void storageData(String response) {
            ChatEntity chatEntity = gson.fromJson(response, ChatEntity.class);
            if (chatEntity == null) return;
            hasNew = true;
            oneFragment.setChatTip(hasNew);
            databaseUtils = new WonderfulDatabaseUtils();
            list = databaseUtils.findAll();
            if (list == null || list.size() == 0) {
                ChatTable table = new ChatTable();
                table.setContent(chatEntity.getContent());
                table.setFromAvatar(chatEntity.getFromAvatar());
                table.setNameFrom(chatEntity.getNameFrom());
                table.setNameTo(chatEntity.getNameTo());
                table.setUidFrom(chatEntity.getUidFrom());
                table.setUidTo(chatEntity.getUidTo());
                table.setOrderId(chatEntity.getOrderId());
                table.setRead(false);
                databaseUtils.saveChat(table);
                return;
            }
            ChatTable table = new ChatTable();
            for (int i = 0; i < list.size(); i++) {
                if (chatEntity.getOrderId().equals(list.get(i).getOrderId())) {
                    findByOrderList = databaseUtils.findByOrder(chatEntity.getOrderId());
                    table = findByOrderList.get(findByOrderList.size()-1);
                    table.setContent(chatEntity.getContent());
                    databaseUtils.update(table);
                    return;
                }
            }
            table.setContent(chatEntity.getContent());
            table.setFromAvatar(chatEntity.getFromAvatar());
            table.setNameFrom(chatEntity.getNameFrom());
            table.setNameTo(chatEntity.getNameTo());
            table.setUidFrom(chatEntity.getUidFrom());
            table.setUidTo(chatEntity.getUidTo());
            table.setOrderId(chatEntity.getOrderId());
            table.setRead(false);
            databaseUtils.saveChat(table);

    }*/

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void getGroupChatEvent(ChatTipEvent tipEvent) {
        oneFragment.setChatTip(tipEvent.isHasNew());
    }*/

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    //重载启动方法 在K线 页面用到
    public static void actionStart(Context context, int type, String symbol) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("symbol", symbol);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, int type, Currency currency) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("currency", currency);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (lastPressTime == 0 || now - lastPressTime > 2 * 1000) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.exit_again));
            lastPressTime = now;
        } else if (now - lastPressTime < 2 * 1000) {
            super.onBackPressed();
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private LoadDialog mDialog = null;

    private void show() {
        if (mDialog == null) {
            mDialog = new LoadDialog(this);
        }
        mDialog.show();
    }

    private void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void getAllCurrencys() {
        //获取首页全部交易对 1，推荐 2.涨幅榜  3.新币榜  4.交易额榜
        //presenter.newTickerCurrency("nav1", "NEW"); //新币榜
        presenter.newTickerCurrency("screen", "INCREASE"); //涨幅榜
        //presenter.newTickerCurrency("nav3", "TRANSACTION_AMOUNT"); //交易额榜
    }

    private void getAllCurrency() {
        //获取合约交易全部交易对
        WonderfulOkhttpUtils.post().url(UrlFactory.getAllCurrency()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        hideDialog();
                    }

                    @Override
                    public void onResponse(String response) {
                        WonderfulLogUtils.logi("miao", "所有币种-合约交易" + response.toString());
                        // 获取所有币种
                        hideDialog();
                        try {
                            List<Currency> obj = new Gson().fromJson(response, new TypeToken<List<Currency>>() {
                            }.getType());
                            setCurrency(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getAllCoinCurrency() {
        //获取币币交易全部交易对
        WonderfulOkhttpUtils.post().url(UrlFactory.getAllSpotCurrency()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        WonderfulLogUtils.logi("miao", "所有币种-币币交易" + e);
                        hideDialog();
                    }

                    @Override
                    public void onResponse(String response) {
                        WonderfulLogUtils.logi("miao", "所有币种-币币交易" + response.toString());
                        // 获取所有币种
                        hideDialog();
                        try {
                            List<Currency> obj = new Gson().fromJson(response, new TypeToken<List<Currency>>() {
                            }.getType());
                            setSpotCurrency(obj);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getAllFavoriteCurrency() {
        //获取自选全部交易对
        User user = MyApplication.getApp().getCurrentUser();
        if (user == null) {
            return;
        }
        WonderfulOkhttpUtils.post().url(UrlFactory.getCollectionUrl())
                .addParams("memberId", user.getId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        WonderfulLogUtils.logi("miao", "所有自选交易" + e);
                        hideDialog();
                    }

                    @Override
                    public void onResponse(String response) {
                        WonderfulLogUtils.logi("miao", "所有自选交易" + response.toString());
                        hideDialog();
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.optInt("code") == 0) {
                                List<Currency> obj = gson.fromJson(object.getJSONArray("data").toString(), new TypeToken<List<Currency>>() {
                                }.getType());
                                setFavoriteCurrency(obj);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        // 获取当前版本号
        versionName = getAppVersionName(this);
        // 请求版本更新
        initProgressDialog();
        getNewVision();
        intentTcp = new Intent(getApplicationContext(), MyTextService.class);
        startService(intentTcp); // 开启服务
        //groupBody = buildGetBodyJson("").toString().getBytes();
        lls = new LinearLayout[]{llOne, llOption, llTwo, llThree, llFive};
        new MainPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        llOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecte(v, 0);
            }
        });
        llOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecte(v, 1);
            }
        });
        llTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecte(v, 2);
            }
        });
        llThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecte(v, 3);
            }
        });
        llFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getApp().isLogin()) {
                    selecte(v, 2);
                } else {
                    LoginActivity.actionStart(MainActivity.this);
                }
            }
        });
        dlRoot.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                usdtMarketFragment2.notifyData();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                oneFragment.notifyData();
            }
        });
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlRoot.closeDrawers();
            }
        });
        if (fragments.size() == 0) {
            recoverFragment();
        }
        if (savedInstanceState == null) {
            hideFragment(oneFragment);
            selecte(llThree, 1);
            selecte(llOne, 0);
            addFragments();
        } else {
            recoverMenuFragment();
        }
        vpMenu.setOffscreenPageLimit(1);
        List<String> titles = Arrays.asList(this.titles);
        vpMenu.setAdapter(new PagerAdapter(getSupportFragmentManager(), menusFragments2, titles));
        tab.setupWithViewPager(vpMenu);
        new OnePresenter(Injection.provideTasksRepository(getApplicationContext()), oneFragment);
//        optionsFragment.setPresenter(new KlinePresenter(Injection.provideTasksRepository(getApplicationContext()), optionsFragment));
//        new TwoRootPresenter(Injection.provideTasksRepository(getApplicationContext()), twoFragment);
        new MinePresenter(Injection.provideTasksRepository(getApplicationContext()), fiveFragment);
        userFragment = new UserFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_drawer_user, userFragment, userFragment.getmTag()).show(userFragment).commit();
    }

    private void initProgressDialog() {
        //创建进度条对话框
        progressDialog = new ProgressDialog(this);
        //设置标题
        progressDialog.setTitle(getResources().getString(R.string.versionUpdateTip4));
        //设置信息
        progressDialog.setMessage(getResources().getString(R.string.versionUpdateTip5));
        progressDialog.setProgress(0);
        //设置显示的格式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    private void recoverMenuFragment() {
        try {
//            favoriteFragment2 = (Favorite2Fragment) getSupportFragmentManager().findFragmentByTag(BaseFragment.makeFragmentName(vpMenu.getId(), 0));
//            menusFragments2.add(favoriteFragment2);
            usdtMarketFragment2 = (USDTMarket2Fragment) getSupportFragmentManager().findFragmentByTag(BaseFragment.makeFragmentName(vpMenu.getId(), 1));
            menusFragments2.add(usdtMarketFragment2);
//            ethMarketFragment2 = (ETHMarket2Fragment) getSupportFragmentManager().findFragmentByTag(BaseFragment.makeFragmentName(vpMenu.getId(), 3));
//            menusFragments2.add(ethMarketFragment2);
//            btcMarketFragment2 = (BTCMarket2Fragment) getSupportFragmentManager().findFragmentByTag(BaseFragment.makeFragmentName(vpMenu.getId(), 2));
//            menusFragments2.add(btcMarketFragment2);
        } catch (Exception e) {

        }
    }

    private void addFragments() {
//        if (favoriteFragment2 == null) {
//            menusFragments2.add(favoriteFragment2 = Favorite2Fragment.getInstance());
//        }
        if (usdtMarketFragment2 == null) {
            menusFragments2.add(usdtMarketFragment2 = USDTMarket2Fragment.getInstance());
        }
//        if (btcMarketFragment2 == null) {
//            menusFragments2.add(btcMarketFragment2 = BTCMarket2Fragment.getInstance());
//        }
//        if (ethMarketFragment2 == null) {
//            menusFragments2.add(ethMarketFragment2 = ETHMarket2Fragment.getInstance());
//        }

    }

    public void selecte(View v, int page) {
        if (page != 1) {
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        } else {
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        }
        if (page == 4) {
            uploadView = true;
        } else {
            uploadView = false;
        }
        currentPage = page;
        llOne.setSelected(false);
        llTwo.setSelected(false);
        llThree.setSelected(false);
        llOption.setSelected(false);
        llFive.setSelected(false);
        v.setSelected(true);
        showFragment(fragments.get(page));
        if (currentFragment == fragments.get(1)) {
            dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void showCoin2Coin(int position) {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        currentPage = 2;
        llOne.setSelected(false);
        llTwo.setSelected(true);
        llThree.setSelected(false);
        llOption.setSelected(false);
        llFive.setSelected(false);
        ThreeFragment fragment = (ThreeFragment) fragments.get(2);
        showFragment(fragment);
        dlRoot.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//        fragment.showTargetFragment(position);
    }

    @Override
    protected void obtainData() {
        getRate();
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        getAllCurrencys();
        presenter.allCurrency();
        getAllCurrency();
        getAllCoinCurrency();
        mHandler.postDelayed(runnable, 10000);
    }

    @Override
    protected void initFragments() {
        if (oneFragment == null) {
            fragments.add(oneFragment = new OneFragment());
        }
//        if (twoFragment == null) {
//            fragments.add(twoFragment = new TwoRootFragment());
//        }
//        if (threeFragment == null) {
//            fragments.add(threeFragment = new ThreeFragment());
//        }
        if (optionsFragment == null) {
            fragments.add(optionsFragment = new OptionsFragment());
        }
//        if (managementFragment == null) {
//            fragments.add(managementFragment = new ManagementFragment());
//        }
        if (fiveFragment == null) {
            fragments.add(fiveFragment = new MineFragment());
        }
    }

    @Override
    protected void recoverFragment() {
        oneFragment = (OneFragment) getSupportFragmentManager().findFragmentByTag(OneFragment.TAG);
        optionsFragment = (OptionsFragment) getSupportFragmentManager().findFragmentByTag(OptionsFragment.TAG);
//        managementFragment = (ManagementFragment) getSupportFragmentManager().findFragmentByTag(ManagementFragment.TAG);
//        twoFragment = (TwoRootFragment) getSupportFragmentManager().findFragmentByTag(TwoRootFragment.TAG);
//        threeFragment = (ThreeFragment) getSupportFragmentManager().findFragmentByTag(ThreeFragment.TAG);
        fiveFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag(MineFragment.TAG);

        if (oneFragment == null) {
            fragments.add(oneFragment = new OneFragment());
        } else {
            fragments.add(oneFragment);
        }
//        if (twoFragment == null) {
//            fragments.add(twoFragment = new TwoRootFragment());
//        } else {
//            fragments.add(twoFragment);
//        }
//        if (threeFragment == null) {
//            fragments.add(threeFragment = new ThreeFragment());
//        } else {
//            fragments.add(threeFragment);
//        }
        if (optionsFragment == null) {
            fragments.add(optionsFragment = new OptionsFragment());
        } else {
            fragments.add(optionsFragment);
        }
//        if (managementFragment == null) {
//            fragments.add(managementFragment = new ManagementFragment());
//        } else {
//            fragments.add(managementFragment);
//        }
        if (fiveFragment == null) {
            fragments.add(fiveFragment = new MineFragment());
        } else {
            fragments.add(fiveFragment);
        }
    }

    @Override
    public int getContainerId() {
        return R.id.flContainer;
    }

    public DrawerLayout getDlRoot(int menuType) {
        //如果menuType与currentMenuType不同，则需要切换数据（币币交易对和杠杆交易对）
        if (currentMenuType != menuType) {
            switch (menuType) {
                case MENU_TYPE_EXCHANGE:
                    baseUsdt = Currency.baseCurrencies(currencyListAllExchange, "USDT");
                    tab.setVisibility(View.GONE);
                    vpMenu.setCurrentItem(0);
                    ll_drawer_symbol.setVisibility(View.VISIBLE);
                    fl_drawer_user.setVisibility(View.GONE);
                    break;
                case MENU_TYPE_USER:
                    //确定币币交易
                    baseFavorite = Currency.baseFavoriteCurrencies(currencyFavoriteAllExchange);
                    baseUsdt = Currency.baseCurrencies(currencySpotListAllExchange, "USDT");
                    baseBtc = Currency.baseCurrencies(currencySpotListAllExchange, "BTC");
                    baseEth = Currency.baseCurrencies(currencySpotListAllExchange, "ETH");
                    tab.setVisibility(View.VISIBLE);
                    ll_drawer_symbol.setVisibility(View.VISIBLE);
                    fl_drawer_user.setVisibility(View.GONE);
                    break;
                default:
            }
            vpMenu.getAdapter().notifyDataSetChanged();
//            favoriteFragment2.dataLoaded(baseFavorite);
            usdtMarketFragment2.dataLoaded(baseUsdt);
//            btcMarketFragment2.dataLoaded(baseBtc);
//            ethMarketFragment2.dataLoaded(baseEth);
            currentMenuType = menuType;
        }
        return dlRoot;
    }

    public static boolean isAgain = false;

    public void subscribeThumb() {
        EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.SUBSCRIBE_SYMBOL_THUMB, null));
        if (lastCmd != null && lastSymbol != null) {
            subKlineThumb(lastCmd, lastUnCmd, lastSymbol);
        }
    }

    ISocket.CMD lastCmd = null;
    ISocket.CMD lastUnCmd = null;
    String lastSymbol = null;

    public void subKlineThumb(ISocket.CMD cmd, ISocket.CMD uncmd, String symbol) {
        if (cmd != null) {
            unSubKlineThumb(lastUnCmd);
        }
        lastCmd = cmd;
        lastUnCmd = uncmd;
        lastSymbol = symbol;
        EventBus.getDefault().post(new SocketMessage(0, cmd,
                buildGetBodyJson(symbol).toString().getBytes()));
    }

    public void unSubKlineThumb(ISocket.CMD cmd) {
        EventBus.getDefault().post(new SocketMessage(0, cmd,
                null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新自选列表
        getAllFavoriteCurrency();
        // 开始订阅
        if (isAgain) {
            isAgain = false;
            if (!TextUtils.isEmpty(getToken())) {
                presenter.find(getToken());
            }
        }
        final Intent intent = new Intent(MainActivity.this, MyService.class);
        flContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                startService(intent);
                subscribeThumb();
            }
        }, 1000);
        //退出登录后 刷新展示未登录状态的数据
        if (!MyApplication.getApp().isLogin() && currencyListAllExchange != null && currencyListAllExchange.size() != 0) {
            notLoginCurrencies();
        }
        //登录回来，刷新展示登录状态的数据
        if (MyApplication.getApp().isLoginStatusChange()) {
            presenter.allCurrency();
            MyApplication.getApp().setLoginStatusChange(false);
        }
        //以下是点击了 K线页面的买入 卖出 后的页面跳转逻辑
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            return;//默认值 或是 不需要跳转 就返回
        }
        Currency currency = (Currency) getIntent().getSerializableExtra("currency");
        showCoin2Coin(0);
        //当type=1 就显示交易fragment就显示买入，对应的page就是0 （即type -1），当type=2，同理！
//        if (threeFragment != null) {
//            threeFragment.showPageFragment(currency, type - 1);
//        }
        getIntent().putExtra("type", 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        stopService(intentTcp);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        /*hasNew = SharedPreferenceInstance.getInstance().getHasNew();
        oneFragment.setChatTip(hasNew);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hideDialog();
        // MainActivity销毁则停止
        WonderfulLogUtils.logi("mysocket", "MainActivity 服务关闭了！！！！！！");
        stopService(new Intent(MainActivity.this, MyTextService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 取消订阅
        EventBus.getDefault().post(new SocketMessage(0, ISocket.CMD.UNSUBSCRIBE_SYMBOL_THUMB, null));
        unSubKlineThumb(lastUnCmd);
        EventBus.getDefault().unregister(this);
    }

    private JSONObject buildGetBodyJson(String symbol) {
        JSONObject obj = new JSONObject();
        try {
            //obj.put("orderId", orderDetial.getOrderSn());
            obj.put("symbol", symbol);
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    private void notLoginCurrencies() {
        for (Currency currency : currencyListAllExchange) {
            currency.setCollect(false);
        }
        for (Currency currency : currencySpotListAllExchange) {
            currency.setCollect(false);
        }
        for (Currency currency : currencyFavoriteAllExchange) {
            currency.setCollect(false);
        }
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void setCurrency(List<Currency> currencies) {
        if (currencies == null || currencies.size() == 0) {
            return;
        }
        this.currencyListAllExchange.clear();
        this.currencyListAllExchange.addAll(currencies);
        MyApplication.list.clear();
        MyApplication.list.addAll(currencies);
        baseUsdt = Currency.baseCurrencies(currencyListAllExchange, "USDT");
        if (MyApplication.getApp().isLogin()) {
            presenter.find(getToken());
        }
        setData();
    }

    private void setSpotCurrency(List<Currency> currencies) {
        if (currencies == null || currencies.size() == 0) {
            return;
        }
        this.currencySpotListAllExchange.clear();
        this.currencySpotListAllExchange.addAll(currencies);
        baseUsdt = Currency.baseCurrencies(currencySpotListAllExchange, "USDT");
        baseBtc = Currency.baseCurrencies(currencySpotListAllExchange, "BTC");
        baseEth = Currency.baseCurrencies(currencySpotListAllExchange, "ETH");
        if (MyApplication.getApp().isLogin()) {
            presenter.find(getToken());
        }
        setData();
        // 当请求成功 为交易fragment设置初始交易对 即symbol值
//        if (threeFragment != null) {
//            threeFragment.setViewSpotContent(currencySpotListAllExchange);
//        }
    }

    private void setFavoriteCurrency(List<Currency> currencies) {
        if (currencies == null || currencies.size() == 0) {
            return;
        }
        this.currencyFavoriteAllExchange.clear();
        this.currencyFavoriteAllExchange.addAll(currencies);
        baseFavorite = Currency.baseFavoriteCurrencies(currencyFavoriteAllExchange);
        if (MyApplication.getApp().isLogin()) {
            presenter.find(getToken());
        }
        setData();
    }

    @Override
    public void allCurrencySuccess(Object obj) {
        try {
            JsonObject object = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray array = object.getAsJsonArray("recommend").getAsJsonArray();
            List<Currency> objs = gson.fromJson(array, new TypeToken<List<Currency>>() {
            }.getType());

//            JsonArray array1 = object.getAsJsonArray("changeRank").getAsJsonArray();
//            List<Currency> currency1 = gson.fromJson(array1, new TypeToken<List<Currency>>() {
//            }.getType());
//            this.currenciesTwo.clear();
//            this.currenciesTwo.addAll(currency1);
            this.currencies.clear();
            this.currencies.addAll(objs);
            if (oneFragment != null) {
                oneFragment.dataLoaded(currencies);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        oneFragment.dataLoaded(currencies);
//        twoFragment.dataLoaded();
//        favoriteFragment2.dataLoaded(baseFavorite);
        usdtMarketFragment2.dataLoaded(baseUsdt);
//        btcMarketFragment2.dataLoaded(baseBtc);
//        ethMarketFragment2.dataLoaded(baseEth);
    }

    @Override
    public void allCurrencyFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    public static List<Favorite> mFavorte = new ArrayList<>();

    public void Find() {
        presenter.find(getToken());
    }

    @Override
    public void findSuccess(List<Favorite> obj) {
        //登录
        if (obj == null) {
            return;
        }
        mFavorte.clear();
        mFavorte.addAll(obj);
        for (Currency currency : currencyListAllExchange) {
            currency.setCollect(false);
        }
        for (Currency currency : currencyListAllExchange) {
            for (Favorite favorite : mFavorte) {
                if (favorite.getSymbol().equals(currency.getSymbol())) {
                    currency.setCollect(true);
                }
            }
        }
        for (Currency currency : currencySpotListAllExchange) {
            currency.setCollect(false);
        }
        for (Currency currency : currencySpotListAllExchange) {
            for (Favorite favorite : mFavorte) {
                if (favorite.getSymbol().equals(currency.getSymbol())) {
                    currency.setCollect(true);
                }
            }
        }
    }

    @Override
    public void findFail(Integer code, String toastMessage) {

    }

    @Override
    public void newTickerSuccess(Object obj, String nav) {
        try {
            JsonObject object = new JsonParser().parse((String) obj).getAsJsonObject();
            JsonArray array = object.getAsJsonArray("data").getAsJsonArray();
            List<Currency> currency = gson.fromJson(array, new TypeToken<List<Currency>>() {
            }.getType());
            if (nav.equals("nav1")) {
                //新币榜
                this.currenciesFour.clear();
                this.currenciesFour.addAll(currency);
                if (oneFragment != null) {
                    oneFragment.dataLoadedFour(currenciesFour);
                }
            } else if (nav.equals("screen")) {
                //涨幅榜
                this.currenciesTwo.clear();
                this.currenciesTwo.addAll(currency);
                if (oneFragment != null) {
                    oneFragment.dataLoadedTwo(currenciesTwo);
                }
            } else if (nav.equals("nav3")) {
                //交易额榜
                this.currenciesThree.clear();
                this.currenciesThree.addAll(currency);
                if (oneFragment != null) {
                    oneFragment.dataLoadedThree(currenciesThree);
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void newTickerFail(Integer code, String toastMessage) {

    }

    public static final int MENU_TYPE_EXCHANGE = 0;//侧滑菜单-合约交易
    public static final int MENU_TYPE_USER = 1;//侧滑菜单-币币交易
    public int currentMenuType = -1;

    @Override
    public void itemClick(Currency currency, int type) {
        selecte(llOption, 1);
        dlRoot.closeDrawers();
//        threeFragment.resetSymbol(currency, currentMenuType);
        optionsFragment.resetSymbol(currency);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        int page = savedInstanceState.getInt("page");
        selecte(lls[page], page);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("page", currentPage);
        super.onSaveInstanceState(outState);
    }

    /**
     * 获取汇率的接口
     */
    public static double rate = 1.0;

    private void getRate() {
        WonderfulOkhttpUtils.post().url(UrlFactory.getRateUrl()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);
                        rate = 0;
                    }

                    @Override
                    public void onResponse(String response) {
                        JsonObject object = new JsonParser().parse(response).getAsJsonObject();
                        rate = object.getAsJsonPrimitive("data").getAsDouble();
                        WonderfulLogUtils.logi("miao", rate + "汇率");
                    }
                });
    }

    // 获取版本信息
    private void getNewVision() {
        WonderfulOkhttpUtils.post().url(UrlFactory.getNewVision()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        super.onError(request, e);

                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("版本信息", versionName + "  " + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.optInt("code") == 0) {
                                vision = gson.fromJson(object.toString(), new TypeToken<Vision>() {
                                }.getType());
                                if (!WonderfulPermissionUtils.isCanUseStorage(MainActivity.this)) {
                                    checkPermission(GlobalConstant.PERMISSION_STORAGE, Permission.STORAGE);
                                } else {
                                    showUpDialog(vision);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void checkPermission(int requestCode, String[] permissions) {
        AndPermission.with(MainActivity.this).requestCode(requestCode).permission(permissions).callback(permissionListener).start();
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    showUpDialog(vision);
                    break;
                default:
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case GlobalConstant.PERMISSION_STORAGE:
                    WonderfulToastUtils.showToast(getResources().getString(R.string.storage_permission));
                    break;
                default:
            }
        }
    };

    //判断文件是否存在
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private void showUpDialog(final Vision obj) {
        //判断是否为最新版本
        if (obj.getData() == null) {
            return;
        }
        if (!versionName.equals(obj.getData().getVersion())) {
//            new AlertDialog.Builder(MainActivity.this)
//                    .setIcon(null)
//                    .setMessage(getResources().getString(R.string.versionUpdateTip2) + "\n不升级将会影响您的使用。")
//                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (obj.getData().getDownloadUrl() == null || "".equals(obj.getData().getDownloadUrl())) {
//                                WonderfulToastUtils.showToast(getResources().getString(R.string.versionUpdateTip3));
//                            } else {
//                                Intent intent = new Intent();
//                                intent.setData(Uri.parse(obj.getData().getDownloadUrl()));//Url 就是你要打开的网址
//                                intent.setAction(Intent.ACTION_VIEW);
//                                startActivity(intent); //启动浏览器
//                            }
//                        }
//                    })
//                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                            finish();
//                        }
//                    })
//                    .show().setCancelable(false);
            final CommonDialogFragment dialog = CommonDialogFragment.getInstance(CommonDialogFragment.TYPE_DEFALUT, obj.getData().getVersion() + "版本更新",
                    "版本请重新下载安装\n\n如果版本过低，将影响部分功能的正常使用，请立即下载升级",
                    getString(R.string.confirm), getString(R.string.cancle), true);
            dialog.setCommitClickListener(new CommonDialogFragment.OnCommitClickListener() {
                @Override
                public void onCommitClick(String pass) {
                    if (obj.getData().getDownloadUrl() == null || "".equals(obj.getData().getDownloadUrl())) {
                        WonderfulToastUtils.showToast(getResources().getString(R.string.versionUpdateTip3));
                    } else {
                        Intent intent = new Intent();
                        intent.setData(Uri.parse(obj.getData().getDownloadUrl()));//Url 就是你要打开的网址
                        intent.setAction(Intent.ACTION_VIEW);
                        startActivity(intent); //启动浏览器
                    }
                }
            });
            dialog.show(getSupportFragmentManager(), "2");
        }
    }


    /**
     * 返回当前程序版本名
     */
    private String versionName;
    private int versionCode;

    public String getAppVersionName(Context context) {
        versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
            Log.e("VersionInfo：", versionName);
            //versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
