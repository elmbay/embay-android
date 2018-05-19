package com.example.elmbay.manager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * The class manages all network operations
 *
 * Created by kgu on 4/11/18.
 */

public class NetworkManager {
    public static final String REQUEST_METHODS[] = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "PATCH"};

    private static final int MY_SOCKET_TIMEOUT_MS = 60000;
    private static final String LOG_TAG = NetworkManager.class.getName();

    // urls
//    public static final String BASE_URL_MOCK = "http://107.3.138.187";
    public static final String BASE_URL_MOCK = "http://private-329923-parrot1.apiary-mock.com";
    public static final String ENDPOINT_USERS = "/v1/elmbay/users";
    public static final String ENDPOINT_CHAPTERS = "/v1/elmbay/courses";

    private static NetworkManager sInstance;
    private Context mAppContext;
    private int mRequestId;
    private RequestQueue mRequestQueue;
    private DefaultRetryPolicy mRetryPolicy;
    private Gson mGson;

    public @NonNull static NetworkManager getInstance() {
        if (sInstance == null) {
            setup();
        }
        return sInstance;
    }

    public int getNextRequestId() { return ++mRequestId; }

    public void submit(@NonNull StringRequest request) {
        request.setRetryPolicy(mRetryPolicy);
        mRequestQueue.add(request);
    }

    public @Nullable <T> T fromJson(@NonNull String jsonString, @NonNull Type targetType) {
        return mGson.fromJson(jsonString, targetType);
    }

    public String toJson(@NonNull Object object) { return mGson.toJson(object); }

    public boolean hasNetworkConnection() {
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        ConnectivityManager connMgr = (ConnectivityManager) mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (ContextCompat.checkSelfPermission(mAppContext, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            try {
                NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                isWifiConn = networkInfo.isConnected();
                if (!isWifiConn) {
                    networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    isMobileConn = networkInfo.isConnected();
                }
            } catch (NullPointerException ex) {
                if (AppManager.DEBUG) {
                    Log.w(LOG_TAG, "Null NetworkInfo: " + ex.getMessage());
                }
            }
        }
        return isWifiConn || isMobileConn;
    }

    private static synchronized void setup() {
        if (sInstance == null) {
            try {
                sInstance = new NetworkManager(AppManager.getInstance().getAppContext());
            } catch (NullPointerException e) {
                Log.w(LOG_TAG, "The app has not set up yet.");
            }
        }
    }

    private NetworkManager(Context appContext) {
        mAppContext = appContext;

        // Creates a default worker pool and calls {@link RequestQueue#start()} on it.
        mRequestQueue = Volley.newRequestQueue(appContext);
        mRetryPolicy = new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        mGson = new Gson();
    }

    private NetworkManager() {}
}
