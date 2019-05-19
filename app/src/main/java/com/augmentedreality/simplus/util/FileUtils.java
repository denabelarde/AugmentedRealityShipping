package com.augmentedreality.simplus.util;

import android.net.Uri;

import java.io.File;

/**
 * Created by ronaldgalang on 14/02/2018.
 */

public interface FileUtils {

    boolean deleteFileRecursively(File fileOrDirectory);

    File createPictureFile(String fileName, String imageSubDirectory);

    File getPictureFromUri(Uri imageUri);


}
