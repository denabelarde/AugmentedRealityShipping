package com.augmentedreality.simplus.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Utility class for converting various types from and to {@link String}.
 */
public class ConverterUtil {

    private static Gson gson;

    private ConverterUtil() { }

    public static <T> T deserialise(String serialisedType, Type type) {
        return getGson().fromJson(serialisedType, type);
    }

    public static <T> String serialise(T type) {
        return getGson().toJson(type);
    }

    private static Gson getGson() {
        if (gson == null) gson = new GsonBuilder().create();
        return gson;
    }
}
