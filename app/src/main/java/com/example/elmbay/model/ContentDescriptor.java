package com.example.elmbay.model;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import static com.example.elmbay.manager.AppManager.PACKAGE_NAME;

/**
 * Created by kgu on 4/10/18.
 */

public class ContentDescriptor {
    public static final int CONTENT_TYPE_VIDEO = 1;
    public static final int CONTENT_TYPE_AUDIO = 2;
    public static final int CONTENT_TYPE_IMAGE = 3;

    @SerializedName("type")
    int mType;

    @SerializedName("uri")
    Uri mUri;

    public void setType(int type) { mType = type; }
    public int getType() { return mType; }

    public Uri getUri() { return mUri; }

    public void setUri(@NonNull Context context, @NonNull String uriString) {
        if (uriString.startsWith("http")) {
            mUri = Uri.parse(uriString);
        } else {
            int resourceId = context.getResources().getIdentifier(uriString, "raw", PACKAGE_NAME);
            mUri = Uri.parse("android.resource://" + PACKAGE_NAME + "/" + resourceId);
        }
    }
}
