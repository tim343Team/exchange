package com.bibi.ui.main.trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;

import com.bibi.R;
import com.bibi.app.Injection;
import com.bibi.base.BaseTransFragment;
import com.bibi.entity.Currency;
import com.bibi.ui.fiat_exchange.FiatExchangeActivity;
import com.bibi.ui.main.TestActivity;
import com.bibi.utils.sysinfo.QMUIStatusBarHelper;
import com.bibi.ui.main.MainActivity;

public class ThreeFragment extends BaseTransFragment {
    public static final String TAG = ThreeFragment.class.getSimpleName();
    Agreement2CoinFragment agreement2CoinFragment = new Agreement2CoinFragment();
    BaseCoinFragment baseCoinFragment = new BaseCoinFragment();
    @BindView(R.id.ll_title)
    RelativeLayout ll_title;
    @BindViews({R.id.tv_title_fiat})
    TextView[] titles;
    List<Fragment> fragmentList = new ArrayList<>();

    private int selectTab = 0;

    @Override
    protected String getmTag() {
        return TAG;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_three;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initViewPager();
        new Agreement2CoinPresenter(Injection.provideTasksRepository(getActivity()), agreement2CoinFragment);
        new BaseCoinPresenter(Injection.provideTasksRepository(getActivity()), baseCoinFragment);
    }



    private void initViewPager() {
        fragmentList.add(agreement2CoinFragment);
        for (int i = 0; i < titles.length; i++) {
            final int finalI = i;
            titles[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectTab = finalI;
                    setTitleSelectedByPosition(finalI);
                }
            });
        }
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_container_trade, agreement2CoinFragment);
        transaction.commitAllowingStateLoss();
        setTitleSelectedByPosition(0);
    }

    private void setTitleSelectedByPosition(int position) {
        for (TextView textView : titles) {
            textView.setSelected(textView == titles[position]);
            showTargetFragment(position);
        }
    }

    public void showPageFragment(Currency currency, int position) {
        if (selectTab == 0) {
            if (agreement2CoinFragment != null) {
                agreement2CoinFragment.resetSymbol(currency, position);
            }
        }
    }


    public void setViewSpotContent(List<Currency> currencyListAll) {
        if (agreement2CoinFragment != null) agreement2CoinFragment.setViewContent(currencyListAll);
    }

    public void resetSymbol(Currency currency, int currentMenuType) {
        switch (currentMenuType) {
            case MainActivity.MENU_TYPE_USER:
                if (agreement2CoinFragment != null) {
                    agreement2CoinFragment.resetSymbol(currency);
                }
                break;

            default:
        }
    }

    public void tcpNotify() {
        if (agreement2CoinFragment != null) {
            agreement2CoinFragment.tcpNotify();
        }
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    public void showTargetFragment(int position) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.show(fragmentList.get(position));
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(getActivity(), ll_title);
            isSetTitle = true;
        }
    }
}
