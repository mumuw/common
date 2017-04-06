package com.lin.common.mvp;

/**
 * Created by lin on 2017/3/30.
 *
 * base presenter that implemented presenter.
 */

public class BasePresenter <T extends  MvpView> implements Presenter<T> {

    @Override
    public void attachView(T mvpView) {

    }

    @Override
    public void detachView() {

    }
}
