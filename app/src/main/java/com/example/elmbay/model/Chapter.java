package com.example.elmbay.model;

import android.support.annotation.Nullable;

import com.example.elmbay.manager.NetworkManager;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kgu on 4/19/18.
 */

public class Chapter {
    @SerializedName("id")
    private int mId;

    @SerializedName("topic")
    private String mTopic;

    // For comparison, lessons is a list of lessons in ascend order
    @SerializedName("lessons")
    private List<Lesson> mLessons;

    private int mStatus;

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public @Nullable String getTopic() { return mTopic; }
    public void setTopic(@Nullable String topic) { mTopic = topic; }

    public @Nullable List<Lesson> getLessons() { return mLessons; }
    public void setLessons(@Nullable List<Lesson> lessons) { mLessons = lessons; }

    public int getStatus() { return mStatus; }
    public void setStatus(int status) { mStatus = status; }

    @Override
    public String toString() {
        return NetworkManager.getInstance().toJson(this);
    }
}
