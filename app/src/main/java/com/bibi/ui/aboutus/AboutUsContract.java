package com.bibi.ui.aboutus;


import com.bibi.base.Contract;
import com.bibi.entity.Vision;

/**
 * Created by Administrator on 2017/9/25.
 */

public interface AboutUsContract {

    interface View extends Contract.BaseView<Presenter> {


        void getNewVisionSuccess(Vision obj);

        void getNewVisionFail(Integer code, String toastMessage);


    }

    interface Presenter extends Contract.BasePresenter {

        void getNewVision(String token);
    }
}
