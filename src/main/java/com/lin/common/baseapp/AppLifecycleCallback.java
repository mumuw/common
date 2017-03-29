package com.lin.common.baseapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;


/**
 * Created by linweilin on 2017/2/22.
 */

public class AppLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    private static final int ON_BACK_TO_FRONT = 100;
    private static final int ON_FRONT_TO_BACK = 101;
    private Handler handler;
    private ActivityManager am;
    private String packageName;
    public AppLifecycleCallback() {
        Context context = AppUtils.getInstance().getContext();
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        packageName = context.getPackageName();
        handler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ON_BACK_TO_FRONT:
                        AppEventManager.getInstance().notifyAppStateChange(true);
                        break;
                    case ON_FRONT_TO_BACK:
                        AppEventManager.getInstance().notifyAppStateChange(false);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (!InitManager.isInit) {
            InitManager.initInActivity(activity, BaseApplication.initDelegate(activity));
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
