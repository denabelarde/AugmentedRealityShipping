package com.augmentedreality.simplus.util;

import android.os.Build;

public class OsVersion {

    public static boolean atLeastLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
