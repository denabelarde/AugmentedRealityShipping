package com.augmentedreality.simplus.util;

import android.os.Environment;

import java.io.File;

class FolderUtils {

    static String createFolderIfNotExists(String path) {
        String result = Environment.getExternalStorageDirectory().toString();
        File folder = new File(path);
        if(folder.exists()) result = path;
        else {
            if(folder.mkdirs()) result = path;
        }
        return result;
    }

}
