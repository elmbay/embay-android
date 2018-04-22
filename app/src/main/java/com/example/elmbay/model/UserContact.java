package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kgu on 4/12/18.
 */

public class UserContact {
    private static final int CONTACT_TYPE_EMAIL = 1;
    private static final int CONTACT_TYPE_PHONE = 2;
    private static final int CONTACT_TYPE_WECHAT = 3;

    @SerializedName("id")
    private String mId;

    @SerializedName("type")
    private int mType;

    @SerializedName("confirmed")
    private boolean mConfirmed;

    public void setId(String id) { mId = id; }
    public String getId() { return mId; }

    public void setType(int type) { mType = type; }
    public int getType() { return mType; }

    public void setConfirmed(boolean value) { mConfirmed = value; }
    public boolean isConfirmed() { return mConfirmed; }

    public boolean isEmail() { return mType == CONTACT_TYPE_EMAIL; }
    public boolean isPhone() { return mType == CONTACT_TYPE_PHONE; }
    public boolean isWeChat() { return mType == CONTACT_TYPE_WECHAT; }
}
