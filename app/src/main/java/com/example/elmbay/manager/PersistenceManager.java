package com.example.elmbay.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kgu on 5/9/18.
 */

public class PersistenceManager {
    private static final String PERSISTENCE_STORE = "v1.private";
    public static final int HOUR_TO_MILLIS = 3600000;

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_PASSWORD = "pwd";
    public static final String KEY_USER_TOKEN = "ut";
    public static final String KEY_USER_TOKEN_EXPIRATION_TIME = "ut_exp";
    public static final String KEY_PROGRESS_LOWMARK_CID = "low_cid";
    public static final String KEY_PROGRESS_LOWMARK_LID = "low_lid";
    public static final String KEY_PROGRESS_HIGHMARK_CID = "high_cid";
    public static final String KEY_PROGRESS_HIGHMARK_LID = "high_lid";
    public static final String KEY_NEXT_LOAD_TIME = "load_tm";

    public static SharedPreferences getShagetPersistenceStore(Context context) {
        return context.getSharedPreferences(PERSISTENCE_STORE, Context.MODE_PRIVATE);
    }

    public static void setString(SharedPreferences persistenceStore, String key, String value) {
        SharedPreferences.Editor editor = persistenceStore.edit();
        editor.putString(key, value);
        // apply() is async and faster than commit() as it doesn't return the operation mCurrStatus
        editor.apply();
    }

    public static void setLong(SharedPreferences persistenceStore, String key, Long value) {
        SharedPreferences.Editor editor = persistenceStore.edit();
        editor.putLong(key, value);
        // apply() is async and faster than commit() as it doesn't return the operation mCurrStatus
        editor.apply();
    }
}
