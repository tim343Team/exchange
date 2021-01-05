package com.bibi.ui.main.options;

import java.util.List;

import com.bibi.base.Contract;
import com.bibi.entity.OptionEntity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/12
 */
public class OptionsContract {
    interface View extends Contract.BaseView<Presenter> {
        void getCurrentSuccess(List<OptionEntity> message);
    }

    interface Presenter extends Contract.BasePresenter {
        void getCurrentOrder(String token, int pageNo, String symbol);
    }

    interface DetailPresenter extends Contract.BasePresenter {
        void getHistory(String token, int pageNo, String symbol);

        void getHold(String token, int pageNo);
    }

    interface DetailView extends Contract.BaseView<DetailPresenter> {
        void getHistorySuccess(List<OptionEntity> message);

        void getHoldSuccess(List<OptionEntity> message);
    }
}
