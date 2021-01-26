package com.bibi.ui.main.options;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bibi.customview.CustomViewPager;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.adapter.PagerAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.base.BaseFragment;
import com.bibi.entity.Currency;
import com.bibi.ui.main.presenter.CommonPresenter;
import com.bibi.ui.main.presenter.ICommonView;
import com.bibi.ui.order.OrdersFragment;
import com.bibi.utils.SharedPreferenceInstance;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/11
 */
public class OptionsActivity extends BaseActivity implements ICommonView {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tab_name)
    TabLayout tab_name;
    @BindView(R.id.option_detail_list)
    ViewPager viewPager;

    private ArrayList<String> tabs = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private PagerAdapter adapter;
    private CommonPresenter commonPresenter;

    private Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //在这里执行定时需要的操作
            commonPresenter.getDrawleSymbol(getToken());
            mHandler.postDelayed(this, 2000);
        }
    };

    @OnClick(R.id.ibBack)
    void back(){
        finish();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, OptionsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_options;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new CommonPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @Override
    protected void obtainData() {
        setData();
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        mHandler.postDelayed(runnable, 500);
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
        tabs.add(getResources().getString(R.string.position));
        tabs.add(getResources().getString(R.string.history));
        tab_name.setTabMode(TabLayout.MODE_FIXED);

        fragments.add(OptionsDetailFragment.getInstance(OptionsDetailFragment.HOLD));
        fragments.add(OptionsDetailFragment.getInstance(OptionsDetailFragment.HISTORY));

        if (adapter == null) {
            adapter = new PagerAdapter(getSupportFragmentManager(), fragments, tabs);
            viewPager.setAdapter(adapter);
            tab_name.setupWithViewPager(viewPager);
            viewPager.setOffscreenPageLimit(fragments.size() - 1);
            viewPager.setCurrentItem(0);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void setPresenter(CommonPresenter presenter) {
        this.commonPresenter = presenter;
    }

    @Override
    public void deleteSuccess(String obj, int position) {

    }

    @Override
    public void deleteFail(Integer code, String toastMessage) {

    }

    @Override
    public void addSuccess(String obj, int position) {

    }

    @Override
    public void addFail(Integer code, String toastMessage) {

    }

    @Override
    public void getSymbolSuccess(List<Currency> cirrency) {
        if (fragments.get(0) != null) {
            ((OptionsDetailFragment) fragments.get(0)).dataLoaded(cirrency);
        }
        if (fragments.get(1) != null) {
            ((OptionsDetailFragment) fragments.get(1)).dataLoaded(cirrency);
        }
    }
}
