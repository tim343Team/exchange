package com.bibi.ui.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.adapter.PagerAdapter;
import com.bibi.base.BaseActivity;
import com.bibi.base.BaseFragment;
import com.bibi.ui.myinfo.MyInfoActivity;
import com.bibi.ui.order.OrdersFragment;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/7
 */
public class NoticeActivity extends BaseActivity{
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tab_name)
    TabLayout tab_name;
    @BindView(R.id.vp_fiat_list)
    ViewPager vp_fiat_list;
    private ArrayList<String> tabs = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private PagerAdapter adapter;

    @OnClick(R.id.ibBack)
    public void back(){
        finish();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, NoticeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_announcement;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        setData();
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    private void setData() {
        tabs.clear();
        fragments.clear();
        tabs.add(getResources().getString(R.string.notice));
        tabs.add(getResources().getString(R.string.announcement));
        tabs.add(getResources().getString(R.string.activity));
        tab_name.setTabMode(TabLayout.MODE_FIXED);

        fragments.add(AnnouncementFragment.getInstance(AnnouncementFragment.NOTICE));
        fragments.add(AnnouncementFragment.getInstance(AnnouncementFragment.ANNOUNCE));
        fragments.add(AnnouncementFragment.getInstance(AnnouncementFragment.ACTIVTY));

        if (adapter == null) {
            adapter = new PagerAdapter(getSupportFragmentManager(), fragments, tabs);
            vp_fiat_list.setAdapter(adapter);
            tab_name.setupWithViewPager(vp_fiat_list);
            vp_fiat_list.setOffscreenPageLimit(fragments.size() - 1);
        } else {
            adapter.notifyDataSetChanged();
        }

    }
}
