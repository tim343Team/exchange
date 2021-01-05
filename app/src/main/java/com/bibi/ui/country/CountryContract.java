package com.bibi.ui.country;


import com.bibi.base.Contract;
import com.bibi.entity.Country;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface CountryContract {
    interface View extends Contract.BaseView<Presenter> {

        void countrySuccess(List<Country> obj);

        void countryFail(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {

        void country();
    }
}
