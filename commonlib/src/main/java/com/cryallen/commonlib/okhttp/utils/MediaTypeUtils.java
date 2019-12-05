package com.cryallen.commonlib.okhttp.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import okhttp3.MediaType;

/**
 * MediaType类
 */
public final class MediaTypeUtils {
    /** MediaType String */
    public static final String APPLICATION_JSON_DEFAULT     = "application/json";
    public static final String APPLICATION_OCTET_STREAM     = "application/octet-stream";
    public static final String APPLICATION_JSON             = "application/json; charset=utf-8";
    public static final String APPLICATION_FORM             = "application/x-www-form-urlencoded";

    /** MediaType */
    public static final MediaType JSON = MediaType.parse(APPLICATION_JSON);
    public static final MediaType OCTET = MediaType.parse(APPLICATION_OCTET_STREAM);
    public static final MediaType FORM = MediaType.parse(APPLICATION_FORM);
    public static final MediaType DEFAULT = MediaType.parse(APPLICATION_JSON_DEFAULT);

    /**
     * 判断两个 MediaType 是否相等，只判断 type 和 subType。
     */
    public static boolean equals(@NonNull MediaType first, @NonNull MediaType second){
        String first_type_subType = first.type().concat(first.subtype());
        String second_type_subType = second.type().concat(second.subtype());

        return TextUtils.equals(first_type_subType, second_type_subType);
    }

    public static boolean isJSON(@NonNull MediaType mediaType){
        return equals(mediaType, JSON);
    }

    public static boolean isOCTET(@NonNull MediaType mediaType){
        return equals(mediaType, OCTET);
    }

    public static boolean isFORM(@NonNull MediaType mediaType){
        return equals(mediaType, FORM);
    }
}
