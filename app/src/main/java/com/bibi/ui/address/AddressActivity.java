package com.bibi.ui.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import com.bibi.entity.ExtractInfo;
import com.bibi.entity.RechargeSupportContract;
import com.bibi.entity.SimpleListBean;
import com.bibi.entity.SimpleListItem;
import com.bibi.ui.dialog.ListDialogFragment;
import com.bibi.ui.main.asset.AssetActivity;
import com.bibi.ui.recharge.RechargeActivity;
import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.adapter.AddressAddAdapter;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.Address;
import com.bibi.entity.TiBiAddress;
import com.bibi.entity.TiBiAddressContent;
import com.bibi.ui.order.OrdersPresenter;
import com.bibi.ui.recharge.RechargeDialogFragment;
import com.bibi.ui.recharge.ReportDialogFragment;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/16
 */
public class AddressActivity extends BaseActivity implements AddressContract.View {
    private AddressContract.Presenter presenter;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int page = 1;
    private AddressAddAdapter adapter;
    private List<TiBiAddressContent> addresses = new ArrayList<>();
    protected boolean isNeedLoad = true;
    private SimpleListBean bean = new SimpleListBean();

    @OnClick(R.id.ibBack)
    public void back() {
        finish();
    }

    @OnClick(R.id.tvAdd)
    public void add() {
        if (bean.getNewsItems() == null) {
            return;
        }
        ListDialogFragment listDialogFragment = ListDialogFragment.getInstance(bean);
        listDialogFragment.show(getSupportFragmentManager(), "bottom");
        listDialogFragment.setCallback(new ListDialogFragment.OperateCallback() {
            @Override
            public void ItemClick(int position) {
                if (MyApplication.getApp().isLogin()) {
                    AddressAddActivity.actionStart(AddressActivity.this, bean.getNewsItems().get(position).getContent());
                    finish();
                } else {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
                }
            }
        });
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AddressActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_add_address;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new AddressPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {
        initRvAddress();
    }

    @Override
    protected void loadData() {
        presenter.getAddress(this.getToken(), page, 20);
        presenter.getAllAddress(this.getToken());
        isNeedLoad = false;
    }

    @Override
    public void setPresenter(AddressContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void refresh() {
        page = 1;
        presenter.getAddress(this.getToken(), page, 20);
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(false);
    }

    private void loadMore() {
        page = page + 1;
        refreshLayout.setEnabled(false);
        presenter.getAddress(this.getToken(), page, 20);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    private void initRvAddress() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new AddressAddAdapter(R.layout.adapter_add_address, addresses);
        adapter.bindToRecyclerView(mRecyclerView);
        adapter.setEmptyView(R.layout.empty_address);
        adapter.setOnDeleteListener(new AddressAddAdapter.OnclickListenerDelete() {
            @Override
            public void onDelete(TiBiAddressContent item) {
                presenter.deleteAddress(AddressActivity.this.getToken(), item.getId() + "");
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        adapter.setEnableLoadMore(false);
    }

    @Override
    public void getAddressSuccess(TiBiAddress tiBiAddress) {
        List<TiBiAddressContent> obj = tiBiAddress.getContent();
        try {
            adapter.setEnableLoadMore(true);
            adapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            if (page == 1) {
                addresses.clear();
                if (obj.size() == 0) adapter.loadMoreEnd();
                else this.addresses.addAll(obj);
            } else {
                if (obj.size() != 0) {
                    this.addresses.addAll(obj);
                } else {
                    adapter.loadMoreEnd();
                }
            }
            adapter.notifyDataSetChanged();
            adapter.disableLoadMoreIfNotFullPage();
        } catch (Exception e) {
            adapter.notifyDataSetChanged();
            adapter.disableLoadMoreIfNotFullPage();
        }
    }

    @Override
    public void getAddressFial(Integer code, String toastMessage) {
        try {
            adapter.setEnableLoadMore(true);
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
//            WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
        } catch (Exception e) {

        }
    }

    @Override
    public void deleteAddressSuccess(String message) {
        WonderfulToastUtils.showToast(message);
        refresh();
    }

    @Override
    public void deleteAddressFial(Integer code, String toastMessage) {
        WonderfulToastUtils.showToast(toastMessage);
    }

    @Override
    public void getAddressCodeSuccess(String message) {

    }

    @Override
    public void getAddressCodeFial(Integer code, String toastMessage) {

    }

    @Override
    public void addAddressCodeSuccess(String message) {

    }

    @Override
    public void addAddressCodeFial(Integer code, String toastMessage) {
    }

    @Override
    public void getAllAddressSuccess(List<ExtractInfo> objs) {
        List<SimpleListItem> simpleListItems = new ArrayList<>();
        for (ExtractInfo contract : objs) {
            SimpleListItem item = new SimpleListItem();
            item.setContent(contract.getName());
            simpleListItems.add(item);
        }
        bean.setNewsItems(simpleListItems);
    }

    @Override
    public void getAllAddressFial(Integer code, String toastMessage) {

    }
}
