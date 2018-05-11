package com.example.elmbay.operation;

import com.example.elmbay.model.ProgressMark;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kgu on 5/10/18.
 */

public class ListChaptersRequest {
    @SerializedName("userToken")
    private String mUserToken;

    @SerializedName("lowMark")
    private ProgressMark mLowMark;

    @SerializedName("highMark")
    private ProgressMark mHighMark;

    public String getUserToken() { return mUserToken; }
    public void setUserToken(String token) { mUserToken = token; }

    public ProgressMark getLowMark() { return mLowMark; }
    public void setLowMark(ProgressMark mark) { mLowMark = mark; }

    public ProgressMark getHighMark() { return mHighMark; }
    public void setHighMark(ProgressMark mark) { mHighMark = mark; }
}
