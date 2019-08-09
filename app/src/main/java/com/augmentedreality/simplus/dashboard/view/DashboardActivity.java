package com.augmentedreality.simplus.dashboard.view;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedreality.simplus.BuildConfig;
import com.augmentedreality.simplus.Constants;
import com.augmentedreality.simplus.R;
import com.augmentedreality.simplus.dashboard.OnAzimuthChangedListener;
import com.augmentedreality.simplus.dashboard.OnLocationChangedListener;
import com.augmentedreality.simplus.dashboard.model.AugmentedPOI;
import com.augmentedreality.simplus.dashboard.model.MyCurrentAzimuth;
import com.augmentedreality.simplus.dashboard.model.MyCurrentLocation;
import com.augmentedreality.simplus.dashboard.presenter.DashboardPresenter;
import com.augmentedreality.simplus.firebase.FirebaseManager;
import com.augmentedreality.simplus.framework.mvp.SimplusMvpActivity;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;
import timber.log.Timber;

public class DashboardActivity extends SimplusMvpActivity<DashboardView, DashboardPresenter>
    implements DashboardView,
               SurfaceHolder.Callback,
               OnLocationChangedListener,
               OnAzimuthChangedListener {

    @Inject
    DashboardPresenter presenter;

    @BindView(R.id.latitude) EditText latitude;

    @BindView(R.id.longhitude) EditText longhitude;

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private boolean isCameraviewOn = false;
    private AugmentedPOI mPoi;

    private double mAzimuthReal = 0;
    private double mAzimuthTeoretical = 0;
    private static double AZIMUTH_ACCURACY = 5;
    private double mMyLatitude = 0;
    private double mMyLongitude = 0;
    private boolean isFirstStart = true;

    private MyCurrentAzimuth myCurrentAzimuth;
    private MyCurrentLocation myCurrentLocation;

    TextView descriptionTextView;
    ImageView pointerIcon;

    private Unbinder unbinder;

    private FirebaseUser firebaseUser;

    private GeoFire geoFire;

    private GeoQuery geoQuery;

    public GeoQueryEventListener geoQueryEventListener;

    private HashMap<String, Double> azimuthTheoriticalMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_view);
        unbinder = ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        firebaseUser = FirebaseAuth.getInstance()
                                   .getCurrentUser();
        setupGeofire();
        setupListeners();
        setupLayout();
        setDefaultAugmentedRealityPoint();
    }

    @OnClick(R.id.setCoordinatesButton)
    public void onClickLogin() {
        if (!latitude.getText()
                     .toString()
                     .isEmpty()
                && !longhitude.getText()
                              .toString()
                              .isEmpty()) {
            mPoi = new AugmentedPOI(
                "Simplus",
                "Simplus Shipping",
                Double.parseDouble(latitude.getText()
                                           .toString()),
                Double.parseDouble(longhitude.getText()
                                             .toString()));
            stopListeners();
            startListeners();
        } else {
            Toast.makeText(this, "Empty fields not allowed", Toast.LENGTH_SHORT)
                 .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }

        if (geoQuery != null && geoQueryEventListener != null) {
            geoQuery.removeAllListeners();
        }
    }

    @NonNull
    @Override
    public DashboardPresenter createPresenter() {
        return presenter;
    }


    private void setDefaultAugmentedRealityPoint() {
        mPoi = new AugmentedPOI(
            "Simplus",
            "Simplus",
            10.8210111,
            122.5298993
        );
    }

    public double calculateTheoreticalAzimuth() {
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
        updateDescription();
        FirebaseManager.updateShipLocation(firebaseUser.getUid(), mMyLatitude, mMyLongitude);
        reinstantiateGeofire(mMyLatitude, mMyLongitude);
    }

    @Override
    public void onAzimuthChanged(float azimuthChangedFrom, float azimuthChangedTo) {
        mAzimuthReal = azimuthChangedTo;

        pointerIcon = findViewById(R.id.icon);

        for (Map.Entry<String, Double> entry : azimuthTheoriticalMap.entrySet()) {
            double minAngle = calculateAzimuthAccuracy(entry.getValue()).get(0);
            double maxAngle = calculateAzimuthAccuracy(entry.getValue()).get(1);
            if (isBetween(minAngle, maxAngle, mAzimuthReal)) {
                pointerIcon.setVisibility(View.VISIBLE);
            } else {
                pointerIcon.setVisibility(View.INVISIBLE);
            }
        }

        updateDescription();
    }



    @Override
    protected void onStop() {
        stopListeners();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstStart) {
            startListeners();
        }
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

    private void stopListeners() {
        if (myCurrentAzimuth != null) {
            myCurrentAzimuth.stop();
        }

        if (myCurrentLocation != null) {
            myCurrentLocation.stop();
        }
    }

    private void setupLayout() {
        descriptionTextView = findViewById(R.id.cameraTextView);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        SurfaceView surfaceView = findViewById(R.id.cameraview);
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

    private void setupGeofire() {
        geoFire = new GeoFire(FirebaseDatabase.getInstance()
                                              .getReferenceFromUrl
                                                   (BuildConfig.FIREBASE_URL
                                                        + Constants.FirebaseReference.SHIP_LOCATIONS));
        geoQueryEventListener = new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Timber.d("onKeyEntered + key ---> " + key);
                mPoi = new AugmentedPOI(
                    key,
                    key,
                    location.latitude,
                    location.longitude
                );
                azimuthTheoriticalMap.put(key, calculateTheoreticalAzimuth());
            }

            @Override
            public void onKeyExited(String key) {
                azimuthTheoriticalMap.remove(key);
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                mPoi = new AugmentedPOI(
                    key,
                    key,
                    location.latitude,
                    location.longitude
                );
                azimuthTheoriticalMap.put(key, calculateTheoreticalAzimuth());
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        };
    }

    private void reinstantiateGeofire(double latitude, double longhitude) {
        if (geoQuery != null && geoQueryEventListener != null) {
            geoQuery.removeAllListeners();
        }
        geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longhitude), 20);
        geoQuery.addGeoQueryEventListener(geoQueryEventListener);
    }

    private void updateTargetDetails() {

    }
}
