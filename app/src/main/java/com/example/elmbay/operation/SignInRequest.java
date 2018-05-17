package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kgu on 4/12/18.
 */

public class SignInRequest {
    @SerializedName("uid")
    private String mUid;

    @SerializedName("uidType")
    private String mUidType;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("isSignUp")
    private boolean mIsSignUp;

    public SignInRequest(@NonNull String uid, @NonNull String uidType, @NonNull String password, boolean isSignUp) {
        mUid = uid;
        mUidType = uidType;
        mPassword = password;
        mIsSignUp = isSignUp;
    }

    public void setUid(String uid) { mUid = uid; }
    public String getUid() { return mUid; }

    public void setUidType(String type) { mUidType = type; }
    public String getUidType() { return mUidType; }

    public void setPassword(String password) { mPassword = password;}
    public String getPassword() { return mPassword; }

    public void setIsSignUp(boolean isSignUp) { mIsSignUp = isSignUp; }
    public boolean isSignUp() { return mIsSignUp; }
}
