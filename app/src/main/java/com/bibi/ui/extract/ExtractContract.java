package com.bibi.ui.extract;


import com.bibi.base.Contract;
import com.bibi.entity.CoinContract;
import com.bibi.entity.ExtractInfo;
import com.bibi.entity.RechargeSupportContract;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface ExtractContract {
    interface View extends Contract.BaseView<Presenter> {
        void errorMes(int e, String meg);

        void getRechargeSupportSuccess(List<RechargeSupportContract> objs, String parentCoin);

        void extractinfoFail(Integer code, String toastMessage);

        void extractinfoSuccess(List<ExtractInfo> obj);

        void usdtInfoSuccess(CoinContract obj);

        void extractSuccess(String obj);

        void extractFail(Integer code, String toastMessage);
    }
        
    interface Presenter extends Contract.BasePresenter {

        void getRechargeSupport(String token, String parentCoin);

        void getUSDTWallet(String token);

        void extractinfo(String token,String parentCoin);

        void extract(String token, String unit, String amount, String remark, String jyPassword, String address, String code, String googleCode,String tag);
    }
}
