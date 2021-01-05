package com.bibi.ui.main.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.OnClick;
import okhttp3.Request;
import com.bibi.R;
import com.bibi.app.UrlFactory;
import com.bibi.data.DataSource;
import com.bibi.data.RemoteDataSource;
import com.bibi.ui.aboutus.AboutUsActivity;
import com.bibi.ui.asset_transfer.AssetTransferActivity;
import com.bibi.ui.bind_account.BindAccountActivity;
import com.bibi.ui.common.ChooseCoinActivity;
import com.bibi.ui.credit.CreditInfoActivity;
import com.bibi.ui.credit.VideoCreditActivity;
import com.bibi.ui.dialog.CommonDialog;
import com.bibi.ui.dialog.CommonDialogFragment;
import com.bibi.ui.entrust.TrustListActivity;
import com.bibi.ui.ieo.IeoActivity;
import com.bibi.ui.login.LoginActivity;
import com.bibi.ui.main.MainContract;
import com.bibi.ui.my_order.MyOrderActivity;
import com.bibi.ui.my_promotion.PromotionActivity;
import com.bibi.ui.myinfo.MyInfoActivity;
import com.bibi.ui.recharge.RechargeActivity;
import com.bibi.ui.score_record.CandyRecordActivity;
import com.bibi.ui.seller.SellerApplyActivity;
import com.bibi.ui.servicefee.ServiceFeeActivity;
import com.bibi.ui.setting.FeeSettingActivity;
import com.bibi.ui.setting.HelpActivity;
import com.bibi.ui.setting.NoticeActivity;
import com.bibi.ui.setting.SettingActivity;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseTransFragment;
import com.bibi.entity.Coin;
import com.bibi.entity.SafeSetting;
import com.bibi.entity.User;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.customview.intercept.WonderfulScrollView;
import com.bibi.utils.WonderfulCommonUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

/**
 * Created by Administrator on 2018/1/29.
 */

public class UserFragment extends BaseTransFragment {
    public static final String TAG = UserFragment.class.getSimpleName();
    @BindView(R.id.llTop)
    LinearLayout llTop;
    @BindView(R.id.ll_account_center)
    LinearLayout ll_account_center;
    @BindView(R.id.llSettings)
    LinearLayout llSettings;
    @BindView(R.id.tv_user_name)
    TextView tvNickName;
    @BindView(R.id.tv_user_id)
    TextView tv_user_id;
    @BindView(R.id.ll_credit)
    LinearLayout ll_credit;


    Unbinder unbinder;
    private SafeSetting safeSetting;
    private PopupWindow loadingPopup;
    private AlertDialog dialog;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user;
    }

    /**
     * 初始化加载dialog
     */
    private void initLoadingPopup() {
        View loadingView = getLayoutInflater().inflate(R.layout.pop_loading, null);
        loadingPopup = new PopupWindow(loadingView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        loadingPopup.setFocusable(true);
        loadingPopup.setClippingEnabled(false);
        loadingPopup.setBackgroundDrawable(new ColorDrawable());
    }

    /**
     * 显示加载框
     */
    @Override
    public void displayLoadingPopup() {
        loadingPopup.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 隐藏加载框
     */
    @Override
    public void hideLoadingPopup() {
        if (loadingPopup != null) {
            loadingPopup.dismiss();
        }

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initLoadingPopup();
        llTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.getApp().isLogin()) {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
                }
            }
        });

        ll_account_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLoginOrCenter();
            }
        });
        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.actionStart(getActivity());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideLoadingPopup();

    }

    @Override
    public void onStart() {
        super.onStart();
        hideLoadingPopup();
    }


    private void toLoginOrCenter() {
        if (MyApplication.getApp().isLogin()) {
            MyInfoActivity.actionStart(getActivity());
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }

    @Override
    protected void obtainData() {
    }

    @Override
    protected void fillWidget() {
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        refreshLoginStatus();
    }

    public void refreshLoginStatus() {
        if (MyApplication.getApp().isLogin()) {
            loginingViewText();
        } else {
            notLoginViewText();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LoginActivity.RETURN_LOGIN:
                if (resultCode == Activity.RESULT_OK && getUserVisibleHint() && MyApplication.getApp().isLogin()) {
                    loginingViewText();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    notLoginViewText();
                }
                break;
            default:
        }
    }

    private void notLoginViewText() {
        tvNickName.setText(getResources().getString(R.string.not_logged_in));
        tv_user_id.setText(getResources().getString(R.string.welcome_to));
        ll_credit.setVisibility(View.GONE);

    }

    private void loginingViewText() {
        getSafeSetting();
        User user = MyApplication.getApp().getCurrentUser();
//        tvNickName.setText("Hi," + WonderfulStringUtils.getHideString(user.getMobile()));
        tv_user_id.setText("UID:" + user.getId());

    }

    private void getSafeSetting() {
        RemoteDataSource.getInstance().safeSetting(getmActivity().getToken(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                if (obj == null) {
                    return;
                }
                safeSetting = (SafeSetting) obj;
                MyApplication.realVerified = safeSetting.getRealVerified();
                ll_credit.setVisibility(safeSetting.getKycStatus()!=4?View.VISIBLE:View.GONE);

            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                if (code == 4000) {
                    MyApplication.getApp().setCurrentUser(null);
                    SharedPreferenceInstance.getInstance().saveaToken("");
                    SharedPreferenceInstance.getInstance().saveTOKEN("");
                    notLoginViewText();
                }
            }
        });
    }

    private void accountClick() {
        if (safeSetting == null) {
            return;
        }
        hideLoadingPopup();
        if (safeSetting.getRealVerified() == 1 && safeSetting.getFundsVerified() == 1) {
            BindAccountActivity.actionStart(getmActivity());
        } else {
            WonderfulToastUtils.showToast(getResources().getString(R.string.password_realname));
        }
    }


    @Override
    public String getmTag() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ll_credit)
    public void startCredit() {
        if (MyApplication.getApp().isLogin()) {
            if (safeSetting == null) {
                return;
            }
            if (safeSetting.getRealVerified() == 0) {
                if (safeSetting.getRealAuditing() == 1) {//审核中
                    WonderfulToastUtils.showToast(getResources().getString(R.string.creditting));
                } else {
                    if (safeSetting.getRealNameRejectReason() != null) {//失败
                        CreditInfoActivity.actionStart(getActivity(), CreditInfoActivity.AUDITING_FILED, safeSetting.getRealNameRejectReason());
                    } else {//未认证
                        CreditInfoActivity.actionStart(getActivity(), CreditInfoActivity.UNAUDITING, safeSetting.getRealNameRejectReason());
                    }
                }
            } else {
                //身份证已通过
                int kycStatu = safeSetting.getKycStatus();
                switch (kycStatu) {
                    //0-未实名,5-待实名审核，2-实名审核失败、1-视频审核,6-待视频审核 ，3-视频审核失败,4-实名成功
                    case 1:
                        //实名通过，进行视频认证
                        VideoCreditActivity.actionStart(getActivity(), "");
                        break;
                    case 3:
                        //视频认证失败，进行视频认证，显示失败原因
                        VideoCreditActivity.actionStart(getActivity(), safeSetting.getRealNameRejectReason());
                        break;
                    case 4:
                        WonderfulToastUtils.showToast(getString(R.string.verification));
                        break;
                    case 6:
                        WonderfulToastUtils.showToast(getString(R.string.video_credit_auditing));
                        break;
                    default:
                }
            }
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }

    @OnClick(R.id.llPromotion)
    public void startPromotion() {
        if (MyApplication.getApp().isLogin()) {
            PromotionActivity.actionStart(getActivity());
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }

    @OnClick(R.id.llOrder)
    public void startOrder() {
        if (MyApplication.getApp().isLogin()) {
            TrustListActivity.show(getActivity(),false);
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }

    @OnClick(R.id.ll_about_us)
    public void startAbout() {
        AboutUsActivity.actionStart(getActivity());
    }

    @OnClick(R.id.llCharge)
    public void startCharge() {
        if (MyApplication.getApp().isLogin()) {
            ChooseCoinActivity.actionStart(getActivity(), ChooseCoinActivity.TYPE_CHARGE);
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }

    @OnClick(R.id.llWithdraw)
    public void startWithdraw() {
        if (MyApplication.getApp().isLogin()) {
            ChooseCoinActivity.actionStart(getActivity(), ChooseCoinActivity.TYPE_WITHDRAW);
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }


    @OnClick(R.id.llTransfer)
    public void startTransfer() {
        if (MyApplication.getApp().isLogin()) {
            AssetTransferActivity.actionStart(getActivity(),AssetTransferActivity.START_TRANSFER_TYPE_EXCHANGE,"BTC");
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }

    @OnClick(R.id.ll_help_center)
    public void startHelpCenter(){
        HelpActivity.actionStart(getActivity());
    }

    @OnClick(R.id.llFee)
    public void startFeeSetting(){
        if (MyApplication.getApp().isLogin()) {
            startActivity(new Intent(getActivity(),FeeSettingActivity.class));
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }

    @OnClick(R.id.ll_seller_credit)
    public void startSellerCredit(){
        if (MyApplication.getApp().isLogin()) {
            if (MyApplication.realVerified == 1) {
                WonderfulOkhttpUtils.get().url(UrlFactory.getShangjia())
                        .addHeader("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN())
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                                int certifiedBusinessStatus = jsonObject1.optInt("certifiedBusinessStatus");
                                if (certifiedBusinessStatus == 5) {
                                    WonderfulToastUtils.showToast(getResources().getString(R.string.refund_deposit_auditing));
                                } else if (certifiedBusinessStatus == 6) {
                                    WonderfulToastUtils.showToast(getResources().getString(R.string.refund_deposit_failed));
                                } else {
                                    JSONObject data = jsonObject.optJSONObject("data");
                                    String reason = data.getString("detail");
                                    SellerApplyActivity.actionStart(getActivity(), certifiedBusinessStatus + "", reason);
                                }
                            } else {
                                WonderfulToastUtils.showToast(jsonObject.optString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                WonderfulToastUtils.showToast(getResources().getString(R.string.realname));
            }
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.RETURN_LOGIN);
        }
    }




}
