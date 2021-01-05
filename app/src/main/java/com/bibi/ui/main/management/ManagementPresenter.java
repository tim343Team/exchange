package com.bibi.ui.main.management;

import java.util.List;

import com.bibi.data.DataSource;
import com.bibi.entity.BannerEntity;
import com.bibi.entity.InvestDatRateEntity;
import com.bibi.entity.InvestQuotEntity;
import com.bibi.entity.InvestTypeEntity;
import com.bibi.entity.ProfitManageEntity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class ManagementPresenter implements ManagementContract.ManagementPresenter {
    private ManagementContract.View view;
    private DataSource dataRepository;

    public ManagementPresenter(DataSource dataRepository, ManagementContract.View view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void banners(String sysAdvertiseLocation) {
        dataRepository.banners(sysAdvertiseLocation, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.bannersSuccess((List<BannerEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void trusteeshipSubmit(String token, String amount,String password,String type,String coinName) {
        dataRepository.trusteeshipSubmit(token,amount,password,type,coinName, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.submitSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

//    @Override
//    public void getInvestQuota(String token) {
//        dataRepository.getInvestQuota(token, new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                view.hideLoadingPopup();
//                view.getQuotaSuccess((InvestQuotEntity) obj);
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.hideLoadingPopup();
//            }
//        });
//    }

    @Override
    public void getDayRate(String token) {
        dataRepository.getDayRate(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getDayRateSuccess((InvestDatRateEntity) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void getInvestRule(String token) {
        dataRepository.getInvestRule(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getInvestRuleSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void getInvestFinish(String token) {
        dataRepository.getInvestFinish(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getInvestFinishSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void getInvestEarning(String token) {
        dataRepository.getInvestEarning(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getInvestEarningSuccess((List<ProfitManageEntity> ) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void getInvestType(String token) {
        dataRepository.getInvestType(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getInvestTypeSuccess((List<InvestTypeEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

}
