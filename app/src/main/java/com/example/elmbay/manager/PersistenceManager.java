package com.example.elmbay.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kgu on 5/9/18.
 */

class PersistenceManager {
    private static final String PERSISTENCE_STORE = "v1.private";

    static SharedPreferences getSharedPersistenceStore(Context context) {
        return context.getSharedPreferences(PERSISTENCE_STORE, Context.MODE_PRIVATE);
    }

    static void setString(SharedPreferences persistenceStore, String key, String value) {
        SharedPreferences.Editor editor = persistenceStore.edit();
        editor.putString(key, value);
        // apply() is async and faster than commit() as it doesn't return the operation mCurrStatus
        editor.apply();
    }

    static void setInt(SharedPreferences persistenceStore, String key, int value) {
        SharedPreferences.Editor editor = persistenceStore.edit();
        editor.putInt(key, value);
        // apply() is async and faster than commit() as it doesn't return the operation mCurrStatus
        editor.apply();
    }

    static void setLong(SharedPreferences persistenceStore, String key, long value) {
        SharedPreferences.Editor editor = persistenceStore.edit();
        editor.putLong(key, value);
        // apply() is async and faster than commit() as it doesn't return the operation mCurrStatus
        editor.apply();
    }
}
