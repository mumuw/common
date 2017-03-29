package com.lin.common.reactnative;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.common.LifecycleState;
import com.lin.common.baseapp.ActivityStackManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by linweilin on 2017/2/16.
 */

public class ReactNativeUtil {
    private static final String TAG = ReactNativeUtil.class.getSimpleName();
    private static ReactNativeUtil mInstance;
    private Context mApplicationContext;
    private ReactInstanceManager mReactInstanceManager;

    public static synchronized ReactNativeUtil getInstance() {
        if (mInstance == null) {
            mInstance = new ReactNativeUtil();
        }
        return mInstance;
    }

    public void init(final Application application) {
        init(application, null);
    }

    public void init(final Application application, List<ReactPackage> reactPackageList) {
        if (!isSupport()) {
            return;
        }
        mApplicationContext = application.getApplicationContext();
        if (mReactInstanceManager != null) {
            return;
        }

        ReactInstanceManager.Builder builder = ReactInstanceManager.builder().setApplication(application)
                .setJSMainModuleName("index")
                .addPackage(new ReactNativePackage())
                .setUseDeveloperSupport(true)
                .setInitialLifecycleState(LifecycleState.BEFORE_RESUME);

        if (reactPackageList != null && !reactPackageList.isEmpty()) {
            for (ReactPackage reactPackage : reactPackageList) {
                builder.addPackage(reactPackage);
            }
        }
        mReactInstanceManager = builder.build();
        if (!mReactInstanceManager.hasStartedCreatingInitialContext()) {
            mReactInstanceManager.createReactContextInBackground();
        }
    }

    public ReactInstanceManager getReactInstanceManager() {
        return mReactInstanceManager;
    }

    public static void notSupportWarning(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog dialog = builder.setTitle("error")
                .setMessage("device no support reactnative")
                .setCancelable(false)
                .setPositiveButton("exit app", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityStackManager.getInstance().appExit();
                    }
                }).create();
        dialog.show();
    }

    public static boolean isSupport() {
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getDeclaredMethod("get", String.class, String.class);
            return String.valueOf(method.invoke(null, "ro.product.cpu.abi", "")).indexOf("mips") == -1;
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        }
        return true;
    }
}
