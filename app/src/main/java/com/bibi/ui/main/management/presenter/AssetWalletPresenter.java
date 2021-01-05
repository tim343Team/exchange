package com.bibi.ui.main.management.presenter;

import java.util.List;

import com.bibi.data.DataSource;
import com.bibi.entity.ContractDetailEntity;
import com.bibi.entity.ProfitDetailEntity;
import com.bibi.entity.ProfitEntity;
import com.bibi.entity.ProfitListEntity;
import com.bibi.ui.main.management.ManagementContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/11
 */
public class AssetWalletPresenter implements ManagementContract.AssetWalletPresenter {
    private ManagementContract.AssetWalletView view;
    private DataSource dataRepository;

    public AssetWalletPresenter(DataSource dataRepository, ManagementContract.AssetWalletView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getStaticProfit(String token,int page) {
        dataRepository.getStaticProfit(token,page, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getDataSuccess((List<ProfitListEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void getTeamProfit(String token,int page) {
        dataRepository.getTeamProfit(token,page, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getDataSuccess((List<ProfitListEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void getProfitDetail(String token, int page) {
        dataRepository.getInvestProfitDetail(token,"",page, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getDetailDataSuccess((List<ProfitDetailEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }
}
