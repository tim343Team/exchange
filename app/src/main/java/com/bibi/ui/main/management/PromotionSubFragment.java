package com.bibi.ui.main.management;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.adapter.PromotionRecordAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseLazyFragment;
import com.bibi.entity.PromotionRecord;
import com.bibi.ui.main.management.presenter.PromotionPresenter;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulCodeUtils;
import butterknife.BindView;


/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/12
 */
public class PromotionSubFragment extends BaseLazyFragment implements ManagementContract.PromotionView {
    @BindView(R.id.rvPromotionRecord)
    RecyclerView rvPromotionRecord;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private int type = 0;//0:推广网络  1：接点网络
    private int page = 1;
    private PromotionRecordAdapter adapter;
    private List<PromotionRecord> recordVisible = new ArrayList<>();
    private List<PromotionRecord> recordParent = new ArrayList<>();
    private ManagementContract.PromotionPresenter presenter;

    public static PromotionSubFragment getInstance(int type) {
        PromotionSubFragment fragment = new PromotionSubFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_promotion_sub;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new PromotionPresenter(Injection.provideTasksRepository(getActivity()), this);
        type = getArguments().getInt("type");
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
        initRv();
    }

    @Override
    protected void loadData() {
        if (type == 0) {
            presenter.getPromotion(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", "40");
        } else if (type == 1) {
            presenter.getInvestPromotion(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", "40");
        }
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

    private void refresh() {
        page = 1;
        recordParent.clear();
        if (type == 0) {
            presenter.getPromotion(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", "40");
        } else if (type == 1) {
            presenter.getInvestPromotion(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", "40");
        }
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(false);
    }

    private void loadMore() {
        page = page + 1;
        refreshLayout.setEnabled(false);
        if (type == 0) {
            presenter.getPromotion(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", "40");
        } else if (type == 1) {
            presenter.getInvestPromotion(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", "40");
        }
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(getmActivity(), LinearLayoutManager.VERTICAL, false);
        rvPromotionRecord.setLayoutManager(manager);
        adapter = new PromotionRecordAdapter(R.layout.adapter_promotion_record, recordVisible,type);
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
                if (type == 0) {
                    presenter.getPromotionByMember(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", item.getId() + "");
                } else if (type == 1) {
                    presenter.getInvestPromotionByMember(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", item.getId() + "");
                }
            }
        });
        adapter.OnclickMerge(new PromotionRecordAdapter.OnclickMerge() {
            @Override
            public void select(PromotionRecord item, int position) {
                item.setSelect(false);
                recordParent.remove(item);
                page = 1;
                if (type == 0) {
                    if (recordParent.size() > 0) {
                        presenter.getPromotionByMember(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", item.getId() + "");
                    } else {
                        presenter.getPromotion(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", "40");
                    }
                } else if (type == 1) {
                    if (recordParent.size() > 0) {
                        presenter.getInvestPromotionByMember(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", item.getId() + "");
                    } else {
                        presenter.getInvestPromotion(SharedPreferenceInstance.getInstance().getTOKEN(), page + "", "40");
                    }
                }

            }
        });
    }

    @Override
    public void setPresenter(ManagementContract.PromotionPresenter presenter) {
        this.presenter = presenter;
    }
}
