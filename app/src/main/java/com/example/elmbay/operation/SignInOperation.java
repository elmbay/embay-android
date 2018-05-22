package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.NetworkManager;

import org.greenrobot.eventbus.EventBus;

/**
 *
 * Created by kgu on 5/18/18.
 */

public class SignInOperation extends BaseOperation {

    public SignInOperation(@NonNull SignInRequest request) {
        super(request);
    }

    Response.Listener<String> onResult() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SignInResult result = NetworkManager.getInstance().fromJson(response, SignInResult.class);
                logResult(result);
                AppManager.getInstance().getSessionData().getUserManager().setSignInResult(result);
                EventBus.getDefault().post(new SignInResponseEvent(null));
            }
        };
    }

    Response.ErrorListener onError() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logError(error);
                EventBus.getDefault().post(new SignInResponseEvent(new OperationError(error)));
            }
        };
    }
}
