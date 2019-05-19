package com.augmentedreality.simplus.splash.presenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SplashPresenterModule {

    @Binds
    abstract SplashPresenter bindSplashPresenter(DefaultSplashPresenter defaultSplashPresenter);

}
