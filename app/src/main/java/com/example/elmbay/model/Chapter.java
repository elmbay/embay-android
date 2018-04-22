package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgu on 4/19/18.
 */

public class Chapter {
    @SerializedName("id")
    private String mId;

    @SerializedName("course")
    private String mCourse;

    @SerializedName("topic")
    private String mTopic;

    private boolean mIsExpanded;

    @SerializedName("lessons")
    private List<Lesson> mLessons;

    public String getId() { return mId; }
    public void setId(String id) { mId = id; }

    public String getCourse() { return mCourse; }
    public void setCourse(String course) { mCourse = course; }

    public String getTopic() { return mTopic; }
    public void setTopic(String topic) { mTopic = topic; }

    public boolean isExpanded() { return mIsExpanded; }
    public void setExpanded(boolean value) {mIsExpanded = value; }

    public List<Lesson> getLessons() { return mLessons; }
    public void setLessons(List<Lesson> lessons) { mLessons = lessons; }
    public void addLesson(Lesson lesson) {
        if (lesson != null) {
            if (mLessons == null) {
                mLessons = new ArrayList<Lesson>();
            }
            mLessons.add(lesson);
        }
    }
}
