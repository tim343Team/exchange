package com.bibi.ui.main.management.presenter;

import java.util.List;

import com.bibi.data.DataSource;
import com.bibi.entity.PromotionRecord;
import com.bibi.ui.main.management.ManagementContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/12
 */
public class PromotionPresenter implements ManagementContract.PromotionPresenter {
    private ManagementContract.PromotionView view;
    private DataSource dataRepository;

    public PromotionPresenter(DataSource dataRepository, ManagementContract.PromotionView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void getPromotion(String token,String page,String number) {
        view.displayLoadingPopup();
        dataRepository.getPromotion(token,page,number, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getPromotionSuccess((List<PromotionRecord>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getPromotionFail(code, toastMessage);
            }
        });
    }

    @Override
    public void getPromotionByMember(String token, String pageNo, String inviteId) {
        view.displayLoadingPopup();
        dataRepository.getPromotionByMember(token,pageNo,inviteId, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getPromotionMemberSuccess((List<PromotionRecord>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getPromotionMemberFail(code, toastMessage);
            }
        });
    }

    @Override
    public void getInvestPromotion(String token, String page, String number) {
        view.displayLoadingPopup();
        dataRepository.getInvestPromotion(token,page,number, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getPromotionSuccess((List<PromotionRecord>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getPromotionFail(code, toastMessage);
            }
        });
    }

    @Override
    public void getInvestPromotionByMember(String token, String pageNo, String pointId) {
        view.displayLoadingPopup();
        dataRepository.getInvestPromotionByMember(token,pageNo,pointId, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getPromotionMemberSuccess((List<PromotionRecord>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getPromotionMemberFail(code, toastMessage);
            }
        });
    }
}
