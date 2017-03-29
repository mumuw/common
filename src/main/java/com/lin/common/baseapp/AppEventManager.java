package com.lin.common.baseapp;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by linweilin on 2017/2/22.
 */

public class AppEventManager {

    public interface AppEventListener{
        void onAppStateChange(boolean front);
    }
    private CopyOnWriteArrayList<AppEventListener> mEventListeners;
    private AppEventManager(){
        mEventListeners = new CopyOnWriteArrayList<>();
    }
    private static AppEventManager sInstance;

    public static AppEventManager getInstance() {
        if (sInstance == null) {
            synchronized (AppEventManager.class) {
                if (sInstance == null) {
                    sInstance = new AppEventManager();
                }
            }
        }
        return sInstance;
    }

    public void addAppEventListener(AppEventListener listener) {
        if (listener != null && !mEventListeners.contains(listener)) {
            mEventListeners.add(listener);
        }
    }

    public void removeAppEventListener(AppEventListener listener) {
        if (listener != null && mEventListeners.contains(listener)) {
            mEventListeners.remove(listener);
        }
    }


    /**
     * 通知app 状态该改变
     * @param front true 切换到前台，false 为切换到后台
     */
    void notifyAppStateChange(boolean front) {
        for (AppEventListener listener : mEventListeners) {
            listener.onAppStateChange(front);
        }
    }
}