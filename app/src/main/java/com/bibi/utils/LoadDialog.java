package com.bibi.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.bibi.R;


public class LoadDialog extends Dialog{

    public LoadDialog(@NonNull Context context) {
        super(context, R.style.custom_dialog);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);
    }
}
