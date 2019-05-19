package com.augmentedreality.simplus.dashboard.presenter;

import com.augmentedreality.simplus.dashboard.view.DashboardView;
import com.augmentedreality.simplus.framework.mvp.DefaultSimplusMvpPresenter;
import com.augmentedreality.simplus.util.rx.RxSchedulerUtils;

import javax.inject.Inject;

public class DefaultDashboardPresenter extends DefaultSimplusMvpPresenter<DashboardView>
    implements DashboardPresenter  {

    @Inject
    public DefaultDashboardPresenter(RxSchedulerUtils rxSchedulerUtils) {
        super(rxSchedulerUtils);
    }
}
