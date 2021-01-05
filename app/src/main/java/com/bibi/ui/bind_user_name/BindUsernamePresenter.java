package com.bibi.ui.bind_user_name;


import com.bibi.data.DataSource;
import com.bibi.ui.bind_email.BindEmailContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/17
 */
public class BindUsernamePresenter implements BindUserNameContract.Presenter  {
    private final DataSource dataRepository;
    private final BindUserNameContract.View view;

    public BindUsernamePresenter(DataSource dataRepository, BindUserNameContract.View view) {
        this.dataRepository = dataRepository;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void bindUsername(String token, String username) {
        view.displayLoadingPopup();
        dataRepository.bindUsername(token, username, new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                view.hideLoadingPopup();
                view.bindUsernameSuccess((String) obj);
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {
                view.hideLoadingPopup();
                view.bindUsernameFail(code, toastMessage);

            }
        });
    }
}
