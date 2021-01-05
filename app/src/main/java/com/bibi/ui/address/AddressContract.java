package com.bibi.ui.address;

import java.util.List;

import com.bibi.base.Contract;
import com.bibi.data.DataSource;
import com.bibi.entity.ExtractInfo;
import com.bibi.entity.TiBiAddress;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/16
 */
public interface AddressContract {


    interface View extends Contract.BaseView<Presenter> {
        void getAddressSuccess(TiBiAddress address);

        void getAddressFial(Integer code, String toastMessage);

        void deleteAddressSuccess(String message);

        void deleteAddressFial(Integer code, String toastMessage);

        void getAddressCodeSuccess(String message);

        void getAddressCodeFial(Integer code, String toastMessage);

        void addAddressCodeSuccess(String message);

        void addAddressCodeFial(Integer code, String toastMessage);

        void getAllAddressSuccess(List<ExtractInfo> obj);

        void getAllAddressFial(Integer code, String toastMessage);
    }

    interface Presenter extends Contract.BasePresenter {
        /**
         * 添加提币地址接口
         */
//        void addAddress(String token, int pageNo, int pageSize, String symbol);

        /**
         * 获取提币地址接口
         */
        void getAddress(String token, int pageNo, int pageSize);

        void deleteAddress(String token, String id);

        void sendCode(String token);

        void addAddress(String token, String address, String unit, String aims, String code, String remark);

        /**
         * 获取提币地址接口
         */
        void getAllAddress(String token);
    }
}
