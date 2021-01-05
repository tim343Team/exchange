package com.bibi.ui.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import com.bibi.entity.ExtractInfo;
import butterknife.BindView;
import butterknife.OnClick;

import com.bibi.R;
import com.bibi.adapter.AddressAddAdapter;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.TiBiAddress;
import com.bibi.entity.User;
import com.bibi.ui.extract.ExtractActivity;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/30
 */
public class AddressAddActivity extends BaseActivity implements AddressContract.View {
    private AddressContract.Presenter presenter;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etCode)
    EditText etCode;

    private CountDownTimer timer;
    String address;
    String unit;
    String aims;
    String code;
    String remark;

    @OnClick(R.id.ibBack)
    void back(){
        finish();
    }

    public static void actionStart(Context context, String unit) {
        Intent intent = new Intent(context, AddressAddActivity.class);
        intent.putExtra("unit", unit);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_address_add;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new AddressPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();

            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    @Override
    protected void obtainData() {
        unit = getIntent().getStringExtra("unit");
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void getAddressSuccess(TiBiAddress address) {

    }

    @Override
    public void getAddressFial(Integer code, String toastMessage) {

    }

    @Override
    public void deleteAddressSuccess(String message) {

    }

    @Override
    public void deleteAddressFial(Integer code, String toastMessage) {

    }

    @Override
    public void getAddressCodeSuccess(String message) {
        WonderfulToastUtils.showToast(getResources().getString(R.string.send_success));
        fillCodeView(90L * 1000);
    }

    @Override
    public void getAddressCodeFial(Integer code, String toastMessage) {
        tvSend.setEnabled(true);
        WonderfulToastUtils.showToast(toastMessage);
    }

    @Override
    public void addAddressCodeSuccess(String message) {
        WonderfulToastUtils.showToast(message);
        finish();
    }

    @Override
    public void addAddressCodeFial(Integer code, String toastMessage) {
        WonderfulToastUtils.showToast(toastMessage);
//        WonderfulCodeUtils.checkedErrorCode(AddressAddActivity.this, code, toastMessage);
    }

    @Override
    public void getAllAddressSuccess(List<ExtractInfo> obj) {

    }

    @Override
    public void getAllAddressFial(Integer code, String toastMessage) {

    }

    @Override
    public void setPresenter(AddressContract.Presenter presenter) {

    }


    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    private void sendCode() {
//        User user = MyApplication.getApp().getCurrentUser();
//        String phone = user.getMobile();
//        if (WonderfulStringUtils.isEmpty(phone)) return;
        presenter.sendCode(getToken());
        tvSend.setEnabled(false);
    }

    private void add() {
        User user = MyApplication.getApp().getCurrentUser();
        aims = user.getMobile();
        address = etAddress.getText().toString();
        code = etCode.getText().toString();
        remark = etRemark.getText().toString();
        if (address.equals("") || code.equals("") || aims.equals("") || unit.equals("")) {
            WonderfulToastUtils.showToast("信息填写不完整");
            return;
        }
        presenter.addAddress(getToken(), address, unit, aims, code, remark);
    }

    private void fillCodeView(long time) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                try{
                    tvSend.setText(getResources().getString(R.string.re_send) + "(" + millisUntilFinished / 1000 + "s)");
                }catch (Exception e){

                }
            }

            @Override
            public void onFinish() {
                tvSend.setText(getResources().getString(R.string.send_code));
                tvSend.setEnabled(true);
                timer.cancel();
                timer = null;
            }
        };
        timer.start();
    }
}
