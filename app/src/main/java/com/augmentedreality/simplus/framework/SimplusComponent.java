package com.augmentedreality.simplus.framework;

import com.augmentedreality.simplus.dashboard.DashboardModule;
import com.augmentedreality.simplus.firebase.FirebaseModule;
import com.augmentedreality.simplus.google.auth.GoogleAuthModule;
import com.augmentedreality.simplus.splash.SplashModule;
import com.augmentedreality.simplus.user.login.LoginModule;
import com.augmentedreality.simplus.util.UtilityModule;
import com.augmentedreality.simplus.util.cache.DiskCacheModule;
import com.augmentedreality.simplus.util.rx.RxUtilityModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
    AndroidSupportInjectionModule.class,
    AppModule.class,
    UtilityModule.class,
    DashboardModule.class,
    RxUtilityModule.class,
    SplashModule.class,
    LoginModule.class,
    FirebaseModule.class,
    DiskCacheModule.class,
    GoogleAuthModule.class
})
@Singleton
public interface SimplusComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(SimplusApplication application);

        SimplusComponent build();
    }

    void inject(SimplusApplication simplusApplication);
}
