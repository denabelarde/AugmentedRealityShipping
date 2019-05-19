package com.augmentedreality.simplus.util.cache;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;


class DefaultDiskCache implements DiskCache {

    private static final String CACHE = "cache";

    private SharedPreferences sharedPreferences;

    private Context context;

    @Inject
    DefaultDiskCache(Context context) {
        sharedPreferences = context.getSharedPreferences(CACHE, Context.MODE_PRIVATE);
        this.context = context;
    }

    @Override
    public Single<String> getString(String key) {
        return Single.defer(
            () -> {
                String value = sharedPreferences.getString(key, "");
                if (value.isEmpty()) {
                    String errorMessage = "No cached text with key " + key;
                    Timber.w(errorMessage);
                    return Single.error(new Throwable(errorMessage));
                }
                return Single.just(value);
            }
        );
    }

    @Override
    public Single<Boolean> getBoolean(String key) {
        return Single.defer(() -> Single.just(sharedPreferences.getBoolean(key, false)));
    }

    @Override
    public Single<Integer> getInt(String key) {
        return Single.defer(() -> Single.just(sharedPreferences.getInt(key, 0)));
    }

    @Override
    public Single<Long> getLong(String key) {
        return Single.defer(() -> Single.just(sharedPreferences.getLong(key, 0)));
    }

    @Override
    public Completable saveString(String key, String value) {
        return Completable.fromCallable(
            () -> {
                sharedPreferences.edit()
                                 .putString(key, value)
                                 .apply();
                return Completable.complete();
            }
        );
    }

    @Override
    public Completable saveBoolean(String key, boolean value) {
        return Completable.fromCallable(
            () -> {
                sharedPreferences.edit()
                                 .putBoolean(key, value)
                                 .apply();
                return Completable.complete();
            }
        );
    }

    @Override
    public Completable saveInt(String key, int value) {
        return Completable.fromCallable(
            () -> {
                sharedPreferences.edit()
                                 .putInt(key, value)
                                 .apply();
                return Completable.complete();
            }
        );
    }

    @Override
    public Completable saveLong(String key, long value) {
        return Completable.fromCallable(
            () -> {
                sharedPreferences.edit()
                                 .putLong(key, value)
                                 .apply();
                return Completable.complete();
            }
        );
    }

    @Override
    public Single<String> getStringNonNull(String key) {
        return Single.defer(
            () -> {
                String value = sharedPreferences.getString(key, "");
                if (value.isEmpty()) {
                    return Single.just("");
                }
                return Single.just(value);
            }
        );
    }

    @Override
    public void removeKey(String key) {
        sharedPreferences.edit()
                         .remove(key)
                         .apply();
    }

    @Override
    public Completable clearAllCache() {
        return Completable.fromCallable(
            () -> {
                sharedPreferences.edit()
                                 .clear()
                                 .apply();
                return Completable.complete();
            }
        );
    }
}
