package com.augmentedreality.simplus.dashboard.view;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedreality.simplus.R;
import com.augmentedreality.simplus.dashboard.model.AugmentedPOI;
import com.augmentedreality.simplus.dashboard.model.MyCurrentAzimuth;
import com.augmentedreality.simplus.dashboard.model.MyCurrentLocation;
import com.augmentedreality.simplus.dashboard.OnAzimuthChangedListener;
import com.augmentedreality.simplus.dashboard.OnLocationChangedListener;
import com.augmentedreality.simplus.framework.permission.RxPermissionFragment;
import com.augmentedreality.simplus.util.GpsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements
                                                         SurfaceHolder.Callback,
                                                         OnLocationChangedListener,
                                                         OnAzimuthChangedListener {

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private boolean isCameraviewOn = false;
    private AugmentedPOI mPoi;

    private double mAzimuthReal = 0;
    private double mAzimuthTeoretical = 0;
    private static double AZIMUTH_ACCURACY = 5;
    private double mMyLatitude = 0;
    private double mMyLongitude = 0;

    private MyCurrentAzimuth myCurrentAzimuth;
    private MyCurrentLocation myCurrentLocation;

    TextView descriptionTextView;
    ImageView pointerIcon;

    private RxPermissionFragment rxPermissionFragment;

    private static final String[] PERMISSIONS =
        new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setUpRxPermission();


        rxPermissionFragment
            .request(PERMISSIONS)
            .subscribe(
                isGranted -> {
                    if (isGranted) {
                        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
                            @Override
                            public void gpsStatus(boolean isGPSEnable) {
                                // turn on GPS
                            }
                        });
                        setupListeners();
                        setupLayout();
                        setAugmentedRealityPoint();
                        startListeners();
                    } else {

                    }
                });


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

    private void setAugmentedRealityPoint() {
        mPoi = new AugmentedPOI(
            "Kościół Marciacki",
            "Kościół Marciacki w Krakowie",
            10.8314471,
            122.5347356
        );
    }

    public double calculateTeoreticalAzimuth() {
        double dX = mPoi.getPoiLatitude() - mMyLatitude;
        double dY = mPoi.getPoiLongitude() - mMyLongitude;

        double phiAngle;
        double tanPhi;
        double azimuth = 0;

        tanPhi = Math.abs(dY / dX);
        phiAngle = Math.atan(tanPhi);
        phiAngle = Math.toDegrees(phiAngle);

        if (dX > 0 && dY > 0) { // I quater
            return azimuth = phiAngle;
        } else
            if (dX < 0 && dY > 0) { // II
                return azimuth = 180 - phiAngle;
            } else
                if (dX < 0 && dY < 0) { // III
                    return azimuth = 180 + phiAngle;
                } else
                    if (dX > 0 && dY < 0) { // IV
                        return azimuth = 360 - phiAngle;
                    }

        return phiAngle;
    }

    private List<Double> calculateAzimuthAccuracy(double azimuth) {
        double minAngle = azimuth - AZIMUTH_ACCURACY;
        double maxAngle = azimuth + AZIMUTH_ACCURACY;
        List<Double> minMax = new ArrayList<Double>();

        if (minAngle < 0)
            minAngle += 360;

        if (maxAngle >= 360)
            maxAngle -= 360;

        minMax.clear();
        minMax.add(minAngle);
        minMax.add(maxAngle);

        return minMax;
    }

    private boolean isBetween(double minAngle, double maxAngle, double azimuth) {
        if (minAngle > maxAngle) {
            if (isBetween(0, maxAngle, azimuth) && isBetween(minAngle, 360, azimuth))
                return true;
        } else {
            if (azimuth > minAngle && azimuth < maxAngle)
                return true;
        }
        return false;
    }

    private void updateDescription() {
        descriptionTextView.setText(mPoi.getPoiName() + " azimuthTeoretical "
                                        + mAzimuthTeoretical + " azimuthReal " + mAzimuthReal +
                                        " latitude "
                                        + mMyLatitude + " longitude " + mMyLongitude);
    }

    @Override
    public void onLocationChanged(Location location) {
        mMyLatitude = location.getLatitude();
        mMyLongitude = location.getLongitude();
        mAzimuthTeoretical = calculateTeoreticalAzimuth();
        Toast.makeText(this,
                       "latitude: " + location.getLatitude() + " longitude: " + location.getLongitude(),
                       Toast.LENGTH_SHORT)
             .show();
        updateDescription();
    }

    @Override
    public void onAzimuthChanged(float azimuthChangedFrom, float azimuthChangedTo) {
        mAzimuthReal = azimuthChangedTo;
        mAzimuthTeoretical = calculateTeoreticalAzimuth();

        pointerIcon = findViewById(R.id.icon);

        double minAngle = calculateAzimuthAccuracy(mAzimuthTeoretical).get(0);
        double maxAngle = calculateAzimuthAccuracy(mAzimuthTeoretical).get(1);

        if (isBetween(minAngle, maxAngle, mAzimuthReal)) {
            pointerIcon.setVisibility(View.VISIBLE);
        } else {
            pointerIcon.setVisibility(View.INVISIBLE);
        }

        updateDescription();
    }

    @Override
    protected void onStop() {
        if (myCurrentAzimuth != null) {
            myCurrentAzimuth.stop();
        }

        if (myCurrentLocation != null) {
            myCurrentLocation.stop();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startListeners();
    }

    private void setupListeners() {
        myCurrentLocation = new MyCurrentLocation(this);
        myCurrentLocation.buildGoogleApiClient(this);
        myCurrentLocation.start();

        myCurrentAzimuth = new MyCurrentAzimuth(this, this);
        myCurrentAzimuth.start();
    }

    private void startListeners() {
        if (myCurrentAzimuth != null) {
            myCurrentAzimuth.start();
        }
        if (myCurrentLocation != null) {
            myCurrentLocation.start();
        }
    }

    private void setupLayout() {
        descriptionTextView = (TextView) findViewById(R.id.cameraTextView);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cameraview);
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (isCameraviewOn) {
            mCamera.stopPreview();
            isCameraviewOn = false;
        }

        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
                isCameraviewOn = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        isCameraviewOn = false;
    }
}
