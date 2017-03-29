package com.lin.common.baseapp;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by linweilin on 2016/12/24.
 */

public class AppUtils {
    private static AppUtils instance;
    private Application application;
    private AssetManager assetManager;
    private Context mContext;
    private ActivityManager mActivityManager;
    private String mProductVersion;//软件版本
    private boolean mDebuggable;//是否调试版本
    private int mPid;//当前运行的进程ID
    private String mAwid;
    private SharedPreferences mSharedPreferences;

    private AppUtils() {
    }

    public synchronized static AppUtils getInstance() {
        if (null == instance) {
            instance = new AppUtils();
        }
        return instance;
    }

    public void init(Application application) {
        this.application = application;
        this.mContext = application.getApplicationContext();
        init();
    }

    public Application getApplication() {
        return application;
    }

    public Context getContext() {
        return mContext;
    }


    public int getScreenWidth() {
        WindowManager manager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    public int getScreenHeight() {
        WindowManager manager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = mContext.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    public float getScreenDensity() {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager manager = (WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            return dm.density;
        } catch (Exception ex) {

        }
        return 1.0f;
    }

    /**
     * 获取应用私有缓存目录
     *
     * @return
     */
    public String getCachePath() {
        return mContext.getFilesDir().getAbsolutePath() + "/cache_dir";
    }

    public File getExternalCachePath() {
        File root = new File(Environment.getExternalStorageDirectory(), mContext.getPackageName());
        File cacheDir = new File(root.getAbsolutePath(), ".cache");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    public AssetManager getAsset() {
        if (null == assetManager) {
            assetManager = mContext.getAssets();
        }
        return assetManager;
    }

    /**
     * 过滤版本号
     *
     * @param versionName
     * @return
     */
    private String clearVersionName(String versionName) {
        if (versionName!=null&&versionName.contains("ctch1")) {
            versionName = versionName.replace("ctch1", "");
        }
        return versionName;
    }

    /**
     * 初始化
     */
    private void init() {
        try {
            String tpackageName = mContext.getPackageName();

            mSharedPreferences = mContext.getSharedPreferences(tpackageName
                    + "_config", Context.MODE_PRIVATE);
            String version = mSharedPreferences.getString("version", null);
            PackageInfo mPackageInfo = mContext.getPackageManager()
                    .getPackageInfo(tpackageName, 0);
            mProductVersion = clearVersionName(mPackageInfo.versionName);
            if (null != version && mProductVersion!=null && compareVersion(version, mProductVersion)) {
                mProductVersion = version;
            }else{
                mProductVersion = "";
            }

            ApplicationInfo applicationInfo = mContext.getPackageManager()
                    .getApplicationInfo(mContext.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            if ((applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
                mDebuggable = true;
            }

            mActivityManager = (ActivityManager) mContext
                    .getSystemService(Context.ACTIVITY_SERVICE);
            mPid = android.os.Process.myPid();

            mAwid = UUID.randomUUID().toString();
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    public String getMetaValue(Context context, String keyName, String defValue)
    {
        Object value = null;
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context
                    .getPackageName(), PackageManager.GET_META_DATA);

            value = applicationInfo.metaData.get(keyName);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(value != null)
        {
            return value.toString();
        }
        else
        {
            return defValue;
        }

    }

    /**
     * 比较版本
     *
     * @param version
     * @param mProductVersion
     * @return
     */
    private boolean compareVersion(String version, String mProductVersion) {
        String[] versions = version.split("\\.");
        String[] productVersions = mProductVersion.split("\\.");
        for (int i = 0; i < versions.length; i++) {
            int v1 = Integer.parseInt(versions[i]);
            int v2 = Integer.parseInt(productVersions[i]);
            if (v1 > v2) {
                return true;
            } else if (v1 == v2) {
                continue;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 是否是开发状态
     */
    public boolean isDebuggable() {
        return mDebuggable;
    }

    /**
     * 获取当前运行的进程ID
     *
     * @return 进程ID
     */
    public int getPid() {
        return mPid;
    }

    public void setProductVersion(String version) {
        if (null != version) {
            mSharedPreferences.edit().putString("version", version).commit();
            mProductVersion = version;
        }
    }

    /**
     * 恢复大版本
     */
    public void recoverProductVersion() {
        mSharedPreferences.edit().remove("version").commit();
    }

    public String getmProductVersion() {
        return mProductVersion;
    }

    public String getmAwid() {
        return mAwid;
    }

    /**
     * 获取当前所占内存
     *
     * @return 当前所占内存
     */
    public long getTotalMemory() {
        android.os.Debug.MemoryInfo[] mems = mActivityManager
                .getProcessMemoryInfo(new int[] { mPid });
        return mems[0].getTotalPrivateDirty();
    }

    /**
     * 获取应用的data/data/....File目录
     *
     * @return File目录
     */
    public String getFilesDirPath() {
        String filesDirPath = "";
        if(mContext.getFilesDir() != null){
            return mContext.getFilesDir().getAbsolutePath();
        }

        return filesDirPath;
    }

    /**
     * 获取应用的data/data/....Cache目录
     *
     * @return Cache目录
     */
    public String getCacheDirPath() {
        String cacheDirPath = "";
        if(mContext.getCacheDir() != null){
            return mContext.getCacheDir().getAbsolutePath();
        }
        return cacheDirPath;
    }

    /**
     * 获取app名称
     * @return
     */
    public String getApplicationLabel() {
        String label = null;
        try {
            PackageManager packageManager = mContext.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), 0);
            label = (String)packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {

        }
        return label;
    }

    public static boolean isInMainProcess(Application var0) {
        String var1 = var0.getApplicationInfo().processName;
        String var2 = getCurrentProcessName(var0);
        return var1.equals(var2);
    }

    public static String getCurrentProcessName(@NonNull Application a){
        int var1 = Process.myPid();
        ActivityManager var2 = (ActivityManager)a.getSystemService("activity");
        Iterator var3 = var2.getRunningAppProcesses().iterator();

        ActivityManager.RunningAppProcessInfo var4;
        do {
            if(!var3.hasNext()) {
                return null;
            }

            var4 = (ActivityManager.RunningAppProcessInfo)var3.next();
        } while(var4.pid != var1);

        return var4.processName;
    }

}
