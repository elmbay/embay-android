package com.example.elmbay.operation;

import com.example.elmbay.manager.NetworkManager;
import com.example.elmbay.model.ProgressMark;
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

    @SerializedName("mark")
    private ProgressMark mHighMark;

    public String getUserToken() { return mUserToken; }
    public void setUserToken(String token) { mUserToken = token; }

    public int getUserTokenLifeInHours() { return mUserTokenLifeInHours; }
    public void setUserTokenLifeInHours(int hours) { mUserTokenLifeInHours = hours; }

    public ProgressMark getHighMark() { return mHighMark; }
    public void setHighMark(ProgressMark mark) { mHighMark = mark; }

    @Override
    public String toString() {
        return NetworkManager.getInstance().toJson(this);
    }
}
