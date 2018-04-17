package com.example.elmbay.manager;

import com.example.elmbay.model.SignInResult;

/**
 * Created by kgu on 4/12/18.
 */

public class SessionData {
    private SignInResult mSignInResult;

    public void setSignInResult(SignInResult signInResult) { mSignInResult = signInResult; }
    public SignInResult getSignInResult() { return mSignInResult; }
}
