package com.example.elmbay.operation;

import com.android.volley.VolleyError;

/**
 * Created by kaininggu on 4/22/18.
 */

public class GetCoursesResponseEvent {
    OperationError mError;

    public GetCoursesResponseEvent(OperationError error) {
        mError = error;
    }

    public boolean hasError() { return mError != null; }
    public OperationError getError() { return mError; }
}
