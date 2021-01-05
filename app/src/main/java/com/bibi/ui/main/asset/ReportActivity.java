package com.bibi.ui.main.asset;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.base.BaseActivity;
import com.bibi.base.BaseFragment;
import com.bibi.ui.main.trade.C2CFragment;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/19
 */
public class ReportActivity extends BaseActivity {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.vp_report)
    ViewPager mViewPager;
    @BindView(R.id.tvDeposit)
    TextView tvDeposit;
    @BindView(R.id.tvDeal)
    TextView tvDeal;
    @BindView(R.id.vDeal)
    View vDeal;
    @BindView(R.id.vDeposit)
    View vDeposit;
    @BindView(R.id.tvRecord)
    TextView tvRecord;
    @BindView(R.id.vRecord)
    View vRecord;

    private String coinName;
    private List<BaseFragment> fragments;
    ChongBiJLFragment chongBiJLFragment = new ChongBiJLFragment();
    TiBiJLAFragment tiBiJLAFragment = new TiBiJLAFragment();
    DealFragment dealFragment = new DealFragment();

    public static void actionStart(Context context, String coinName) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra("coinName", coinName);
        context.startActivity(intent);
    }

    @OnClick(R.id.llChongBi)
    public void onClickChongBi() {
        tvDeposit.setTextColor(getResources().getColor(R.color.colorPrimary));
        tvRecord.setTextColor(getResources().getColor(R.color.primaryTextGray));
        tvDeal.setTextColor(getResources().getColor(R.color.primaryTextGray));
        vDeposit.setVisibility(View.VISIBLE);
        vRecord.setVisibility(View.INVISIBLE);
        vDeal.setVisibility(View.INVISIBLE);
        mViewPager.setCurrentItem(0);
    }

    @OnClick(R.id.llTiBi)
    public void onClickTiBi() {
        tvDeposit.setTextColor(getResources().getColor(R.color.primaryTextGray));
        tvRecord.setTextColor(getResources().getColor(R.color.colorPrimary));
        tvDeal.setTextColor(getResources().getColor(R.color.primaryTextGray));
        vDeposit.setVisibility(View.INVISIBLE);
        vRecord.setVisibility(View.VISIBLE);
        vDeal.setVisibility(View.INVISIBLE);
        mViewPager.setCurrentItem(1);
    }

    @OnClick(R.id.llDeal)
    public void onClickDeal() {
        tvDeposit.setTextColor(getResources().getColor(R.color.primaryTextGray));
        tvRecord.setTextColor(getResources().getColor(R.color.primaryTextGray));
        tvDeal.setTextColor(getResources().getColor(R.color.colorPrimary));
        vDeposit.setVisibility(View.INVISIBLE);
        vRecord.setVisibility(View.INVISIBLE);
        vDeal.setVisibility(View.VISIBLE);
        mViewPager.setCurrentItem(2);
    }

    @OnClick(R.id.ibBack)
    public void onBackClick() {
        finish();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_asset_report;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initViewPager();
    }

    @Override
    protected void obtainData() {
        coinName = getIntent().getStringExtra("coinName");
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    private void initViewPager() {
        fragments = new ArrayList<BaseFragment>() {{
            add(chongBiJLFragment);
            add(tiBiJLAFragment);
            add(dealFragment);
        }};
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<BaseFragment> baseFragments;
        private List<String> listTitle;

        public MyPagerAdapter(FragmentManager fm, List<BaseFragment> baseFragments) {
            super(fm);
            this.baseFragments = baseFragments;
        }

        @Override
        public int getCount() {
            return baseFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return baseFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (listTitle == null || listTitle.size() == 0) return super.getPageTitle(position);
            return listTitle.get(position);
        }
    }
}
