package com.example.elmbay.model;

import android.content.Context;
import android.net.Uri;

import com.example.elmbay.manager.AppManager;
import com.google.gson.annotations.SerializedName;

import java.io.File;

import static android.support.v4.content.FileProvider.getUriForFile;
import static com.example.elmbay.manager.AppManager.PACKAGE_NAME;

/**
 *
 * Created by kgu on 4/10/18.
 */

public class ContentDescriptor {
    public static final String CONTENT_TYPE_VIDEO = "video/mp4";
    public static final String CONTENT_TYPE_AUDIO = "audio/*";
    public static final String CONTENT_TYPE_IMAGE = "image/*";
    public static final String CONTENT_TYPE_AUDIO_RECORDING = "audio/3gpp";

    @SerializedName("mimeType")
    private String mMimeType;

    @SerializedName("uriString")
    private String mUriString;

    private transient Uri mUri;
    private transient File mFile;

    public String getMimeType() { return mMimeType; }
    public void setMimeType(String mimeType) { mMimeType = mimeType; }

    public String getUriString() { return mUriString; }
    public void setUriString(String uriString) { mUriString = uriString; }

    public Uri getUri() {
        if (mUriString != null && mUri == null) {
            initUri();
        }
        return mUri;
    }

    public void initUri() {
        if (mUriString == null) {
            mUri = null;
        } else {
            Context context = AppManager.getInstance().getAppContext();
            if (CONTENT_TYPE_AUDIO_RECORDING.equals(mMimeType)) {
                mFile = new File(AppManager.getInstance().getSessionData().getOutputFileDir(), mUriString);
                mUri = getUriForFile(context, AppManager.PACKAGE_NAME, mFile);
            } else if (mUriString.startsWith("http") ){
                mUri = Uri.parse(mUriString);
            } else {
                int resourceId = AppManager.getInstance().getAppContext().getResources().getIdentifier(mUriString, "raw", PACKAGE_NAME);
                mUri = Uri.parse("android.resource://" + PACKAGE_NAME + "/" + resourceId);
            }
        }
    }

    public boolean isRemote() {
        return mUriString != null && mUriString.startsWith("http");
    }

    public File getFile() { return mFile; }

    public String getAbsolutePath() {
        return mFile == null ? null : mFile.getAbsolutePath();
    }

    public boolean deleteFile() { return mFile == null || mFile.delete(); }

    public boolean exists() { return mFile != null && mFile.exists(); }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{mimeType=").append(mMimeType).append(",uriString=").append(mUriString).append("}");
        return builder.toString();
    }
}
