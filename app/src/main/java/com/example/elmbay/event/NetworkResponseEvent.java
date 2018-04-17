package com.example.elmbay.event;

import com.android.volley.VolleyError;

import java.lang.reflect.Type;

/**
 * Created by kgu on 4/11/18.
 */

public class NetworkResponseEvent {
    Type mResultType;
    VolleyError mError;

    public NetworkResponseEvent(Type resultType, VolleyError error) {
        mResultType = resultType;
        mError = error;
    }

    public boolean hasError() { return mError != null; }
    public Type getResultType() { return mResultType; }
    public VolleyError getError() { return mError; }
}
