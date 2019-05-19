package com.augmentedreality.simplus.dashboard.presenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DashboardPresenterModule {

    @Binds
    abstract DashboardPresenter bindDashboard(DefaultDashboardPresenter defaultDashboardPresenter);

}
