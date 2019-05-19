package com.augmentedreality.simplus.framework.mvp;

import android.os.Bundle;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import dagger.android.AndroidInjection;

public abstract class SimplusMvpActivity<V extends SimplusMvpView,
                                             P extends SimplusMvpPresenter<V>>
    extends MvpActivity<V, P>
    implements SimplusMvpView {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

}

