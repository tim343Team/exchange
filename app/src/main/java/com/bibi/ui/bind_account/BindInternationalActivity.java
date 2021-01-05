package com.bibi.ui.bind_account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import com.bibi.R;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.entity.AccountSetting;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/18
 */
public class BindInternationalActivity extends BaseActivity implements BindAccountContact.InternationView {
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etNewPwd)
    EditText etNewPwd;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;

    private BindAccountContact.InternationPresenter presenter;
    private AccountSetting setting;

    public static void actionStart(Context context, AccountSetting accountSetting) {
        Intent intent = new Intent(context, BindInternationalActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("accountSetting", accountSetting);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_internation;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new BindInternationPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        setting = (AccountSetting) getIntent().getSerializableExtra("accountSetting");
        if (setting.getSwiftVerified() == 1) {
            etAddress.setText(setting.getSwift().getSwiftAddress() + "");
        }
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
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
    public void setPresenter(BindAccountContact.InternationPresenter presenter) {
        this.presenter = presenter;
    }


    private void confirm() {
        String name = etName.getText().toString();
        String address = etAddress.getText().toString();
        String pwd = etNewPwd.getText().toString();
        if (!WonderfulStringUtils.isEmpty(name, address, pwd)) {
            if (setting.getSwiftVerified() == 0) {
                presenter.getUpdateInter(getToken(), name, address, pwd);
            } else if (setting.getSwiftVerified() == 1) {
                presenter.updateInter(getToken(), name, address, pwd);
            }
        } else
            WonderfulToastUtils.showToast(getResources().getString(R.string.Incomplete_information));
    }

    @Override
    public void getBindSwiftSuccess(String obj) {
        WonderfulToastUtils.showToast(R.string.set_up_success);
        finish();
    }

    @Override
    public void getBindSwiftFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
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
