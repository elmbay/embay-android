package com.example.elmbay.model;

import android.content.Context;
import android.content.res.Resources;
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
    public static final int CONTENT_TYPE_VIDEO = 1;
    public static final int CONTENT_TYPE_AUDIO = 2;
    public static final int CONTENT_TYPE_IMAGE = 3;
    public static final int CONTENT_TYPE_OUTPUT_FILE = 4;

    private static Resources sResources;
    private static File sOutputDir;

    @SerializedName("type")
    private int mType;

    @SerializedName("uriString")
    private String mUriString;

    private transient Uri mUri;
    private transient File mFile;

    public ContentDescriptor() {
        if (sResources == null) {
            synchronized (this) {
                if (sResources == null) {
                    Context context = AppManager.getInstance().getAppContext();
                    sResources = context.getResources();
                    // child must matches "path" in xml/filepaths.xml
                    sOutputDir = new File(context.getExternalCacheDir(), "output");
                    if (!sOutputDir.exists()) {
                        sOutputDir.mkdir();
                    }
                }
            }
        }
    }

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
        if (mUriString != null && mUri == null) {
            initUri();
        }
        return mUri;
    }

    public void initUri() {
        if (mUriString == null) {
            mUri = null;
        } else {
            if (mType == CONTENT_TYPE_OUTPUT_FILE) {
                mFile = new File(sOutputDir, mUriString);
                mUri = getUriForFile(AppManager.getInstance().getAppContext(), AppManager.PACKAGE_NAME, mFile);
            } else if (mUriString.startsWith("http") ){
                mUri = Uri.parse(mUriString);
            } else {
                int resourceId = sResources.getIdentifier(mUriString, "raw", PACKAGE_NAME);
                mUri = Uri.parse("android.resource://" + PACKAGE_NAME + "/" + resourceId);
            }
        }
    }

    public File getFile() { return mFile; }

    public String getAbsolutePath() {
        return mFile == null ? null : mFile.getAbsolutePath();
    }

    public boolean deleteFile() { return mFile == null ? true : mFile.delete(); }

    public boolean exists() { return mFile != null && mFile.exists(); }
}
