package com.bibi.ui.bind_user_name;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import com.bibi.R;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.User;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/17
 */
public class BindUserNameActivity  extends BaseActivity implements BindUserNameContract.View  {
    @BindView(R.id.ibBack)
    ImageButton ibBack;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.etUserName)
    EditText etUsername;
    @BindView(R.id.tvBind)
    TextView tvBind;
    @BindView(R.id.view_back)
    View view_back;

    private BindUserNameContract.Presenter presenter;
    private String userName;

    public static void actionStart(Context context,String userName) {
        Intent intent = new Intent(context, BindUserNameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_bind_user_name;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new BindUsernamePresenter(Injection.provideTasksRepository(getApplicationContext()), this);
        userName = getIntent().getStringExtra("userName");
        etUsername.setText(userName);
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
        tvBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindUserName();
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
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    private void bindUserName(){
        userName = etUsername.getText().toString();
        presenter.bindUsername(getToken(),userName);
    }

    @Override
    public void bindUsernameSuccess(String obj) {
        User user = MyApplication.getApp().getCurrentUser();
        user.setNickname(userName);
        MyApplication.getApp().setCurrentUser(user);
        finish();
    }

    @Override
    public void bindUsernameFail(Integer code, String toastMessage) {

    }


    @Override
    public void setPresenter(BindUserNameContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
