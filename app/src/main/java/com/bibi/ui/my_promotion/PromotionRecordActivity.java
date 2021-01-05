package com.bibi.ui.my_promotion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.adapter.PromotionRecordAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.entity.PromotionRecord;
import com.bibi.utils.WonderfulCodeUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/17
 */
public class PromotionRecordActivity extends BaseActivity implements PromotionRecordContract.View {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.rvPromotionRecord)
    RecyclerView rvPromotionRecord;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private int page = 1;
    private String token;
    private PromotionRecordContract.Presenter presenter;
    private List<PromotionRecord> recordVisible = new ArrayList<>();
    private List<PromotionRecord> recordParent = new ArrayList<>();
    private List<Integer> levels = new ArrayList<>();
    private PromotionRecordAdapter adapter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PromotionRecordActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.ibBack)
    public void back() {
        finish();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_promotion_record;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new PromotionRecordPresenter(Injection.provideTasksRepository(this), this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    protected void obtainData() {
        token = this.getToken();
    }

    @Override
    protected void fillWidget() {
        initRv();
    }

    @Override
    protected void loadData() {
        presenter.getPromotion(this.getToken(), page + "", "40");
    }

    @Override
    public void getPromotionFail(Integer code, String toastMessage) {
        try {
            adapter.setEnableLoadMore(true);
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
        } catch (Exception e) {

        }
    }

    @Override
    public void getPromotionSuccess(List<PromotionRecord> obj) {
        try {
            adapter.setEnableLoadMore(true);
            adapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            if (page == 1) {
                recordVisible.clear();
                if (obj.size() == 0) adapter.loadMoreEnd();
                else this.recordVisible.addAll(obj);
            } else {
                if (obj.size() != 0) {
                    this.recordVisible.addAll(obj);
                } else {
                    adapter.loadMoreEnd();
                }
            }
            adapter.notifyDataSetChanged();
            adapter.disableLoadMoreIfNotFullPage();
        } catch (Exception e) {

        }
    }

    @Override
    public void getPromotionMemberFail(Integer code, String toastMessage) {
        try {
            adapter.setEnableLoadMore(true);
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
        } catch (Exception e) {

        }
    }

    @Override
    public void getPromotionMemberSuccess(List<PromotionRecord> obj) {
        try {
            adapter.setEnableLoadMore(true);
            adapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            if (page == 1) {
                recordVisible.clear();
                if (obj.size() == 0) adapter.loadMoreEnd();
                else {
                    recordVisible.addAll(recordParent);
                    this.recordVisible.addAll(obj);
                }
            } else {
                if (obj.size() != 0) {
                    this.recordVisible.addAll(obj);
                } else {
                    adapter.loadMoreEnd();
                }
            }
            adapter.notifyDataSetChanged();
            adapter.disableLoadMoreIfNotFullPage();
        } catch (Exception e) {

        }
    }

    @Override
    public void setPresenter(PromotionRecordContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void refresh() {
        page = 1;
        recordParent.clear();
        presenter.getPromotion(this.getToken(), page + "", "40");
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(false);
    }

    private void loadMore() {
        page = page + 1;
        refreshLayout.setEnabled(false);
        presenter.getPromotion(this.getToken(), page + "", "40");
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPromotionRecord.setLayoutManager(manager);
        adapter = new PromotionRecordAdapter(R.layout.adapter_promotion_record, recordVisible);
        adapter.bindToRecyclerView(rvPromotionRecord);
        adapter.setEmptyView(R.layout.empty_no_message);
//        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                loadMore();
//            }
//        }, rvPromotionRecord);
        adapter.setEnableLoadMore(false);
        adapter.OnclickSpread(new PromotionRecordAdapter.OnclickSpread() {
            @Override
            public void select(PromotionRecord item, int position) {
                item.setSelect(true);
                recordParent.add(item);
                page = 1;
                presenter.getPromotionByMember(token, page + "", item.getId() + "");
            }
        });
        adapter.OnclickMerge(new PromotionRecordAdapter.OnclickMerge() {
            @Override
            public void select(PromotionRecord item, int position) {
                item.setSelect(false);
                recordParent.remove(item);
                page = 1;
                if(recordParent.size()>0) {
                    presenter.getPromotionByMember(token, page + "", item.getId() + "");
                }else {
                    presenter.getPromotion(token, page + "", "40");
                }
            }
        });
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }
}
