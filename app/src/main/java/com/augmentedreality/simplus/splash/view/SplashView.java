package com.augmentedreality.simplus.splash.view;


import com.augmentedreality.simplus.framework.mvp.SimplusMvpView;

public interface SplashView extends SimplusMvpView {

    void showGpsNotAcceptedError();

    void navigatetoDashboardScreen();

    void navigateToLoginScreen();

    void dismiss();

}
