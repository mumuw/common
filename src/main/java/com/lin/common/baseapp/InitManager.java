package com.lin.common.baseapp;

import android.app.Application;
import android.content.Context;

import com.lin.common.reactnative.ReactNativeUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.Map;

/**
 * Created by linweilin on 2017/2/22.
 */

public class InitManager {
    private static final String TAG = "InitManager";

    public interface InitDelegate {
        void initReactNative();
        Map<String, String> getKeys();
    }

    private InitManager() {
    }

    public static boolean isInit = false;

    /**
     * step 1.
     * 初始化基础框架，在Application中调用。
     */
    public static void initInApplication(Application application, InitDelegate delegate) {
        if (application == null || delegate == null) {
            throw new IllegalArgumentException("init argument can not be null,but application:" + application + ",delegate:" + delegate);
        }
        AppUtils.getInstance().init(application);
        initImageLoader();
    }

    /**
     * step 2.
     * 在Activity中初始化配置信息
     *
     * @param delegate 初始化SDK
     */
    public static void initInActivity(Context context, final InitDelegate delegate) {
        if (context == null || delegate == null) {
            throw new IllegalArgumentException("init argument can not be null,but context:" + context + ",delegate:" + delegate);
        }
        final Application application = (Application)context.getApplicationContext();
        if (!AppUtils.isInMainProcess(application)){
            return;
        }
        if(!isInit ) {
            initInMainThread(application,delegate);
            InitManager.isInit = true;
        }
    }

    /**
     * 在主线程中执行
     */
    private static void initInMainThread(Application application, InitDelegate delegate) {
        delegate.initReactNative();                 //模块注册需放主线程(否则会报warning)
//        ReactNativeUtil.getInstance().init(application);
    }

    private static void initImageLoader(){
        File cacheDir  = StorageUtils.getCacheDirectory(AppUtils.getInstance().getContext());
        ImageLoaderConfiguration configuration =
                new ImageLoaderConfiguration.Builder(AppUtils.getInstance().getContext())
                        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 内存缓存文件的最大长宽
                        .diskCacheExtraOptions(480, 800, null)  // 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个
//                        .taskExecutor(...)
//                        .taskExecutorForCachedImages(...)
                        .threadPoolSize(3) // default  线程池内加载的数量
                        .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
                        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                        .denyCacheImageMultipleSizesInMemory()
                        .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
                        .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
                        .memoryCacheSizePercentage(13) // default
                        .diskCache(new UnlimitedDiscCache(cacheDir)) // default 可以自定义缓存路径
                        .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
                        .diskCacheFileCount(100)  // 可以缓存的文件数量
                        // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
                        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                        .imageDownloader(new BaseImageDownloader(AppUtils.getInstance().getContext())) // default
                        .imageDecoder(new BaseImageDecoder(true)) // default
                        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                        .writeDebugLogs() // 打印debug log
                        .build(); //开始构建
        ImageLoader.getInstance().init(configuration);

    }


}
