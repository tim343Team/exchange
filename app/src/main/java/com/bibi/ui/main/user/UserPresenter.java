package com.bibi.ui.main.user;

import com.bibi.ui.main.MainContract;
import com.bibi.data.DataSource;
import com.bibi.entity.Coin;
import com.bibi.entity.SafeSetting;

import java.util.List;

/**
 * Created by Administrator on 2018/2/24.
 */

public class UserPresenter implements MainContract.UserPresenter {
    private MainContract.UserView view;
    private DataSource dataRepository;

    public UserPresenter(DataSource dataRepository, MainContract.UserView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }



    @Override
    public void safeSetting(String token) {
        dataRepository.safeSetting(token, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.safeSettingSuccess((SafeSetting) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.safeSettingFail(code, toastMessage);

            }
        });
    }
}
