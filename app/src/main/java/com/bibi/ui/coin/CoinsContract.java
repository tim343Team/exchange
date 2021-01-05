package com.bibi.ui.coin;

import java.util.List;

import com.bibi.base.Contract;
import com.bibi.entity.Currency;
import com.bibi.entity.HoldEntity;
import com.bibi.ui.order.OrdersContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/7/28
 */
public class CoinsContract {

    interface CoinsView extends Contract.BaseView<CoinsContract.Presenter> {
        void errorMes(int e, String meg);
        /**
         * 当前持仓
         */
        void getHoldSuccess(List<HoldEntity> holdEntityList);

        /**
         * 当前持仓
         */
        void getHoldFial(int e, String meg);

        void getCancleSuccess(String success);

        void setSpotCurrency(List<Currency> obj);
    }

    interface Presenter extends Contract.BasePresenter {
        void getHoldOrder(String token, int pageNo, int pageSize, String startTime, String endTime);

        void cancle(String token,String orderId);

        void getHoldFinishOrder(String token,int pageNo, int pageSize, String startTime, String endTime);

        void allSpotCurrency();
    }
}
