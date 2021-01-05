package com.bibi.ui.notice;

import java.util.List;

import com.bibi.base.Contract;
import com.bibi.entity.AnnounceEntity;
import com.bibi.entity.SafeSetting;
import com.bibi.ui.myinfo.MyInfoContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/8
 */
public class AnnouncementContract {
    interface View extends Contract.BaseView<Presenter> {
        void getAnnounceSuccess(List<AnnounceEntity> success);

        void getAnnounceFial(int e, String meg);
    }

    interface Presenter extends Contract.BasePresenter {
        void getAnnouncement(String token, String cate, int pageNo, int pageSize);
    }
}
