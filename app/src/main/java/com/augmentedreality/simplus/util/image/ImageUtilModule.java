package com.augmentedreality.simplus.util.image;

import dagger.Binds;
import dagger.Module;

@Module
public interface ImageUtilModule {

    @Binds
    ImageCapturer bindImageCapturer(DefaultImageCapturer screenshotUtil);

}
