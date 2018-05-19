package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.example.elmbay.manager.NetworkManager;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by kgu on 4/12/18.
 */

public class SignInRequest {
    @SerializedName("u")
    private String mUid;

    @SerializedName("t")
    private String mUidType;

    @SerializedName("p")
    private String mPassword;

    @SerializedName("new")
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

    @Override
    public String toString() {
        return NetworkManager.getInstance().toJson(this);
    }

    Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("u", mUid);
        params.put("t", mUidType);
        params.put("p", mPassword);
        params.put("new", mIsSignUp ? "1" : "0");
        return params;
    }
}
