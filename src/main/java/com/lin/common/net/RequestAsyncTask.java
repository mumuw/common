package com.lin.common.net;

import android.os.AsyncTask;

/**
 * Created by linweilin on 2016/11/8.
 */

public abstract class RequestAsyncTask extends
        AsyncTask<String,Void,Response>{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Response doInBackground(String... url) {
        return null;
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
    }


    private Response getResponseFromUrl(){

        return null;
    }
}
