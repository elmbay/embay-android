package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgu on 4/12/18.
 */

public class SignInRequest {
    @SerializedName("userName")
    private String mUserName;

    @SerializedName("password")
    private String mPassword;

    @SerializedName("contacts")
    private List<UserContact> mUserContacts;

    public SignInRequest(String userName, String password, List<UserContact> userContacts) {
        mUserName = userName;
        mPassword = password;
        mUserContacts = userContacts;
    }

    public void setUserName(String userName) { mUserName = userName; }
    public String getUserName() { return mUserName; }

    public void setPassword(String password) { mPassword = password;}
    public String getPassword() { return mPassword; }

    public void setUserContacts(List<UserContact> userContacts) { mUserContacts = userContacts; }
    public List<UserContact> getUserContacts() { return mUserContacts; }

    public void addContact(UserContact userContact) {
        if (userContact != null) {
            if (mUserContacts == null) {
                mUserContacts = new ArrayList<UserContact>();
            }
            mUserContacts.add(userContact);
        }
    }
}
