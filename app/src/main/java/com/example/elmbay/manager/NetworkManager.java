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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * The class manages all network operations
 *
 * Created by kgu on 4/11/18.
 */

public class NetworkManager {
    private static final String S_REQUEST_METHODS[] = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "PATCH"};
    private static final int MY_SOCKET_TIMEOUT_MS = 60000;
    private static final String LOG_TAG = NetworkManager.class.getName();

    // urls
//    public static final String BASE_URL_MOCK = "http://107.3.138.187";
    public static final String BASE_URL_MOCK = "http://private-329923-parrot1.apiary-mock.com";
    public static final String ENDPOINT_USERS = "/v1/elmbay/users";
    public static final String ENDPOINT_CHAPTERS = "/v1/elmbay/chapters";

    private static NetworkManager sInstance;
    private Context mAppContext;
    private RequestQueue mRequestQueue;
    private DefaultRetryPolicy mRetryPolicy;

    public @NonNull static NetworkManager getInstance() {
        if (sInstance == null) {
            setup();
        }
        return sInstance;
    }

    /**
     * Enqueue an http JsonObjectRequest
     */
    public void submit(@NonNull JsonObjectRequest request) {
        if (AppManager.DEBUG) {
            Log.i(LOG_TAG, "method:\t" + S_REQUEST_METHODS[request.getMethod()]
                    + "\n\turl:\t" + request.getUrl()
                    + "\n\tbody:\t" + request.getBody());
        }
        request.setRetryPolicy(mRetryPolicy);
        mRequestQueue.add(request);
    }

    /**
     * Convert a JsonObjectRequest (from network response) to a target object
     */
    public static @Nullable <T> T fromJSONObject(@NonNull JSONObject jsonObject, @NonNull Type targetType) {
        Gson gson = new Gson();
        String str = jsonObject.toString();
        if (AppManager.DEBUG) {
            Log.i(LOG_TAG, str);
        }
        return gson.fromJson(str, targetType);
    }

    public static @Nullable JSONObject toJSONObject(@NonNull Object object) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            if (AppManager.DEBUG) {
                Log.e(LOG_TAG, "Failed to convert " + object.getClass().getSimpleName() + " to JSONObject: " + e.getMessage() + ": " + jsonString);
            }
            return null;
        }
    }

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
    }

    private NetworkManager() {}
}
