package com.augmentedreality.simplus.user.login.presenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by denabelarde on 27/04/2018.
 */

@Module
public abstract class LoginPresenterModule {

    @Binds
    abstract LoginPresenter providePresenter(DefaultLoginPresenter defaultLoginPresenter);

}