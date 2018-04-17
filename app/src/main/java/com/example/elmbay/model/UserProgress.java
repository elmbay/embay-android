package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kgu on 4/6/18.
 */

public class UserProgress {

    @SerializedName("id")
    String UserId;

    @SerializedName("course")
    String mCourse;

    @SerializedName("lessonId")
    String mLessonId;

    @SerializedName("lessonStatus")
    int mLessonStatus;

    public void setUserId(String userId) { UserId = userId; }
    public String getUserId() { return UserId; }

    public void setCourse(String course) { mCourse = course; }
    public String getCourse() { return mCourse; }

    public void setLessonId(String lessonId) { mLessonId = lessonId; }
    public String getLessonId() { return mLessonId; }

    public void setLessonStatus(int status) { mLessonStatus = status; }
    public int getLessonStatus() { return mLessonStatus; }
}
