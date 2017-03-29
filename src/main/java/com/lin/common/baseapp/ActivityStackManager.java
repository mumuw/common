package com.lin.common.baseapp;

import android.app.Activity;
import android.os.Process;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by linweilin on 2016/12/24.
 */

public class ActivityStackManager {
    private Stack<Activity> activities;
    private static ActivityStackManager mInstance;

    public static ActivityStackManager getInstance() {
        if (mInstance == null) {
            synchronized (ActivityStackManager.class) {
                if (mInstance == null) {
                    mInstance = new ActivityStackManager();
                }
            }
        }
        return mInstance;
    }

    public void addActivity(Activity activity){
        if (activities == null){
            activities = new Stack<>();
        }
        if(activities.search(activity) == -1) {
            activities.push(activity);
        }
        publishActivityStack();
    }

    public void setTopActivity(Activity activity){
        if(activities != null && activities.size()>0){
            if(activities.search(activity) == -1){
                activities.push(activity);
                return;
            }

            int location = activities.search(activity);
            if(location != 1){
                activities.remove(activity);
                activities.push(activity);
            }
        }
    }

    public void finishTopActivity(){
        if (activities != null && activities.size() > 0){
            Activity activity = activities.pop();
            if (activity != null && !activity.isFinishing()){
                activity.finish();
            }
        }
    }

    public void finishActivity(Activity activity){
        if(activities != null && activities.size()>0){
            activities.remove(activity);
        }
    }

    public boolean isTopActivity(Activity activity){
        return activity.equals(activities.peek());
    }

    public int getActivityPosition(Activity acitivty){
        return activities.search(new SoftReference<Activity>(acitivty));
    }

    public Stack<Activity> getActivityStack() {
        return activities;
    }


    public void finishAllActivity(){
        if(activities != null && activities.size()>0){
            while(!activities.empty()){
                Activity activity = activities.pop();
                if (activity != null) {
                    activity.finish();
                }
            }
            activities.clear();
            activities = null;
        }
    }

    public Activity getTopActivity(){
        if(activities != null && activities.size()>0){
            return activities.peek();
        }
        return null;
    }

    public void publishActivityStack(){
        Iterator<Activity> it = activities.iterator();
        StringBuilder builder = new StringBuilder();
        builder.append("ActivityStack:\n");
        while(it.hasNext()){
            Activity activity = it.next();
            if (activity != null) {
                builder.append(activity.getClass().getName() + "\n");
            }
        }
    }

    public void appExit(){
        finishAllActivity();
        Process.killProcess(Process.myPid());
    }

}
