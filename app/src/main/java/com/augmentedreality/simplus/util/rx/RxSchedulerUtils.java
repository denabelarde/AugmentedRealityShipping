package com.augmentedreality.simplus.util.rx;

import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;

/**
 /**
 * Transformer for any RxJava stream so it subscribes to a separate thread and is
 * observed on the android UI thread.
 *
 *
 */
public interface RxSchedulerUtils {

    CompletableTransformer forCompletable();

    <T> FlowableTransformer<T, T> forFlowable();

    <T> ObservableTransformer<T, T> forObservable();

    <T> SingleTransformer<T, T> forSingle();
}
