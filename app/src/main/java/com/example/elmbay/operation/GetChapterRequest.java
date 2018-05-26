package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.example.elmbay.manager.NetworkManager;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

import static com.example.elmbay.manager.NetworkManager.ENDPOINT_LESSONS;

/**
 *
 * Created by kgu on 5/23/18.
 */

public class GetChapterRequest implements IRequest {
    @SerializedName("token")
    private String mUserToken;

    @SerializedName("cid")
    private int mChapterId;

    public void setUserToken(@NonNull String token) { mUserToken = token; }
    public void setChapterId(@NonNull int id) { mChapterId = id; }

    @Override
    public String toString() {
        return IRequest.REQUEST_METHOD_TO_STRING[getMethod()] + " " + getEndpoint() + " " + NetworkManager.getInstance().toJson(this);
    }

    public int getMethod() { return Request.Method.GET; }

    public String getEndpoint() { return NetworkManager.BASE_URL_MOCK + ENDPOINT_LESSONS + "?cid=" + mChapterId; }

    public Map<String, String> getParams() {
        return null;
    }
}
