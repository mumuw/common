package com.lin.common.simplenet;

import com.lin.common.net.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linweilin on 2016/11/9.
 */

public abstract class Request<T> implements Comparable<Request<T>> {

    //请求类型枚举
    public static enum HttpMethod {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        /** http request type */
        private String mHttpMethod = "";

        private HttpMethod(String method) {
            mHttpMethod = method;
        }

        @Override
        public String toString() {
            return mHttpMethod;
        }
    }

    //请求优先级枚举
    public static enum Priority{
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    /**
     * 网路请求监听，会被执行在ui线程
     * @param <T> 请求的response类型
     */
    public static interface RequestLinstener<T>{
        public void onComplete(int stCode,T response,String errMsg);
    }

    //默认的post put 的编码格式
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
    //请求序列号
    protected int mSerialNum = 0;
    //默认优先级
    protected Priority mPriority = Priority.NORMAL;
    //是否取消该请求
    protected boolean isCancel = false;
    //请求监听
    protected RequestLinstener<T> mRequestListener;
    //请求的url
    private String mUrl = "";
    //请求的方法
    HttpMethod mHttpMethod = HttpMethod.GET;
    //请求头
    private Map<String,String> mHeaders = new HashMap<String,String>();
    //请求参数
    private Map<String,String> mBodyParams = new HashMap<String,String>();


    public Request(HttpMethod method,String url,RequestLinstener<T> linstener) {
        this.mHttpMethod = method;
        this.mUrl = url;
        this.mRequestListener = linstener;
    }

    //从原生的网络请求中解析结果，子类覆写
    public abstract T parseResponse(Response response);

    public Request() {
        super();
    }

    /**
     * 处理Response，该方法运行在主线程
     * @param response
     */
    public final void deliveryResponse(Response response){
        T result = parseResponse(response);
        if (mRequestListener != null){
//            int stCode = response != null?
        }
    }
}
