package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kgu on 4/10/18.
 */

public class SignInResult {
    @SerializedName("userProgress")
    UserProgress mUserProgress;

    @SerializedName("lessons")
    Lesson mLessons[];

    public void setUserProgress(UserProgress userProgress) { mUserProgress = userProgress; }
    public UserProgress getUserProgress() { return mUserProgress; }

    public void setLessons(Lesson lessons[]) { mLessons = lessons; }
    public Lesson[] getLessons() { return mLessons; }
}
