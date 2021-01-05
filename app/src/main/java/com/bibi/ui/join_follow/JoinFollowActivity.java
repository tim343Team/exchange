package com.bibi.ui.join_follow;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.entity.CoinTypeEntity;
import com.bibi.entity.FollowHistoryContent;
import com.bibi.entity.NiurenArrayEntity;
import com.bibi.entity.NiurenEntity;
import com.bibi.ui.main.MainContract;
import com.bibi.ui.main.follow.FollowPresenter;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/12
 */
public class JoinFollowActivity extends BaseActivity implements MainContract.FollowView{
    @BindView(R.id.llTitle)
    RelativeLayout llTitle;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    private MainContract.FollowPresenter presenter;

    @OnClick(R.id.ibBack)
    public void back(){
        finish();
    }

    @OnClick(R.id.tvAdmit)
    public void admit(){
        presenter.applyNiuren(this.getToken());
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.fragment_join_follow;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new FollowPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        presenter.getLockUSDT(this.getToken());
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
    public void niurenRankSuccess(NiurenArrayEntity obj) {

    }

    @Override
    public void niurenRankListSuccess(NiurenArrayEntity obj) {

    }

    @Override
    public void niurenRankFail(Integer code, String toastMessage) {

    }

    @Override
    public void CoinTypeSuccess(List<CoinTypeEntity> obj) {

    }

    @Override
    public void LeverageSuccess(List<String> obj) {

    }

    @Override
    public void HistorySuccess(List<FollowHistoryContent> obj) {

    }

    @Override
    public void historyFial(Integer code, String toastMessage) {

    }

    @Override
    public void cancelSuccess(String obj) {

    }

    @Override
    public void applySuccess(String obj) {
        WonderfulToastUtils.showToast(obj);
        finish();
    }

    @Override
    public void applyFial(Integer code, String toastMessage) {
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void addFollowSuccess(String obj) {

    }

    @Override
    public void addFollowFial(Integer code, String toastMessage) {

    }

    @Override
    public void getLockUSDTSuccess(String obj) {
        String sInfoFormat = getResources().getString(R.string.follow_step_content_1);
        String sFinalInfo=String.format(sInfoFormat, obj);
        tvInfo.setText(sFinalInfo);
    }


    @Override
    public void setPresenter(MainContract.FollowPresenter presenter) {
        this.presenter = presenter;
    }
}
