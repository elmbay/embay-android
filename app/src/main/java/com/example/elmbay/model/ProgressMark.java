package com.example.elmbay.model;

import com.example.elmbay.manager.NetworkManager;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kgu on 4/6/18.
 */

public class ProgressMark {
    @SerializedName("csid")
    private int mCourseId;

    @SerializedName("cid")
    private int mChapterId;

    @SerializedName("lid")
    private int mLessonId;

    public void setCourseId(int courseId) { mCourseId = courseId; }
    public int getCourseId() { return mCourseId; }

    public void setChapterId(int chapterId) { mChapterId = chapterId; }
    public int getChapterId() { return mChapterId; }

    public void setLessonId(int lessonId) { mLessonId = lessonId; }
    public int getLessonId() { return mLessonId; }

    @Override
    public String toString() {
        return NetworkManager.getInstance().toJson(this);
    }

//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("{csid:").append(mCourseId).append("{id=").append(mChapterId).append(",lid=").append(mLessonId).append("}");
//        return builder.toString();
//    }
}
