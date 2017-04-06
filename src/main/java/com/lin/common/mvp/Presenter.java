package com.lin.common.mvp;

/**
 * Created by lin on 2017/3/30.
 *
 * Base presenter of mvp.
 */

public interface Presenter <V extends MvpView>{

    void attachView(V mvpView);

    void detachView();
}
