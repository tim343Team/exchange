package com.bibi.ui.main.management.presenter;

import java.util.List;

import com.bibi.data.DataSource;
import com.bibi.entity.BannerEntity;
import com.bibi.entity.ContractDetailEntity;
import com.bibi.ui.main.management.ManagementContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/11
 */
public class ContractPresenter implements ManagementContract.ContractPresenter {
    private ManagementContract.ContractView view;
    private DataSource dataRepository;

    public ContractPresenter(DataSource dataRepository, ManagementContract.ContractView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getInvestDetail(String token,int pageNo) {
        dataRepository.getInvestDetail(token,pageNo, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getDataSuccess((List<ContractDetailEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }


}
