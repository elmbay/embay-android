package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kgu on 4/12/18.
 */

public class SignInRequest {
    @SerializedName("email")
    private String mEmail;

    @SerializedName("phone")
    private String mPhone;

    @SerializedName("password")
    private String mPassword;

    public SignInRequest(String email, String phone, @NonNull String password) {
        mEmail = email;
        mPhone = phone;
        mPassword = password;
    }

    public void setEmail(String email) { mEmail = email; }
    public String getEmail() { return mEmail; }

    public void setPhone(String phone) { mPhone = phone; }
    public String getPhone() { return mPhone; }

    public void setPassword(String password) { mPassword = password;}
    public String getPassword() { return mPassword; }
}
