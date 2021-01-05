package com.bibi.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.tencent.captchasdk.TCaptchaDialog;
import com.tencent.captchasdk.TCaptchaVerifyListener;

import butterknife.OnClick;
import com.bibi.R;
import com.bibi.app.GlobalConstant;
import com.bibi.config.AppConfig;
import com.bibi.ui.dialog.CommonDialog;
import com.bibi.ui.dialog.CommonDialogFragment;
import com.bibi.ui.forgot_pwd.ForgotPwdActivity;
import com.bibi.ui.main.MainActivity;
import com.bibi.ui.message_detail.MessageHelpActivity;
import com.bibi.ui.signup.SignUpActivity;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.Captcha;
import com.bibi.entity.User;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.EncryUtils;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulCommonUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import com.bibi.app.Injection;

public class LoginActivity extends BaseActivity implements LoginContract.View {
    public static final int RETURN_LOGIN = 0;
    //    @BindView(R.id.ibBack)
//    TextView ibBack;
//    @BindView(R.id.llTitle)
//    LinearLayout llTitle;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvForgetPas)
    TextView tvForgetPas;
    @BindView(R.id.tvSignUp)
    TextView tvSignUp;
    @BindView(R.id.tvToRegist)
    TextView tvToRegist;
    @BindView(R.id.tvToLogin)
    TextView tvToLogin;
    @BindView(R.id.view_login)
    RelativeLayout view_login;
    @BindView(R.id.view_register)
    RelativeLayout view_register;

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etRegisterPassword)
    EditText etRegisterPassword;
    @BindView(R.id.etRePassword)
    EditText etRePassword;
    @BindView(R.id.tuijian)
    EditText tuijian;
    @BindView(R.id.yan)
    ImageView yan;
    @BindView(R.id.yan1)
    ImageView yan1;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.layout_yonghu)
    LinearLayout layoutYonghu;
    @BindView(R.id.text_yonghu)
    TextView text_yonghu;
    //    @BindView(R.id.tv_login_pass)
//    TextView tv_login_pass;
//    @BindView(R.id.tv_login_code)
//    TextView tv_login_code;
//    @BindView(R.id.tvSend)
//    TextView tvSend;
    @BindView(R.id.iv_yan)
    ImageView iv_yan;
    private boolean isIvYan = false;
    private boolean isYan = false;
    private boolean isYan1 = false;
    private LoginContract.Presenter presenter;
    private Handler handler = new Handler();
    private String username;
    private String password;
    private CountDownTimer timer;
    private String challenge="";
    private String validate="";

    private void startLogin() {
        presenter.getLoginAuthType(username);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_login_new;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new LoginPresenter(Injection.provideTasksRepository(getApplicationContext()), this);

//        ibBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                signUpByPhone();
//                SignUpActivity.actionStart(LoginActivity.this);
            }
        });

        tvForgetPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPwdActivity.actionStart(LoginActivity.this);
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        iv_yan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isIvYan = !isIvYan;
                Drawable no = getResources().getDrawable(R.drawable.yan_no);
                Drawable yes = getResources().getDrawable(R.drawable.yan_yes);
                if (isIvYan) {
                    //显示
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_yan.setImageDrawable(yes);

                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_yan.setImageDrawable(no);
                }
            }
        });
        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCode();
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
                    etRegisterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    yan.setImageDrawable(yes);

                } else {
                    etRegisterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    yan.setImageDrawable(no);
                }
            }
        });
        yan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isYan1 = !isYan1;
                Drawable no = getResources().getDrawable(R.drawable.yan_no);
                Drawable yes = getResources().getDrawable(R.drawable.yan_yes);
                if (isYan1) {
                    //显示
                    etRePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    yan1.setImageDrawable(yes);

                } else {
                    etRePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    yan1.setImageDrawable(no);
                }
            }
        });
        text_yonghu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageHelpActivity.actionStart(LoginActivity.this, GlobalConstant.USER_AGREEMENT_ID);
            }
        });

        String strings[] = SharedPreferenceInstance.getInstance().getPhoneAndPass();
        etPassword.setText(strings[1]);
        etUsername.setText(strings[0]);
    }

    private void login() {
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        if (WonderfulStringUtils.isEmpty(username, password)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.Incomplete_information));
            return;
        }
        startLogin();
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
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
//            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loginFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void loginSuccess(User user) {
        user.setRealVerified(TextUtils.isEmpty(user.getRealName()) ? 0 : 1);
        MyApplication.realVerified = user.getRealVerified();
        MyApplication.getApp().setCurrentUser(null);
        MainActivity.isAgain = true;
        String key = WonderfulCommonUtils.getSerialNumber() + etUsername.getText().toString() + etPassword.getText().toString();
        String md5Key = getMD5(key);
        SharedPreferenceInstance.getInstance().saveToken(EncryUtils.getInstance().encryptString(md5Key, MyApplication.getApp().getPackageName()));
        MyApplication.getApp().setLoginStatusChange(true);
        MyApplication.getApp().setCurrentUser(user);
        SharedPreferenceInstance.getInstance().saveID(user.getId());
        SharedPreferenceInstance.getInstance().saveTOKEN(user.getToken());
        SharedPreferenceInstance.getInstance().saveaToken(EncryUtils.getInstance().decryptString(SharedPreferenceInstance.getInstance().getToken(), MyApplication.getApp().getPackageName()));
        setResult(RESULT_OK);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 600);
    }

    public String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }


    String AUTH_GOOGLE_NO = "0";
    String AUTH_GOOGLE_YES = "1";

    @Override
    public void getLoginAuthTypeSuccess(String type) {
        if (AUTH_GOOGLE_NO.equals(type)) {
            presenter.login(username, password, "", "", "", loginType == LOGIN_BY_PASS);
        } else if (AUTH_GOOGLE_YES.equals(type)) {
            CommonDialogFragment commonDialogFragment = CommonDialogFragment.getInstance(CommonDialogFragment.TYPE_GOOGLE_CODE, getString(R.string.text_input_google_code), getString(R.string.text_input_google_code), getString(R.string.confirm), getString(R.string.cancle), true);
            commonDialogFragment.show(getSupportFragmentManager(), "google");
            commonDialogFragment.setCommitClickListener(new CommonDialogFragment.OnCommitClickListener() {
                @Override
                public void onCommitClick(String pass) {
                    if (TextUtils.isEmpty(pass)) {
                        WonderfulToastUtils.showToast(getResources().getString(R.string.text_input_google_code));
                    } else {
                        presenter.login(username, password, pass, "", "", loginType == LOGIN_BY_PASS);
                    }
                }
            });
        }
    }

    @Override
    public void getLoginAuthTypeFail(Integer code, String toastMessage) {
        Log.i("getLoginAuthTypeFail", code + toastMessage);
        WonderfulToastUtils.showToast(toastMessage);
    }

    @Override
    public void sendLoginCodeSuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
//        fillCodeView(60*1000);
    }

    @Override
    public void sendLoginCodeFail(Integer code, String toastMessage) {
        WonderfulToastUtils.showToast(toastMessage);
//        tvSend.setEnabled(true);
    }

    @Override
    public void phoneCodeSuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
        fillCodeView(90 * 1000);
    }

    @Override
    public void phoneCodeFail(Integer code, String toastMessage) {
        tvGetCode.setEnabled(true);
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void signUpByPhoneSuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
    }


    @Override
    public void signUpByPhoneFail(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    final int LOGIN_BY_PASS = 0;
    final int LOGIN_BY_CODE = 1;
    int loginType = LOGIN_BY_PASS;

    @OnClick(R.id.tvToRegist)
    public void toRegist() {
        view_login.setVisibility(View.GONE);
        view_register.setVisibility(View.VISIBLE);
        layoutYonghu.setVisibility(View.VISIBLE);
        tvToLogin.setTextColor(getResources().getColor(R.color.colorTextNormal));
        tvToRegist.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @OnClick(R.id.tvToLogin)
    public void toLogin() {
        view_register.setVisibility(View.GONE);
        layoutYonghu.setVisibility(View.GONE);
        view_login.setVisibility(View.VISIBLE);
        tvToRegist.setTextColor(getResources().getColor(R.color.colorTextNormal));
        tvToLogin.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void signUpByPhone() {
        String phone = etPhone.getText().toString();
        String username = etUsername.getText().toString();
        String code = etCode.getText().toString();
        String password = etRegisterPassword.getText().toString();
        String rePassword = etRePassword.getText().toString();
        String country = "中国";
        String tuijianma = tuijian.getText().toString();
        if (WonderfulStringUtils.isEmpty(phone, code, password, rePassword, country)) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.Incomplete_information));
            return;
        }
//        if (WonderfulStringUtils.isEmpty((tuijianma))) {
//            WonderfulToastUtils.showToast(getResources().getString(R.string.tuijianma));
//            return;
//        }
        if (!checkbox.isChecked()) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.xieyi));
            return;
        }
        if (!password.equals(rePassword)) {
            WonderfulToastUtils.showToast(R.string.pwd_diff);
            return;
        }
        presenter.signUpByPhone(phone, phone, password, country, code, tuijianma, challenge, validate, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            ;
            timer = null;
        }
    }

    private String countryStr = "中国";
    private String phone;

    private void getCode() {
        phone = etPhone.getText().toString();
        if (WonderfulStringUtils.isEmpty(phone) || phone.length() < 11) {
            WonderfulToastUtils.showToast(R.string.phone_not_correct);
            return;
        }
        tvGetCode.setEnabled(false);
        /**
         @param context，上下文
         @param appid，业务申请接入验证码时分配的appid
         @param listener，验证码验证结果回调
         @param jsonString，业务自定义参数
         */
        /*dialog = new TCaptchaDialog(getmActivity(), AppConfig.AUTH_APP_ID, listener, null);
        dialog.show();*/
        presenter.phoneCode(phone, countryStr);
    }

    private void fillCodeView(long time) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvGetCode.setText(getResources().getString(R.string.re_send) + "（" + millisUntilFinished / 1000 + "）");
            }

            @Override
            public void onFinish() {
                tvGetCode.setText(R.string.send_code);
                tvGetCode.setEnabled(true);
                timer.cancel();
                timer = null;
            }
        };
        timer.start();
    }
}
