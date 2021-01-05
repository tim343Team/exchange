package com.bibi.ui.main.management.presenter;

import com.bibi.data.DataSource;
import com.bibi.entity.ProfitEntity;
import com.bibi.ui.main.management.ManagementContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/11
 */
public class TransferPresenter implements ManagementContract.TransferPresenter {
    private ManagementContract.TransferView view;
    private DataSource dataRepository;

    public TransferPresenter(DataSource dataRepository, ManagementContract.TransferView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getStaticTransfer(String token,String amount) {
        dataRepository.getStaticTransfer(token,amount, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getDataSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void getTeamTransfer(String token,String amount) {
        dataRepository.getTeamTransfer(token,amount, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getDataSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }
}
