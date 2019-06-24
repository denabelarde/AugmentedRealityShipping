package com.augmentedreality.simplus;

public class Constants {

    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";

    public static final int standardScaleSize = 512;

    public interface ACTION {
        String STARTFOREGROUND_ACTION = "com.augmentedreality.simplus.startforeground";
        String STOPFOREGROUND_ACTION = "com.augmentedreality.simplus.stopforeground";
    }

    public static final class FirebaseReference {
        public static final String USERS = "users";
    }

    public static final class DiskCacheKey {
        public static final String IS_LOGGED_IN = "IS_LOGGED_IN";
        public static final String ID_TOKEN = "ID_TOKEN";
        public static final String EMAIL = "EMAIL";
        public static final String NAME = "NAME";
        public static final String PHOTO = "PHOTO";
        public static final String PREVIOUSLY_STREAMED_APPS = "PREVIOUSLY_STREAMED_APPS";
        public static final String LAST_POPUP_DATETIME = "LAST_POPUP_DATETIME";
        public static final String SHOW_RATE_DIALOG = "SHOW_RATE_DIALOG";
        public static final String SUBS_FAILED_POPUP_SHOWN = "SUBS_FAILED_POPUP_SHOWN";
    }


}
