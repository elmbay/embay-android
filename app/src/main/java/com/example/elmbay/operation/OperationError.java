package com.example.elmbay.operation;

import com.android.volley.VolleyError;
import com.example.elmbay.R;

/**
 *
 * Created by kaininggu on 5/19/18.
 */

public class OperationError {
    private int mHttpStatusCode;
    private int mMessageId;

    OperationError(VolleyError error) {
        mHttpStatusCode = error.networkResponse.statusCode;
        setMessageId();
    }

    public int getMessageId() { return mMessageId; }

    private void setMessageId() {
        switch (mHttpStatusCode) {
            case 404:
                mMessageId = R.string.http_404;
        }
    }
}
