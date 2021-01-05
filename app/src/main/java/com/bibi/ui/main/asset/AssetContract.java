package com.bibi.ui.main.asset;

import java.util.List;

import com.bibi.base.Contract;
import com.bibi.entity.RechargeSupportContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/1
 */
public interface AssetContract {

    interface View extends Contract.BaseView<AssetContract.Presenter> {
        void errorMes(int e, String meg);

        void getRechargeSupportSuccess(List<RechargeSupportContract> objs, String parentCoin);
    }

    interface Presenter extends Contract.BasePresenter {
        void getRechargeSupport(String token, String parentCoin);
    }
}
