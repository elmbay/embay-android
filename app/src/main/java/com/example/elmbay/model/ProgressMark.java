package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kgu on 4/6/18.
 */

public class ProgressMark {
    @SerializedName("cid")
    private int mChapterId;

    @SerializedName("lid")
    private int mLessonId;

    public void setChapterId(int chapterId) { mChapterId = chapterId; }
    public int getChapterId() { return mChapterId; }

    public void setLessonId(int lessonId) { mLessonId = lessonId; }
    public int getLessonId() { return mLessonId; }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{chapterId=").append(mChapterId).append(",lessonId=").append(mLessonId).append("}");
        return builder.toString();
    }
}
