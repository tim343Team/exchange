package com.bibi.ui.main.mine;

import com.bibi.data.DataSource;
import com.bibi.ui.credit.CreditContract;
import com.bibi.ui.main.MainContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/5
 */
public class MinePresenter implements MainContract.MinePresenter  {
    private final DataSource dataRepository;
    private final MainContract.MineView view;

    public MinePresenter(DataSource dataRepository, MainContract.MineView view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void uploadBase64Pic(String token, String base64Data, final int type) {
        view.displayLoadingPopup();
        dataRepository.uploadBase64Pic(token, base64Data, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.uploadBase64PicSuccess((String) obj, type);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.uploadBase64PicFail(code, toastMessage);

            }
        });
    }

    @Override
    public void changeAvatar(String token,final String url) {
        view.displayLoadingPopup();
        dataRepository.changeAvatar(token, url, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.changeAvatarSuccess((String) obj,url);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.changeAvatarFail(code, toastMessage);
            }
        });
    }
}
