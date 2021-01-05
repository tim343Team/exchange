package com.bibi.ui.aboutus;


import com.bibi.data.DataSource;
import com.bibi.entity.AppInfo;
import com.bibi.entity.Vision;

/**
 * Created by Administrator on 2017/9/25.
 */

public class AboutUsPresenter implements AboutUsContract.Presenter {
    private final DataSource dataRepository;
    private final AboutUsContract.View view;

    public AboutUsPresenter(DataSource dataRepository, AboutUsContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void getNewVision(String token) {
        view.displayLoadingPopup();
        dataRepository.getNewVision(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.getNewVisionSuccess((Vision) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.getNewVisionFail(code, toastMessage);
            }
        });
    }

}
