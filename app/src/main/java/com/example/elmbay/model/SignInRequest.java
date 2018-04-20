package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kgu on 4/12/18.
 */

public class SignInRequest {
    @SerializedName("userName")
    String mUserName;

    @SerializedName("password")
    String mPassword;

    @SerializedName("contacts")
    UserContact mUserContacts[];

    public SignInRequest(String userName, String password, UserContact userContacts[]) {
        mUserName = userName;
        mPassword = password;
        mUserContacts = userContacts;
    }

    public void setUserName(String userName) { mUserName = userName; }
    public String getUserName() { return mUserName; }

    public void setPassword(String password) { mPassword = password;}
    public String getPassword() { return mPassword; }

    public void setUserContacts(UserContact userContacts[]) { mUserContacts = userContacts; }
    public UserContact[] getUserContacts() { return mUserContacts; }

    public void addContact(UserContact userContact) {
        if (userContact != null) {
            if (mUserContacts == null) {
                mUserContacts = new UserContact[1];
            } else {
                UserContact oldUserContacts[] = mUserContacts;
                mUserContacts = new UserContact[oldUserContacts.length + 1];
                System.arraycopy(oldUserContacts, 0, mUserContacts, 0, oldUserContacts.length);
            }
            mUserContacts[mUserContacts.length - 1] = userContact;
        }
    }
}
