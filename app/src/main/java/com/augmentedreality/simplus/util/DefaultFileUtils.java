package com.augmentedreality.simplus.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


import com.augmentedreality.simplus.BuildConfig;

import java.io.File;

import javax.inject.Inject;

public class DefaultFileUtils implements FileUtils {

    private Context context;

    //    private static final String TRADIE_DIR = "/SitebookTradie/JobPhotos";
    private static final String IMAGE_FILE_EXTENSION = ".png";

    @Inject
    public DefaultFileUtils(Context context) {
        this.context = context;
    }


    @Override
    public boolean deleteFileRecursively(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteFileRecursively(child);

        return fileOrDirectory.delete();
    }

    @Override
    public File createPictureFile(String fileName, String imageSubDirectory) {
        return new File(getPictureDirectory(imageSubDirectory), fileName + IMAGE_FILE_EXTENSION);
    }

    @Override
    public File getPictureFromUri(Uri imageUri) {
        String path = null;
        if (imageUri.getScheme()
                    .equalsIgnoreCase("content")) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver()
                                   .query(imageUri, projection, null, null, null);
            int columnIndex = cursor
                                  .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) path = cursor.getString(columnIndex);
            cursor.close();
        } else
            if (imageUri.getScheme()
                        .equalsIgnoreCase("file")) {
                path = imageUri.getPath();
            }
        return new File(path);
    }

    private static File getPictureDirectory(String imageSubDirectory) {
        final File directory = new File(Environment.getExternalStorageDirectory()
                                            + "/Android/data/"
                                            + BuildConfig.APPLICATION_ID
                                            + "/" + imageSubDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }
}
