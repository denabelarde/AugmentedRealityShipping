package com.augmentedreality.simplus.splash.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.augmentedreality.simplus.R;
import com.augmentedreality.simplus.dashboard.view.DashboardActivity;
import com.augmentedreality.simplus.framework.mvp.SimplusMvpActivity;
import com.augmentedreality.simplus.framework.permission.RxPermissionFragment;
import com.augmentedreality.simplus.splash.presenter.SplashPresenter;
import com.augmentedreality.simplus.user.login.view.LoginActivity;
import com.augmentedreality.simplus.util.AlertDialogBinder;
import com.augmentedreality.simplus.util.GpsUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SplashActivity extends SimplusMvpActivity<SplashView, SplashPresenter>
    implements SplashView, GoogleApiClient.OnConnectionFailedListener  {

    @Inject
    SplashPresenter presenter;

    @Inject
    AlertDialogBinder alertDialogBinder;

    private GoogleApiClient googleApiClient;
//
//    @Inject
//    GpsUtils gpsUtils;

    private RxPermissionFragment rxPermissionFragment;

    private static final String[] PERMISSIONS =
        new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initializeGoogleApiClient();
        setUpRxPermission();
        checkPermissions();
    }

    private void setUpRxPermission() {
        final FragmentManager fm = getSupportFragmentManager();
        rxPermissionFragment =
            (RxPermissionFragment) fm.findFragmentByTag(RxPermissionFragment.TAG);
        if (rxPermissionFragment == null) {
            rxPermissionFragment = RxPermissionFragment.newInstance();
            fm.beginTransaction()
              .add(rxPermissionFragment, RxPermissionFragment.TAG)
              .commitAllowingStateLoss();
            fm.executePendingTransactions();
        }
    }

    private void checkPermissions() {
        rxPermissionFragment
            .request(PERMISSIONS)
            .subscribe(
                isGranted -> {
                    if (isGranted) {
                        new GpsUtils(SplashActivity.this).turnGPSOn(isGPSEnable -> {
//                            if (isGPSEnable) {
//                                presenter.decideWhichScreenToRedirectTo();
//                            } else {
//                                alertDialogBinder.showAlertDialog(this,
//                                                                  getString(R.string.accept_permission),
//                                                                  getString(R.string.ok),
//                                                                  true,
//                                                                  (dialog, which) -> dialog.dismiss());
//                            }
                        });
                        presenter.decideWhichScreenToRedirectTo();
                    } else {
                        alertDialogBinder.showAlertDialog(this,
                                                          getString(R.string.accept_permission),
                                                          getString(R.string.ok),
                                                          true,
                                                          (dialog, which) -> dialog.dismiss());
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @NonNull
    @Override
    public SplashPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void navigatetoDashboardScreen() {
        startActivity(new Intent(this, DashboardActivity.class)
                          .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    @Override
    public void navigateToLoginScreen() {
        startActivity(new Intent(this, LoginActivity.class)
                          .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    @Override
    public void dismiss() {
        finish();
    }

    private void initializeGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                              .enableAutoManage(this, this)
                              .addApi(AppInvite.API)
                              .build();
    }
}
