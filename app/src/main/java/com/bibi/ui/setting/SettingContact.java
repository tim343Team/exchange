package com.bibi.ui.setting;

import com.bibi.base.Contract;
import com.bibi.entity.Vision;

/**
 * Created by Administrator on 2018/4/24 0024.
 */

public class SettingContact {

    interface View extends Contract.BaseView<Presenter> {

        void getNewVisionSuccess(Vision obj);

        void getNewVisionFail(Integer code, String toastMessage);

    }

    interface Presenter extends Contract.BasePresenter {

        void getNewVision(String token);

    }
}
