package com.bibi.data;

/**
 * Created by Administrator on 2017/9/25.
 */
public class DataRepository implements DataSource {
    private static DataRepository INSTANCE = null;
    private final DataSource mRemoteDataSource;
    private final DataSource mLocalDataSource;
    private boolean isLocal = false;

    private DataRepository(DataSource mRemoteDataSource, DataSource mLocalDataSource) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mLocalDataSource = mLocalDataSource;
    }

    public static DataRepository getInstance(DataSource mRemoteDataSource, DataSource mLocalDataSource) {
        if (INSTANCE == null) INSTANCE = new DataRepository(mRemoteDataSource, mLocalDataSource);
        return INSTANCE;
    }

    @Override
    public void phoneCode(String phone, String country, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.phoneCode(phone, country, dataCallback);
        else mRemoteDataSource.phoneCode(phone, country, dataCallback);
    }

    @Override
    public void signUpByPhone(String phone, String username, String password, String country, String code, String tuijianma, String challenge, String validate, String seccode, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.signUpByPhone(phone, username, password, country, code, tuijianma, challenge, validate, seccode, dataCallback);
        else
            mRemoteDataSource.signUpByPhone(phone, username, password, country, code, tuijianma, challenge, validate, seccode, dataCallback);
    }

    @Override
    public void signUpByEmail(String email, String username, String password, String country, String challenge, String validate, String tuijian2, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.signUpByEmail(email, username, password, country, challenge, validate, tuijian2, dataCallback);
        else
            mRemoteDataSource.signUpByEmail(email, username, password, country, challenge, validate, tuijian2, dataCallback);
    }

    @Override
    public void login(String username, String password, String challenge, String validate, String seccode, boolean isPass, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.login(username, password, challenge, validate, seccode, isPass, dataCallback);
        else
            mRemoteDataSource.login(username, password, challenge, validate, seccode, isPass, dataCallback);
    }


    @Override
    public void KData(String symbol, Long from, Long to, String resolution, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.KData(symbol, from, to, resolution, dataCallback);
        else mRemoteDataSource.KData(symbol, from, to, resolution, dataCallback);
    }

    @Override
    public void SpotKData(String symbol, Long from, Long to, String resolution, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.SpotKData(symbol, from, to, resolution, dataCallback);
        else mRemoteDataSource.SpotKData(symbol, from, to, resolution, dataCallback);
    }

    @Override
    public void allCurrency(DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.allCurrency(dataCallback);
        else mRemoteDataSource.allCurrency(dataCallback);
    }

    @Override
    public void allHomeCurrency(DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.allHomeCurrency(dataCallback);
        else mRemoteDataSource.allHomeCurrency(dataCallback);
    }

    @Override
    public void newTickerCurrency(String nav, String content, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.newTickerCurrency(nav,content,dataCallback);
        else mRemoteDataSource.newTickerCurrency(nav,content,dataCallback);
    }

    @Override
    public void find(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.find(token, dataCallback);
        else mRemoteDataSource.find(token, dataCallback);
    }

    @Override
    public void delete(String token, String symbol, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.delete(token, symbol, dataCallback);
        else mRemoteDataSource.delete(token, symbol, dataCallback);
    }

    @Override
    public void add(String token, String symbol, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.add(token, symbol, dataCallback);
        else mRemoteDataSource.add(token, symbol, dataCallback);
    }

    @Override
    public void exChange(String token, String symbol, String price, String amount, String direction, String type, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.exChange(token, symbol, price, amount, direction, type, dataCallback);
        else
            mRemoteDataSource.exChange(token, symbol, price, amount, direction, type, dataCallback);
    }

    @Override
    public void wallet(String token, String coinName, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.wallet(token, coinName, dataCallback);
        else mRemoteDataSource.wallet(token, coinName, dataCallback);
    }

    @Override
    public void all(DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.all(dataCallback);
        else mRemoteDataSource.all(dataCallback);
    }

    @Override
    public void getFeeRate(DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getFeeRate(dataCallback);
        else mRemoteDataSource.getFeeRate(dataCallback);
    }

    @Override
    public void advertise(int pageNo, int pageSize, String advertiseType,String unit, String id, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.advertise(pageNo, pageSize, advertiseType,unit, id, dataCallback);
        else mRemoteDataSource.advertise(pageNo, pageSize, advertiseType,unit, id, dataCallback);
    }

    @Override
    public void country(DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.country(dataCallback);
        else mRemoteDataSource.country(dataCallback);
    }

    @Override
    public void create(String token, String price, String advertiseType, String coinId, String minLimit, String maxLimit, int timeLimit, String countryZhName, String priceType, String premiseRate, String remark, String number, String pay, String jyPassword, String auto, String autoword, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.create(token, price, advertiseType, coinId, minLimit, maxLimit, timeLimit, countryZhName
                    , priceType, premiseRate, remark, number, pay, jyPassword, auto, autoword, dataCallback);
        else
            mRemoteDataSource.create(token, price, advertiseType, coinId, minLimit, maxLimit, timeLimit, countryZhName
                    , priceType, premiseRate, remark, number, pay, jyPassword, auto, autoword, dataCallback);
    }

    @Override
    public void uploadBase64Pic(String token, String base64Data, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.uploadBase64Pic(token, base64Data, dataCallback);
        else mRemoteDataSource.uploadBase64Pic(token, base64Data, dataCallback);
    }

    @Override
    public void changeAvatar(String token, String url, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.changeAvatar(token, url, dataCallback);
        else mRemoteDataSource.changeAvatar(token, url, dataCallback);
    }

    @Override
    public void name(String token, String realName, String idCard, String idCardFront, String idCardBack, String handHeldIdCard, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.name(token, realName, idCard, idCardFront, idCardBack, handHeldIdCard, dataCallback);
        else
            mRemoteDataSource.name(token, realName, idCard, idCardFront, idCardBack, handHeldIdCard, dataCallback);
    }

    @Override
    public void accountPwd(String token, String jyPassword, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.accountPwd(token, jyPassword, dataCallback);
        else mRemoteDataSource.accountPwd(token, jyPassword, dataCallback);
    }

    @Override
    public void allAds(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.allAds(token, dataCallback);
        else mRemoteDataSource.allAds(token, dataCallback);
    }

    @Override
    public void release(String token, int id, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.release(token, id, dataCallback);
        else mRemoteDataSource.release(token, id, dataCallback);
    }

    @Override
    public void deleteAds(String token, int id, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.deleteAds(token, id, dataCallback);
        else mRemoteDataSource.deleteAds(token, id, dataCallback);
    }

    @Override
    public void offShelf(String token, int id, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.offShelf(token, id, dataCallback);
        else mRemoteDataSource.offShelf(token, id, dataCallback);
    }

    @Override
    public void adDetail(String token, int id, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.adDetail(token, id, dataCallback);
        else mRemoteDataSource.adDetail(token, id, dataCallback);
    }

    @Override
    public void updateAd(String token, int id, String price, String advertiseType, String coinId, String minLimit, String maxLimit, int timeLimit, String countryZhName, String priceType, String premiseRate, String remark, String number, String pay, String jyPassword, String auto, String autoword, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.updateAd(token, id, price, advertiseType, coinId, minLimit, maxLimit, timeLimit, countryZhName, priceType, premiseRate, remark, number, pay, jyPassword, auto, autoword, dataCallback);
        else
            mRemoteDataSource.updateAd(token, id, price, advertiseType, coinId, minLimit, maxLimit, timeLimit, countryZhName, priceType, premiseRate, remark, number, pay, jyPassword, auto, autoword, dataCallback);
    }

    @Override
    public void c2cInfo(int id, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.c2cInfo(id, dataCallback);
        else mRemoteDataSource.c2cInfo(id, dataCallback);
    }

    @Override
    public void c2cBuy(String token, String id, String coinId, String price, String money, String amount, String remark, String mode, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.c2cBuy(token, id, coinId, price, money, amount, remark, mode, dataCallback);
        else
            mRemoteDataSource.c2cBuy(token, id, coinId, price, money, amount, remark, mode, dataCallback);
    }

    @Override
    public void c2cSell(String token, String id, String coinId, String price, String money, String amount, String remark, String mode, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.c2cSell(token, id, coinId, price, money, amount, remark, mode, dataCallback);
        else
            mRemoteDataSource.c2cSell(token, id, coinId, price, money, amount, remark, mode, dataCallback);

    }

    @Override
    public void myOrder(String token, int status, int pageNo, int pageSize, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.myOrder(token, status, pageNo, pageSize, dataCallback);
        else mRemoteDataSource.myOrder(token, status, pageNo, pageSize, dataCallback);
    }

    @Override
    public void myWallet(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.myWallet(token, dataCallback);
        else mRemoteDataSource.myWallet(token, dataCallback);
    }

    @Override
    public void myContractWallet(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.myContractWallet(token, dataCallback);
        else mRemoteDataSource.myContractWallet(token, dataCallback);
    }

    @Override
    public void getSpotWallet(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getSpotWallet(token, dataCallback);
        else mRemoteDataSource.getSpotWallet(token, dataCallback);
    }

    @Override
    public void eryuanWalletWallet(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.eryuanWalletWallet(token, dataCallback);
        else mRemoteDataSource.eryuanWalletWallet(token, dataCallback);
    }

    @Override
    public void myFiatWallet(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.myFiatWallet(token, dataCallback);
        else mRemoteDataSource.myFiatWallet(token, dataCallback);
    }

    @Override
    public void getRate(DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getRate( dataCallback);
        else mRemoteDataSource.getRate(dataCallback);
    }

    @Override
    public void extractinfo(String token,String parentCoin, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.extractinfo(token,parentCoin, dataCallback);
        else mRemoteDataSource.extractinfo(token,parentCoin, dataCallback);
    }

    @Override
    public void getUSDTWallet(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getUSDTWallet(token, dataCallback);
        else mRemoteDataSource.getUSDTWallet(token, dataCallback);
    }

    @Override
    public void extract(String token, String unit, String amount, String remark, String jyPassword, String address, String code, String googleCode,String tag, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.extract(token, unit, amount, remark, jyPassword, address, code, googleCode,tag, dataCallback);
        else
            mRemoteDataSource.extract(token, unit, amount, remark, jyPassword, address, code, googleCode,tag, dataCallback);
    }

    @Override
    public void allTransaction(String token, int pageNo, int limit, int memberId, String startTime, String endTime, String symbol, String type, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.allTransaction(token, pageNo, limit, memberId, startTime, endTime, symbol, type, dataCallback);
        else
            mRemoteDataSource.allTransaction(token, pageNo, limit, memberId, startTime, endTime, symbol, type, dataCallback);
    }

    @Override
    public void safeSetting(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.safeSetting(token, dataCallback);
        else mRemoteDataSource.safeSetting(token, dataCallback);
    }

    @Override
    public void avatar(String token, String url, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.avatar(token, url, dataCallback);
        else mRemoteDataSource.avatar(token, url, dataCallback);
    }

    @Override
    public void bindPhone(String token, String phone, String code, String password, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.bindPhone(token, phone, code, password, dataCallback);
        else mRemoteDataSource.bindPhone(token, phone, code, password, dataCallback);
    }

    @Override
    public void sendCode(String token, String phone, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.sendCode(token, phone, dataCallback);
        else mRemoteDataSource.sendCode(token, phone, dataCallback);
    }

    @Override
    public void bindEmail(String token, String email, String code, String password, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.bindEmail(token, email, code, password, dataCallback);
        else mRemoteDataSource.bindEmail(token, email, code, password, dataCallback);
    }

    @Override
    public void bindUsername(String token, String username, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.bindUsername(token, username, dataCallback);
        else mRemoteDataSource.bindUsername(token, username, dataCallback);
    }

    @Override
    public void sendEmailCode(String token, String email, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.sendEmailCode(token, email, dataCallback);
        else mRemoteDataSource.sendEmailCode(token, email, dataCallback);
    }

    @Override
    public void sendEditLoginPwdCode(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.sendEditLoginPwdCode(token, dataCallback);
        else mRemoteDataSource.sendEditLoginPwdCode(token, dataCallback);
    }

    @Override
    public void editPwd(String token, String oldPassword, String newPassword, String code, String googleCode, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.editPwd(token, oldPassword, newPassword, code, token, dataCallback);
        else mRemoteDataSource.editPwd(token, oldPassword, newPassword, code, token, dataCallback);
    }

    @Override
    public void plate(String symbol, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.plate(symbol, dataCallback);
        else mRemoteDataSource.plate(symbol, dataCallback);
    }

    @Override
    public void entrust(String token, int pageSize, int pageNo, String symbol, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.entrust(token, pageSize, pageNo, symbol, dataCallback);
        else mRemoteDataSource.entrust(token, pageSize, pageNo, symbol, dataCallback);
    }

    @Override
    public void cancleEntrust(String token, String orderId, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.cancleEntrust(token, orderId, dataCallback);
        else mRemoteDataSource.cancleEntrust(token, orderId, dataCallback);
    }

    @Override
    public void phoneForgotCode(String phone, String challenge, String validate, String seccode, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.phoneForgotCode(phone, challenge, validate, seccode, dataCallback);
        else
            mRemoteDataSource.phoneForgotCode(phone, challenge, validate, seccode, dataCallback);
    }

    @Override
    public void forgotPwd(String account, String code, String mode, String password, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.forgotPwd(account, code, mode, password, dataCallback);
        else mRemoteDataSource.forgotPwd(account, code, mode, password, dataCallback);
    }

    @Override
    public void emailForgotCode(String email, String challenge, String validate, String seccode, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.emailForgotCode(email, challenge, validate, seccode, dataCallback);
        else
            mRemoteDataSource.emailForgotCode(email, challenge, validate, seccode, dataCallback);
    }

    @Override
    public void captch(DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.captch(dataCallback);
        else mRemoteDataSource.captch(dataCallback);
    }

    @Override
    public void sendChangePhoneCode(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.sendChangePhoneCode(token, dataCallback);
        else mRemoteDataSource.sendChangePhoneCode(token, dataCallback);
    }

    @Override
    public void changePhone(String token, String password, String phone, String code, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.changePhone(token, password, phone, code, dataCallback);
        else mRemoteDataSource.changePhone(token, password, phone, code, dataCallback);
    }

    @Override
    public void message(int pageNo, int pageSize, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.message(pageNo, pageSize, dataCallback);
        else mRemoteDataSource.message(pageNo, pageSize, dataCallback);
    }

    @Override
    public void messageDetail(String id, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.messageDetail(id, dataCallback);
        else mRemoteDataSource.messageDetail(id, dataCallback);
    }

    @Override
    public void remark(String token, String remark, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.remark(token, remark, dataCallback);
        else mRemoteDataSource.remark(token, remark, dataCallback);
    }

    @Override
    public void appInfo(DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.appInfo(dataCallback);
        else mRemoteDataSource.appInfo(dataCallback);
    }

    @Override
    public void banners(String sysAdvertiseLocation, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.banners(sysAdvertiseLocation, dataCallback);
        else mRemoteDataSource.banners(sysAdvertiseLocation, dataCallback);
    }

    @Override
    public void orderDetail(String token, String orderSn, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.orderDetail(token, orderSn, dataCallback);
        else mRemoteDataSource.orderDetail(token, orderSn, dataCallback);
    }

    @Override
    public void cancle(String token, String orderSn, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.cancle(token, orderSn, dataCallback);
        else mRemoteDataSource.cancle(token, orderSn, dataCallback);
    }

    @Override
    public void payDone(String token, String orderSn, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.payDone(token, orderSn, dataCallback);
        else mRemoteDataSource.payDone(token, orderSn, dataCallback);
    }

    @Override
    public void releaseOrder(String token, String orderSn, String jyPassword, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.releaseOrder(token, orderSn, jyPassword, dataCallback);
        else mRemoteDataSource.releaseOrder(token, orderSn, jyPassword, dataCallback);
    }

    @Override
    public void appeal(String token, String remark, String orderSn, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.appeal(token, remark, orderSn, dataCallback);
        else mRemoteDataSource.appeal(token, remark, orderSn, dataCallback);
    }

    @Override
    public void editAccountPed(String token, String newPassword, String oldPassword, String msgCode, String googleCode, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.editAccountPed(token, newPassword, oldPassword, msgCode, googleCode, dataCallback);
        else
            mRemoteDataSource.editAccountPed(token, newPassword, oldPassword, msgCode, googleCode, dataCallback);
    }

    @Override
    public void resetAccountPwd(String token, String newPassword, String code, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.resetAccountPwd(token, newPassword, code, dataCallback);
        else mRemoteDataSource.resetAccountPwd(token, newPassword, code, dataCallback);
    }

    @Override
    public void resetAccountPwdCode(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.resetAccountPwdCode(token, dataCallback);
        else mRemoteDataSource.resetAccountPwdCode(token, dataCallback);
    }

    @Override
    public void getHistoryMessage(String token, String orderId, int pageNo, int pageSize, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.getHistoryMessage(token, orderId, pageNo, pageSize, dataCallback);
        else mRemoteDataSource.getHistoryMessage(token, orderId, pageNo, pageSize, dataCallback);
    }

    @Override
    public void getEntrustHistory(String token, String symbol, int pageNo, int pageSize, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.getEntrustHistory(token, symbol, pageNo, pageSize, dataCallback);
        else mRemoteDataSource.getEntrustHistory(token, symbol, pageNo, pageSize, dataCallback);
    }

    @Override
    public void getCreditInfo(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getCreditInfo(token, dataCallback);
        else mRemoteDataSource.getCreditInfo(token, dataCallback);
    }

    @Override
    public void getNewVision(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getNewVision(token, dataCallback);
        else mRemoteDataSource.getNewVision(token, dataCallback);
    }

    @Override
    public void getSymbol(DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getSymbol(dataCallback);
        else mRemoteDataSource.getSymbol(dataCallback);
    }

    @Override
    public void getAccountSetting(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getAccountSetting(token, dataCallback);
        else mRemoteDataSource.getAccountSetting(token, dataCallback);
    }

    @Override
    public void getBindBank(String token, String bank, String branch, String jyPassword, String realName, String cardNo, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.getBindBank(token, bank, branch, jyPassword, realName, cardNo, dataCallback);
        else
            mRemoteDataSource.getBindBank(token, bank, branch, jyPassword, realName, cardNo, dataCallback);
    }

    @Override
    public void getUpdateBank(String token, String bank, String branch, String jyPassword, String realName, String cardNo, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.getUpdateBank(token, bank, branch, jyPassword, realName, cardNo, dataCallback);
        else
            mRemoteDataSource.getUpdateBank(token, bank, branch, jyPassword, realName, cardNo, dataCallback);
    }

    @Override
    public void getBindWeiChat(String token, String wechat, String jyPassword, String realName, String qrCodeUrl, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.getBindWeiChat(token, wechat, jyPassword, realName, qrCodeUrl, dataCallback);
        else
            mRemoteDataSource.getBindWeiChat(token, wechat, jyPassword, realName, qrCodeUrl, dataCallback);
    }

    @Override
    public void getUpdateWeiChat(String token, String wechat, String jyPassword, String realName, String qrCodeUrl, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.getUpdateWeiChat(token, wechat, jyPassword, realName, qrCodeUrl, dataCallback);
        else
            mRemoteDataSource.getUpdateWeiChat(token, wechat, jyPassword, realName, qrCodeUrl, dataCallback);
    }

    @Override
    public void getBindAli(String token, String ali, String jyPassword, String realName, String qrCodeUrl, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.getBindAli(token, ali, jyPassword, realName, qrCodeUrl, dataCallback);
        else
            mRemoteDataSource.getBindAli(token, ali, jyPassword, realName, qrCodeUrl, dataCallback);
    }

    @Override
    public void getUpdateAli(String token, String ali, String jyPassword, String realName, String qrCodeUrl, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.getUpdateAli(token, ali, jyPassword, realName, qrCodeUrl, dataCallback);
        else
            mRemoteDataSource.getUpdateAli(token, ali, jyPassword, realName, qrCodeUrl, dataCallback);
    }

    @Override
    public void getUpdateInter(String token, String name, String address, String jyPassword, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.getUpdateInter(token, name,address, jyPassword,dataCallback);
        else
            mRemoteDataSource.getUpdateInter(token, name,address, jyPassword,dataCallback);
    }

    @Override
    public void updateInter(String token, String name, String address, String jyPassword, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.updateInter(token, name,address, jyPassword,dataCallback);
        else
            mRemoteDataSource.updateInter(token, name,address, jyPassword,dataCallback);
    }

    @Override
    public void getCheckMatch(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getCheckMatch(token, dataCallback);
        else mRemoteDataSource.getCheckMatch(token, dataCallback);
    }

    @Override
    public void getStartMatch(String token, String amount, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getStartMatch(token, amount, dataCallback);
        else mRemoteDataSource.getStartMatch(token, amount, dataCallback);
    }

    @Override
    public void getPromotion(String token, String page, String number, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getPromotion(token, page, number, dataCallback);
        else mRemoteDataSource.getPromotion(token, page, number, dataCallback);
    }

    @Override
    public void getPromotionByMember(String token, String pageNo, String inviteId, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getPromotionByMember(token, pageNo, inviteId, dataCallback);
        else mRemoteDataSource.getPromotionByMember(token, pageNo, inviteId, dataCallback);
    }

    @Override
    public void getInvestPromotion(String token, String page, String number, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getInvestPromotion(token, page, number, dataCallback);
        else mRemoteDataSource.getInvestPromotion(token, page, number, dataCallback);
    }

    @Override
    public void getInvestPromotionByMember(String token, String pageNo, String pointId, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getInvestPromotionByMember(token, pageNo, pointId, dataCallback);
        else mRemoteDataSource.getInvestPromotionByMember(token, pageNo, pointId, dataCallback);
    }

    @Override
    public void getPromotionReward(String token, String page, String number, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getPromotionReward(token, page, number, dataCallback);
        else mRemoteDataSource.getPromotionReward(token, page, number, dataCallback);
    }

    @Override
    public void getLoginAuthType(String phone, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getLoginAuthType(phone, dataCallback);
        else mRemoteDataSource.getLoginAuthType(phone, dataCallback);
    }

    @Override
    public void getScoreRecord(String token, String pageNum, String pageSize, String type, String createStartTime, String createEndTime, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.getScoreRecord(token, pageNum, pageSize, type, createStartTime, createEndTime, dataCallback);
        else
            mRemoteDataSource.getScoreRecord(token, pageNum, pageSize, type, createStartTime, createEndTime, dataCallback);
    }

    @Override
    public void sendEditAccountPwdCode(String token, DataCallback dataCallback) {
        if (isLocal)
            mLocalDataSource.sendEditAccountPwdCode(token, dataCallback);
        else
            mRemoteDataSource.sendEditAccountPwdCode(token, dataCallback);
    }

    @Override
    public void getDepositList(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getDepositList(token, dataCallback);
        else mRemoteDataSource.getDepositList(token, dataCallback);
    }

    @Override
    public void commitSellerApply(String token, String coinId, String json, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.commitSellerApply(token, coinId, json, dataCallback);
        else mRemoteDataSource.commitSellerApply(token, coinId, json, dataCallback);
    }

    @Override
    public void getNiurenRank(String token,String time, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getNiurenRank(token,time, dataCallback);
        else mRemoteDataSource.getNiurenRank(token,time, dataCallback);
    }

    @Override
    public void getCoinType(DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getCoinType(dataCallback);
        else mRemoteDataSource.getCoinType(dataCallback);
    }

    @Override
    public void getLeverage(DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getLeverage(dataCallback);
        else mRemoteDataSource.getLeverage(dataCallback);
    }

    @Override
    public void getFollowHistory(String token,String pageNo, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getFollowHistory(token,pageNo, dataCallback);
        else mRemoteDataSource.getFollowHistory(token,pageNo, dataCallback);
    }

    @Override
    public void getCancelFollow(String token, String followId, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getCancelFollow(token,followId, dataCallback);
        else mRemoteDataSource.getCancelFollow(token,followId, dataCallback);
    }

    @Override
    public void getApplyNiuren(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getApplyNiuren(token, dataCallback);
        else mRemoteDataSource.getApplyNiuren(token, dataCallback);
    }

    @Override
    public void addFollow(String token,String symbol, String leverage, String amount, String jyPassword, String fMemberId, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.addFollow(token,symbol,leverage,amount,jyPassword,fMemberId, dataCallback);
        else mRemoteDataSource.addFollow(token,symbol,leverage,amount,jyPassword,fMemberId, dataCallback);
    }

    @Override
    public void getLockUSDT(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getLockUSDT(token, dataCallback);
        else mRemoteDataSource.getLockUSDT(token, dataCallback);
    }

    @Override
    public void getGoogleCode(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getGoogleCode(token, dataCallback);
        else mRemoteDataSource.getGoogleCode(token, dataCallback);
    }

    @Override
    public void bindGoogle(String token, String secret, String code, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.bindGoogle(token,secret,code, dataCallback);
        else mRemoteDataSource.bindGoogle(token,secret,code, dataCallback);
    }

    @Override
    public void getDrawleSymbol(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getDrawleSymbol(token,dataCallback);
        else mRemoteDataSource.getDrawleSymbol(token, dataCallback);
    }

    @Override
    public void getCurrentOrder(String token, int pageNo, String symbol, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getCurrentOrder(token,pageNo,symbol, dataCallback);
        else mRemoteDataSource.getCurrentOrder(token,pageNo,symbol, dataCallback);
    }

    @Override
    public void getEryuanHistory(String token, int pageNo, String symbol, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getEryuanHistory(token,pageNo,symbol, dataCallback);
        else mRemoteDataSource.getEryuanHistory(token,pageNo,symbol, dataCallback);
    }

    @Override
    public void getErryuanHold(String token, int pageNo, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getErryuanHold(token,pageNo, dataCallback);
        else mRemoteDataSource.getErryuanHold(token,pageNo, dataCallback);
    }

    @Override
    public void getInvestDetail(String token,int pageNo, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getInvestDetail(token,pageNo, dataCallback);
        else mRemoteDataSource.getInvestDetail(token,pageNo, dataCallback);
    }

    @Override
    public void trusteeshipSubmit(String token, String amount,String password,String type,String coinName, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.trusteeshipSubmit(token,amount,password,type,coinName, dataCallback);
        else mRemoteDataSource.trusteeshipSubmit(token,amount,password,type,coinName, dataCallback);
    }

    @Override
    public void getInvestQuota(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getInvestQuota(token, dataCallback);
        else mRemoteDataSource.getInvestQuota(token, dataCallback);
    }

    @Override
    public void getDayRate(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getDayRate(token, dataCallback);
        else mRemoteDataSource.getDayRate(token, dataCallback);
    }

    @Override
    public void getInvestRule(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getInvestRule(token, dataCallback);
        else mRemoteDataSource.getInvestRule(token, dataCallback);
    }

    @Override
    public void getInvestFinish(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getInvestFinish(token, dataCallback);
        else mRemoteDataSource.getInvestFinish(token, dataCallback);
    }

    @Override
    public void getInvestEarning(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getInvestEarning(token, dataCallback);
        else mRemoteDataSource.getInvestEarning(token, dataCallback);
    }

    @Override
    public void getInvestType(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getInvestType(token, dataCallback);
        else mRemoteDataSource.getInvestType(token, dataCallback);
    }

    @Override
    public void getInvestProfit(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getInvestProfit(token, dataCallback);
        else mRemoteDataSource.getInvestProfit(token, dataCallback);
    }

    @Override
    public void getInvestProfitDetail(String token,String type,int pageNo, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getInvestProfitDetail(token,type,pageNo ,dataCallback);
        else mRemoteDataSource.getInvestProfitDetail(token,type,pageNo, dataCallback);
    }

    @Override
    public void getStaticProfit(String token,int page, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getStaticProfit(token,page, dataCallback);
        else mRemoteDataSource.getStaticProfit(token,page, dataCallback);
    }

    @Override
    public void getTeamProfit(String token,int page, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getTeamProfit(token,page, dataCallback);
        else mRemoteDataSource.getTeamProfit(token,page, dataCallback);
    }

    @Override
    public void getStaticTransfer(String token,String amount, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getStaticTransfer(token,amount, dataCallback);
        else mRemoteDataSource.getStaticTransfer(token,amount, dataCallback);
    }

    @Override
    public void getTeamTransfer(String token,String amount, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getTeamTransfer(token,amount, dataCallback);
        else mRemoteDataSource.getTeamTransfer(token,amount, dataCallback);
    }

    @Override
    public void setTickersZone(String token, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.setTickersZone(token, dataCallback);
        else mRemoteDataSource.setTickersZone(token, dataCallback);
    }

    @Override
    public void setTickersPage(String token, String sort, String direction, String nav,String legalCurrency, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.setTickersPage(token,sort,direction,nav,legalCurrency, dataCallback);
        else mRemoteDataSource.setTickersPage(token,sort,direction,nav,legalCurrency, dataCallback);
    }

    @Override
    public void addCollection(String symbol, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.addCollection(symbol, dataCallback);
        else mRemoteDataSource.addCollection(symbol, dataCallback);
    }

    @Override
    public void cancleCollection(String symbol, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.cancleCollection(symbol, dataCallback);
        else mRemoteDataSource.cancleCollection(symbol, dataCallback);
    }

    @Override
    public void getFavorite(String token,String id, DataCallback dataCallback) {
        if (isLocal) mLocalDataSource.getFavorite(token,id, dataCallback);
        else mRemoteDataSource.getFavorite(token,id, dataCallback);
    }

}

