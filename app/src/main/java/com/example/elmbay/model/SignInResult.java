package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kgu on 4/10/18.
 */

public class SignInResult {
    @SerializedName("userProgress")
    private UserProgress mUserProgress;

    @SerializedName("chapters")
    private List<Chapter> mChapters;

    public void setUserProgress(UserProgress userProgress) { mUserProgress = userProgress; }
    public UserProgress getUserProgress() { return mUserProgress; }

    public void setChapters(List<Chapter> lessons) { mChapters = lessons; }
    public List<Chapter> getChapters() { return mChapters; }
}
