package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.example.elmbay.manager.NetworkManager;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import static com.example.elmbay.manager.NetworkManager.ENDPOINT_USERS;

/**
 *
 * Created by kgu on 4/12/18.
 */

public class SignInRequest implements IRequest {
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
        return IRequest.REQUEST_METHOD_TO_STRING[getMethod()] + " " + getEndpoint() + " " + NetworkManager.getInstance().toJson(this);
    }

    public int getMethod() { return Request.Method.POST; }

    public String getEndpoint() { return NetworkManager.BASE_URL_MOCK + ENDPOINT_USERS; }

    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("u", mUid);
        params.put("t", mUidType);
        params.put("p", mPassword);
        params.put("new", mIsSignUp ? "1" : "0");
        return params;
    }
}
