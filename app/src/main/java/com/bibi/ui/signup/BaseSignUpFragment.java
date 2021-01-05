package com.bibi.ui.signup;

import com.bibi.base.BaseTransFragment;

/**
 * Created by Administrator on 2018/2/2.
 */

public abstract class BaseSignUpFragment extends BaseTransFragment {
    public interface OperateCallback {
        void switchType(Type type);
    }

    public enum Type {
        PHONE, EMAIL
    }
}
