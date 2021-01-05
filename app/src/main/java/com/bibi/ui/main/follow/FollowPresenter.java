package com.bibi.ui.main.follow;

import java.util.List;

import com.bibi.data.DataSource;
import com.bibi.entity.BannerEntity;
import com.bibi.entity.CoinTypeEntity;
import com.bibi.entity.FollowHistoryContent;
import com.bibi.entity.FollowHistoryEntity;
import com.bibi.entity.NiurenArrayEntity;
import com.bibi.entity.NiurenEntity;
import com.bibi.ui.main.MainContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/12
 */
public class FollowPresenter implements MainContract.FollowPresenter {
    private MainContract.FollowView view;
    private DataSource dataRepository;

    public FollowPresenter(DataSource dataRepository, MainContract.FollowView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void rankList() {
//        dataRepository.getNiurenRank("", new DataSource.DataCallback() {
//            @Override
//            public void onDataLoaded(Object obj) {
//                view.hideLoadingPopup();
//                view.niurenRankListSuccess((NiurenArrayEntity) obj);
//            }
//
//            @Override
//            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.hideLoadingPopup();
//                view.niurenRankFail(code, toastMessage);
//            }
//        });
    }

    @Override
    public void rank(String token,String time) {
        dataRepository.getNiurenRank(token,time, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.niurenRankSuccess((NiurenArrayEntity) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.niurenRankFail(code, toastMessage);
            }
        });
    }

    @Override
    public void coin() {
        dataRepository.getCoinType(new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.CoinTypeSuccess((List<CoinTypeEntity>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void leverage() {
        dataRepository.getLeverage(new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.LeverageSuccess((List<String>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }

    @Override
    public void history(String token,String pageNo) {
        dataRepository.getFollowHistory(token,pageNo,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.HistorySuccess((List<FollowHistoryContent>) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.historyFial(code, toastMessage);
            }
        });
    }

    @Override
    public void cancel(String token, String followId) {
        dataRepository.getCancelFollow(token,followId,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.cancelSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
//                view.hideLoadingPopup();
//                view.niurenRankFail(code, toastMessage);
            }
        });
    }

    @Override
    public void applyNiuren(String token) {
        dataRepository.getApplyNiuren(token,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.applySuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.applyFial(code, toastMessage);
            }
        });
    }

    @Override
    public void addFollow(String token,String symbol, String leverage, String amount, String jyPassword, String fMemberId) {
        dataRepository.addFollow(token,symbol,leverage,amount,jyPassword,fMemberId,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.addFollowSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.addFollowFial(code, toastMessage);
            }
        });
    }

    @Override
    public void getLockUSDT(String token) {
        dataRepository.getLockUSDT(token,new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getLockUSDTSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
            }
        });
    }
}
