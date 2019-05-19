package com.augmentedreality.simplus.util.rx;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

/**
 * Transforms an {@link Observable} that emits an {@link List} to instead emit the items on that
 * {@link List} one by one, not changing its type.
 *
 *
 */
public class ListToItemsTransformer<T> implements ObservableTransformer<List<T>, T> {

    @Override
    public ObservableSource<T> apply(Observable<List<T>> upstream) {
        return upstream.flatMap(
            Observable::fromIterable
        );
    }
}
