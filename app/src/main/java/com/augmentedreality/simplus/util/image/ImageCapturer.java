package com.augmentedreality.simplus.util.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.widget.FrameLayout;

import io.reactivex.Single;

public interface ImageCapturer {

    Single<Camera> showCameraPreview(Activity activity, FrameLayout previewLayout);

    Single<Bitmap> capturePhoto(Activity activity, Camera camera);

}
