package com.augmentedreality.simplus.util.rx;


import dagger.Binds;
import dagger.Module;

@Module
public interface RxUtilityModule {

    @Binds
    RxSchedulerUtils bindRxScheduleUtils(DefaultRxSchedulerUtils defaultRxSchedulerUtils);

}
