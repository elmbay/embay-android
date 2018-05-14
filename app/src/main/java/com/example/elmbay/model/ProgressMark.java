package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kgu on 4/6/18.
 */

public class ProgressMark {
    @SerializedName("chapterId")
    int mChapterId;

    @SerializedName("lessonId")
    int mLessonId;

    public void setChapterId(int chapterId) { mChapterId = chapterId; }
    public int getChapterId() { return mChapterId; }

    public void setLessonId(int lessonId) { mLessonId = lessonId; }
    public int getLessonId() { return mLessonId; }

//    public Lesson getNextLesson(List<Chapter> chapters) {
//        if (TextUtils.isEmpty(mChapterId)) {
//            mChapterId = "0";
//        }
//
//        if (TextUtils.isEmpty(mLessonId)) {
//            mLessonId = "0";
//        }
//
//        if (chapters != null && chapters.size() > 0){
//            for (Chapter chapter : chapters) {
//                List<Lesson> lessons = chapter.getLessons();
//                if (lessons != null && lessons.size() > 0) {
//                    int cmpChapter = mChapterId.compareTo(chapter.getId());
//                    switch (cmpChapter) {
//                        case 0:
//                            for (Lesson lesson : lessons) {
//                                int cmpLesson = mLessonId.compareTo(lesson.getId());
//                                if (cmpLesson < 0) {
//                                    // Found next lesson
//                                    return lesson;
//                                }
//                            }
//                            break;
//                        case -1:
//                            // Found next lesson
//                            return lessons.get(0);
//                    }
//                }
//            }
//        }
//        return null;
//    }
}
