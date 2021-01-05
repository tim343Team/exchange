package com.bibi.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bibi.R;
import com.bibi.base.BaseActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulToastUtils;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/30
 */
public class StyleActivity extends BaseActivity{
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.ivDay)
    ImageView ivDay;
    @BindView(R.id.ivNight)
    ImageView ivNight;

    private int style=1;

    @OnClick(R.id.ibBack)
    void back(){
        finish();
    }

    @OnClick(R.id.llSwitchNight)
    void switchNight(){
        SharedPreferenceInstance.getInstance().saveStyleCode(2);
        WonderfulToastUtils.showToast("设置夜间主题成功，请重启应用");
        setTheme(R.style.AppThemeNight);
        ivDay.setVisibility(View.GONE);
        ivNight.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.llSwitchDay)
    void switchDay(){
        SharedPreferenceInstance.getInstance().saveStyleCode(1);
        WonderfulToastUtils.showToast("设置日间主题成功，请重启应用");
        setTheme(R.style.AppThemeDay);
        ivDay.setVisibility(View.VISIBLE);
        ivNight.setVisibility(View.GONE);
    }


    public static void actionStart(Context context) {
        Intent intent = new Intent(context, StyleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_style;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if(SharedPreferenceInstance.getInstance().getStyleCode()==1){
            style=1;
            ivDay.setVisibility(View.VISIBLE);
            ivNight.setVisibility(View.GONE);
        }else {
            style=2;
            ivDay.setVisibility(View.GONE);
            ivNight.setVisibility(View.VISIBLE);
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
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

}
