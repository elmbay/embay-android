package com.example.elmbay.manager;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.operation.GetCourseResult;

import java.util.List;

import static com.example.elmbay.manager.PersistenceManager.KEY_NEXT_LOAD_TIME;
import static com.example.elmbay.manager.SessionData.HOUR_TO_MILLIS;

/**
 *
 * Created by kgu on 5/21/18.
 */

public class CourseManager {

    public static final int COURSE_STATUS_UNKNOWN = 0;
    public static final int COURSE_STATUS_INPROGRESS = 1;
    public static final int COURSE_STATUS_DONE = 2;
    public static final int COURSE_STATUS_LOCKED = 3;

    private SharedPreferences mPersistenceStore;
    private GetCourseResult mCourseResult;
    private long mNextLoadTime;
    private int mInProgressChapterIndex = -1;
    private int mInProgressLessonIndex = -1;
    private Chapter mChapterSelected;
    private Lesson mLessonSelected;

    CourseManager(SharedPreferences persistenceStore) {
        mPersistenceStore = persistenceStore;
        mNextLoadTime = mPersistenceStore.getLong(KEY_NEXT_LOAD_TIME, System.currentTimeMillis() + HOUR_TO_MILLIS);
    }

    void logout() {
        mCourseResult = null;
    }

    public long getNextLoadTime() { return mNextLoadTime; }
    private void setNextLoadTime(long time) {
        mNextLoadTime = time;
        PersistenceManager.setLong(mPersistenceStore, KEY_NEXT_LOAD_TIME, mNextLoadTime);
    }

    public @Nullable GetCourseResult getCourseResult() { return mCourseResult; }
    public void setCourseResult(@NonNull GetCourseResult result) {
        mCourseResult = result;

        UserManager userManager = AppManager.getInstance().getSessionData().getUserManager();
        userManager.syncHighMark(result.getHighMark());

        long now = System.currentTimeMillis();
        long nextLoadTime;
        if (result.getNextLoadInHours() > 0) {
            nextLoadTime = now + result.getNextLoadInHours() * HOUR_TO_MILLIS;
        } else {
            nextLoadTime = now + HOUR_TO_MILLIS;
        }
        setNextLoadTime(nextLoadTime);

        setCoursesStatus();
    }

    public void setLessonsResult(@NonNull Chapter result) {
        List<Lesson> lessons = result.getLessons();
        if (isEmpty(lessons) || mCourseResult == null || mCourseResult.getCourse() == null || isEmpty(mCourseResult.getCourse().getChapters())) {
            return;
        }
        int cid = result.getId();
        for (Chapter chapter : mCourseResult.getCourse().getChapters()) {
            if (chapter.getId() == cid && isEmpty(chapter.getLessons())) {
                chapter.setLessons(lessons);
                int status = chapter.getStatus();
                for (int i = 0; i < lessons.size(); ++i) {
                    lessons.get(i).setStatus(status);
                    if (status == COURSE_STATUS_INPROGRESS) {
                        mInProgressLessonIndex = 0;
                        status = COURSE_STATUS_LOCKED;
                    }
                }
                break;
            }
        }
    }

    public int getInProgressChapterIndex() { return mInProgressChapterIndex; }
    public int getInProgressLessonIndex() { return mInProgressLessonIndex; }

    public @Nullable Chapter getChapterSelected() { return mChapterSelected; }
    public void setChapterSelected(@Nullable Chapter chapter) { mChapterSelected = chapter; }
    public @Nullable Lesson getLessonSelected() { return mLessonSelected; }
    public void setLessonSelected(@Nullable Lesson lesson) { mLessonSelected = lesson; }

    public void finishedSelectedLesson() {

        // No highMark change if the current lesson is not the lesson marked as in progress

        if (mLessonSelected == null || mInProgressLessonIndex < 0) {
            return;
        }

        List<Chapter> chapters = mCourseResult.getCourse().getChapters();
        Chapter chapter = chapters.get(mInProgressChapterIndex);

        List<Lesson> lessons = chapter.getLessons();
        Lesson lesson = lessons.get(mInProgressLessonIndex);

        lesson.setStatus(COURSE_STATUS_DONE);
        UserManager userManager = AppManager.getInstance().getSessionData().getUserManager();
        userManager.syncHighMark(chapter.getId(), lesson.getId());

        // Update chapter(optional) and lesson state, and locate the next in-progress lesson
        if (++mInProgressLessonIndex < lessons.size()) {
            // Advance to the next lesson in the same chapter
            lessons.get(mInProgressLessonIndex).setStatus(COURSE_STATUS_INPROGRESS);
        } else {
            chapter.setStatus(COURSE_STATUS_DONE);
            if (++mInProgressChapterIndex < chapters.size()) {
                // Advance to the next chapter
                chapters.get(mInProgressChapterIndex).setStatus(COURSE_STATUS_INPROGRESS);
                mInProgressLessonIndex = -1;
                lessons = chapters.get(mInProgressChapterIndex).getLessons();
                if (!isEmpty(lessons)) {
                    mInProgressLessonIndex = 0;
                    lessons.get(0).setStatus(COURSE_STATUS_INPROGRESS);
                }
            } else {
                // All lessons are done
                mInProgressChapterIndex = -1;
                mInProgressLessonIndex = -1;
            }
        }
    }

    private boolean validateChapters() {
        if (mCourseResult == null || mCourseResult.getCourse() == null || isEmpty(mCourseResult.getCourse().getChapters())) {
            mInProgressChapterIndex = -1;
            mInProgressLessonIndex = -1;
            mLessonSelected = null;
            return false;
        }
        return true;
    }

    private void setCoursesStatus() {
        if (!validateChapters()) return;

        UserManager userManager = AppManager.getInstance().getSessionData().getUserManager();
        int lastDoneChapter = userManager.getHighMark().getChapterId();
        int lastDoneLesson = userManager.getHighMark().getLessonId();

        mInProgressChapterIndex = -1;
        mInProgressLessonIndex = -1;

        List<Chapter> chapters = mCourseResult.getCourse().getChapters();

        // Find the index to the last all-finished chapter and mark it and the older chapters as done
        int idx = -1;
        if (lastDoneChapter > 0) {
            while (++idx < chapters.size()) {
                Chapter chapter = chapters.get(idx);
                if (chapter.getId() == lastDoneChapter) {
                    // This chapter may still have un-done lessons
                    --idx;
                    break;
                }
                setChapterStatus(chapter, COURSE_STATUS_DONE);
            }
        }

        // Find the index to the in-progress chapter
        while (++idx < chapters.size()) {
            Chapter chapter = chapters.get(idx);
            chapter.setStatus(COURSE_STATUS_INPROGRESS);
            setInProgressLessonsStatus(chapter, lastDoneLesson);
            if (chapter.getStatus() == COURSE_STATUS_INPROGRESS) {
                mChapterSelected = chapter;
                mInProgressChapterIndex = idx;
                break;
            }
            lastDoneLesson = 0;
        }

        // Mark all newer chapters locked
        while (++idx < chapters.size()) {
            setChapterStatus(chapters.get(idx), COURSE_STATUS_LOCKED);
        }
    }

    private void setChapterStatus(Chapter chapter, int status) {
        if (status == COURSE_STATUS_INPROGRESS) {

        } else {
            chapter.setStatus(status);
            List<Lesson> lessons = chapter.getLessons();
            if (!isEmpty(lessons)) {
                for (Lesson lesson : lessons) {
                    lesson.setStatus(status);
                }
            }
        }
    }

    private void setInProgressLessonsStatus(@NonNull Chapter chapter, int lastDoneLessonId) {
        List<Lesson> lessons = chapter.getLessons();
        if (isEmpty(lessons)) {
            return;
        }

        // Find the index to the last done lesson and mark the older lessons (inclusive) as done
        int idx = -1;
        boolean hasNewLesson = false;
        if (lastDoneLessonId > 0) {
            while (++idx < lessons.size()) {
                lessons.get(idx).setStatus(COURSE_STATUS_DONE);
                if (lessons.get(idx).getId() == lastDoneLessonId) {
                    break;
                }
            }
        }

        if (++idx < lessons.size()) {
            hasNewLesson = true;
            // Mark the lesson next to the last done one as in-progess
            lessons.get(idx).setStatus(COURSE_STATUS_INPROGRESS);
            mLessonSelected = lessons.get(idx);
            mInProgressLessonIndex = idx;

            // Mark the newer lessons as locked
            while (++idx < lessons.size()) {
                lessons.get(idx).setStatus(COURSE_STATUS_LOCKED);
            }
        }

        chapter.setStatus(hasNewLesson ? COURSE_STATUS_INPROGRESS : COURSE_STATUS_DONE);
    }

    private static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }
}
