package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kgu on 4/10/18.
 */

public class ContentDescriptor {
    public static final int CONTANT_TYPE_VIDEO = 1;
    public static final int CONTANT_TYPE_AUDIO = 2;
    public static final int CONTANT_TYPE_IMAGE = 3;
    public static final int CONTANT_TYPE_HTML = 4;

    @SerializedName("uri")
    String mUri;

    @SerializedName("type")
    int mType;

    public void setUri(String uri) { mUri = uri; }
    public String getUri() { return mUri; }

    public void setType(int type) { mType = type; }
    public int getType() { return mType; }
}
