package cz.folprechtova.hides.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class JSONUtils {

    @Nullable
    public static <T> List<T> getListFromJson(String json, Class<T[]> clazz) {
        if (TextUtils.isEmpty(json)) { //pokud jsme poslali prázdný text, nechceme se o překlad pokoušet
            return null;
        } else {
            return Arrays.asList(new Gson().fromJson(json, clazz)); //instance Gsonu sama dle zaslané třídy přeloží JSON na objekty
        }
    }
}
