package com.bibi.ui.defi;

import com.bibi.base.Contract;
import com.bibi.entity.DifiBean;
import com.bibi.ui.my_promotion.PromotionRewardContract;

import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/23
 */
public interface DeFiContract{

    interface View extends Contract.BaseView<DeFiContract.Presenter>{
        void newTickerSuccess(Object obj);

        void newTickerFail(Integer code, String toastMessage);
    }


    interface Presenter extends Contract.BasePresenter{
        void getDeFiList(String token, String page);
    }
}
