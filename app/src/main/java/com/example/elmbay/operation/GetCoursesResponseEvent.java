package com.example.elmbay.operation;

import com.android.volley.VolleyError;

/**
 * Created by kaininggu on 4/22/18.
 */

public class GetCoursesResponseEvent {
    VolleyError mError;

    public GetCoursesResponseEvent(VolleyError error) {
        mError = error;
    }

    public boolean hasError() { return mError != null; }
    public VolleyError getError() { return mError; }
}
