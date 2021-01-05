package com.bibi.ui.order;

import java.util.List;

import com.bibi.base.Contract;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.HoldEntity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/25
 */
public class OrdersContract {

    interface OrdersView extends Contract.BaseView<OrdersPresenter> {
        void errorMes(int e, String meg);
        /**
         * 当前持仓
         */
        void getHoldSuccess(List<HoldEntity> holdEntityList);

        /**
         * 当前持仓
         */
        void getHoldFial(int e, String meg);

        /**
         * 当前持仓完成
         */
        void getHoldFinishSuccess(List<HoldEntity> holdEntityList);

        /**
         * 当前持仓完成回执
         */
        void getHoldFinishFial(int e, String meg);

        /**
         * 当前持仓完成
         */
        void getHoldCurrentSuccess(List<HoldEntity> holdEntityList);

        /**
         * 当前持仓完成回执
         */
        void getHoldCurrentFial(int e, String meg);

        /**
         * 取消
         */
        void getCancleSuccess(String success);
        /**
         * 取消
         */
        void getCancleFial(int e, String meg);
        /**
         * 平仓
         */
        void getCloseAllSuccess(String success);

        /**
         * 平仓
         */
        void getCloseSuccess(String success);

        /**
         * 平仓
         */
        void getCloseFial(int e, String meg);

        /**
         * 修改止盈止损
         */
        void getModifySuccess(String success);

        void allCurrencySuccess(Object obj);

        void getAssetPNLSuccess(AssetEntity assetEntity);
    }

    interface OrdersPresenter extends Contract.BasePresenter {
        /**
         * 获取当前的持仓
         *         pageNo: 1,
         pageSize: 3,
         symbol: BTC/USDT,
         holdStatus : HOLD
         */
        void getHoldOrder(String token, int pageNo, int pageSize, String symbol, String holdStatus, String startTime, String endTime);

        /**
         * 查询当前平仓成交接口
         *         pageNo: 1,
         pageSize: 3,
         symbol: BTC/USDT,
         */
        void getHoldFinishOrder(String token, int pageNo, int pageSize, String symbol, String startTime, String endTime);

        /**
         * 查询当前委托接口
         *         pageNo: 1,
         pageSize: 3,
         symbol: BTC/USDT,
         */
        void getHoldCurrentOrder(String token, int pageNo, int pageSize, String symbol, String startTime, String endTime);

        /**
         * 取消订单接口
         */
        void getCancle(String token, String orderId);

        /**
         * 平仓
         */
        void getClose(String token, String orderId);

        /**
         * 一键平仓
         */
        void getCloseAll(String token);

        /**
         * 盈利损失统计
         */
        void getAssetPNL(String token);

        /**
         *  修改止损止盈
         */
        void getModifyProfit(String token, String orderId, String stopProfitPrice, String stopLossPrice);

        void allCurrency();
    }
}
