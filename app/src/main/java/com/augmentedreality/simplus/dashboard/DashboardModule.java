package com.augmentedreality.simplus.dashboard;

import com.augmentedreality.simplus.dashboard.presenter.DashboardPresenterModule;
import com.augmentedreality.simplus.dashboard.view.DashboardActivity;
import com.augmentedreality.simplus.framework.ActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface DashboardModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = DashboardPresenterModule.class)
    abstract DashboardActivity contributeDashboardActivityInjector();

}
