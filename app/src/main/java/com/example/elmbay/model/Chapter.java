package com.example.elmbay.model;

import android.support.annotation.Nullable;

import com.example.elmbay.manager.Helper;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kgu on 4/19/18.
 */

public class Chapter {
    @SerializedName("id")
    private int mId;

    @SerializedName("course")
    private String mCourse;

    @SerializedName("topic")
    private String mTopic;

    // For comparison, lessons is a list of lessons in ascend order
    @SerializedName("lessons")
    private List<Lesson> mLessons;

    private int mStatus;

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public @Nullable String getCourse() { return mCourse; }
    public void setCourse(@Nullable String course) { mCourse = course; }

    public @Nullable String getTopic() { return mTopic; }
    public void setTopic(@Nullable String topic) { mTopic = topic; }

    public @Nullable List<Lesson> getLessons() { return mLessons; }
    public void setLessons(@Nullable List<Lesson> lessons) { mLessons = lessons; }

    public int getStatus() { return mStatus; }
    public void setStatus(int status) { mStatus = status; }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{id=").append(mId);
        if (mCourse != null) {
            builder.append(",course=").append(mCourse);
        }
        if (mTopic != null) {
            builder.append(",topic=").append(mTopic);
        }
        if (mLessons != null) {
            builder.append(",lessons=[").append(Helper.listToString(mLessons)).append("]");
        }
        return builder.toString();
    }
}
