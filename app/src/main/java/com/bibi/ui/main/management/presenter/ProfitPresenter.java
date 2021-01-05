package com.bibi.ui.main.management.presenter;

import java.util.List;

import com.bibi.data.DataSource;
import com.bibi.entity.ContractDetailEntity;
import com.bibi.entity.InvestProfitEntity;
import com.bibi.entity.ProfitDetailEntity;
import com.bibi.ui.main.management.ManagementContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/11
 */
public class ProfitPresenter implements ManagementContract.ProfitPresenter {
    private ManagementContract.ProfitView view;
    private DataSource dataRepository;

    public ProfitPresenter(DataSource dataRepository, ManagementContract.ProfitView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getInvestProfit(String token) {
        dataRepository.getInvestProfit(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getDataSuccess((List<InvestProfitEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void getInvestProfitDetail(String token,String type,int pageNo) {
        dataRepository.getInvestProfitDetail(token,type,pageNo, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getDataDetailSuccess((List<ProfitDetailEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }
}
