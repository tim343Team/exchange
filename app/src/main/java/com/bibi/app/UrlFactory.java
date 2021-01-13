package com.bibi.app;

import com.bibi.config.AppConfig;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/9
 */
public class UrlFactory {
    public static final String host = AppConfig.BASE_URL;

    //获取充币地址
    public static String getChongbi() {
        return host + "/uc/asset/wallet/reset-address";
    }

    //申请成为商家
    public static String getSellerApply() {
        return host + "/uc/approve/certified/business/apply";
    }

    //    提币验证码
    public static String getCode() {
        return host + "/uc/mobile/withdraw/code";
    }

    //    提币接口
    public static String getTIBi() {
        return host + "/uc/withdraw/apply";
    }

    //获取保证金币种列表
    public static String getDepositList() {
        return host + "/uc/approve/business-auth-deposit/list";
    }

    //帮助中心
    public static String getHelp() {
        return host + "/uc/ancillary/more/help";
    }

    //帮助中心
    public static String getHelpNew() {
        return host + "/uc/ancillary/system/help";
    }

    //合约指南
    public static String getContractUrl() {
        return host + "/uc/ancillary/more/help/detail";
    }


    public static String getHelpXinShou() {
        return host + "/uc/ancillary/more/help/page";
    }


    //交易明细接口
    public static String getCha() {
        return host + "/uc/asset/transaction/all";
    }

    //提币明细
    public static String getChaTiBi() {
        return host + "/uc/withdraw/record";
    }

    //交易记录
    public static String getChargeHis() {
        return host + "/uc/asset/transaction";
    }

    //
    public static String getShangjia() {
        return host + "/uc/approve/certified/business/status";
    }

    // 获取汇率
    public static String getRateUrl() {
        return host + "/market/exchange-rate/usd-cny";
    }

    public static String getPhoneCodeUrl() {
        return host + "/uc/mobile/code";
    }

    //注册
    public static String getSignUpByPhone() {
        return host + "/uc/register/phone";
    }

    public static String getSignUpByEmail() {
        return host + "/uc/register/email";
    }

    public static String getLoginUrl() {
        return host + "/uc/login";
    }

    public static String getLoginCodeUrl() {
        return host + "/uc/login/phone";
    }

    public static String getKDataUrl() {
        return host + "/market/history";
    }

    public static String getSpotKDataUrl() {
        //return host + "/market/spot-market/history";
        return host + "/market/history";
    }

    public static String getAllCurrency() {
        return host + "/market/symbol-thumb";
    }

    public static String getAllSpotCurrency() {
        //return host + "/market/spot-market/symbol-thumb";
        return host + "/market/symbol-thumb";
    }


    /**
     * 首页获取所有的币种
     */
    public static String getAllCurrencys() {
        //return host + "/market/spot-market/overview";
        return host + "/market/overview";
    }

    /**
     * 首页获取DeFi等新币所有的币种
     */
    public static String getNewTickersCurrencys() {
        return host + "/market/newTickers/page-query";
    }

    /**
     * 得到信息，来设置输入小数点位数的限制
     */
    public static String getSymbolInfo() {
        return host + "/market/symbol-info";
    }

    /**
     * 得到币币信息，来设置输入小数点位数的限制
     */
    public static String getSpotSymbolInfo() {
        //return host + "/market/spot-market/symbol-info";
        return host + "/market/symbol-info";
    }


    public static String getFindUrl() {
        return host + "/exchange/favor/find";
    }

    public static String getDeleteUrl() {
        return host + "/exchange/favor/delete";
    }

    public static String getAddUrl() {
        return host + "/exchange/favor/add";
    }

    public static String getExChangeUrl() {
        return host + "/exchange/order/add";
    }

    public static String getRechargeUrl() {
        return host + "/uc/recharge/address";
    }//充值地址记录

    public static String getWalletUrl() {
        return host + "/uc/asset/wallet/";
    }//合约资产

    public static String getUsdtWalletUrl() {
        return host + "/uc/asset/wallet/USDT";
    }//USDT资产

    public static String getOTCWalletUrl() {
        return host + "/uc/otc/wallet/USDT";
    }//OTC资产

    public static String getContractWalletUrl() {
        return host + "/uc/asset/wallet";
    }//合约资产

    public static String getSpotWalletUrl() {
        //return host + "/uc/asset/spotWallet";
        return host + "/uc/asset/eryuanWallet";
    }//币币资产

    public static String getEryuanWalletUrl() {
        return host + "/uc/asset/eryuanWallet";
    }//期权资产

    public static String getFiatWalletUrl() {
        //return host + "/uc/otc/wallet/get";
        return host + "/uc/asset/memberWallet";
    }//法币资产


    public static String getFiatAssetUrl() {
        return host + "/uc/otc/wallet/get";
    }//法币资产

    public static String getMarginAssetUrl() {
        return host + "/margin-trade/lever_wallet/list";
    }//杠杆资产

    public static String getExchangeRate() {
        return host + "/market/exchange-rate/usd-cny";
    }//获取汇率

    public static String getAllUrl() {
        return host + "/otc/coin/all";
    }

    public static String getFeeRateUrl() {
        return host + "/otc/order/getOtcFeeRate";
    }

    public static String getAdvertiseUrl() {
        return host + "/otc/advertise/page-by-unit";
    }

    public static String getCountryUrl() {
        return host + "/uc/support/country";
    }

    public static String getReleaseAdUrl() {
        return host + "/otc/advertise/create";
    }

    public static String getUploadPicUrl() {
        return host + "/uc/upload/oss/base64";
    }

    public static String changeAvatarUrl() {
        return host + "/uc/approve/change/avatar ";
    }

    public static String getNameUrl() {
        return host + "/uc/approve/real/name";
    }

    public static String getAccountPwdUrl() {
        return host + "/uc/approve/transaction/password";
    }

    public static String getAllAdsUrl() {
        return host + "/otc/advertise/all";
    }

    public static String getReleaseUrl() {
        return host + "/otc/advertise/on/shelves";
    }

    public static String getDeleteAdsUrl() {
        return host + "/otc/advertise/delete";
    }

    public static String getOffShelfUrl() {
        return host + "/otc/advertise/off/shelves";
    }

    public static String getAdDetailUrl() {
        return host + "/otc/advertise/detail";
    }

    public static String getUpdateAdUrl() {
        return host + "/otc/advertise/update";
    }

    public static String getC2CInfoUrl() {
        return host + "/otc/order/pre";
    }

    public static String getC2CBuyUrl() {
        return host + "/otc/order/buy";
    }

    public static String getC2CSellUrl() {
        return host + "/otc/order/sell";
    }

    public static String getMyOrderUrl() {
        return host + "/otc/order/self";
    }

    public static String getExtractinfo2Url() {
        return host + "/uc/withdraw/support/coin/info2";
    }

    public static String getExtractinfoUrl() {
        return host + "/uc/withdraw/support/coin/info";
    }

    public static String getExtractUrl() {
        return host + "/uc/withdraw/apply";
    }

    public static String getAllTransactionUrl2() {
        return host + "/uc/asset/transaction";
    }

    public static String getSafeSettingUrl() {
        return host + "/uc/approve/security/setting";
    }

    public static String getAvatarUrl() {
        return host + "/uc/approve/change/avatar";
    }

    public static String getBindPhoneUrl() {
        return host + "/uc/approve/bind/phone";
    }

    public static String getSendCodeUrl() {
        return host + "/uc/mobile/bind/code";
    }

    public static String getBindEmailUrl() {
        return host + "/uc/approve/bind/email";
    }

    public static String getBindUserNameUrl() {
        return host + "/uc/member/updateNickname";
    }

    public static String getSendEmailCodeUrl() {
        return host + "/uc/bind/email/code";
    }

    public static String getEditLoginPwdUrl() {
        return host + "/uc/mobile/update/password/code";
    }

    public static String getSendEditAccountPwdCodeUrl() {
        return host + "/uc/mobile/trade/code";
    }

    public static String getEditPwdUrl() {
        return host + "/uc/approve/update/password";
    }

    public static String getRechargeSupportedUrl() {
        return host + "/uc/coin/recharge/supported";
    }

    public static String getPlateUrl() {
        return host + "/market/exchange-plate";
    }

    public static String getSpotPlateUrl() {
        //return host + "/market/spot-market/exchange-plate-mini";
        return host + "/market/exchange-plate-mini";
    }

    /**
     * 查询推广统计
     */
    public static String getSummary() {
        return host + "/uc/promotion/summary ";
    }

    /**
     * 查询提币地址
     */
    public static String getAddressUrl() {
        return host + "/uc/withdraw/address/page";
    }

    /**
     * 删除提币地址
     */
    public static String getDeleteAddressUrl() {
        return host + "/uc/withdraw/address/delete";
    }

    /**
     * 添加提币地址验证码
     */
    public static String getAddressCode() {
        return host + "/uc/mobile/add/address/code";
    }

    /**
     * 添加提币地址
     */
    public static String addAddressUrl() {
        return host + "/uc/withdraw/address/add";
    }

    /**
     * 查询当前委托
     */
    public static String getEntrustUrl() {
        return host + "/exchange/order/personal/current";
    }

    /**
     * 查询币币当前委托
     */
    public static String getSpotEntrustUrl() {
        return host + "/exchange/spot-order/current";
    }

    /**
     * 查询币币全部委托
     */
    public static String getSpotAllEntrustUrl() {
        return host + "/exchange/spot-order/personal/current";
    }

    /**
     * 查询币币全部成交
     */
    public static String getSpotAllHistoryUrl() {
        return host + "/exchange/spot-order/personal/history";
    }

    /**
     * 查询当前持仓
     */
    public static String getHoldUrl() {
        return host + "/exchange/order/hold";
    }

    /**
     * 查询当前成交(带时间选择)
     */
    public static String getPersonalHoldUrl() {
        return host + "/exchange/order/personal/hold";
    }

    /**
     * 查询成交
     */
    public static String getHoldFinishUrl() {
        return host + "/exchange/order/personal/closeList";
    }

    /**
     * 查询当前委托
     */
    public static String getHoldCurrentUrl() {
        return host + "/exchange/order/personal/current";
    }

    /**
     * 添加订单
     */
    public static String getAddOrderUrl() {
        return host + "/exchange/order/addOrder";
    }

    /**
     * 添加币币订单
     */
    public static String getAddSpotOrderUrl() {
        return host + "/exchange/spot-order/add";
    }

    /**
     * 获取历史委托记录
     */
    public static String getHistoryEntrus() {
        return host + "/exchange/order/personal/history";
    }

    public static String getCancleEntrustUrl() {
        return host + "/exchange/order/cancel/";
    }

    public static String getPhoneForgotPwdCodeUrl() {
        return host + "/uc/mobile/reset/code";
    }

    public static String getEmailForgotPwdCodeUrl() {
        return host + "/uc/reset/email/code";
    }

    public static String getForgotPwdUrl() {
        return host + "/uc/reset/login/password";
    }

    public static String getCaptchaUrl() {
        return host + "/uc/start/captcha";
    }

    public static String getSendChangePhoneCodeUrl() {
        return host + "/uc/mobile/change/code";
    }

    public static String getChangePhoneUrl() {
        return host + "/uc/approve/change/phone";
    }

    public static String getMessageUrl() {
        return host + "/uc/news/page";
    }

    public static String getMessageDetailUrl() {
        return host + "/uc/news/";
    }

    public static String getMessageHelpUrl() {
        return host + "/uc/ancillary/more/help/detail";
    }

    public static String getMessageHelpUrlNew() {
        return host + "/uc/ancillary/system/help/";
    }


    public static String getRemarkUrl() {
        return host + "/uc/feedback";
    }

    public static String getAppInfoUrl() {
        return host + "/uc/ancillary/website/info";
    }

    public static String getBannersUrl() {
        return host + "/uc/ancillary/system/advertise";
    }

    public static String getOrderDetailUrl() {
        return host + "/otc/order/detail";
    }

    public static String getCancleUrl() {
        return host + "/otc/order/cancel";
    }

    public static String getpayDoneUrl() {
        return host + "/otc/order/pay";
    }

    public static String getReleaseOrderUrl() {
        return host + "/otc/order/release";
    }

    public static String getAppealUrl() {
        return host + "/otc/order/appeal";
    }

    public static String getEditAccountPwdUrl() {
        return host + "/uc/approve/update/transaction/password";
    }

    public static String getResetAccountPwdUrl() {
        return host + "/uc/approve/reset/transaction/password";
    }

    public static String getResetAccountPwdCodeUrl() {
        return host + "/uc/mobile/transaction/code";
    }

    public static String getHistoryMessageUrl() {
        return host + "/chat/getHistoryMessage";
    }

    public static String getEntrustHistory() {
        return host + "/exchange/order/history";
    }

    public static String getCreditInfo() {
        return host + "/uc/approve/real/detail";
    }

    public static String getNewVision() {
        return host + "/uc/ancillary/system/app/version/0";
    }

    public static String getSymbolUrl() {
        return host + "/market/symbol";
    }

    public static String getAccountSettingUrl() {
        return host + "/uc/approve/account/setting";
    }

    public static String getBindBankUrl() {
        return host + "/uc/approve/bind/bank";
    }

    public static String getUpdateBankUrl() {
        return host + "/uc/approve/update/bank";
    }

    public static String getBindAliUrl() {
        return host + "/uc/approve/bind/ali";
    }

    public static String getUpdateAliUrl() {
        return host + "/uc/approve/update/ali";
    }

    public static String getUpdateInterUrl() {
        return host + "/uc/approve/bind/swift";
    }

    public static String updateInterUrl() {
        return host + "/uc/approve/update/swift";
    }

    public static String getBindWechatUrl() {
        return host + "/uc/approve/bind/wechat";
    }

    public static String getUpdateWechatUrl() {
        return host + "/uc/approve/update/wechat";
    }

    public static String getCheckMatchUrl() {
        return host + "/uc/asset/wallet/match-check";
    }

    public static String getStartMatchUrl() {
        return host + "/uc/asset/wallet/match";
    }

    public static String getPromotionUrl() {
        return host + "/uc/promotion/record";
    }

    public static String getPromotionByMemberUrl() {
        return host + "/uc/promotion/recordByMember";
    }

    public static String getInvestPromotionUrl() {
        return host + "/uc/invest/net/record";
    }

    public static String getInvestPromotionByMemberUrl() {
        return host + "/uc/invest/net/recordByMember";
    }

    public static String getPromotionRewardUrl() {
        return host + "/uc/promotion/reward/record";
    }

    public static String getDepth() {
        return host + "/market/exchange-plate-full";
    } // 获取深度图数据

    public static String getSpotDepth() {
        return host + "/market/exchange-plate-full";
        //return host + "/market/spot-market/exchange-plate-full";
    } // 获取Spot深度图数据

    public static String getCoinDetail() {
        return host + "/uc/coin/detail";
    } // 获取币种详情

    public static String getVolume() {
        return host + "/market/latest-trade";
    } // 获取成交数据

    public static String getSpotVolume() {
        //return host + "/market/spot-market/latest-trade";
        return host + "/market/latest-trade";
    } // 获取Spot成交数据

    public static String getLoginAuthType() {
        return host + "/uc/get/user";
    } // 获取认证方式（是否绑定谷歌认证）

    public static String getServiceFeeAll() {
        return host + "/uc/integration/grade";
    } // 获取所有手续费

    public static String getUserWithdrawLimit() {
        return host + "/uc/integration/day_withdraw/limit";
    } // 获取用户当日提币数量和次数

//    public static String getScoreRecord() {
//        return host + "/uc/integration/record/page_query";
//    } // 用户积分查询

    public static String getScoreRecord() {
        return host + "/uc/promotion/reward/record";
    } // 用户积分查询

    public static String getCandyRecord() {
        return host + "/uc/gift/record";
    } // 获取用户糖果记录

    public static String getRandom() {
        return host + "/uc/approve/video/random";
    } // 获取视频随机数

    public static String uploadVideo() {
        return host + "/uc/upload/video";
    } // 上传视频

    public static String creditVideo() {
        return host + "/uc/approve/kyc/real/name";
    } // 提交视频认证

    public static String getTransferSupportCoin() {
        return host + "/uc/transfer/support_coin";
    } // 获取支持划转币种

    public static String transferFiat() {
        return host + "/uc/asset/transferContract";
    } // 合约 和 币币互转

    public static String transferOptions() {
        return host + "/uc/otc/wallet/transfer";
    } // 币币和otc互转

    public static String transferIntoMargin() {
        return host + "/margin-trade/lever_wallet/change_into";
    } // 币币转杠杆
    public static String transferOutMargin() {
        return host + "/margin-trade/lever_wallet/turn_out";
    } // 杠杆转币币

    public static String borrowCoin() {
        return host + "/margin-trade/loan/loan";
    } // 借贷

    public static String borrowRecord() {
        return host + "/margin-trade/loan/record_list";
    } // 借贷记录

    public static String getRepaymentRecord() {
        return host + "/margin-trade/repayment/history";
    } // 归还记录

    public static String giveBackCoin() {
        return host + "/margin-trade/loan/repayment";
    } // 还币


    public static String addMarginOrder() {
        return host + "/margin-trade/order/add";
    } // 添加杠杆订单

    public static String getOrderHistoryMargin() {
        return host + "/margin-trade/order/history";
    } // 查询历史委托-杠杆

    public static String getOrderCurrentMargin() {
        return host + "/margin-trade/order/current";
    } // 查询当前委托-杠杆

    public static String getOrderDetailMargin() {
        return host + "/margin-trade/order/detail/";
    } // 查询杠杆委托成交明细
    public static String getCancelOrderMargin() {
        return host + "/margin-trade/order/cancel/";
    } // 取消当前杠杆委托


    public static String getIeo() {
        return host + "/uc/ieo/all";
    } // 获取ieo

    public static String getIeoRecord() {
        return host + "/uc/ieo/record";
    } // 获取ieo记录

    public static String takeOrderIeo() {
        return host + "/uc/ieo/order";
    } // ieo下单

    public static String getDefaultSymbol() {
        return host + "/market/default/symbol";
    } // 获取合约首次进入默认的交易对

    public static String getDefaultCoinSymbol() {
        //return host + "/market/spot-market/default/symbol";
        return host + "/market/default/symbol";
    } // 获取币币首次进入默认的交易对


    public static String getTransactionType() {
        return host + "/uc/asset/transaction_type";
    }

    public static String getSpotURL() {
        return host + "/uc/asset/spotWallet";
    } // 查询合约钱包余额


    public static String getAssetPnl() {
        return host + "/uc/asset/contractAssetPNL";
    } // 盈利损失统计

    public static String getFeeRate() {
        return host + "/uc/member/getFeeRate";
    }

    public static String sendLoginCode() {
        return host + "/uc/mobile/login/send";
    }

    public static String getTotalAssets() {
        return host + "/margin-trade/lever_wallet/getTotalAssets";
    }

    public static String getNiurenRankUrl() {
        return host + "/exchange/rank/niurenRank";
    }

    public static String getCoinList() {
        return host + "/exchange/exchange-coin/coin-list";
    }

    //杠杆
    public static String getLeverage() {
        return host + "/exchange/exchange-coin/getLeverage";
    }

    //取消订单
    public static String getCancleOrderUrl() {
        return host + "/exchange/order/cancel/";
    }

    //取消币币订单
    public static String getCancleSpotOrderUrl() {
        return host + "/exchange/spot-order/cancel/";
    }

    public static String getCloseOrderUrl() {
        return host + "/market/order/close/";
    }

    public static String getCloseAllOrderUrl() {
        return host + "/exchange/order/closeAll";
    }

    //修改止盈 止损
    public static String getModifyProffitLoss() {
        return host + "/exchange/order/modifyStopProfitLoss";
    }

    public static String getFollowHistory() {
        return host + "/uc/follow/followList";
    }

    public static String getCancelFollow() {
        return host + "/uc/follow/cancelFollow";
    }

    public static String getApplyNiuRen() {
        return host + "/uc/follow/applyNiuren";
    }

    public static String getLockUSDTUrl() {
        return host + "/uc/follow/getNiurenLockUSDT";
    }

    public static String addFollow() {
        return host + "/uc/follow/addFollow";
    }

    public static String cancleFollow() {
        return host + "/uc/follow/cancelFollow";
    }

    public static String googleCodeUrl() {
        return host + "/uc/google/sendgoogle";
    }

    public static String bindGoogleUrl() {
        return host + "/uc/google/googleAuth";
    }

    public static String getAnnouncementUrl() {
        return host + "/uc/announcement/pageByClassification";
    }

    public static String getInvestDetailUrl() {
//        return host + "/uc/invest/profit/detail"; //合约明细
        return host + "/uc/invest/detail"; //锁仓明细
    }

    public static String getInvestSubmitUrl() {
        return host + "/uc/invest/trusteeship/submit";
    }

    public static String getInvestQuotaUrl() {
        return host + "/uc/invest/quota";
    }

    public static String getDayRateUrl() {
        return host + "/uc/invest/unlimited/day-rate";
    }

    public static String getInvestRuleUrl() {
        return host + "/uc/invest/unlimited/rule";
    }

    public static String getInvestFinishUrl() {
        return host + "/uc/invest/finish";
    }

    public static String getInvestEarning() {
        return host + "/uc/invest/profit/status";
    }

    public static String getInvestTypeUrl() {
        return host + "/uc/invest/type";
    }

    public static String getInvestRemind() {
        return host + "/uc/invest/show/remind";
    }

    public static String getInvestProfitStaticUrl() {
        return host + "/uc/invest/profit/statistics";
    }

    public static String getInvestProfitDetailUrl() {
        return host + "/uc/invest/profit/staticDetail";
    }

    public static String getInvestStaticProfitUrl() {
        return host + "/uc/invest/static/profit";
    }

    public static String getInvestTeamProfitUrl() {
        return host + "/uc/invest/team/profit";
    }

    public static String getInvestStaticTransferUrl() {
        return host + "/uc/invest/static/transfer";
    }

    public static String getInvestTeamTransferUrl() {
        return host + "/uc/invest/team/transfer";
    }

    //二元期权相关

    /**
     * 得到信息，来设置输入小数点位数的限制
     */
    public static String getEryuanSymbolInfo() {
        return host + "/market/eryuan-symbol-info";
    }

    public static String getEryuanSymbolUrl() {
        return host + "/market/eryuan-symbol-thumb";
    }

    public static String getEryuanCoinThumbUrl() {
        return host + "/market/eryuan-symbol-coinThumb";
    }

    public static String getEryuantddOrderUrl() {
        return host + "/market/eryuan-order/addOrder";
        //return host + "/multi-coin-option/order/addOrder";
    }

    public static String getEryuantCurrentUrl() {
        return host + "/market/eryuan-order/current";
    }

    public static String getEryuantHistoryUrl() {
        return host + "/market/eryuan-order/history";
    }

    public static String getEryuantHoldUrl() {
        return host + "/market/eryuan-order/personal/hold";
    }

    //换肤
    public static String getMarketZoneUrl() {
        return host + "/market/tickers/zone";
    }

    public static String setTickersPageUrl() {
        return host + "/market/tickers/page-query";
    }

    public static String addCollectionUrl() {
        return host + "/uc/collection/addCollection";
    }

    public static String cancleCollectionUrl() {
        return host + "/uc/collection/cancelCollection";
    }

    public static String getCollectionUrl() {
        return host + "/market/tickers/member-collection";
    }
}
