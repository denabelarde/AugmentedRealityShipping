package com.augmentedreality.simplus.util;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUploadUtils {

    public static RequestBody toStringRequestBody (String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private static RequestBody toJPEGRequestBody(File value) {
        return RequestBody.create(MediaType.parse("image/jpeg"), value);
    }

    public static MultipartBody.Part prepareImagePart(File value) {
        return MultipartBody.Part.createFormData("photo", value.getName(), toJPEGRequestBody(value));
    }

}
