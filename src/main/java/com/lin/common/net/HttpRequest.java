package com.lin.common.net;

import android.os.Handler;

import com.alibaba.fastjson.JSON;
import com.lin.common.utils.BaseUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linweilin on 2016/11/8.
 */

public class HttpRequest implements Runnable {

    private HttpUriRequest request = null;
    private URLData urlData = null;
    private List<RequestParameter> parameters = null;
    private RequestCallback requestCallback = null;
    private String url;
    private HttpResponse response = null;
    private DefaultHttpClient httpClient;

    protected Handler handler;

    public HttpRequest(final URLData data,
                       final List<RequestParameter> params,
                       final RequestCallback callBack) {
        try {
            urlData = data;
            url = urlData.getUrl();
            this.parameters = params;
            this.requestCallback = callBack;

            if (httpClient == null) {
                httpClient = new DefaultHttpClient();
            }

            handler = new Handler();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HttpUriRequest getRequest() {
        return request;
    }

    @Override
    public void run() {
        try {
            if (urlData.getNetType().equals("get")) {
                final StringBuffer paramBuffer = new StringBuffer();
                if (parameters != null && parameters.size() > 0) {
                    for (final RequestParameter p : parameters) {
                        paramBuffer.append(paramBuffer.length() == 0 ? "" : "&" + p.getName() + "=" + BaseUtils.UrlEncodeUnicode(p.getValue()));
                    }
                    url = url + "?" + paramBuffer.toString();
                }
                request = new HttpGet(url);
            } else if (urlData.getNetType().equals("post")) {
                request = new HttpPost(url);
                if (parameters != null && parameters.size() > 0) {
                    final List<BasicNameValuePair> list = new ArrayList<>();
                    for (final RequestParameter p : parameters) {
                        list.add(new BasicNameValuePair(p.getName(), p.getValue()));
                    }
                    ((HttpPost) request).setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
                }
            } else {
                return;
            }
            request.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            request.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);

            response = httpClient.execute(request);

            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                final ByteArrayOutputStream content = new ByteArrayOutputStream();
                response.getEntity().writeTo(content);
                String strResponse = new String(content.toByteArray()).trim();

                if (requestCallback != null) {
                    final Response responseInJson = JSON.parseObject(strResponse, Response.class);
                    if (responseInJson != null && responseInJson.isError()) {
                        handleNetworkError(responseInJson.getErrorMessage());
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                HttpRequest.this.requestCallback.onSuccess(responseInJson.getResult());
                            }
                        });
                    }
                } else {
                    handleNetworkError("网络异常");
                }
            } else {
                handleNetworkError("网络异常");
            }


        } catch (UnsupportedEncodingException u) {
            handleNetworkError("网络异常");
        } catch (IOException i) {
            handleNetworkError("网络异常");
        }
    }

    public void handleNetworkError(final String errorMsg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                HttpRequest.this.requestCallback.onFail(errorMsg);
            }
        });
    }


}
