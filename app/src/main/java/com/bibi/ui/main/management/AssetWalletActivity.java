package com.bibi.ui.main.management;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import com.bibi.R;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.ProfitDetailEntity;
import com.bibi.entity.ProfitEntity;
import com.bibi.entity.ProfitListEntity;
import com.bibi.ui.asset_transfer.AssetTransferActivity;
import com.bibi.ui.main.asset.AssetActivity;
import com.bibi.ui.main.asset.ReportActivity;
import com.bibi.ui.main.management.presenter.AssetWalletPresenter;
import com.bibi.ui.main.management.presenter.ContractPresenter;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class AssetWalletActivity extends BaseActivity implements ManagementContract.AssetWalletView {
    @BindView(R.id.ivSetting)
    TextView tvSetting;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivSee)
    ImageView ivSee;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvCnyAmount)
    TextView tvCnyAmount;

    private int type = 0;//钱包收益 团队钱包
    private boolean isShow = true;
    double balanceUsd = 0;
    double balanceCny = 0;
    private ManagementContract.AssetWalletPresenter presenter;

    @OnClick(R.id.ibBack)
    void back() {
        finish();
    }

    @OnClick(R.id.ivSetting)
    public void startReport() {
        if (MyApplication.getApp().isLogin()) {
            ManagementListActivity.actionStart(this, type);
        } else {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
        }
    }

    @OnClick(R.id.tv_asset_transfer)
    public void startTransfer() {
        if (MyApplication.getApp().isLogin()) {
            if (type == 0) {
                TransferActivity.actionStart(this, AssetTransferActivity.START_TRANSFER_TYPE_EXCHANGE, balanceUsd);
            } else {
                TransferActivity.actionStart(this, AssetTransferActivity.START_TRANSFER_TYPE_FIAT, balanceUsd);
            }
        } else {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
        }
    }

    public static void actionStart(Context context, int type) {
        Intent intent = new Intent(context, AssetWalletActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_asset_wallet;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            tvSetting.setText(getResources().getString(R.string.profit_details));
            tvTitle.setText(getResources().getString(R.string.profit_wallet));
        } else {
            tvSetting.setText(getResources().getString(R.string.team_detail));
            tvTitle.setText(getResources().getString(R.string.team_profit));
        }
        ivSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchSee();
            }
        });
    }

    @Override
    protected void obtainData() {
        presenter = new AssetWalletPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
//        if(type==0){
//            presenter.getStaticProfit(getToken(),1);
//        }else {
//            presenter.getTeamProfit(getToken(),1);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (type == 0) {
            presenter.getStaticProfit(getToken(), 1);
        } else {
            presenter.getTeamProfit(getToken(), 1);
        }
    }

    private void switchSee() {
        if (!"*****".equals(tvCnyAmount.getText())) {
            tvCnyAmount.setText("*****");
            tvAmount.setText("********");
            Drawable drawable = getResources().getDrawable(R.drawable.icon_eye_guan);
            ivSee.setImageDrawable(drawable);
            isShow = false;
        } else {
            tvAmount.setText(new DecimalFormat("#0.00").format(balanceUsd));
            tvCnyAmount.setText("≈" + WonderfulMathUtils.getRundNumber(balanceCny, 2, null) + " CNY");
            Drawable drawable = getResources().getDrawable(R.drawable.icon_eye_open);
            ivSee.setImageDrawable(drawable);
            isShow = true;
        }
    }

    @Override
    public void getDataSuccess(List<ProfitListEntity> datas) {
        balanceUsd = 0.0;
        for (ProfitListEntity profitListEntity : datas) {
            balanceUsd += profitListEntity.getBalance();
        }
        if (isShow) {
            tvAmount.setText(new DecimalFormat("#0.00").format(balanceUsd));
        } else {
            tvAmount.setText("********");
        }
    }

    @Override
    public void getDetailDataSuccess(List<ProfitDetailEntity> datas) {

    }

    @Override
    public void setPresenter(ManagementContract.AssetWalletPresenter presenter) {
        this.presenter = presenter;
    }
}
