package com.bibi.ui.main.trade;

import java.util.List;

import com.bibi.base.Contract;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.Exchange;
import com.bibi.entity.HoldEntity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/7/27
 */
public interface Agreement2CoinContract {

    interface View extends Contract.BaseView<Presenter>{

        void errorMes(int e, String meg);

        /**
         * 提交委托成功(买入或者卖出成功)
         */
        void getDataLoad(int code, String message);
        /**
         * 盘口信息
         *
         * @param ask 买
         * @param bid 卖
         */
        void getSuccess(List<Exchange> ask, List<Exchange> bid);

        void getAssetUSDTSuccess(AssetEntity assetEntity);

        void setDefaultSymbol(String symbol);

        void getAccuracy(int one, int two);

        /**
         * 当前持仓
         */
        void getHoldSuccess(List<HoldEntity> holdEntityList);

        /**
         * 当前持仓
         */
        void getHoldFial(int e, String meg);

        void getCancleSuccess(String success);
    }


    interface Presenter {
        /**
         * 查询合约钱包
         */
        void getAsset(String token,String type);

        /**
         * 获取首次进入的默认交易对-目前仅处理币币交易
         */
        void getDefaultSymbol();

        /**
         * 获取盘口的信息
         */
        void getExchange(String symbol);

        /**
         * 获取精确度
         */
        void getSymbolInfo(String symbol);

//        symbol: BTC/USDT
//        price: 1
//        amount: 1
//        direction:BUY
//        type: LIMIT_PRICE
//        useDiscount: 0

        void getAddOrder(String token, String symbol, String price, String amount, String direction, String type, String useDiscount);

        void getHoldOrder(String token, int pageNo, int pageSize, String symbol);

        void cancle(String token,String orderId);
    }
}
