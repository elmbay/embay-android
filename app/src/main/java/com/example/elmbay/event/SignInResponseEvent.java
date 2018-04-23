package com.example.elmbay.event;

import com.android.volley.VolleyError;

import java.lang.reflect.Type;

/**
 * Created by kgu on 4/11/18.
 */

public class SignInResponseEvent {
    VolleyError mError;

    public SignInResponseEvent(VolleyError error) {
        mError = error;
    }

    public boolean hasError() { return mError != null; }
    public VolleyError getError() { return mError; }
}
