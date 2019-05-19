package com.augmentedreality.simplus.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;

import java.util.UUID;

public class ResourceUtils {

    public static int color(Context context, @ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(id, null);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static int getImgResUsingString(Context context, String iconId) {
        int resId = -1;
        if (!iconId.isEmpty()) {
            try {
                resId = getResourceByString(context,
                                                           iconId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resId;
    }

    public static int getResourceByString(Context context, String imageFileName) {
        return context.getResources()
                      .getIdentifier(imageFileName,
                                     "drawable", context.getPackageName());
    }


    public static String getScreenAspectRatio(Activity activity, int orientation) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(dm);

        float displayY = (float) dm.heightPixels;
        float displayX = (float) dm.widthPixels;
        float aspectRatio = 0f;
        if(displayY > displayX) { aspectRatio = displayY / displayX; }
        else { aspectRatio = displayX / displayY; }

        if (aspectRatio >= 1.333 && aspectRatio < 1.5) {
            return orientation == Configuration.ORIENTATION_LANDSCAPE ? "4:3" : "3:4";
        } else if (aspectRatio >= 1.5 && aspectRatio < 1.6) {
            return orientation == Configuration.ORIENTATION_LANDSCAPE ? "3:2" : "2:3";
        } else if (aspectRatio >= 1.6 && aspectRatio < 1.667) {
            return orientation == Configuration.ORIENTATION_LANDSCAPE ? "16:10" : "10:16";
        } else if (aspectRatio >= 1.667 && aspectRatio < 1.7) {
            return orientation == Configuration.ORIENTATION_LANDSCAPE ? "5:3" : "3:5";
        } else if (aspectRatio >= 1.7 && aspectRatio < 1.8) {
            return orientation == Configuration.ORIENTATION_LANDSCAPE ? "16:9" : "9:16";
        } else if (aspectRatio >= 1.8 && aspectRatio < 2) {
            return orientation == Configuration.ORIENTATION_LANDSCAPE ? "19:10" : "10:19";
        } else if (aspectRatio >= 2) {
            return orientation == Configuration.ORIENTATION_LANDSCAPE ? "18:9" : "9:18";
        }
        return orientation == Configuration.ORIENTATION_LANDSCAPE ? "19:10" : "10:19";
    }

    public static String getUniquePsuedoID() {
        String m_szDevIDShort =
            "35" + (Build.BOARD.length() % 10)
                + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10)
                + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10)
                + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = null;
        try {
            serial = Build.class.getField("SERIAL")
                                           .get(null)
                                           .toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial"; // some value
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static void resetSearchView(SearchView searchView) {
        searchView.setQuery("",false);
        searchView.setIconified(false);
        searchView.clearFocus();
    }
}
