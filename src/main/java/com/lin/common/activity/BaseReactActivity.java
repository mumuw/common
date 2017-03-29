package com.lin.common.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.lin.common.baseapp.ActivityStackManager;
import com.lin.common.reactnative.ReactNativeUtil;
public class BaseReactActivity extends Activity implements DefaultHardwareBackBtnHandler {
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addActivity(this);
        mReactRootView = new ReactRootView(this);
        mReactInstanceManager = ReactNativeUtil.getInstance().getReactInstanceManager();

        // 注意这里的HelloWorld必须对应“index.android.js”中的
        // “AppRegistry.registerComponent()”的第一个参数
        mReactRootView.startReactApplication(mReactInstanceManager, "demoReact", null);

        setContentView(mReactRootView);
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }
}
