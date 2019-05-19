package com.augmentedreality.simplus.splash;


import com.augmentedreality.simplus.framework.ActivityScope;
import com.augmentedreality.simplus.splash.presenter.SplashPresenterModule;
import com.augmentedreality.simplus.splash.view.SplashActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface SplashModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = SplashPresenterModule.class)
    abstract SplashActivity contributeSplashActivityInjector();

}