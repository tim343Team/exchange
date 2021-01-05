package com.bibi.ui.my_promotion;


import java.util.List;

import com.bibi.base.Contract;
import com.bibi.entity.ScoreRecordBean;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public interface PromotionRewardContract {

    interface View extends Contract.BaseView<Presenter>{
        void getPromotionRewardFail(Integer code, String toastMessage);

        void getPromotionRewardSuccess(List<ScoreRecordBean> obj);
    }


    interface Presenter extends Contract.BasePresenter{
        void getPromotionReward(String token, String page, String number);
    }

}
