package com.bibi.ui.main.management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bibi.R;
import com.bibi.adapter.PagerAdapter;
import com.bibi.base.BaseActivity;
import com.bibi.base.BaseFragment;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/12
 */
public class PromotionRootActivity extends BaseActivity {
    @BindView(R.id.llTitle)
    RelativeLayout llTitle;
    @BindView(R.id.vp_fiat_list)
    ViewPager vp_fiat_list;
    @BindView(R.id.tb_fiat_coin_name)
    TabLayout tab;
    @BindArray(R.array.promotion_tab)
    String[] titles;

    private List<BaseFragment> fragments = new ArrayList<>();

    @OnClick(R.id.ibBack)
    public void back() {
        finish();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PromotionRootActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_promotion_root;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        addFragments();
        tab.setTabMode(TabLayout.MODE_FIXED);
        vp_fiat_list.setOffscreenPageLimit(2);
        List<String> titles = Arrays.asList(this.titles);
        vp_fiat_list.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments, titles));
        tab.setupWithViewPager(vp_fiat_list);
        vp_fiat_list.setOffscreenPageLimit(fragments.size() - 1);
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

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    private void addFragments() {
        fragments.add(PromotionSubFragment.getInstance(0));
        fragments.add(PromotionSubFragment.getInstance(1));
    }

}
