package com.bibi.ui.extract;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import com.bibi.entity.RechargeSupportContract;
import com.bibi.entity.SimpleListBean;
import com.bibi.entity.SimpleListItem;
import com.bibi.ui.dialog.ListDialogFragment;
import com.bibi.ui.main.asset.AssetActivity;
import com.bibi.ui.recharge.RechargeActivity;

import butterknife.OnClick;

import com.bibi.R;
import com.bibi.adapter.TextWatcher;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.Address;
import com.bibi.entity.Coin;
import com.bibi.entity.CoinContract;
import com.bibi.entity.ExtractInfo;
import com.bibi.app.UrlFactory;
import com.bibi.ui.common.ChooseCoinActivity;
import com.bibi.ui.dialog.CommonDialog;
import com.bibi.ui.dialog.CommonDialogFragment;
import com.bibi.ui.main.asset.ReportActivity;
import com.bibi.ui.recharge.ReportDialogFragment;
import com.bibi.ui.wallet.TiBiJLActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.okhttp.StringCallback;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import com.bibi.app.Injection;

import okhttp3.Request;

import static com.bibi.ui.extract.AddressActivity.RETURN_ADDRESS;

public class ExtractActivity extends BaseActivity implements ExtractContract.View {
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.llAddressName)
    LinearLayout llAddressName;
    @BindView(R.id.tvAddressTitle)
    TextView tvAddressTitle;
    @BindView(R.id.etAddressName)
    TextView etAddressName;
    @BindView(R.id.llAddress)
    LinearLayout llAddress;
    @BindView(R.id.tvCanUse)
    TextView tvCanUse;
    @BindView(R.id.tvUnit1)
    TextView tvUnit1;
    @BindView(R.id.tvUnit2)
    TextView tvUnit2;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.ivInto)
    ImageView ivInto;
    @BindView(R.id.etCount)
    EditText etCount;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvExtract)
    TextView tvExtract;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etAddressTag)
    EditText etAddressTag;
    @BindView(R.id.llCode)
    LinearLayout llCode;
    @BindView(R.id.llAddressTag)
    LinearLayout llAddressTag;
    private ExtractContract.Presenter presenter;
    @BindView(R.id.yan)
    ImageView yan;
    private boolean isYan = false;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etCode)
    EditText etCode;
    private CountDownTimer timer;
    @BindView(R.id.view_back)
    View view_back;

    private List<ExtractInfo> extractInfos = new ArrayList<>();
    private CoinContract coinContract;
    private AlertDialog authDialog;
    private String addressType = "";

    @OnClick(R.id.llAddressName)
    public void selectAddressName() {
        presenter.getRechargeSupport(getToken(), coinContract.getCoin().getName());
    }

    public static void actionStart(Context context, CoinContract coin) {
        Intent intent = new Intent(context, ExtractActivity.class);
        intent.putExtra("coin", coin);
        context.startActivity(intent);

    }

    private void fillCodeView(long time) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (tvGetCode != null) {
                    tvGetCode.setText(getResources().getString(R.string.re_send) + "（" + millisUntilFinished / 1000 + "）");
                }

            }

            @Override
            public void onFinish() {
                if (tvGetCode != null) {
                    tvGetCode.setText(R.string.send_code);
                    tvGetCode.setEnabled(true);
                }
                timer.cancel();
                timer = null;
            }
        };
        timer.start();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_extract;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new ExtractPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivInto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (extractInfos.size() == 0) {
                    WonderfulToastUtils.showToast(getResources().getString(R.string.noAddAddressTip));
                    return;
                }
                for (ExtractInfo extractInfo : extractInfos) {
                    if (extractInfo.getName().equals(addressType)) {
                        AddressActivity.actionStart(ExtractActivity.this, extractInfo.getAddresses());
                        break;
                    }
                }
            }
        });
        tvExtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extract();
            }
        });
        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coinContract != null)
                    etCount.setText(new BigDecimal(coinContract.getBalance()).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString() + "");
            }
        });
        etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                for (ExtractInfo extractInfo : extractInfos) {
                    if (extractInfo.getName().equals(addressType)) {
                        if (etCount.getText().toString().equals("")) {
                            tvFee.setText("手续费：" + new BigDecimal(extractInfo.getTxFee()).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
                        } else {
                            double number = Double.parseDouble(etCount.getText().toString());
                            if (number * extractInfo.getTxFeeRatio() > extractInfo.getTxFee()) {
                                tvFee.setText("手续费：" + new BigDecimal(number * extractInfo.getTxFeeRatio()).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
                            } else {
                                tvFee.setText("手续费：" + new BigDecimal(extractInfo.getTxFee()).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
                            }
                        }
                    }
                }
            }
        });
        yan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isYan = !isYan;
                Drawable no = getResources().getDrawable(R.drawable.yan_no);
                Drawable yes = getResources().getDrawable(R.drawable.yan_yes);
                if (isYan) {
                    //显示
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    yan.setImageDrawable(no);

                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    yan.setImageDrawable(yes);
                }
            }
        });

        String type = MyApplication.getApp().getCurrentUser().getGoogleState();
        if (AUTH_GOOGLE_NO.equals(type)) {
            llCode.setVisibility(View.VISIBLE);
        } else {
            llCode.setVisibility(View.GONE);
        }
        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
    }

    private void send() {
        tvGetCode.setEnabled(false);
        WonderfulOkhttpUtils.post().url(UrlFactory.getCode()).addHeader("x-auth-token", SharedPreferenceInstance.getInstance().getTOKEN()).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                WonderfulLogUtils.logi("发送提币出错", "发送提币出错：" + e.getMessage());
                tvGetCode.setEnabled(true);
            }

            @Override
            public void onResponse(String response) {
                WonderfulLogUtils.logi("发送提币回执：", "发送提币回执：" + response.toString());
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("code") == 0) {
                        WonderfulToastUtils.showToast(getResources().getString(R.string.send_success));
                        fillCodeView(90 * 1000);
                    } else {
                        WonderfulCodeUtils.checkedErrorCode(ExtractActivity.this, object.optInt("code"), object.optString("message"));
                        WonderfulLogUtils.logi("发送提币出错", "发送提币出错：");
                        tvGetCode.setEnabled(true);
                    }
                } catch (JSONException e) {
                    WonderfulLogUtils.logi("发送提币出错", "发送提币出错：" + e.getMessage());
                    tvGetCode.setEnabled(true);
                    e.printStackTrace();
                }
            }
        });
    }

    String AUTH_GOOGLE_NO = "0";
    String AUTH_GOOGLE_YES = "1";

    private void extract() {
        if (coinContract == null) return;
        final String address = etAddress.getText().toString();
//        final String unit = coin.getCoin().getUnit();
        final String unit = etAddressName.getText().toString();
        final String amount = etCount.getText().toString();
        final String code = etCode.getText().toString();
        final String tag = etAddressTag.getText().toString();
        if (WonderfulStringUtils.isEmpty(address) || WonderfulStringUtils.isEmpty(unit) ||
                WonderfulStringUtils.isEmpty(amount)) {
            WonderfulToastUtils.showToast(R.string.Incomplete_information);
            return;
        } else {
            final String jyPassword = etPassword.getText().toString();
            final String remark = "";
            //如果开启了谷歌验证，还需要填写谷歌验证码
            String type = MyApplication.getApp().getCurrentUser().getGoogleState();
            if (AUTH_GOOGLE_NO.equals(type)) {
                if (WonderfulStringUtils.isEmpty(code)) {
                    WonderfulToastUtils.showToast(R.string.Incomplete_information);
                    return;
                }
                presenter.extract(SharedPreferenceInstance.getInstance().getTOKEN(), unit, amount, remark, jyPassword, address, code, "", tag);
            } else if (AUTH_GOOGLE_YES.equals(type)) {
                CommonDialogFragment commonDialogFragment = CommonDialogFragment.getInstance(CommonDialogFragment.TYPE_GOOGLE_CODE, getString(R.string.text_input_google_code), getString(R.string.text_input_google_code), getString(R.string.confirm), getString(R.string.cancle), true);
                commonDialogFragment.show(getSupportFragmentManager(), "google");
                commonDialogFragment.setCommitClickListener(new CommonDialogFragment.OnCommitClickListener() {
                    @Override
                    public void onCommitClick(String pass) {
                        if (TextUtils.isEmpty(pass)) {
                            WonderfulToastUtils.showToast(getResources().getString(R.string.text_input_google_code));
                        } else {
                            presenter.extract(SharedPreferenceInstance.getInstance().getTOKEN(), unit, amount, remark, jyPassword, address, "", pass, tag);
                        }
                    }
                });
            }
        }
    }


    @Override
    protected void obtainData() {
        coinContract = (CoinContract) getIntent().getSerializableExtra("coin");
        tvTitle.setText(coinContract.getCoin().getUnit() + getResources().getString(R.string.withdraw));
        if (coinContract.getCoin().getUnit().equals("XRP")) {
            llAddressTag.setVisibility(View.VISIBLE);
        } else {
            llAddressTag.setVisibility(View.GONE);
        }
    }


    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
//        presenter.getUSDTWallet(getToken());
        fillView();
        presenter.extractinfo(getToken(), coinContract.getCoin().getName());
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RETURN_ADDRESS) {
            if (resultCode == RESULT_OK && data != null) {
                etAddress.setText(((Address) data.getSerializableExtra("address")).getAddress());
            }
        }
    }

    @Override
    public void setPresenter(ExtractContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void errorMes(int e, String meg) {

    }

    @Override
    public void getRechargeSupportSuccess(List<RechargeSupportContract> objs, String parentCoin) {
        final SimpleListBean bean = new SimpleListBean();
        if (objs.size() == 0) {
            List<SimpleListItem> simpleListItems = new ArrayList<>();
            SimpleListItem item = new SimpleListItem();
            item.setContent(parentCoin);
            simpleListItems.add(item);
            bean.setNewsItems(simpleListItems);
        } else {
            List<SimpleListItem> simpleListItems = new ArrayList<>();
            for (RechargeSupportContract contract : objs) {
                SimpleListItem item = new SimpleListItem();
                item.setContent(contract.getName());
                simpleListItems.add(item);
            }
            bean.setNewsItems(simpleListItems);
        }
        ListDialogFragment listDialogFragment = ListDialogFragment.getInstance(bean);
        listDialogFragment.show(getSupportFragmentManager(), "bottom");
        listDialogFragment.setCallback(new ListDialogFragment.OperateCallback() {
            @Override
            public void ItemClick(int position) {
                etAddressName.setText(bean.getNewsItems().get(position).getContent());
                addressType = bean.getNewsItems().get(position).getContent();
                tvAddressTitle.setVisibility(View.VISIBLE);
                llAddress.setVisibility(View.VISIBLE);
                if (extractInfos.size() > 0)
                    for (ExtractInfo extractInfo : extractInfos) {
                        if (extractInfo.getName().equals(addressType)) {
                            if (etCount.getText().toString().equals("")) {
                                tvFee.setText("手续费：" + new BigDecimal(extractInfo.getTxFee()).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
                            } else {
                                double number = Double.parseDouble(etCount.getText().toString());
                                if (number * extractInfo.getTxFeeRatio() > extractInfo.getTxFee()) {
                                    tvFee.setText("手续费：" + new BigDecimal(number * extractInfo.getTxFeeRatio()).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
                                } else {
                                    tvFee.setText("手续费：" + new BigDecimal(extractInfo.getTxFee()).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString());
                                }
                            }
                        }
                    }
            }
        });
    }

    @Override
    public void extractinfoFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void extractinfoSuccess(List<ExtractInfo> obj) {
        extractInfos.clear();
        if (obj == null) return;
        extractInfos.addAll(obj);
//        for (ExtractInfo extractInfo : obj) {
//            if (extractInfo.getUnit().contains("ERC20")) {
//                this.extractInfoErc = extractInfo;
//            } else if (extractInfo.getUnit().contains("Omni")) {
//                this.extractInfoOmni = extractInfo;
//            }
//        }
    }

    @Override
    public void usdtInfoSuccess(CoinContract obj) {
        fillView();
    }

    @Override
    public void extractSuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
        finish();
    }

    @Override
    public void extractFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    private void fillView() {
        tvCanUse.setText(new BigDecimal(coinContract.getBalance()).setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString() + "");
        tvUnit1.setText(" " + coinContract.getCoin().getUnit());
        tvUnit2.setText(coinContract.getCoin().getUnit());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        obtainData();
    }

}
