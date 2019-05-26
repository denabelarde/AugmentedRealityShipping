package com.augmentedreality.simplus.user.login;

import com.augmentedreality.simplus.framework.ActivityScope;
import com.augmentedreality.simplus.user.login.presenter.LoginPresenterModule;
import com.augmentedreality.simplus.user.login.view.LoginActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


/**
 * Created by denabelarde on 27/04/2018.
 */

@Module
@SuppressWarnings("squid:S1610")
public abstract class LoginModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = LoginPresenterModule.class)
    abstract LoginActivity contributeLoginActivityInjector();

}
