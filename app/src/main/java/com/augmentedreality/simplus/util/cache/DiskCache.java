package com.augmentedreality.simplus.util.cache;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Also known as SharedPreferences
 * A key-value pair cache of random values.
 */
public interface DiskCache {

    /**
     * @return a {@link Single} that emits when no value is saved with the given key
     */
    Single<String> getString(String key);

    Single<Boolean> getBoolean(String key);

    Single<Integer> getInt(String key);

    Single<String> getStringNonNull(String key);

    Single<Long> getLong(String key);

    /**
     * Saves the value with a key of the given parameter, overriding any existing values with the
     * same key.
     */
    Completable saveString(String key, String value);

    Completable saveBoolean(String key, boolean value);

    Completable saveInt(String key, int value);

    Completable saveLong(String key, long value);

    void removeKey(String key);

    Completable clearAllCache();

}

