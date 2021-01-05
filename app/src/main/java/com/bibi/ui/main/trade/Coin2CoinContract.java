package com.bibi.ui.main.trade;

import com.bibi.base.Contract;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.EntrustHistory;
import com.bibi.entity.Exchange;
import com.bibi.entity.HoldEntity;
import com.bibi.entity.Money;

import java.util.List;


public interface Coin2CoinContract {
    interface View extends Contract.BaseView<Presenter> {

        void errorMes(int e, String meg);

        /**
         * 盘口信息
         *
         * @param ask 买
         * @param bid 卖
         */
        void getSuccess(List<Exchange> ask, List<Exchange> bid);

        /**
         * 当前持仓
         */
        void getHoldSuccess(List<HoldEntity> holdEntityList);

        /**
         * 当前持仓
         */
        void getHoldFial(int e, String meg);

        /**
         * 当前委托
         */
        void getDataLoaded(List<EntrustHistory> entrusts);

        /**
         * 历史委托
         */
        void getHistorySuccess(List<EntrustHistory> success);

        /**
         * 提交委托成功(买入或者卖出成功)
         */
        void getDataLoad(int code, String message);

        /**
         * 取消委托
         */
        void getCancelSuccess(String success);

        /**
         * 修改止盈止损
         */
        void getModifySuccess(String success);
        /**
         * 平仓
         */
        void getCloseSuccess(String success);

        /**
         * 平仓
         */
        void getCloseFial(int e, String meg);

        // 钱包
        void getWalletSuccess(Object coin, int type);

        void getAccuracy(int one, int two);

        void showDialog();

        void hideDialog();

        void setDefaultSymbol(String symbol);

        void getAssetPNLSuccess(AssetEntity assetEntity);

        void getLeverage(List<String> objs);

        void allCurrencySuccess(Object obj);
    }

    interface Presenter {
        /**
         * 获取盘口的信息
         */
        void getExchange(String symbol);

        /**
         * 获取当前的持仓
         *         pageNo: 1,
         pageSize: 3,
         symbol: BTC/USDT,
         holdStatus : HOLD
         */
        void getHoldOrder(String token, int pageNo, int pageSize, String symbol, String holdStatus);

        /**
         * 获取当前的委托
         */
        void getCurrentOrder(String token, int pageNo, int pageSize, String symbol, String type, String direction, String startTime, String endTime);

        /**
         * 获取历史委托
         */
        void getOrderHistory(String token, int pageNo, int pageSize, String symbol, String type, String direction, String startTime, String endTime);

        /**
         * 提交委托 Leverage 杠杆倍数
         stopProfitPrice 止盈价格
         stopLossPrice  止损价格

         */
        void getAddOrder(String token, String symbol, String price, String amount, String direction, String type, String leverage, String stopProfitPrice, String stopLossPrice);

        /**
         * 取消委托
         */
        void getCancelEntrust(String token, String orderId);

        /**
         * 获取钱包
         */
        void getWallet(int type, String token, String coinName);

        /**
         * 获取精确度
         */
        void getSymbolInfo(String symbol);

        /**
         * 获取首次进入的默认交易对-目前仅处理币币交易
         */
        void getDefaultSymbol();

        /**
         * 盈利损失统计
         */
        void getAssetPNL(String token);

        /**
         * 杠杆
         */
        void getLeverage(String symbol);

        /**
         * 平仓
         */
        void getClose(String token, String orderId);

        /**
         *  修改止损止盈
         */
        void getModifyProfit(String token, String orderId, String stopProfitPrice, String stopLossPrice);

        void allCurrency();

    }
}
