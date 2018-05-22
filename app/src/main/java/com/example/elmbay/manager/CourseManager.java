package com.example.elmbay.manager;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.operation.GetCoursesResult;

import java.util.List;

import static com.example.elmbay.manager.SessionData.HOUR_TO_MILLIS;
import static com.example.elmbay.manager.PersistenceManager.KEY_NEXT_LOAD_TIME;

/**
 *
 * Created by kgu on 5/21/18.
 */

public class CourseManager {

    public static final int COURSE_STATUS_LOCKED = 0;
    public static final int COURSE_STATUS_INPROGRESS = 1;
    public static final int COURSE_STATUS_DONE = 2;

    private SharedPreferences mPersistenceStore;
    private GetCoursesResult mCoursesResult;
    private long mNextLoadTime;
    private int mInProgressChapterIndex = -1;
    private int mInProgressLessonIndex = -1;
    private Chapter mCurrentChapter;
    private Lesson mCurrentLesson;

    CourseManager(SharedPreferences persistenceStore) {
        mPersistenceStore = persistenceStore;
        fromPersistenceStore();
    }

    private void fromPersistenceStore() {
        mNextLoadTime = mPersistenceStore.getLong(KEY_NEXT_LOAD_TIME, System.currentTimeMillis() + HOUR_TO_MILLIS);
    }

    void logout() {
        mCoursesResult = null;
    }


    public long getNextLoadTime() { return mNextLoadTime; }
    private void setNextLoadTime(long time) {
        mNextLoadTime = time;
        PersistenceManager.setLong(mPersistenceStore, KEY_NEXT_LOAD_TIME, mNextLoadTime);
    }

    public @Nullable
    GetCoursesResult getCoursesResult() { return mCoursesResult; }
    public void setCoursesResult(@Nullable GetCoursesResult result) {
        mCoursesResult = result;
        long now = System.currentTimeMillis();
        long nextLoadTime = now + HOUR_TO_MILLIS;
        if (result != null) {
            UserManager userManager = AppManager.getInstance().getSessionData().getUserManager();
            userManager.advanceHighMark(result.getHighMark());
            if (result.getNextLoadInHours() > 0) {
                nextLoadTime = now + result.getNextLoadInHours() * HOUR_TO_MILLIS;
            }
        }
        setNextLoadTime(nextLoadTime);
        setChapterAndLessonStatus();
    }

    public int getInProgressChapterIndex() { return mInProgressChapterIndex; }

    public @Nullable Chapter getCurrentChapter() { return mCurrentChapter; }
    public void setCurrentChapter(@Nullable Chapter chapter) { mCurrentChapter = chapter; }
    public @Nullable Lesson getCurrentLesson() { return mCurrentLesson; }
    public void setCurrentLesson(@Nullable Lesson lesson) { mCurrentLesson = lesson; }

    public void finishedCurrentLesson() {

        // No highMark change if the current lesson is not the lesson marked as in progress

        if (mCurrentLesson == null || mInProgressChapterIndex < 0 || mInProgressLessonIndex < 0) {
            return;
        }

        List<Chapter> chapters = mCoursesResult.getCourse().getChapters();
        if (isEmpty(chapters) || chapters.size() <= mInProgressChapterIndex) {
            return;
        }
        Chapter chapter = chapters.get(mInProgressChapterIndex);

        List<Lesson> lessons = chapter.getLessons();
        if (isEmpty(lessons) || lessons.size() <= mInProgressLessonIndex) {
            return;
        }
        Lesson lesson = lessons.get(mInProgressLessonIndex);

        if (lesson != mCurrentLesson) {
            return;
        }

        UserManager userManager = AppManager.getInstance().getSessionData().getUserManager();
        userManager.advanceHighMark(chapter.getId(), lesson.getId());

        // Update chapter(optional) and lesson state, and locate the next in-progress lesson
        lesson.setStatus(COURSE_STATUS_DONE);
        if (mInProgressLessonIndex < lessons.size() - 1) {
            // Advance to the next lesson in the same chapter
            lessons.get(++mInProgressLessonIndex).setStatus(COURSE_STATUS_INPROGRESS);
        } else {
            chapter.setStatus(COURSE_STATUS_DONE);
            while (++mInProgressChapterIndex < chapters.size()) {
                // Advance to the next chapter
                lessons = chapters.get(mInProgressChapterIndex).getLessons();
                if (isEmpty(lessons)) {
                    chapters.get(mInProgressChapterIndex).setStatus(COURSE_STATUS_DONE);
                } else {
                    chapters.get(mInProgressChapterIndex).setStatus(COURSE_STATUS_INPROGRESS);
                    lessons.get(0).setStatus(COURSE_STATUS_INPROGRESS);
                    mInProgressLessonIndex = 0;
                    break;
                }
            }
            if (mInProgressChapterIndex >= chapters.size()) {
                // All lessons are done
                mInProgressChapterIndex = -1;
                mInProgressLessonIndex = -1;
            }
        }
    }

    private boolean validateChapters() {
        if (mCoursesResult == null || mCoursesResult.getCourse() == null || isEmpty(mCoursesResult.getCourse().getChapters())) {
            mInProgressChapterIndex = -1;
            mInProgressLessonIndex = -1;
            mCurrentLesson = null;
            return false;
        }
        return true;
    }

    private void setChapterAndLessonStatus() {
        if (!validateChapters()) return;

        UserManager userManager = AppManager.getInstance().getSessionData().getUserManager();
        int lastDoneChapter = userManager.getHighMark().getChapterId();
        int lastDoneLesson = userManager.getHighMark().getLessonId();
        mInProgressChapterIndex = -1;
        mInProgressLessonIndex = -1;

        List<Chapter> chapters = mCoursesResult.getCourse().getChapters();
        for (int i = 0; i < chapters.size(); ++i) {
            Chapter chapter = chapters.get(i);
            List<Lesson> lessons = chapter.getLessons();

            if (isEmpty(lessons)) {
                chapter.setStatus(COURSE_STATUS_DONE);
                continue;
            }

            int cmpChapter = chapter.getId() - lastDoneChapter;
            int chapterStatus;
            if (cmpChapter < 0) {
                chapterStatus = COURSE_STATUS_DONE;
            } else if (cmpChapter == 0) {
                if (lessons.get(lessons.size() - 1).getId() > lastDoneLesson) {
                    chapterStatus = COURSE_STATUS_INPROGRESS;
                    mInProgressChapterIndex = i;
                } else {
                    chapterStatus = COURSE_STATUS_DONE;
                    lastDoneLesson = 0;
                }
            } else if (mInProgressChapterIndex < 0) {
                chapterStatus = COURSE_STATUS_INPROGRESS;
                mInProgressChapterIndex = i;
            } else {
                chapterStatus = COURSE_STATUS_LOCKED;
            }
            chapter.setStatus(chapterStatus);

            for (int j = 0; j < lessons.size(); ++j) {
                Lesson lesson = lessons.get(j);
                if (chapterStatus != COURSE_STATUS_INPROGRESS) {
                    lesson.setStatus(chapterStatus);
                } else {
                    int cmpLesson = lesson.getId() - lastDoneLesson;
                    if (cmpLesson <= 0) {
                        lesson.setStatus(COURSE_STATUS_DONE);
                    } else if (mInProgressLessonIndex < 0){
                        lesson.setStatus(COURSE_STATUS_INPROGRESS);
                        mInProgressLessonIndex = j;
                    } else {
                        lesson.setStatus(COURSE_STATUS_LOCKED);
                    }
                }
            }
        }
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }
}
