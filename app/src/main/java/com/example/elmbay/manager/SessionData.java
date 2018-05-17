package com.example.elmbay.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.model.ProgressMark;
import com.example.elmbay.operation.ListChaptersResult;
import com.example.elmbay.operation.SignInResult;

import java.io.File;
import java.util.List;

/**
 * The class manages both cache and persistence data for the session
 *
 * Created by kgu on 4/12/18.
 */

public class SessionData {
    public static final int HOUR_TO_MILLIS = 3600000;

    // persistence keys
    private static final String KEY_UID = "uid";
    private static final String KEY_UID_TYPE = "uid_type";
    private static final String KEY_PASSWORD = "pwd";
    private static final String KEY_USER_TOKEN = "ut";
    private static final String KEY_USER_TOKEN_EXPIRATION_TIME = "ut_exp";
    private static final String KEY_PROGRESS_HIGHMARK_CID = "high_cid";
    private static final String KEY_PROGRESS_HIGHMARK_LID = "high_lid";
    private static final String KEY_NEXT_LOAD_TIME = "load_tm";

    public static final String UID_TYPE_UNKNOWN = "U";
    public static final String UID_TYPE_EMAIL = "E";
    public static final String UID_TYPE_PHONE = "P";

    public static final int COURSE_STATUS_LOCKED = 0;
    public static final int COURSE_STATUS_INPROGRESS = 1;
    public static final int COURSE_STATUS_DONE = 2;

    private SharedPreferences mPersistenceStore;
    private File mOutputFileDir;

    private String mUserToken;
    private long mUserTokenExpirationTime;
    private ProgressMark mHighMark;

    private ListChaptersResult mListChaptersResult;
    private long mNextLoadTime;
    private int mInProgressChapterIndex = -1;
    private int mInProgressLessonIndex = -1;
    private Lesson mCurrentLesson;

    SessionData(@NonNull Context context) {
        mPersistenceStore = PersistenceManager.getSharedPersistenceStore(context);
        fromPersistenceStore();

        // child (the second param) must matches "path" in xml/filepaths.xml
        mOutputFileDir = new File(context.getExternalCacheDir(), "output");
        if (!mOutputFileDir.exists()) {
            mOutputFileDir.mkdir();
        }
    }

    public void logout() {
        mUserToken = "";
    }

    public File getOutputFileDir() { return mOutputFileDir; }

    // PII data stays in persistence store only
    public @NonNull String getUid() { return mPersistenceStore.getString(KEY_UID, ""); }
    public String getUidType() { return mPersistenceStore.getString(KEY_UID_TYPE, UID_TYPE_UNKNOWN); }
    public void setUid(@NonNull String uid, String uidType) {
        PersistenceManager.setString(mPersistenceStore, KEY_UID, uid);
        PersistenceManager.setString(mPersistenceStore, KEY_UID_TYPE, uidType);
    }

    public @NonNull String getPassword() { return mPersistenceStore.getString(KEY_PASSWORD, ""); }
    public void setPassword(@NonNull String password) { PersistenceManager.setString(mPersistenceStore, KEY_PASSWORD, password); }

    public void setSignInResult(@NonNull SignInResult result) {
        setUserToken(result.getUserToken());
        setUserTokenExpirationTime(System.currentTimeMillis() + result.getUserTokenLifeInHours() * SessionData.HOUR_TO_MILLIS);
        advanceHighMark(result.getHighMark());
    }

    public @NonNull String getUserToken() { return mUserToken; }
    private void setUserToken(@Nullable String userToken) {
        if (userToken != null && !mUserToken.equals(userToken)) {
            mUserToken = userToken;
            PersistenceManager.setString(mPersistenceStore, KEY_USER_TOKEN, userToken);
        }
    }

    public long getUserTokenExpirationTime() { return mUserTokenExpirationTime; }
    private void setUserTokenExpirationTime(long time) {
        mUserTokenExpirationTime = time;
        PersistenceManager.setLong(mPersistenceStore, KEY_USER_TOKEN_EXPIRATION_TIME, time);
    }

    public @NonNull ProgressMark getHighMark() { return mHighMark; }
    private void advanceHighMark(@Nullable ProgressMark mark) {
        if (mark != null) {
            advanceHighMark(mark.getChapterId(), mark.getLessonId());
        }
    }
    public void advanceHighMark(int chapterId, int lessonId) {
        boolean needUpdate = false;
        if (mHighMark.getChapterId() < chapterId) {
            needUpdate = true;
        } else if (mHighMark.getChapterId() == chapterId) {
            if (mHighMark.getLessonId() < lessonId) {
                needUpdate = true;
            }
        }
        if (needUpdate) {
            mHighMark.setChapterId(chapterId);
            mHighMark.setLessonId(lessonId);
            persistHighMark();
        }
    }
    private void persistHighMark() {
        PersistenceManager.setInt(mPersistenceStore, KEY_PROGRESS_HIGHMARK_CID, mHighMark.getChapterId());
        PersistenceManager.setInt(mPersistenceStore, KEY_PROGRESS_HIGHMARK_LID, mHighMark.getLessonId());
        //TODO: update server side - this may not needed if server can record highmark upon receiving a load request
    }

    public long getNextLoadTime() { return mNextLoadTime; }
    private void setNextLoadTime(long time) {
        mNextLoadTime = time;
        PersistenceManager.setLong(mPersistenceStore, KEY_NEXT_LOAD_TIME, mNextLoadTime);
    }

    public @Nullable ListChaptersResult getListChaptersResult() { return mListChaptersResult; }
    public void setListChaptersResult(@Nullable ListChaptersResult result) {
        mListChaptersResult = result;
        long now = System.currentTimeMillis();
        long nextLoadTime = now + HOUR_TO_MILLIS;
        if (result != null) {
            advanceHighMark(result.getHighMark());
            if (result.getNextLoadInHours() > 0) {
                nextLoadTime = now + result.getNextLoadInHours() * HOUR_TO_MILLIS;
            }
        }
        setNextLoadTime(nextLoadTime);
        setChapterAndLessonStatus();
    }

    public int getInProgressChapterIndex() { return mInProgressChapterIndex; }

    public @Nullable Lesson getCurrentLesson() { return mCurrentLesson; }
    public void setCurrentLesson(@Nullable Lesson lesson) { mCurrentLesson = lesson; }

    public void finishedCurrentLesson() {

        // No highMark change if the current lesson is not the lesson marked as in progress

        if (mCurrentLesson == null || mInProgressChapterIndex < 0 || mInProgressLessonIndex < 0) {
            return;
        }

        List<Chapter> chapters = mListChaptersResult.getChapters();
        if (Helper.isEmpty(chapters) || chapters.size() <= mInProgressChapterIndex) {
            return;
        }
        Chapter chapter = chapters.get(mInProgressChapterIndex);

        List<Lesson> lessons = chapter.getLessons();
        if (Helper.isEmpty(lessons) || lessons.size() <= mInProgressLessonIndex) {
            return;
        }
        Lesson lesson = lessons.get(mInProgressLessonIndex);

        if (lesson != mCurrentLesson) {
            return;
        }

        advanceHighMark(chapter.getId(), lesson.getId());

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
                if (Helper.isEmpty(lessons)) {
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

    private void fromPersistenceStore() {
        mUserToken = mPersistenceStore.getString(KEY_USER_TOKEN, "");
        mUserTokenExpirationTime = mPersistenceStore.getLong(KEY_USER_TOKEN_EXPIRATION_TIME, 0);
        mHighMark = new ProgressMark();
        mHighMark.setChapterId(mPersistenceStore.getInt(KEY_PROGRESS_HIGHMARK_CID, 0));
        mHighMark.setLessonId(mPersistenceStore.getInt(KEY_PROGRESS_HIGHMARK_LID, 0));
        mNextLoadTime = mPersistenceStore.getLong(KEY_NEXT_LOAD_TIME, System.currentTimeMillis() + HOUR_TO_MILLIS);
    }

    private boolean validateChapters() {
        if (mListChaptersResult == null || Helper.isEmpty(mListChaptersResult.getChapters())) {
            mInProgressChapterIndex = -1;
            mInProgressLessonIndex = -1;
            mCurrentLesson = null;
            return false;
        }
        return true;
    }

    private void setChapterAndLessonStatus() {
        if (!validateChapters()) return;

        int lastDoneChapter = mHighMark.getChapterId();
        int lastDoneLesson = mHighMark.getLessonId();
        mInProgressChapterIndex = -1;
        mInProgressLessonIndex = -1;

        List<Chapter> chapters = mListChaptersResult.getChapters();
        for (int i = 0; i < chapters.size(); ++i) {
            Chapter chapter = chapters.get(i);
            List<Lesson> lessons = chapter.getLessons();

            if (Helper.isEmpty(lessons)) {
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
}
