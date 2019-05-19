package com.augmentedreality.simplus.framework.mvp;

import com.augmentedreality.simplus.util.rx.RxSchedulerUtils;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import io.reactivex.disposables.Disposable;

public class DefaultSimplusMvpPresenter<V extends SimplusMvpView>
    extends MvpBasePresenter<V>
    implements SimplusMvpPresenter<V> {

    private Disposable disposable;

    private RxSchedulerUtils rxSchedulerUtils;

    private SimplusMvpView view;

    public DefaultSimplusMvpPresenter(RxSchedulerUtils rxSchedulerUtils
    ) {
        this.rxSchedulerUtils = rxSchedulerUtils;
    }

    @Override
    public void attachView(SimplusMvpView view) {
        this.view = view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        dispose();
    }

    private void dispose() {
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }




}
