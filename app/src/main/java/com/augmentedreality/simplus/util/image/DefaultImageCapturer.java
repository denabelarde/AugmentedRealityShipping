package com.augmentedreality.simplus.util.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.widget.FrameLayout;

import com.augmentedreality.simplus.widgets.CameraPreview;

import javax.inject.Inject;

import io.reactivex.Single;


import static com.augmentedreality.simplus.util.ImageUtils.getCameraInstance;
import static com.augmentedreality.simplus.util.ImageUtils.getImageRotationAngle;
import static com.augmentedreality.simplus.util.ImageUtils.rotateBitmap;

public class DefaultImageCapturer implements ImageCapturer {

    private Camera camera;

    private CameraPreview cameraPreview;

    @Inject
    public DefaultImageCapturer() {
    }

    @Override
    public Single<Camera> showCameraPreview(Activity activity, FrameLayout previewLayout) {
        return Single.create(
            emitter -> {
                camera = getCameraInstance();
                camera.setDisplayOrientation(90);
                Camera.Parameters params = camera.getParameters();
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                camera.setParameters(params);
                cameraPreview = new CameraPreview(activity, camera);
                previewLayout.addView(cameraPreview);
                emitter.onSuccess(camera);
            });
    }

    @Override
    public Single<Bitmap> capturePhoto(Activity activity, Camera camera) {
        return Single.create(
            emitter -> {
                Camera.PictureCallback mPicture = (data, c) -> {
                    try {
                        int angleToRotate =
                            getImageRotationAngle(activity, Camera.CameraInfo.CAMERA_FACING_FRONT);
                        Bitmap bitmap = rotateBitmap(
                            BitmapFactory.decodeByteArray(data, 0, data.length),
                            angleToRotate);
                        emitter.onSuccess(bitmap);
                    } catch (Exception e) {
                        emitter.onError(new Throwable());
                    }
                };
                camera.takePicture(null, null, mPicture);
            });
    }

}
