package com.lin.common.reactnative;

import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.ModuleSpec;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.views.viewpager.ReactViewPagerManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

/**
 * Created by linweilin on 2017/2/21.
 */

public class ReactNativePackage extends MainReactPackage {

    private static List<Class> nativeModules = new ArrayList<>();
    private static List<Class> viewManagers = new ArrayList<>();
    public static void registerNativeModule(Class nativeModule) {
        if (nativeModules.contains(nativeModule))
            return;
        nativeModules.add(nativeModule);
    }

    public void registerViewManager(Class viewManager) {
        if (viewManagers.contains(viewManager))
            return;
        viewManagers.add(viewManager);
    }

    private <T> T createModule(Class clazz, ReactApplicationContext context) {
        try {
            Constructor constructor = clazz.getConstructor(ReactApplicationContext.class);
            return (T) constructor.newInstance(context);
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    public List<ModuleSpec> getNativeModules(final ReactApplicationContext context) {
        List<ModuleSpec>superModuleList = super.getNativeModules(context);
        List<ModuleSpec>result = new ArrayList<>(superModuleList);

        for (int i = 0; i < nativeModules.size(); i++) {
            final Class clazz = nativeModules.get(i);
            result.add(new ModuleSpec(clazz, new Provider<NativeModule>(){
                public NativeModule get(){
                    return createModule(clazz, context);
                }
            }));
        }
        return result;
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        List<Class<? extends JavaScriptModule>> javaScriptModules = super.createJSModules();
        List<Class<? extends JavaScriptModule>> list = new ArrayList<>();
        list.addAll(javaScriptModules);
        return list;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        List<ViewManager> viewManagerList = super.createViewManagers(reactApplicationContext);
        List<ViewManager> list = new ArrayList<>();
        for (ViewManager viewManager : viewManagerList) {
            if (viewManager instanceof ReactViewPagerManager) {
                continue;
            }
            list.add(viewManager);
        }
        for (int i = 0; i < viewManagers.size(); i++) {
            ViewManager viewManager = createModule(viewManagers.get(i), reactApplicationContext);
            if (viewManager != null)
                list.add(viewManager);
        }

        return list;
    }

}
