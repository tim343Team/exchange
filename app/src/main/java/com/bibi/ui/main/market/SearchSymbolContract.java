package com.bibi.ui.main.market;
import com.bibi.base.Contract;
import com.bibi.entity.Currency;

import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/28
 */
public interface SearchSymbolContract {

    interface View extends Contract.BaseView<SearchSymbolContract.Presenter> {
        void getTickersPageSuccess(List<Currency> obj);

        void getZoneFail(Integer code, String toastMessage);

        void getColletSuccess(String symbol,boolean isCollet);

        void addCollectionSuccess(String symbol,String message);

        void cancleCollectionSuccess(String symbol,String message);

        void addCollectionFail(int code,String message);

        void cancleCollectionFail(int code,String message);

        void getFavoriteSuccess(List<Currency> obj);

        void getFavoriteFail(int code,String message);

    }

    interface Presenter extends Contract.BasePresenter {
        //排序目标  上图三种分别为：名称=symbol 最新价= close 涨跌幅=change
        //排序顺序  ASC和DESC  点击排序sort和direction要一起传递，可都不传
        //一级分类板块名  不传参默认全部  MAIN=主板  GEM=创业板
        //二级分类法币名  不传参默认USDT
        void setTickersPage(String token,String sort,String direction,String nav,String legalCurrency);

        //添加收藏
        void addCollection(String symbol);

        void cancleCollection(String symbol);

        void getFavorite(String token,String id);
    }
}
