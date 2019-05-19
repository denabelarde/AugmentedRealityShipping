package com.augmentedreality.simplus.util.cache;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DiskCacheModule {

    @Binds
    abstract DiskCache provideDiskCache(
        DefaultDiskCache defaultSharedPrefsUserLoader);
}
