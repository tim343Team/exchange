package com.bibi.ui.my_promotion;

import com.bibi.base.Contract;
import com.bibi.entity.SummaryEntity;
import com.bibi.entity.TiBiAddress;
import com.bibi.ui.address.AddressContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/27
 */
public class PromotionContract {

    interface View extends Contract.BaseView<Presenter> {
        void getSummarySuccess(SummaryEntity obj);
    }

    interface Presenter extends Contract.BasePresenter {
        void summary(String token);
    }
}
