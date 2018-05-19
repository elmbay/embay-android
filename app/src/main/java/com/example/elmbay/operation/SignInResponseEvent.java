package com.example.elmbay.operation;

/**
 * Created by kgu on 4/11/18.
 */

public class SignInResponseEvent {
    OperationError mError;

    public SignInResponseEvent(OperationError error) {
        mError = error;
    }

    public boolean hasError() { return mError != null; }
    public OperationError getError() { return mError; }
}
