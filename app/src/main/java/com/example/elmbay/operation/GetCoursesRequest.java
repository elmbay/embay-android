package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.example.elmbay.manager.NetworkManager;
import com.example.elmbay.model.ProgressMark;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import static com.example.elmbay.manager.NetworkManager.ENDPOINT_COURSES;

/**
 *
 * Created by kgu on 5/10/18.
 */

public class GetCoursesRequest implements IRequest {
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

    public int getMethod() { return Request.Method.POST; }

    public String getEndpoint() { return NetworkManager.BASE_URL_MOCK + ENDPOINT_COURSES; }

    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("token", mUserToken);
        params.put("mark", mHighMark.toString());
        return params;

    }
}
