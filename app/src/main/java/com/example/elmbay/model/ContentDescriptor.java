package com.example.elmbay.model;

import android.net.Uri;

import com.example.elmbay.manager.AppManager;
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
    private int mType;

    @SerializedName("uriString")
    private String mUriString;

    private transient Uri mUri;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{type=").append(mType).append(",uriString=").append(mUriString).append("}");
        return builder.toString();
    }

    public void setType(int type) { mType = type; }
    public int getType() { return mType; }

    public String getUriString() { return mUriString; }
    public void setUriString(String uriString) { mUriString = uriString; }

    public Uri getUri() {
        if (mUri == null && mUriString != null) {
            deriveUri();
        }
        return mUri;
    }

    private void deriveUri() {
        if (mUriString != null) {
            if (mUriString.startsWith("http")) {
                mUri = Uri.parse(mUriString);
            } else {
                int resourceId = AppManager.getInstance().getAppContext().getResources().getIdentifier(mUriString, "raw", PACKAGE_NAME);
                mUri = Uri.parse("android.resource://" + PACKAGE_NAME + "/" + resourceId);
            }
        }
    }
}
