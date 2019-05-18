package com.augmentedreality.simplus.framework;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    Context provideContext(SimplusApplication application) {
        return application.getApplicationContext();
    }
}
