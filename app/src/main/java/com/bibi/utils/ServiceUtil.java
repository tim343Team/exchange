package com.bibi.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

public class ServiceUtil {
    public static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
    }

    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static AudioManager getAudioManager(Context context) {
        return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static PowerManager getPowerManager(Context context) {
        return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    public static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static Vibrator getVibrator(Context context) {
        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (TextUtils.isEmpty(ServiceName)) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

//    public static boolean isFindService(Context context, String ServiceName) {
//        if (TextUtils.isEmpty(ServiceName)) {
//            return false;
//        }
//        ActivityManager myManager = (ActivityManager) context
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
//                .getRunningServices(30);
//        for (int i = 0; i < runningService.size(); i++) {
//            if (runningService.get(i).service.getClassName().toString()
//                    .equals(ServiceName)) {
//                return runningService.get(i);
//            }
//        }
//        return false;
//    }
}
