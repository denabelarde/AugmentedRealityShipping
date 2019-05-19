package com.augmentedreality.simplus.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Base64;
import android.view.Surface;


import com.augmentedreality.simplus.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import static com.augmentedreality.simplus.util.FolderUtils.createFolderIfNotExists;

public class ImageUtils {

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    public static int getImageRotationAngle(Activity mContext, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = mContext.getWindowManager()
                               .getDefaultDisplay()
                               .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap resizeImageForImageView(Bitmap bitmap, int scaleSize) {
        Bitmap resizedBitmap;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int newWidth = -1;
        int newHeight = -1;
        float multFactor;
        if (originalHeight > originalWidth) {
            newHeight = scaleSize;
            multFactor = (float) originalWidth / (float) originalHeight;
            newWidth = (int) (newHeight * multFactor);
        } else
            if (originalWidth > originalHeight) {
                newWidth = scaleSize;
                multFactor = (float) originalHeight / (float) originalWidth;
                newHeight = (int) (newWidth * multFactor);
            } else
                if (originalHeight == originalWidth) {
                    newHeight = scaleSize;
                    newWidth = scaleSize;
                }
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        return resizedBitmap;
    }

    public static File convertBitmapToFile(Bitmap bitmap, String folder) {
        File result = new File(createFolderIfNotExists(
                Environment.getExternalStorageDirectory()
                        + "/Android/data/"
                        + BuildConfig.APPLICATION_ID
                        + File.separator
                        + folder),
                System.currentTimeMillis() + ".jpg");

        try {
            result.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            assert fos != null;
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
