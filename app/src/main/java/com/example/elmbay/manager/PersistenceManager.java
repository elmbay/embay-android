package com.example.elmbay.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * Created by kgu on 5/9/18.
 */

class PersistenceManager {
    // persistence keys
    static final String KEY_UID = "uid";
    static final String KEY_UID_TYPE = "uid_type";
    static final String KEY_PASSWORD = "pwd";
    static final String KEY_USER_TOKEN = "ut";
    static final String KEY_USER_TOKEN_EXPIRATION_TIME = "ut_exp";
    static final String KEY_PROGRESS_HIGHMARK_CSID = "high_csid";
    static final String KEY_PROGRESS_HIGHMARK_CID = "high_cid";
    static final String KEY_PROGRESS_HIGHMARK_LID = "high_lid";

    private static final String PERSISTENCE_STORE = "v1.private";

    static SharedPreferences getSharedPersistenceStore(Context context) {
        return context.getSharedPreferences(PERSISTENCE_STORE, Context.MODE_PRIVATE);
    }

    static void setString(SharedPreferences persistenceStore, String key, String value) {
        SharedPreferences.Editor editor = persistenceStore.edit();
        editor.putString(key, value);
        // apply() is async and faster than commit() as it doesn't return the operation status
        editor.apply();
    }

    static void setInt(SharedPreferences persistenceStore, String key, int value) {
        SharedPreferences.Editor editor = persistenceStore.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    static void setLong(SharedPreferences persistenceStore, String key, long value) {
        SharedPreferences.Editor editor = persistenceStore.edit();
        editor.putLong(key, value);
        editor.apply();
    }
}
