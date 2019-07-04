package com.augmentedreality.simplus.framework;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.squareup.leakcanary.BuildConfig;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

public class SimplusApplication extends Application
    implements HasActivityInjector, HasServiceInjector, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingSupportAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Service> dispatchingServiceAndroidInjector;


    @Override
    public void onCreate() {
        super.onCreate();
        initializeComponent().inject(this);
        if (BuildConfig.DEBUG) {
            initializeTimber();
            initializeLeakCanary();
        }
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingServiceAndroidInjector;
    }

    SimplusComponent initializeComponent() {
        return DaggerSimplusComponent.builder()
                                     .application(this)
                                     .build();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingSupportAndroidInjector;
    }

    private void initializeLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) LeakCanary.install(this);
    }

    private void initializeTimber() {
        Timber.plant(new Timber.DebugTree());
    }
}
