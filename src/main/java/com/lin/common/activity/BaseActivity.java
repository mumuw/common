package com.lin.common.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.lin.common.baseapp.ActivityStackManager;
import com.lin.common.net.RequestCallback;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addActivity(this);
        if (getContentViewId() != 0){
            setContentView(getContentViewId());
        }
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    protected boolean needCallback;

    protected ProgressDialog dlg;

    public abstract class AbstractRequestCallback implements RequestCallback {
        public abstract void onSuccess(String content);

        @Override
        public void onFail(String errorMessage) {
            dlg.dismiss();
            new AlertDialog.Builder(BaseActivity.this).setTitle("error")
                    .setMessage(errorMessage).setPositiveButton("确定", null)
                    .show();
        }
    }
    //获取布局id
    protected abstract int getContentViewId();
    //初始化变量
    protected abstract void initVariables();
    //初始化view
    protected abstract void initViews(Bundle savedInstanceState);
    //获取数据
    protected abstract void loadData();

    @Override
    protected void onResume() {
        super.onResume();
        ActivityStackManager.getInstance().setTopActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.getInstance().finishActivity(this);
    }
}
