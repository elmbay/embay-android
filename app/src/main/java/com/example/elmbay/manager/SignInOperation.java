package com.example.elmbay.manager;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.elmbay.event.NetworkResponseEvent;
import com.example.elmbay.model.SignInRequest;
import com.example.elmbay.model.SignInResult;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by kgu on 4/10/18.
 */

public class SignInOperation {
    SignInRequest mRequest;
    boolean mIsSignUp;

    public SignInOperation(SignInRequest request, boolean isSignUp) {
        mRequest = request;
        mIsSignUp = isSignUp;
    }

    public void submit() {
        int method = mIsSignUp ? Request.Method.POST : Request.Method.GET;

        Uri.Builder builder = Uri.parse(NetworkManager.BASE_URL_MOCK).buildUpon();
        builder.appendPath(NetworkManager.ENDPOINT_USERS);
        if (!mIsSignUp) {
            builder.appendPath(mRequest.getUserName());
        }
        String url = builder.build().toString();

        JSONObject jsonObject = NetworkManager.toJSONObject(mRequest);

        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonObject, onResult(), onError());
        NetworkManager.getInstance().submit(request);
    }

    private Response.Listener<JSONObject> onResult() {
        Response.Listener<JSONObject> resultListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SignInResult result = NetworkManager.fromJSONObject(response, SignInResult.class);
                AppManager.getInstance().getSessionData().setSignInResult(result);
                EventBus.getDefault().post(new NetworkResponseEvent(result.getClass(), null));
            }
        };
        return resultListener;
    }

    private Response.ErrorListener onError() {
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EventBus.getDefault().post(new NetworkResponseEvent(SignInResult.class, error));
            }
        };
        return errorListener;
    }
}
