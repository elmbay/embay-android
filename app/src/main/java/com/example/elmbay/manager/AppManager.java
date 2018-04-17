package com.example.elmbay.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by kgu on 4/10/18.
 */

public class AppManager {
    public static final String PACKAGE_NAME = "com.example.elmbay";

    private static final String LOG_TAG = AppManager.class.getName();
    private static AppManager sInstance;

    private Context mAppContext;
    private SessionData mSessionData;

    public static synchronized void setup(Context context) {
        if (sInstance == null) {
            sInstance = new AppManager();
        }
    }

    public static @NonNull AppManager getInstance() { return sInstance; }

    public Context getAppContext() { return mAppContext; }

    public SessionData getSessionData() { return mSessionData; }

    public String getAppName() { return mAppContext.getPackageName(); }

    public String getAppVersion() {
        try {
            return mAppContext.getPackageManager().getPackageInfo(getAppName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(LOG_TAG, "Missing android:versionName entry in AndroidManifest.xml");
            return null;
        }
    }

    private AppManager(@NonNull Context context) {
        mAppContext = context.getApplicationContext();
        mSessionData = new SessionData();
    }

    private AppManager() {}
}
