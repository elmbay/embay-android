package com.example.elmbay.operation;

import com.example.elmbay.manager.NetworkManager;
import com.example.elmbay.model.Course;
import com.example.elmbay.model.ProgressMark;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kgu on 4/10/18.
 */

public class GetCoursesResult {
    @SerializedName("course")
    private Course mCourse;

    @SerializedName("mark")
    private ProgressMark mHighMark;

    @SerializedName("hour")
    private int mNextLoadInHours = 1;

    public void setCourse(Course course) { mCourse = course; }
    public Course getCourse() { return mCourse; }

    public void setHighMark(ProgressMark mark) { mHighMark = mark; }
    public ProgressMark getHighMark() { return mHighMark; }

    public void setNextLoadInHours(int hours) { mNextLoadInHours = hours; }
    public int getNextLoadInHours() { return mNextLoadInHours; }

    @Override
    public String toString() {
        return NetworkManager.getInstance().toJson(this);
    }
}
