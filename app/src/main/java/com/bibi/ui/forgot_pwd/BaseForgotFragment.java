package com.bibi.ui.forgot_pwd;

import com.bibi.base.BaseTransFragment;

/**
 * Created by Administrator on 2018/2/2.
 */

public abstract class BaseForgotFragment extends BaseTransFragment {
    public interface OperateCallback {
        void switchType(Type type);
    }

    public enum Type {
        PHONE, EMAIL
    }
}
