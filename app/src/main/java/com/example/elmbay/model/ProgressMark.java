package com.example.elmbay.model;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.elmbay.manager.PersistenceManager;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kgu on 4/6/18.
 */

public class ProgressMark {
    @SerializedName("chapterId")
    String mChapterId;

    @SerializedName("lessonId")
    String mLessonId;

    public void setChapterId(String chapterId) { mChapterId = chapterId; }
    public String getChapterId() { return mChapterId; }

    public void setLessonId(String lessonId) { mLessonId = lessonId; }
    public String getLessonId() { return mLessonId; }

    public void fromPersistenceStore(SharedPreferences persistenceStore, boolean isHighMark) {
        mChapterId = persistenceStore.getString(
                isHighMark ? PersistenceManager.KEY_PROGRESS_HIGHMARK_CID : PersistenceManager.KEY_PROGRESS_LOWMARK_CID,
                "0");
        mLessonId = persistenceStore.getString(isHighMark ? PersistenceManager.KEY_PROGRESS_HIGHMARK_LID : PersistenceManager.KEY_PROGRESS_LOWMARK_LID, "0");
    }

    public void toPersistenceStore(SharedPreferences persistenceStore, boolean isHighMark) {
        PersistenceManager.setString(persistenceStore,
                isHighMark ? PersistenceManager.KEY_PROGRESS_HIGHMARK_CID : PersistenceManager.KEY_PROGRESS_LOWMARK_CID,
                mChapterId == null ? "0" : mChapterId);
        PersistenceManager.setString(persistenceStore,
                isHighMark ? PersistenceManager.KEY_PROGRESS_HIGHMARK_LID : PersistenceManager.KEY_PROGRESS_LOWMARK_LID,
                mLessonId == null ? "0" : mLessonId);
    }

    public Lesson getNextLesson(List<Chapter> chapters) {
        if (TextUtils.isEmpty(mChapterId)) {
            mChapterId = "0";
        }

        if (TextUtils.isEmpty(mLessonId)) {
            mLessonId = "0";
        }

        if (chapters != null && chapters.size() > 0){
            for (Chapter chapter : chapters) {
                List<Lesson> lessons = chapter.getLessons();
                if (lessons != null && lessons.size() > 0) {
                    int cmpChapter = mChapterId.compareTo(chapter.getId());
                    switch (cmpChapter) {
                        case 0:
                            for (Lesson lesson : lessons) {
                                int cmpLesson = mLessonId.compareTo(lesson.getId());
                                if (cmpLesson < 0) {
                                    // Found next lesson
                                    return lesson;
                                }
                            }
                            break;
                        case -1:
                            // Found next lesson
                            return lessons.get(0);
                    }
                }
            }
        }
        return null;
    }
}
