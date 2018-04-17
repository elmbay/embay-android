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
    Contact mContacts[];

    public SignInRequest(String userName, String password, Contact contacts[]) {
        mUserName = userName;
        mPassword = password;
        mContacts = contacts;
    }

    public void setUserName(String userName) { mUserName = userName; }
    public String getUserName() { return mUserName; }

    public void setPassword(String password) { mPassword = password;}
    public String getPassword() { return mPassword; }

    public void setContacts(Contact contacts[]) { mContacts = contacts; }
    public Contact[] getContacts() { return mContacts; }

    public void addContact(Contact contact) {
        if (contact != null) {
            if (mContacts == null) {
                mContacts = new Contact[1];
            } else {
                Contact oldContacts[] = mContacts;
                mContacts = new Contact[oldContacts.length + 1];
                System.arraycopy(oldContacts, 0, mContacts, 0, oldContacts.length);
            }
            mContacts[mContacts.length - 1] = contact;
        }
    }
}
