package com.augmentedreality.simplus.framework;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
    AndroidSupportInjectionModule.class,
    AppModule.class,

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
