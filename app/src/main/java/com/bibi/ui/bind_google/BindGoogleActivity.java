package com.bibi.ui.bind_google;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import com.bibi.R;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.GoogleCodeEntity;
import com.bibi.ui.account_pwd.AccountPwdActivity;
import com.bibi.ui.bind_email.BindEmailContract;
import com.bibi.ui.bind_email.BindEmailPresenter;
import com.bibi.utils.WonderfulCommonUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/3
 */
public class BindGoogleActivity extends BaseActivity implements BindGoogleContract.View {
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.tvSet)
    TextView tvSet;
    @BindView(R.id.llCopy)
    TextView llCopy;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.edit_code)
    TextView editCode;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;

    String secret="";
    String code="";
    private BindGoogleContract.Presenter presenter;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, BindGoogleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_google;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new BindGooglePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy();
            }
        });
        tvSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
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
        presenter.getGoogleCode(getToken());
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    private void copy() {
        WonderfulCommonUtils.copyText(this, tvAddress.getText().toString());
        WonderfulToastUtils.showToast(R.string.copy_success);
    }

    private void save() {
        code=editCode.getText().toString();
        if (secret.equals("") || code.equals("")) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.Incomplete_information));
            return;
        }
        presenter.bindGoogle(getToken(),secret,code);
    }


    @Override
    public void setPresenter(BindGoogleContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getGoogleSuccess(GoogleCodeEntity obj) {
        secret = obj.getSecret();
        tvAddress.setText(secret);
    }

    @Override
    public void getGoogleFail(Integer code, String toastMessage) {

    }

    @Override
    public void bindGoogleSuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
        MyApplication.getApp().getCurrentUser().setGoogleState("1");
        finish();
    }

    @Override
    public void bindGoogleFail(Integer code, String toastMessage) {
        WonderfulToastUtils.showToast(toastMessage);
    }
}
