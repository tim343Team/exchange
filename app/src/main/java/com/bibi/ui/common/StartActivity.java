package com.bibi.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bibi.R;
import com.bibi.ui.main.MainActivity;
import com.bibi.serivce.MyTextService;
import com.bibi.utils.SharedPreferenceInstance;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
//        startService(new Intent(StartActivity.this, MyTextService.class));
        if (!isTaskRoot()) {
            finish();
            return;
        }
        Timer timer = new Timer();
        timer.schedule(new MyTask(),2000);
    }

    class MyTask extends TimerTask {
        @Override
        public void run() {
            if(SharedPreferenceInstance.getInstance().getIsFirstUse()){
                LeadActivity.actionStart(StartActivity.this);
            }else {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }

}
