package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.example.elmbay.model.ProgressMark;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kgu on 5/10/18.
 */

public class ListChaptersRequest {
    @SerializedName("token")
    private String mUserToken;

    @SerializedName("mark")
    private ProgressMark mHighMark;

    public void setUserToken(@NonNull String token) { mUserToken = token; }
    public void setHighMark(@NonNull ProgressMark mark) { mHighMark = mark; }
}
