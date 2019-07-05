package com.augmentedreality.simplus.splash.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.augmentedreality.simplus.R;
import com.augmentedreality.simplus.dashboard.view.DashboardActivity;
import com.augmentedreality.simplus.framework.mvp.SimplusMvpActivity;
import com.augmentedreality.simplus.framework.permission.RxPermissionFragment;
import com.augmentedreality.simplus.splash.presenter.SplashPresenter;
import com.augmentedreality.simplus.user.login.view.LoginActivity;
import com.augmentedreality.simplus.util.AlertDialogBinder;
import com.augmentedreality.simplus.util.GpsUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import dagger.android.AndroidInjection;


import static com.augmentedreality.simplus.util.GpsUtils.GPS_REQUEST;

public class SplashActivity extends SimplusMvpActivity<SplashView, SplashPresenter>
    implements SplashView, GoogleApiClient.OnConnectionFailedListener {

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

    private void requireGps() {
//        new GpsUtils(SplashActivity.this).turnGPSOn(isGPSEnable -> {
//            if (isGPSEnable) {
//                presenter.decideWhichScreenToRedirectTo();
//            } else {
//                alertDialogBinder.showAlertDialog(this,
//                                                  getString(R.string.gps_required),
//                                                  getString(R.string.ok),
//                                                  true,
//                                                  (dialog, which) -> finish());
//            }
//        });

        new GpsUtils(SplashActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                if (isGPSEnable) {
                    presenter.decideWhichScreenToRedirectTo();
                } else {
                    alertDialogBinder.showAlertDialog(SplashActivity.this,
                                                      getString(R.string.gps_required),
                                                      getString(R.string.ok),
                                                      true,
                                                      (dialog, which) -> finish());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.
                              RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                presenter.decideWhichScreenToRedirectTo();
            }
        }
    }

    @SuppressLint("CheckResult")
    private void checkPermissions() {
        rxPermissionFragment
            .request(PERMISSIONS)
            .subscribe(
                isGranted -> {
                    if (isGranted) {
                        requireGps();
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
    public void showGpsNotAcceptedError() {
        alertDialogBinder.showAlertDialog(this,
                                          getString(R.string.gps_required),
                                          getString(R.string.ok),
                                          true,
                                          (dialog, which) -> finish());
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

}
