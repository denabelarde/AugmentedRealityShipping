package com.augmentedreality.simplus.splash.presenter;


import com.augmentedreality.simplus.framework.mvp.DefaultSimplusMvpPresenter;
import com.augmentedreality.simplus.splash.view.SplashView;
import com.augmentedreality.simplus.util.rx.RxSchedulerUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

public class DefaultSplashPresenter extends DefaultSimplusMvpPresenter<SplashView> implements
                                                                                   SplashPresenter {

    private RxSchedulerUtils rxSchedulerUtils;
    private SplashView view;
    private Disposable disposable;
    private FirebaseAuth firebaseAuth;

    @Inject
    public DefaultSplashPresenter(RxSchedulerUtils rxSchedulerUtils,
                                  FirebaseAuth firebaseAuth) {
        super(rxSchedulerUtils);
        this.rxSchedulerUtils = rxSchedulerUtils;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void attachView(SplashView view) {
        super.attachView(view);
        this.view = view;
    }


    @Override
    public void decideWhichScreenToRedirectTo() {
        disposable = Completable.complete()
                                .delay(2, TimeUnit.SECONDS)
                                .andThen(checkIfUserHasExistingFirebaseToken())
                                .doOnError(throwable -> redirectToDashboardScreen()     )
                                .compose(rxSchedulerUtils.forCompletable())
                                .subscribe(view::dismiss);
    }

    private Completable checkIfUserHasExistingFirebaseToken() {
        if (firebaseAuth.getCurrentUser() == null) {
            return redirectToLoginScreen();
        } else {
            return Completable.error(new Throwable());
        }
    }

    private Completable redirectToDashboardScreen() {
        return Completable.complete()
                          .doOnComplete(view::navigatetoDashboardScreen);
    }

    private Completable redirectToLoginScreen() {
        return Completable.complete()
                          .doOnComplete(view::navigateToLoginScreen);
    }

    void dispose() {
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }

    @Override
    public void detachView(boolean retainInstance) {
        dispose();
    }
}
