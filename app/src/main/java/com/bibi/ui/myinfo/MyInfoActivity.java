package com.bibi.ui.myinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.kyleduo.switchbutton.SwitchButton;

import com.bibi.R;
import com.bibi.ui.account_pwd.AccountPwdActivity;
import com.bibi.ui.account_pwd.EditAccountPwdActivity;
import com.bibi.ui.bind_account.BindAccountActivity;
import com.bibi.ui.bind_email.BindEmailActivity;
import com.bibi.ui.bind_email.EmailActivity;
import com.bibi.ui.bind_google.BindGoogleActivity;
import com.bibi.ui.bind_phone.BindPhoneActivity;
import com.bibi.ui.bind_phone.PhoneActivity;
import com.bibi.ui.credit.CreditInfoActivity;
import com.bibi.ui.credit.VideoActivity;
import com.bibi.ui.credit.VideoCreditActivity;
import com.bibi.ui.edit_login_pwd.EditLoginPwdActivity;
import com.bibi.ui.set_lock.SetLockActivity;
import com.bibi.app.GlobalConstant;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.ui.dialog.HeaderSelectDialogFragment;
import com.bibi.entity.SafeSetting;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.customview.CircleImageView;
import com.bibi.utils.WonderfulBitmapUtils;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulFileUtils;
import com.bibi.utils.WonderfulPermissionUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.bibi.utils.WonderfulUriUtils;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import com.bibi.app.Injection;
import com.bibi.utils.okhttp.WonderfulOkhttpUtils;

public class MyInfoActivity extends BaseActivity implements MyInfoContract.View {
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.imgPhone)
    ImageView imgPhone;
    @BindView(R.id.imgEmail)
    ImageView imgEmail;
    @BindView(R.id.ibRegist)
    TextView ibRegist;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.llAccountPwd)
    LinearLayout llAccountPwd;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvAcountPwd)
    TextView tvAcountPwd;
    @BindView(R.id.llPhone)
    LinearLayout llPhone;
    @BindView(R.id.llEmail)
    LinearLayout llEmail;
    @BindView(R.id.llGoogle)
    LinearLayout llGoogle;
    @BindView(R.id.llLoginPwd)
    LinearLayout llLoginPwd;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvGoogle)
    TextView tvGoogle;
    @BindView(R.id.imgGoogle)
    ImageView imgGoogle;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;
    @BindView(R.id.llLockSet)
    LinearLayout llLockSet;
    @BindView(R.id.switchButton)
    SwitchButton switchButton;
    @BindView(R.id.view_back)
    View view_back;
    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SetLockActivity.actionStart(MyInfoActivity.this, isChecked ? 0 : 1);
        }
    };
    private File imageFile;
    private String filename = "header.jpg";
    private Uri imageUri;
    private String url;
    private MyInfoContract.Presenter presenter;
    private SafeSetting safeSetting;
    private HeaderSelectDialogFragment headerSelectDialogFragment;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchButton.setOnCheckedChangeListener(null);
        String password = SharedPreferenceInstance.getInstance().getLockPwd();
        switchButton.setChecked(!WonderfulStringUtils.isEmpty(password));
        switchButton.setOnCheckedChangeListener(listener);
    }


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new MyInfoPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        imageFile = WonderfulFileUtils.getCacheSaveFile(this, filename);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLoadingPopup();
                finish();
            }
        });
        view_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLoadingPopup();
                finish();
            }
        });
        llPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneClick();
            }
        });
        llEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailClick();
            }
        });
        llAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountClick();
            }
        });
        llGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleClick();
            }
        });
        llLoginPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPwdClick();
            }
        });
        llAccountPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountPwdClick();
            }
        });
        switchButton.setChecked(!WonderfulStringUtils.isEmpty(SharedPreferenceInstance.getInstance().getLockPwd()));
        switchButton.setOnCheckedChangeListener(listener);
    }

    private void accountClick() {
        if (safeSetting == null) {
            return;
        }
        if (safeSetting.getRealVerified() == 1 && safeSetting.getFundsVerified() == 1) {
            BindAccountActivity.actionStart(this);
        } else {
            WonderfulToastUtils.showToast(getResources().getString(R.string.password_realname));
        }
    }

    private void googleClick() {
        if (safeSetting == null) {
            return;
        }
        if (safeSetting.getGoogleState() == 0 && safeSetting.getFundsVerified() == 1) {
            BindGoogleActivity.actionStart(this);
        } else {
            WonderfulToastUtils.showToast(getResources().getString(R.string.password_realname));
        }
    }

    private void accountPwdClick() {
        if (safeSetting == null) {
            return;
        }
        if (safeSetting.getFundsVerified() == 0) {
            AccountPwdActivity.actionStart(this);
        } else if (safeSetting.getFundsVerified() == 1) {
            EditAccountPwdActivity.actionStart(this);
        }
    }

    private void loginPwdClick() {
        if (safeSetting == null) {
            return;
        }
        if (safeSetting.getPhoneVerified() == 0) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.binding_phone_first));
            return;
        }
        EditLoginPwdActivity.actionStart(this, safeSetting.getMobilePhone());
    }

    private void emailClick() {
        if (safeSetting == null) {
            return;
        }
        if (safeSetting.getEmailVerified() == 0) {
            BindEmailActivity.actionStart(this);
        } else {
            EmailActivity.actionStart(this, safeSetting.getEmail());
        }
    }

    private void phoneClick() {
        if (safeSetting == null) {
            return;
        }
        if (safeSetting.getPhoneVerified() == 0) {
            BindPhoneActivity.actionStart(this);
        } else {
            PhoneActivity.actionStart(this, safeSetting.getMobilePhone());
        }
    }

    private void showHeaderSelectDialog() {
        if (headerSelectDialogFragment == null) {
            headerSelectDialogFragment = HeaderSelectDialogFragment.getInstance(MyInfoActivity.this);
        }
        headerSelectDialogFragment.show(getSupportFragmentManager(), "header_select");
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        presenter.safeSetting(getToken());
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
    public void setPresenter(MyInfoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void safeSettingSuccess(SafeSetting obj) {
        this.safeSetting = obj;
        fillViews();
    }

    private void fillViews() {
        tvPhone.setText(safeSetting.getPhoneVerified() == 0 ? getResources().getString(R.string.unbound) : safeSetting.getMobilePhone());
        tvPhone.setEnabled(safeSetting.getPhoneVerified() == 0);
        llPhone.setEnabled(safeSetting.getPhoneVerified() == 0);
        imgPhone.setVisibility(safeSetting.getPhoneVerified() == 0 ? View.VISIBLE : View.INVISIBLE);
        tvEmail.setText(safeSetting.getEmailVerified() == 0 ? getResources().getString(R.string.unbound) : safeSetting.getEmail());
        tvEmail.setEnabled(safeSetting.getEmailVerified() == 0);
        imgEmail.setVisibility(safeSetting.getEmailVerified() == 0 ? View.VISIBLE : View.INVISIBLE);
        llEmail.setEnabled(safeSetting.getEmailVerified() == 0);
        tvAcountPwd.setText(safeSetting.getFundsVerified() == 0 ? R.string.not_set : R.string.had_set);
        tvAcountPwd.setEnabled(safeSetting.getFundsVerified() == 0);
        tvAccount.setText(safeSetting.getAccountVerified() == 0 ? R.string.not_set : R.string.had_set);
        tvAccount.setEnabled(safeSetting.getAccountVerified() == 0);
        tvGoogle.setEnabled(safeSetting.getGoogleState()==0);
        tvGoogle.setText(safeSetting.getGoogleState()==0? R.string.unverified : R.string.verification);
        imgPhone.setVisibility(safeSetting.getGoogleState() == 0 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SetLockActivity.RETURN_SET_LOCK:
                switchButton.setOnCheckedChangeListener(null);
                String password = SharedPreferenceInstance.getInstance().getLockPwd();
                switchButton.setChecked(!WonderfulStringUtils.isEmpty(password));
                switchButton.setOnCheckedChangeListener(listener);
                break;
            default:
        }
    }

    @Override
    public void safeSettingFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }


}
