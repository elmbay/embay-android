package com.example.elmbay.model;

import com.example.elmbay.manager.Helper;
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

    @SerializedName("lessons")
    private List<Lesson> mLessons;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{id=").append(mId).append(",course=").append(mCourse).append(",topic=").append(mTopic);
        if (mLessons != null) {
            builder.append(",lessons=[").append(Helper.listToString(mLessons)).append("]");
        }
        return builder.toString();
    }

    public String getId() { return mId; }
    public void setId(String id) { mId = id; }

    public String getCourse() { return mCourse; }
    public void setCourse(String course) { mCourse = course; }

    public String getTopic() { return mTopic; }
    public void setTopic(String topic) { mTopic = topic; }

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
