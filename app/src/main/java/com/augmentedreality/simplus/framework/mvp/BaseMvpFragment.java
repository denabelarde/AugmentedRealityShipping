package com.augmentedreality.simplus.framework.mvp;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

public abstract class BaseMvpFragment<V extends SimplusMvpView, P extends SimplusMvpPresenter<V>>
    extends MvpFragment<V, P> implements SimplusMvpView {


}
