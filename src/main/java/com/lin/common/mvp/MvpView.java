package com.lin.common.mvp;

/**
 * Created by lin on 2017/3/30.
 *
 * Base interface of mvp View
 */

public interface MvpView {
    /**
     * error
     * @param e
     */
    void onFailure(Throwable e);
}
