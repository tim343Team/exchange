package com.bibi.ui.my_promotion;


import java.util.List;

import com.bibi.base.Contract;
import com.bibi.entity.PromotionRecord;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public interface PromotionRecordContract {

    interface View extends Contract.BaseView<Presenter>{
        void getPromotionFail(Integer code, String toastMessage);

        void getPromotionSuccess(List<PromotionRecord> obj);

        void getPromotionMemberFail(Integer code, String toastMessage);

        void getPromotionMemberSuccess(List<PromotionRecord> obj);
    }


    interface Presenter extends Contract.BasePresenter{
        void getPromotion(String token, String page, String number);

        void getPromotionByMember(String token, String pageNo, String inviteId);
    }

}

