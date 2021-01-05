package com.bibi.ui.main;

import com.bibi.base.Contract;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.BannerEntity;
import com.bibi.entity.C2C;
import com.bibi.entity.Coin;
import com.bibi.entity.CoinInfo;
import com.bibi.entity.CoinTypeEntity;
import com.bibi.entity.Currency;
import com.bibi.entity.EntrustHistory;
import com.bibi.entity.Favorite;
import com.bibi.entity.FollowHistoryContent;
import com.bibi.entity.FollowHistoryEntity;
import com.bibi.entity.NiurenArrayEntity;
import com.bibi.entity.NiurenEntity;
import com.bibi.entity.Plate;
import com.bibi.entity.SafeSetting;

import java.util.List;

/**
 * Created by Administrator on 2018/2/24.
 */

public interface MainContract {
    interface View extends Contract.BaseView<Presenter> {

        void allCurrencySuccess(Object obj);

        void allCurrencyFail(Integer code, String toastMessage);

        void findSuccess(List<Favorite> obj);

        void findFail(Integer code, String toastMessage);

        void newTickerSuccess(Object obj,String nav);

        void newTickerFail(Integer code, String toastMessage);

    }

    interface Presenter extends Contract.BasePresenter {

        void allCurrency();

        void newTickerCurrency(String nav,String content);

        void find(String token);

        void startTCP(short cmd, byte[] body);
    }

    interface OnePresenter extends Contract.BasePresenter {

        void banners(String sysAdvertiseLocation);
    }

    interface OneView extends Contract.BaseView<OnePresenter> {

        void bannersSuccess(List<BannerEntity> obj);

        void bannersFail(Integer code, String toastMessage);
    }

    interface FollowPresenter extends Contract.BasePresenter {
        void rankList();

        void rank(String token, String time);

        void coin();

        void leverage();

        void history(String token, String page);

        void cancel(String token, String followId);

        void applyNiuren(String token);

        void addFollow(String token, String symbol, String leverage, String amount, String jyPassword, String fMemberId);

        void getLockUSDT(String token);

    }

    interface FollowView extends Contract.BaseView<FollowPresenter> {
        void niurenRankSuccess(NiurenArrayEntity obj);

        void niurenRankListSuccess(NiurenArrayEntity obj);

        void niurenRankFail(Integer code, String toastMessage);

        void CoinTypeSuccess(List<CoinTypeEntity> obj);

        void LeverageSuccess(List<String> obj);

        void HistorySuccess(List<FollowHistoryContent> obj);

        void historyFial(Integer code, String toastMessage);

        void cancelSuccess(String obj);

        void applySuccess(String obj);

        void applyFial(Integer code, String toastMessage);

        void addFollowSuccess(String obj);

        void addFollowFial(Integer code, String toastMessage);

        void getLockUSDTSuccess(String obj);
    }

    interface MineView extends Contract.BaseView<MinePresenter> {
        void uploadBase64PicFail(Integer code, String toastMessage);

        void changeAvatarFail(Integer code, String toastMessage);

        void uploadBase64PicSuccess(String obj, int type);

        void changeAvatarSuccess(String obj, String url);
    }

    interface MinePresenter extends Contract.BasePresenter {
        void uploadBase64Pic(String token, String base64, int type);

        void changeAvatar(String token, String url);
    }

    interface TwoPresenter extends Contract.BasePresenter {
    }

    interface TwoView extends Contract.BaseView<TwoPresenter> {

    }


    interface TwoRootPresenter extends Contract.BasePresenter {
        void setTickersZone(String token);

        //排序目标  上图三种分别为：名称=symbol 最新价= close 涨跌幅=change
        //排序顺序  ASC和DESC  点击排序sort和direction要一起传递，可都不传
        //一级分类板块名  不传参默认全部  MAIN=主板  GEM=创业板
        //二级分类法币名  不传参默认USDT
        void setTickersPage(String token,String sort,String direction,String nav,String legalCurrency);
    }

    interface TwoRootView extends Contract.BaseView<TwoRootPresenter> {
        void getZoneFail(Integer code, String toastMessage);

        void getZoneSuccess(List<String> obj);

        void getTickersPageSuccess(List<Currency> obj);
    }

    interface ThreePresenter extends Contract.BasePresenter {

    }

    interface ThreeView extends Contract.BaseView<ThreePresenter> {
    }

    interface BaseCoinPresenter extends Contract.BasePresenter {

        void all();
    }

    interface BaseCoinView extends Contract.BaseView<BaseCoinPresenter> {

        void allSuccess(List<CoinInfo> obj);

        void allFail(Integer code, String toastMessage);
    }

    interface UserPresenter extends Contract.BasePresenter {


        void safeSetting(String token);
    }

    interface UserView extends Contract.BaseView<UserPresenter> {

        void safeSettingSuccess(SafeSetting obj);

        void safeSettingFail(Integer code, String toastMessage);
    }

    interface BuyPresenter extends Contract.BasePresenter {

        void exChange(String token, String symbol, String price, String amount, String direction, String type);

        void walletBase(String token, String s);

        void walletOther(String token, String s);

        void plate(String symbol);
    }

    interface BuyView extends Contract.BaseView<BuyPresenter> {

        void exChangeSuccess(String obj);

        void exChangeFail(Integer code, String toastMessage);

        void walletBaseSuccess(Coin obj);

        void walletBaseFail(Integer code, String toastMessage);

        void walletOtherSuccess(Coin obj);

        void walletOtherFail(Integer code, String toastMessage);

        void plateSuccess(Plate obj);

        void plateFail(Integer code, String toastMessage);
    }

    interface SellPresenter extends Contract.BasePresenter {

        void exChange(String token, String symbol, String price, String amount, String direction, String type);

        void walletBase(String token, String baseCoin);

        void walletOther(String token, String otherCoin);

        void plate(String symbol);
    }

    interface SellView extends Contract.BaseView<SellPresenter> {

        void exChangeSuccess(String obj);

        void exChangeFail(Integer code, String toastMessage);

        void walletBaseSuccess(Coin obj);

        void walletBaseFail(Integer code, String toastMessage);

        void walletOtherSuccess(Coin obj);

        void walletOtherFail(Integer code, String toastMessage);

        void plateSuccess(Plate obj);

        void plateFail(Integer code, String toastMessage);
    }

    interface C2CPresenter extends Contract.BasePresenter {

        void advertise(int pageNo, int pageSize, String advertiseType, String unit, String id);
    }

    interface C2CView extends Contract.BaseView<C2CPresenter> {

        void advertiseSuccess(C2C obj);

        void advertiseFail(Integer code, String toastMessage);
    }

    interface EntrustPresenter extends Contract.BasePresenter {

        void entrust(String token, int pageSize, int pageNo, String symbol);

        void cancleEntrust(String token, String orderId);
    }

    interface EntrustView extends Contract.BaseView<EntrustPresenter> {

        void entrustSuccess(List<EntrustHistory> obj);

        void entrustFail(Integer code, String toastMessage);

        void cancleEntrustSuccess(String obj);

        void cancleEntrustFail(Integer code, String toastMessage);
    }



}
