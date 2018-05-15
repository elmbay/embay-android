package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kgu on 4/6/18.
 */

public class ProgressMark {
    @SerializedName("chapterId")
    private int mChapterId;

    @SerializedName("lessonId")
    private int mLessonId;

    public void setChapterId(int chapterId) { mChapterId = chapterId; }
    public int getChapterId() { return mChapterId; }

    public void setLessonId(int lessonId) { mLessonId = lessonId; }
    public int getLessonId() { return mLessonId; }
}
