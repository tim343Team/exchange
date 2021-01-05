package com.bibi.ui.main.management;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bibi.entity.InvestTypeEntity;
import com.bibi.entity.ProfitManageEntity;
import com.github.tifezh.kchartlib.chart.base.IValueFormatter;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseTransFragment;
import com.bibi.entity.BannerEntity;
import com.bibi.entity.InvestDatRateEntity;
import com.bibi.entity.InvestQuotEntity;
import com.bibi.holder.BannerViewHolder;
import com.bibi.ui.dialog.InvestProfitDialog;
import com.bibi.ui.dialog.PasswordDialog;
import com.bibi.ui.dialog.PositionSettingDialog;
import com.bibi.ui.dialog.TacticsWalletDialog;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/7/27
 */
public class ManagementFragment extends BaseTransFragment implements ManagementContract.View {
    public static final String TAG = ManagementFragment.class.getSimpleName();
    @BindView(R.id.llTitle)
    RelativeLayout llTitle;
    @BindView(R.id.banner)
    MZBannerView banner;
    @BindView(R.id.tvPosition)
    TextView tvPosition;
    @BindView(R.id.llTypeDetaiRoot)
    LinearLayout llTypeDetaiRoot;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;

    private ArrayList<TextView> textViews = new ArrayList<>();
    private String coinName = ""; //选择的时段
    private String type = ""; //选择的时段
    private TextView selectedTextView; //选择的时间
    private List<String> imageUrls = new ArrayList<>();
    private ManagementContract.ManagementPresenter presenter;
    private String sysAdvertiseLocation = "0";
    private int amount = 0;
    private String min = "0";
    private String max = "0";
    private String content = "";
    private boolean isAddView = false;

    @OnClick(R.id.tvPosition)
    void positionSetting() {
        PositionSettingDialog modifyProfitFragment = PositionSettingDialog.getInstance(min, max);
        modifyProfitFragment.show(getActivity().getSupportFragmentManager(), "center");
        modifyProfitFragment.setCallback(new PositionSettingDialog.OperateCallback() {
            @Override
            public void cancleOrder(String stopLossPrice) {
                try {
                    amount = Integer.parseInt(stopLossPrice);
                } catch (Exception e) {
                    amount = 0;
                }
                tvPosition.setText(stopLossPrice);
            }
        });
    }

    @OnClick(R.id.tvContract)
    void contractDetail() {
        if (MyApplication.getApp().isLogin()) {
            ContractDetailDetailActivity.actionStart(getActivity());
        } else {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
        }
    }

    @OnClick(R.id.tvProfit)
    void profitDetail() {
        if (MyApplication.getApp().isLogin()) {
            ProfitDetailActivity.actionStart(getActivity());
        } else {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
        }
    }

    @OnClick(R.id.btnProfit)
    void AssetProfitWallet() {
        if (MyApplication.getApp().isLogin()) {
            AssetWalletActivity.actionStart(getActivity(), 0);
        } else {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
        }
    }

    @OnClick(R.id.btnTeam)
    void AssetTeamWallet() {
        if (MyApplication.getApp().isLogin()) {
            AssetWalletActivity.actionStart(getActivity(), 1);
        } else {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
        }
    }

    @OnClick(R.id.tvTactics)
    void tvTacticsWallet() {
        TacticsWalletDialog modifyProfitFragment = TacticsWalletDialog.getInstance(content);
        modifyProfitFragment.show(getActivity().getSupportFragmentManager(), "center");
    }

    @OnClick(R.id.btnPromotion)
    void gotoPromotion() {
        PromotionRootActivity.actionStart(getContext());
    }

    @OnClick(R.id.btnEnter)
    void tvEnter() {
        if (type.equals("")) {
            WonderfulToastUtils.showToast("请选择投资类型");
            return;
        }
        PasswordDialog passwordDialog = PasswordDialog.getInstance();
        passwordDialog.show(getActivity().getSupportFragmentManager(), "center");
        passwordDialog.setCallback(new PasswordDialog.OperateCallback() {
            @Override
            public void cancleOrder(String stopLossPrice) {
                if (MyApplication.getApp().isLogin()) {
                    if (amount > 0) {
                        if (!stopLossPrice.equals("")) {
                            presenter.trusteeshipSubmit(SharedPreferenceInstance.getInstance().getTOKEN(), amount + "", stopLossPrice, type, coinName);
                        } else {
                            WonderfulToastUtils.showToast("请输入资金密码");
                        }
                    } else {
                        WonderfulToastUtils.showToast("请输入仓位");
                    }
                } else {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
                }
            }
        });

    }

    @Override
    protected String getmTag() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_management;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void obtainData() {
        presenter = new ManagementPresenter(Injection.provideTasksRepository(getActivity()), this);
    }

    @Override
    protected void fillWidget() {
//        InvestProfitDialog dialog = InvestProfitDialog.getInstance("300.00");
//        dialog.show(getActivity().getSupportFragmentManager(), "center");
    }

    @Override
    protected void loadData() {
        presenter.banners(sysAdvertiseLocation);
//        presenter.getInvestQuota(SharedPreferenceInstance.getInstance().getTOKEN());
        presenter.getInvestType(SharedPreferenceInstance.getInstance().getTOKEN());
        presenter.getDayRate(SharedPreferenceInstance.getInstance().getTOKEN());
        presenter.getInvestRule(SharedPreferenceInstance.getInstance().getTOKEN());
        presenter.getInvestEarning(SharedPreferenceInstance.getInstance().getTOKEN());
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.pause();
    }


    public void tcpNotify() {

    }

    @Override
    public void bannersSuccess(final List<BannerEntity> obj) {
        //轮播图回执
        if (obj == null) {
            return;
        }
        imageUrls.clear();
        for (BannerEntity bannerEntity : obj) {
            imageUrls.add(bannerEntity.getUrl());
        }
        if (imageUrls.size() > 0) {
            banner.setPages(imageUrls, new MZHolderCreator<BannerViewHolder>() {
                @Override
                public BannerViewHolder createViewHolder() {
                    return new BannerViewHolder();
                }
            });
            banner.start();
        }
    }

    @Override
    public void submitSuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
    }

//    @Override
//    public void getQuotaSuccess(InvestQuotEntity obj) {
//        max = obj.getMax() + "";
//        min = obj.getMin() + "";
//    }

    @Override
    public void getDayRateSuccess(InvestDatRateEntity obj) {

    }

    @Override
    public void getInvestRuleSuccess(String obj) {
        content = obj;
    }

    @Override
    public void getInvestFinishSuccess(String obj) {

    }

    @Override
    public void getInvestEarningSuccess(List<ProfitManageEntity> obj) {
        double profit = 0.0;
        for (ProfitManageEntity entity : obj) {
            profit += entity.getProfit();
        }
        if (profit > 0) {
            InvestProfitDialog dialog = InvestProfitDialog.getInstance(profit + "");
            dialog.show(getActivity().getSupportFragmentManager(), "center");
            dialog.setCallback(new InvestProfitDialog.OperateCallback() {
                @Override
                public void ItemClick() {
                    presenter.getInvestFinish(SharedPreferenceInstance.getInstance().getTOKEN());
                }
            });
        }
    }

    @Override
    public void getInvestTypeSuccess(List<InvestTypeEntity> obj) {
        if (isAddView) {
            return;
        }
        for (InvestTypeEntity entity : obj) {
            //上下线
            max = entity.getMax() + "";
            min = entity.getMin() + "";
            coinName = entity.getCoinName();
            //收益详情
            View view = LayoutInflater.from(getmActivity()).inflate(R.layout.layout_management_type_detail, null);
            TextView tvTerm = view.findViewById(R.id.tvTerm);
            TextView tvDayRate = view.findViewById(R.id.tvDayRate);
            tvTerm.setText(entity.getTermCnName());
            tvDayRate.setText("日收益" + entity.getRate());
            llTypeDetaiRoot.addView(view);
            //投资类型
            TextView textView = (TextView) LayoutInflater.from(getmActivity()).inflate(R.layout.tab_type_textview, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMarginStart(10);
            layoutParams.setMarginEnd(10);
            layoutParams.weight = 1;
            textView.setLayoutParams(layoutParams);
            textView.setText(entity.getTermCnName());
            textView.setTag(entity.getTerm());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedTextView = (TextView) view;
                    String selectedTag = (String) selectedTextView.getTag();
                    type = selectedTag;
                    selectTag();
                }
            });
            textViews.add(textView);
            mRadioGroup.addView(textView);
            //
            isAddView = true;
        }
        //默认选择第一个tab
        selectedTextView = textViews.get(0);
        //画线
        Drawable home_zhang_no = getResources().getDrawable(
                R.drawable.circle_conner_rect_primay_solid);
        selectedTextView.setBackground(home_zhang_no);
        type = (String) selectedTextView.getTag();
    }


    @Override
    public void setPresenter(ManagementContract.ManagementPresenter presenter) {
        this.presenter = presenter;
    }

    private void selectTag() {
        for (int j = 0; j < textViews.size(); j++) {
            textViews.get(j).setSelected(false);
            textViews.get(j).setBackground(null);
            String tag = (String) textViews.get(j).getTag();
            if (tag.equals(type)) {
                textViews.get(j).setSelected(true);
                Drawable home_zhang_no1 = getResources().getDrawable(
                        R.drawable.circle_conner_rect_primay_solid);
                selectedTextView.setBackground(home_zhang_no1);
            }
        }
    }

}
