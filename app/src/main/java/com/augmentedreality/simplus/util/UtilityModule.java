package com.augmentedreality.simplus.util;

import android.content.Context;
import android.media.projection.MediaProjectionManager;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class UtilityModule {

    @Binds
    abstract FileUtils bindFileUtils(DefaultFileUtils defaultFileUtils);

    @Provides
    @Singleton
    static MediaProjectionManager provideMediaProjectionManager(Context context) {
        return (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    @Binds
    abstract AlertDialogBinder bindAlertDialog(DefaultAlertDialogBinder defaultAlertDialogBinder);


}
