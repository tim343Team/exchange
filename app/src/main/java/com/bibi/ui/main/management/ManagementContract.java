package com.bibi.ui.main.management;

import java.util.List;

import com.bibi.base.Contract;
import com.bibi.entity.BannerEntity;
import com.bibi.entity.ContractDetailEntity;
import com.bibi.entity.InvestDatRateEntity;
import com.bibi.entity.InvestProfitEntity;
import com.bibi.entity.InvestQuotEntity;
import com.bibi.entity.InvestTypeEntity;
import com.bibi.entity.ProfitDetailEntity;
import com.bibi.entity.ProfitEntity;
import com.bibi.entity.ProfitListEntity;
import com.bibi.entity.ProfitManageEntity;
import com.bibi.entity.PromotionRecord;
import com.bibi.ui.my_promotion.PromotionRecordContract;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class ManagementContract {
    interface View extends Contract.BaseView<ManagementContract.ManagementPresenter> {
        void bannersSuccess(List<BannerEntity> obj);

        void submitSuccess(String obj);

//        void getQuotaSuccess(InvestQuotEntity obj);

        void getDayRateSuccess(InvestDatRateEntity obj);

        void getInvestRuleSuccess(String obj);

        void getInvestFinishSuccess(String obj);

        void getInvestEarningSuccess(List<ProfitManageEntity> obj);

        void getInvestTypeSuccess(List<InvestTypeEntity> obj);
        }

    interface ManagementPresenter extends Contract.BasePresenter {
        void banners(String sysAdvertiseLocation);

        void trusteeshipSubmit(String token,String amount,String password,String type,String coinName);

//        void getInvestQuota(String token);

        void getDayRate(String token);

        void getInvestRule(String token);

        void getInvestFinish(String token);

        void getInvestEarning(String token);

        void getInvestType(String token);
        }

    public interface ContractView extends Contract.BaseView<ManagementContract.ContractPresenter> {
        void getDataSuccess(List<ContractDetailEntity> datas);

        void submitSuccess(String data);
    }

    public interface ContractPresenter extends Contract.BasePresenter {
        void getInvestDetail(String token,int pageNo);
    }

    public interface ProfitView extends Contract.BaseView<ManagementContract.ProfitPresenter> {
        void getDataDetailSuccess(List<ProfitDetailEntity> datas);

        void getDataSuccess(List<InvestProfitEntity> datas);
    }

    public interface ProfitPresenter extends Contract.BasePresenter {
        void getInvestProfit(String token);

        void getInvestProfitDetail(String token,String type,int pageNo);
    }

    public interface AssetWalletView extends Contract.BaseView<ManagementContract.AssetWalletPresenter> {
        void getDataSuccess(List<ProfitListEntity> datas);

        void getDetailDataSuccess(List<ProfitDetailEntity> datas);
    }

    public interface AssetWalletPresenter extends Contract.BasePresenter {
        void getStaticProfit(String token,int page);

        void getTeamProfit(String token,int page);

        void getProfitDetail(String token,int page);
    }

    public interface TransferView extends Contract.BaseView<ManagementContract.TransferPresenter> {
        void getDataSuccess(String datas);
    }

    public interface TransferPresenter extends Contract.BasePresenter {
        void getStaticTransfer(String token,String amount);

        void getTeamTransfer(String token,String amount);
    }

    public interface PromotionView extends Contract.BaseView<ManagementContract.PromotionPresenter>{
        void getPromotionFail(Integer code, String toastMessage);

        void getPromotionSuccess(List<PromotionRecord> obj);

        void getPromotionMemberFail(Integer code, String toastMessage);

        void getPromotionMemberSuccess(List<PromotionRecord> obj);
    }


    public interface PromotionPresenter extends Contract.BasePresenter{
        void getPromotion(String token, String page, String number);

        void getPromotionByMember(String token, String pageNo, String inviteId);

        void getInvestPromotion(String token, String page, String number);

        void getInvestPromotionByMember(String token, String pageNo, String pointId);
    }
}
