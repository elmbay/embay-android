package com.example.elmbay.manager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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
    private static final String LOG_TAG = NetworkManager.class.getName();
    private static final String sRequestMethods[] = {"GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS", "TRACE", "PATCH"};

    // urls
    public static final String BASE_URL_MOCK = "http://private-329923-parrot1.apiary-mock.com";
    public static final String ENDPOINT_USERS = "/v1/elmbay/users";

    private static NetworkManager sInstance;
    private Context mAppContext;
    private RequestQueue mRequestQueue;

    public static NetworkManager getInstance() {
        if (sInstance == null) {
            setup();
        }
        return sInstance;
    }

    /**
     * Enqueue an http JsonObjectRequest
     *
     * @param request
     */
    public void submit(JsonObjectRequest request) {
        if (AppManager.DEBUG) {
            Log.i(LOG_TAG, "method:\t" + sRequestMethods[request.getMethod()]
                    + "\n\turl:\t" + request.getUrl()
                    + "\n\tbody:\t" + request.getBody());
        }
        mRequestQueue.add(request);
    }

    /**
     * Convert a JsonObjectRequest (from network response) to a target object
     *
     * @param jsonObject
     * @param targetType
     * @param <T>
     * @return
     */
    public static <T> T fromJSONObject(JSONObject jsonObject, Type targetType) {
        Gson gson = new Gson();
        String str = jsonObject.toString();
        if (AppManager.DEBUG) {
            Log.i(LOG_TAG, str);
        }
        return gson.fromJson(str, targetType);
    }

    public static JSONObject toJSONObject(Object object) {
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
            NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            isWifiConn = networkInfo.isConnected();
            if (!isWifiConn) {
                networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                isMobileConn = networkInfo.isConnected();
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
    }

    private NetworkManager() {}
}
