package com.augmentedreality.simplus.splash.presenter;


import com.augmentedreality.simplus.framework.mvp.SimplusMvpPresenter;
import com.augmentedreality.simplus.splash.view.SplashView;

public interface SplashPresenter extends SimplusMvpPresenter<SplashView> {

    void decideWhichScreenToRedirectTo();

}
