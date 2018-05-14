package com.example.elmbay.operation;

import com.example.elmbay.model.ProgressMark;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kgu on 5/10/18.
 */

public class SignInResult {
    @SerializedName("userToken")
    private String mUserToken;

    @SerializedName("userTokenLifeInHours")
    private int mUserTokenLifeInHours;

    @SerializedName("highMark")
    private ProgressMark mHighMark;

    public String getUserToken() { return mUserToken; }
    public void setUserToken(String token) { mUserToken = token; }

    public int getUserTokenLifeInHours() { return mUserTokenLifeInHours; }
    public void setUserTokenLifeInHours(int hours) { mUserTokenLifeInHours = hours; }

    public ProgressMark getHighMark() { return mHighMark; }
    public void setHighMark(ProgressMark mark) { mHighMark = mark; }
}
