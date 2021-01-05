package com.bibi.ui.main.market;

import com.bibi.ui.main.MainContract;
import com.bibi.data.DataSource;

/**
 * Created by Administrator on 2018/2/24.
 */

public class TwoPresenter implements MainContract.TwoPresenter {
    private MainContract.TwoView view;
    private DataSource dataRepository;

    public TwoPresenter(DataSource dataRepository, MainContract.TwoView view) {
        this.view = view;
        this.dataRepository = dataRepository;
        this.view.setPresenter(this);
    }


}
