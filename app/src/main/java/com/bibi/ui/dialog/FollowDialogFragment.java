package com.bibi.ui.dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.adapter.FollowHistoryAdapter;
import com.bibi.adapter.TypeRecyAdapter;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseDialogFragment;
import com.bibi.entity.CoinTypeEntity;
import com.bibi.entity.FollowHistoryContent;
import com.bibi.entity.NiurenArrayEntity;
import com.bibi.entity.NiurenEntity;
import com.bibi.ui.main.MainContract;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulCommonUtils;
import com.bibi.utils.WonderfulDpPxUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/12
 */
@SuppressLint("ValidFragment")
public class FollowDialogFragment extends BaseDialogFragment implements RadioGroup.OnCheckedChangeListener, MainContract.FollowView {
        @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.typeRecy)
    RecyclerView typeRecy;
    @BindView(R.id.typeRecyAuto)
    RecyclerView typeRecyAuto;
    @BindView(R.id.tab1)
    LinearLayout tab1;
    @BindView(R.id.tab2)
    LinearLayout tab2;
    @BindView(R.id.tvtab1)
    TextView tvtab1;
    @BindView(R.id.tvtab2)
    TextView tvtab2;
    @BindView(R.id.vtab1)
    View vtab1;
    @BindView(R.id.vtab2)
    View vtab2;
    @BindView(R.id.mOneTCP)
    EditText mOneTCP;
    @BindView(R.id.mPassword)
    EditText mPassword;
    @BindView(R.id.mRadioGroupMargin)
    RadioGroup mRadioGroupMargin;
    @BindView(R.id.mRadioGroupMarginAuto)
    RadioGroup mRadioGroupMarginAuto;
    @BindViews({R.id.mTabMarginOne, R.id.mTabMarginTwo, R.id.mTabMarginThree})
    RadioButton[] radioButtons;
    @BindViews({R.id.mTabMarginOneAuto, R.id.mTabMarginTwoAuto, R.id.mTabMarginThreeAuto})
    RadioButton[] radioAutoButtons;

    private List<String> lergave = new ArrayList<>();
    private List<CoinTypeEntity> coinTypeEntities = new ArrayList<>();
    private TypeRecyAdapter adapter, adapterAuto;
    private MainContract.FollowPresenter presenter;
    private String leverage = "";
    private String token = "";
    private String memberId = "";

    @OnClick(R.id.imBack)
    public void back(){
        dismiss();
    }

    @OnClick(R.id.mOneAdd)
    public void add(){
        try {
            double num = mOneTCP.getText().toString().equals("") ? 0.0 : Double.valueOf(mOneTCP.getText().toString());
            mOneTCP.setText(String.valueOf(num + 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClick(R.id.mOneSub)
    public void sub(){
        try {
            double num1 = Double.valueOf(mOneTCP.getText().toString());
            if ((num1 - 1) > 0) {
                mOneTCP.setText(String.valueOf(num1 - 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.lltab1)
    public void select1() {
        tvtab1.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
        tvtab2.setTextColor(getActivity().getResources().getColor(R.color.text_595959));
        vtab1.setVisibility(View.VISIBLE);
        vtab2.setVisibility(View.INVISIBLE);
        tab1.setVisibility(View.VISIBLE);
        tab2.setVisibility(View.GONE);
        int height = 0;
        if (ImmersionBar.hasNavigationBar(getActivity()))
            height = WonderfulCommonUtils.getStatusBarHeight(getActivity());
//        window.setLayout(MyApplication.getApp().getmWidth() - 60, llContent.getHeight() + WonderfulDpPxUtils.dip2px(getActivity(), height));
//        window.setLayout(MyApplication.getApp().getmWidth() - 60, MyApplication.getApp().getmHeight() - WonderfulDpPxUtils.dip2px(getActivity(), height));
    }

    @OnClick(R.id.lltab2)
    public void select2() {
//        tvtab1.setTextColor(getActivity().getResources().getColor(R.color.text_595959));
//        tvtab2.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
//        vtab1.setVisibility(View.INVISIBLE);
//        vtab2.setVisibility(View.VISIBLE);
//        tab1.setVisibility(View.GONE);
//        tab2.setVisibility(View.VISIBLE);
//        int height = 0;
//        if (ImmersionBar.hasNavigationBar(getActivity()))
//            height = WonderfulCommonUtils.getStatusBarHeight(getActivity());
//        window.setLayout(MyApplication.getApp().getmWidth() - 60, MyApplication.getApp().getmHeight() - WonderfulDpPxUtils.dip2px(getActivity(), height));
//        window.setLayout(MyApplication.getApp().getmWidth() - 60, llContent.getHeight() + WonderfulDpPxUtils.dip2px(getActivity(), height));
    }

    @OnClick(R.id.tvBuy)
    public void tvBuy() {
        try{
            if (!adapter.getSymbolType().equals("") && Double.parseDouble(mOneTCP.getText().toString()) > 0 && !mPassword.getText().toString().equals("") && !leverage.equals("")) {
                presenter.addFollow(token, adapter.getSymbolType(), leverage, mOneTCP.getText().toString(), mPassword.getText().toString(), memberId);
            } else {
                WonderfulToastUtils.showToast("信息填写不完整");
            }
        }catch (Exception e){
            WonderfulToastUtils.showToast("信息填写不完整");
        }

    }

    @SuppressLint("ValidFragment")
    public FollowDialogFragment(List<String> leverages, List<CoinTypeEntity> coinTypeEntities, String token, String memberId, MainContract.FollowPresenter presenter) {
        this.lergave = leverages;
        this.coinTypeEntities = coinTypeEntities;
        this.token = token;
        this.memberId = memberId;
        this.presenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_follow;
    }

    @Override
    protected void initLayout() {
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.bottomDialog);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                int height = 0;
                if (ImmersionBar.hasNavigationBar(getActivity()))
                    height = WonderfulCommonUtils.getStatusBarHeight(getActivity());
//                Log.e("TAGGGGGGGG:", llContent.getHeight() + "");
                window.setLayout(MyApplication.getApp().getmWidth() - 60, llContent.getHeight() + WonderfulDpPxUtils.dip2px(getActivity(), height));
//                window.setLayout(MyApplication.getApp().getmWidth() - 60, MyApplication.getApp().getmHeight() - WonderfulDpPxUtils.dip2px(getActivity(), height));
            }
        });
//        window.setLayout(llContent.getWidth(), llContent.getHeight());

    }

    @Override
    protected void initView() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        for (int i = 0; i < lergave.size(); i++) {
            if (i > 2) {
                break;
            }
            if (i == 0) {
                leverage = lergave.get(i);
            }
            radioButtons[i].setVisibility(View.VISIBLE);
            radioButtons[i].setText(lergave.get(i));
            radioAutoButtons[i].setVisibility(View.VISIBLE);
            radioAutoButtons[i].setText(lergave.get(i));
        }
        mRadioGroupMargin.setOnCheckedChangeListener(this);
        mRadioGroupMarginAuto.setOnCheckedChangeListener(this);
        radioButtons[0].setSelected(true);
        radioAutoButtons[0].setSelected(true);

        LinearLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        typeRecy.setLayoutManager(manager);
        adapter = new TypeRecyAdapter(R.layout.adapter_gride_type, coinTypeEntities);
        adapter.bindToRecyclerView(typeRecy);
        adapter.setOnSelectListener(new TypeRecyAdapter.OnclickListenerSelect() {
            @Override
            public void select(String symbol) {
                adapter.setSymbolType(symbol);
                adapter.notifyItemRangeChanged(0, coinTypeEntities.size());
            }
        });

        LinearLayoutManager manager2 = new GridLayoutManager(getActivity(), 4);
        typeRecyAuto.setLayoutManager(manager2);
        adapterAuto = new TypeRecyAdapter(R.layout.adapter_gride_type, coinTypeEntities);
        adapterAuto.bindToRecyclerView(typeRecyAuto);
        adapterAuto.setOnSelectListener(new TypeRecyAdapter.OnclickListenerSelect() {
            @Override
            public void select(String symbol) {
                adapterAuto.setSymbolType(symbol);
                adapterAuto.notifyItemRangeChanged(0, coinTypeEntities.size());
            }
        });
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getCheckedRadioButtonId()) {
            case R.id.mTabMarginOne: //
                radioButtons[0].setSelected(true);
                radioButtons[1].setSelected(false);
                radioButtons[2].setSelected(false);
                leverage = radioButtons[0].getText().toString();
                break;
            case R.id.mTabMarginTwo: //
                radioButtons[0].setSelected(false);
                radioButtons[1].setSelected(true);
                radioButtons[2].setSelected(false);
                leverage = radioButtons[1].getText().toString();
                break;
            case R.id.mTabMarginThree: //
                radioButtons[0].setSelected(false);
                radioButtons[1].setSelected(false);
                radioButtons[2].setSelected(true);
                leverage = radioButtons[2].getText().toString();
                break;
            case R.id.mTabMarginOneAuto: //
                radioAutoButtons[0].setSelected(true);
                radioAutoButtons[1].setSelected(false);
                radioAutoButtons[2].setSelected(false);
                break;
            case R.id.mTabMarginTwoAuto: //
                radioAutoButtons[0].setSelected(false);
                radioAutoButtons[1].setSelected(true);
                radioAutoButtons[2].setSelected(false);
                break;
            case R.id.mTabMarginThreeAuto: //
                radioAutoButtons[0].setSelected(false);
                radioAutoButtons[1].setSelected(false);
                radioAutoButtons[2].setSelected(true);
                break;
            default:
        }
    }

    @Override
    public void niurenRankSuccess(NiurenArrayEntity obj) {

    }

    @Override
    public void niurenRankListSuccess(NiurenArrayEntity obj) {

    }

    @Override
    public void niurenRankFail(Integer code, String toastMessage) {

    }

    @Override
    public void CoinTypeSuccess(List<CoinTypeEntity> obj) {

    }

    @Override
    public void LeverageSuccess(List<String> obj) {

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
        dismiss();
    }

    @Override
    public void addFollowFial(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);

    }

    @Override
    public void getLockUSDTSuccess(String obj) {

    }

    @Override
    public void setPresenter(MainContract.FollowPresenter presenter) {
//        this.presenter = presenter;
    }

    @Override
    public void hideLoadingPopup() {

    }

    @Override
    public void displayLoadingPopup() {

    }
}
