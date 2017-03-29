package com.lin.common.net;

import android.content.Context;

import com.lin.common.baseapp.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin on 2016/12/20.
 */

public class RequestManager {
    private static RequestManager requestManager = new RequestManager(AppUtils.getInstance().getContext());
    ArrayList<HttpRequest> requestList = null;

    public static RequestManager getInstance(){
        return requestManager;
    }

    public RequestManager(final Context context) {
        requestList = new ArrayList<>();
    }

    public void addRequest(final HttpRequest request) {
        requestList.add(request);
    }

    public void cancelRequest() {
        if (requestList != null && requestList.size() > 0) {
            for (HttpRequest request : requestList) {
                request.getRequest().abort();
                requestList.remove(request.getRequest());
            }
        }
    }

    public HttpRequest createRequest(final URLData urlData, final RequestCallback requestCallback) {
        return createRequest(urlData, null, requestCallback);
    }

    public HttpRequest createRequest(final URLData urlData, final List<RequestParameter> params, final RequestCallback requestCallback) {
        final HttpRequest request = new HttpRequest(urlData, params, requestCallback);
        addRequest(request);
        return request;
    }

}
