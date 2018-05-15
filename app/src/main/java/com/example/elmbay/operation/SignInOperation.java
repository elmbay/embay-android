package com.example.elmbay.operation;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.NetworkManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import static com.example.elmbay.manager.NetworkManager.ENDPOINT_USERS;

/**
 *
 * Created by kgu on 4/10/18.
 */

public class SignInOperation {
    private static final String LOG_TAG = SignInOperation.class.getName();
    private SignInRequest mRequest;

    public SignInOperation(@NonNull SignInRequest request) {
        mRequest = request;
    }

    public void submit() {
        int method = Request.Method.POST;

        Uri.Builder builder = Uri.parse(NetworkManager.BASE_URL_MOCK + ENDPOINT_USERS).buildUpon();
        String url = builder.build().toString();

        JSONObject jsonObject = NetworkManager.toJSONObject(mRequest);

        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonObject, onResult(), onError());
        NetworkManager.getInstance().submit(request);
    }

    private Response.Listener<JSONObject> onResult() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SignInResult result = NetworkManager.fromJSONObject(response, SignInResult.class);
                if (AppManager.DEBUG) {
                    Log.i(LOG_TAG, "SignInResult=" + (result == null ? "null" : result.toString()));
                }
                AppManager.getInstance().getSessionData().setSignInResult(result);
                EventBus.getDefault().post(new SignInResponseEvent(null));
            }
        };
    }

    private Response.ErrorListener onError() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (AppManager.DEBUG) {
                    Log.w(LOG_TAG, "ListChaptersResult error" + error.getMessage());
                }
                EventBus.getDefault().post(new SignInResponseEvent(error)
                );
            }
        };
    }
}
