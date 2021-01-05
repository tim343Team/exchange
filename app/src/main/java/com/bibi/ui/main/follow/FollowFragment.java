package com.bibi.ui.main.follow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.adapter.FollowAdapter;
import com.bibi.base.BaseTransFragment;
import com.bibi.customview.CircleImageView;
import com.bibi.entity.CoinTypeEntity;
import com.bibi.entity.Follow;
import com.bibi.entity.FollowHistoryContent;
import com.bibi.entity.FollowHistoryEntity;
import com.bibi.entity.MyNiurenEntity;
import com.bibi.entity.NiurenArrayEntity;
import com.bibi.entity.NiurenEntity;
import com.bibi.ui.dialog.BuyOrSellDialogFragment;
import com.bibi.ui.dialog.FollowDialogFragment;
import com.bibi.ui.fiat_exchange.FiatExchangeActivity;
import com.bibi.ui.join_follow.JoinFollowActivity;
import com.bibi.ui.main.MainContract;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/11
 */
public class FollowFragment extends BaseTransFragment implements MainContract.FollowView {
    @BindView(R.id.llTitle)
    RelativeLayout llTitle;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.tvJoin)
    TextView tvJoin;
    @BindView(R.id.tvTabOne)
    TextView tvTabOne;
    @BindView(R.id.tvTabTwo)
    TextView tvTabTwo;
    @BindView(R.id.tvTabThree)
    TextView tvTabThree;
    @BindViews({R.id.ivHeader_1, R.id.ivHeader_2, R.id.ivHeader_3})
    CircleImageView[] ivHeaders;
    @BindViews({R.id.tvUserNameOne, R.id.tvUserNameTwo, R.id.tvUserNameThree})
    TextView[] tvUserNames;
    @BindViews({R.id.tvProfitOne, R.id.tvProfitTwo, R.id.tvProfitThree})
    TextView[] tvProfits;
    @BindView(R.id.vTabOne)
    View vTabOne;
    @BindView(R.id.vTabTwo)
    View vTabTwo;
    @BindView(R.id.vTabThree)
    View vTabThree;
    @BindView(R.id.llRask)
    LinearLayout llRask;
    @BindView(R.id.llJoin)
    RelativeLayout llJoin;
    @BindView(R.id.imgMyAvatar)
    CircleImageView imgMyAvatar;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvRask)
    TextView tvRask;
    @BindView(R.id.tvProfit)
    TextView tvProfit;
    private FollowAdapter followAdapter;
    private List<String> leverages = new ArrayList<>();
    private List<NiurenEntity> follows = new ArrayList<>();
    private List<CoinTypeEntity> coinTypeEntities = new ArrayList<>();
    private FollowDialogFragment followDialogFragment;
    private MainContract.FollowPresenter presenter;

    @OnClick(R.id.tvTabOne)
    public void showHourRank() {
        tvTabOne.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        tvTabTwo.setTextColor(getActivity().getResources().getColor(R.color.white));
        tvTabThree.setTextColor(getActivity().getResources().getColor(R.color.white));
        vTabOne.setVisibility(View.VISIBLE);
        vTabTwo.setVisibility(View.INVISIBLE);
        vTabThree.setVisibility(View.INVISIBLE);
        presenter.rank(SharedPreferenceInstance.getInstance().getTOKEN(),"/24h");
    }

    @OnClick(R.id.tvTabTwo)
    public void showWeekRank() {
        tvTabOne.setTextColor(getActivity().getResources().getColor(R.color.white));
        tvTabTwo.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        tvTabThree.setTextColor(getActivity().getResources().getColor(R.color.white));
        vTabOne.setVisibility(View.INVISIBLE);
        vTabTwo.setVisibility(View.VISIBLE);
        vTabThree.setVisibility(View.INVISIBLE);
        presenter.rank(getmActivity().getToken(),"/week");
    }

    @OnClick(R.id.tvTabThree)
    public void showMonthRank() {
        tvTabOne.setTextColor(getActivity().getResources().getColor(R.color.white));
        tvTabTwo.setTextColor(getActivity().getResources().getColor(R.color.white));
        tvTabThree.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        vTabOne.setVisibility(View.INVISIBLE);
        vTabTwo.setVisibility(View.INVISIBLE);
        vTabThree.setVisibility(View.VISIBLE);
        presenter.rank(getmActivity().getToken(),"/month");
    }

    @OnClick(R.id.tvHistory)
    public void showHistory() {
        FollowHistoryActivity.actionStart(getActivity());
    }

    @OnClick({R.id.tvFollowDialog_1, R.id.tvFollowDialog_2, R.id.tvFollowDialog_3})
    public void showDialog(TextView tv) {
        switch (tv.getId()) {
            case R.id.tvFollowDialog_1:
                if (follows.size() > 0) {
                    showDialog(follows.get(0).getMemberId() + "");
                }
                break;
            case R.id.tvFollowDialog_2:
                if (follows.size() > 1) {
                    showDialog(follows.get(1).getMemberId() + "");
                }
                break;
            case R.id.tvFollowDialog_3:
                if (follows.size() > 2) {
                    showDialog(follows.get(2).getMemberId() + "");
                }
                break;
            default:
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_follow;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), JoinFollowActivity.class));
            }
        });
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {
        initFollowContent();
    }

    @Override
    protected void loadData() {
        presenter.rank(getmActivity().getToken(),"/24h");
//        presenter.rankList();
        presenter.coin();
        presenter.leverage();
    }

    @Override
    protected String getmTag() {
        return null;
    }

    @Override
    public void setPresenter(MainContract.FollowPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(getActivity(), llTitle);
            isSetTitle = true;
        }
    }

    private void initFollowContent() {
        // 牛人榜的适配器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        followAdapter = new FollowAdapter(follows);
        View head = View.inflate(getActivity(), R.layout.adapter_follow_head, null);
        followAdapter.addHeaderView(head);
        followAdapter.isFirstOnly(true);
        followAdapter.setLoad(true);
        mRecyclerView.setAdapter(followAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        followAdapter.setOnSelectListener(new FollowAdapter.OnclickListenerSelect() {
            @Override
            public void select(String memberId) {
                Log.e("fMemberId itme:",memberId+"");
                showDialog(memberId);
            }
        });
    }

    private void showDialog(String memberId) {
        if (followDialogFragment == null)
        followDialogFragment = new FollowDialogFragment(leverages, coinTypeEntities, getmActivity().getToken(), memberId, presenter);
        followDialogFragment.show(getActivity().getSupportFragmentManager(), "center");
    }

    public void tcpNotify() {
        if (followAdapter != null) {
            followAdapter.clearPosition();
            followAdapter.notifyDataSetChanged();
        }
    }

    public void dataLoaded() {

    }

    @Override
    public void niurenRankSuccess(NiurenArrayEntity objArray) {
        List<NiurenEntity> obj = objArray.getObjs();
        List<MyNiurenEntity> objMy = objArray.getObjsMy();
        for (int i = 0; i < obj.size(); i++) {
            if (i > 2) {
                break;
            }
            tvUserNames[i].setText(obj.get(i).getUserName());
            tvProfits[i].setText(new DecimalFormat("#0.0000").format(obj.get(i).getProfit()));
            if (obj.get(i).getAvatar() == null || WonderfulStringUtils.isEmpty(obj.get(i).getAvatar()))
                Glide.with(this).load(R.mipmap.icon_default_header).centerCrop().into(ivHeaders[i]);
            else
                Glide.with(this).load(obj.get(i).getAvatar()).centerCrop().into(ivHeaders[i]);
        }
        follows.clear();
        follows.addAll(obj);
        followAdapter.clearPosition();
        followAdapter.notifyDataSetChanged();
        //我的排名
        if(objMy.size()>0){
            llJoin.setVisibility(View.GONE);
            llRask.setVisibility(View.VISIBLE);
            tvUserName.setText(objMy.get(0).getUserName());
            tvRask.setText(getResources().getString(R.string.current_ranking)+objMy.get(0).getRank());
            tvProfit.setText(objMy.get(0).getProfit()+"");
            if (objMy.get(0).getAvatar() == null || WonderfulStringUtils.isEmpty(objMy.get(0).getAvatar()))
                Glide.with(getmActivity()).load(R.mipmap.icon_default_header).centerCrop().into(imgMyAvatar);
            else
                Glide.with(getmActivity()).load(objMy.get(0).getAvatar()).centerCrop().into(imgMyAvatar);
        }else {
            llJoin.setVisibility(View.VISIBLE);
            llRask.setVisibility(View.GONE);
        }
    }

    @Override
    public void niurenRankListSuccess(NiurenArrayEntity obj) {
//        follows.clear();
//        follows.addAll(obj);
//        followAdapter.notifyDataSetChanged();
    }

    @Override
    public void niurenRankFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void CoinTypeSuccess(List<CoinTypeEntity> obj) {
        coinTypeEntities.clear();
        coinTypeEntities.addAll(obj);
    }

    @Override
    public void LeverageSuccess(List<String> obj) {
        leverages.clear();
        leverages.addAll(obj);
    }

    @Override
    public void HistorySuccess(List<FollowHistoryContent> obj) {

    }

    @Override
    public void historyFial(Integer code, String toastMessage) {

    }

    @Override
    public void cancelSuccess(String obj) {

    }

    @Override
    public void applySuccess(String obj) {

    }

    @Override
    public void applyFial(Integer code, String toastMessage) {

    }

    @Override
    public void addFollowSuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
        followDialogFragment.dismiss();
    }

    @Override
    public void addFollowFial(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void getLockUSDTSuccess(String obj) {

    }
}
