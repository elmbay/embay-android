package com.example.elmbay.operation;

import com.example.elmbay.manager.NetworkManager;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kgu on 5/10/18.
 */

public class SignInResult {
    @SerializedName("token")
    private String mUserToken;

    @SerializedName("hour")
    private int mUserTokenLifeInHours;

    public String getUserToken() { return mUserToken; }
    public void setUserToken(String token) { mUserToken = token; }

    public int getUserTokenLifeInHours() { return mUserTokenLifeInHours; }
    public void setUserTokenLifeInHours(int hours) { mUserTokenLifeInHours = hours; }

    @Override
    public String toString() {
        return NetworkManager.getInstance().toJson(this);
    }
}
