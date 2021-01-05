package com.bibi.ui.notice;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import com.bibi.R;
import com.bibi.adapter.AnnouncementAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseLazyFragment;
import com.bibi.entity.AnnounceEntity;
import com.bibi.utils.SharedPreferenceInstance;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/8
 */
public class AnnouncementFragment extends BaseLazyFragment implements AnnouncementContract.View{
    public static String NOTICE = "NOTICE";
    public static String ANNOUNCE = "ANNOUNCE";
    public static String ACTIVTY = "ACTIVTY";

    @BindView(R.id.rvAds)
    RecyclerView rvAds;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    Unbinder unbinder;

    private AnnouncementContract.Presenter presenter;
    private String type;
    private int pageNo = 1;
    private int pageSize = 20;
    private AnnouncementAdapter adapter;
    private List<AnnounceEntity> data = new ArrayList<>();

    public static AnnouncementFragment getInstance(String type) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_announcement_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new AnnouncementPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        type = getArguments().getString("type");
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
        initRvAds();
    }

    @Override
    protected void loadData() {
        load(type);
    }

    @Override
    public void setPresenter(AnnouncementContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void refresh() {
        pageNo = 1;
        load(type);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreEnd(false);
        adapter.notifyDataSetChanged();
    }

    private void initRvAds() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAds.setLayoutManager(manager);
        if (type.equals(NOTICE)) {
            adapter = new AnnouncementAdapter(R.layout.adapter_announcement, data, type);
        } else if (type.equals(ANNOUNCE)) {
            adapter = new AnnouncementAdapter(R.layout.adapter_announcement, data, type);
        } else {
            adapter = new AnnouncementAdapter(R.layout.adapter_announcement, data, type);

        }
        adapter.setOnDetailListener(new AnnouncementAdapter.OnclickListenerDetail() {
            @Override
            public void onDetail(AnnounceEntity item) {
                AnnouncementDetailActivity.actionStart(getmActivity(),item);
            }
        });
        adapter.bindToRecyclerView(rvAds);
        adapter.isFirstOnly(true);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageNo++;
                loadMore();
            }
        }, rvAds);
        adapter.setEmptyView(R.layout.empty_no_order);
    }

    private void loadMore() {
        refreshLayout.setEnabled(false);
        load(type);
    }

    private void load(String type) {
        if (type.equals(NOTICE)) {
            presenter.getAnnouncement(SharedPreferenceInstance.getInstance().getTOKEN(),"NOTICE", pageNo, pageSize);
        } else if (type.equals(ANNOUNCE)) {
            presenter.getAnnouncement(SharedPreferenceInstance.getInstance().getTOKEN(),"ANNOUNCEMENT", pageNo, pageSize);
        } else if (type.equals(ACTIVTY)) {
            presenter.getAnnouncement(SharedPreferenceInstance.getInstance().getTOKEN(),"ACTIVITY", pageNo, pageSize);
        }

    }

    @Override
    public void getAnnounceSuccess(List<AnnounceEntity> data) {
        adapter.loadMoreComplete();
        if (refreshLayout==null){
            return;
        }
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);

        if (data == null || data.size() == 0) {
            if (pageNo == 1) {
                this.data.clear();
                adapter.notifyDataSetChanged();
            }
            return;
        }
        if (pageNo == 1) {
            this.data.clear();
            this.data.addAll(data);
        } else {
            this.data.addAll(data);
        }
        if (data.size() < pageSize) {
            adapter.loadMoreEnd();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getAnnounceFial(int e, String meg) {

    }
}
