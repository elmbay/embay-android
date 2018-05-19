package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.example.elmbay.manager.NetworkManager;
import com.example.elmbay.model.ProgressMark;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by kgu on 5/10/18.
 */

public class GetCoursesRequest {
    @SerializedName("token")
    private String mUserToken;

    @SerializedName("mark")
    private ProgressMark mHighMark;

    public void setUserToken(@NonNull String token) { mUserToken = token; }
    public void setHighMark(@NonNull ProgressMark mark) { mHighMark = mark; }

    @Override
    public String toString() {
        return NetworkManager.getInstance().toJson(this);
    }

    Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("token", mUserToken);
        params.put("mark", mHighMark.toString());
        return params;

    }
}
