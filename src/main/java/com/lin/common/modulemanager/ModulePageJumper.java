package com.lin.common.modulemanager;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by linweilin on 2017/3/13.
 */

public interface ModulePageJumper {
    String MODULE_CALLBACK_ID = "module_callback_id";
    String ACTIVITY_FLAGS = "activity_flags";
    /**
     * 启动页面
     * @param context 上下文
     * @param moduleId 模块名
     * @param pageId 页面ID
     * @param params 启动参数
     * @param callBackId 回调
     * @return 是否处理了传入的module/pageId的启动请求,没有就返回false,会调用其他ModulePageJumper进行处理
     */
    boolean startModulePage(Context context, String moduleId, String pageId, Bundle params, String callBackId);

}
