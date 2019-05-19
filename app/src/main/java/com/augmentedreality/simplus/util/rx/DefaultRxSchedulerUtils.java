package com.augmentedreality.simplus.util.rx;

import javax.inject.Inject;

import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Julious Igmen
 */
class DefaultRxSchedulerUtils implements RxSchedulerUtils {

    @Inject
    public DefaultRxSchedulerUtils() {
    }

    @Override
    public CompletableTransformer forCompletable() {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread())
                                   .subscribeOn(Schedulers.io());
    }

    @Override
    public <T> FlowableTransformer<T, T> forFlowable() {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread())
                                   .subscribeOn(Schedulers.io());
    }

    @Override
    public <T> ObservableTransformer<T, T> forObservable() {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread())
                                   .subscribeOn(Schedulers.io());
    }

    @Override
    public <T> SingleTransformer<T, T> forSingle() {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread())
                                   .subscribeOn(Schedulers.io());
    }
}
