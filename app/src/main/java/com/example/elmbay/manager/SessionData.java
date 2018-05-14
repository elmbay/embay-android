package com.example.elmbay.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.model.ProgressMark;
import com.example.elmbay.operation.ListChaptersResult;
import com.example.elmbay.operation.SignInResult;

import java.util.List;

/**
 * The class manages both cache and persistence data for the session
 *
 * Created by kgu on 4/12/18.
 */

public class SessionData {
    public static final int HOUR_TO_MILLIS = 3600000;

    // persistence keys
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "pwd";
    private static final String KEY_USER_TOKEN = "ut";
    private static final String KEY_USER_TOKEN_EXPIRATION_TIME = "ut_exp";
    private static final String KEY_PROGRESS_HIGHMARK_CID = "high_cid";
    private static final String KEY_PROGRESS_HIGHMARK_LID = "high_lid";
    private static final String KEY_NEXT_LOAD_TIME = "load_tm";

    public static final int STATUS_LOCKED = 0;
    public static final int STATUS_READY = 1;
    public static final int STATUS_INPROGRESS = 2;
    public static final int STATUS_DONE = 3;

    private Context mContext;
    private SharedPreferences mPersistenceStore;

    private SignInResult mSignInResult;
    private String mUserToken;
    private long mUserTokenExpirationTime;

    private ListChaptersResult mListChaptersResult;
    private boolean mHasValidChapters;
    private long mNextLoadTime;
    private int mReadyChapterIndex = -1;
    private int mReadyLessonIndex = -1;
    private Lesson mCurrentLesson;
    private ProgressMark mHighMark;

    public SessionData(Context context) {
        mContext = context;
        mPersistenceStore = PersistenceManager.getSharedPersistenceStore(context);
        fromPersistenceStore();
    }

    // PII data stays in persistence store only
    public String getEmail() { return mPersistenceStore.getString(KEY_EMAIL, ""); }
    public void setEmail(String email) { PersistenceManager.setString(mPersistenceStore, KEY_EMAIL, email); }

    public String getPhone() { return mPersistenceStore.getString(KEY_PHONE, ""); }
    public void setPhone(String phone) { PersistenceManager.setString(mPersistenceStore, KEY_PHONE, phone); }

    public String getPassword() { return mPersistenceStore.getString(KEY_PASSWORD, ""); }
    public void setPassword(String password) { PersistenceManager.setString(mPersistenceStore, KEY_PASSWORD, password); }

    public SignInResult getSignInResult() { return mSignInResult; }
    public void setSignInResult(SignInResult result) { mSignInResult = result; }

    public ListChaptersResult getListChaptersResult() { return mListChaptersResult; }
    public void setListChaptersResult(ListChaptersResult result) {
        mListChaptersResult = result;
        if (!validChapters()) return;

        int lastDoneChapter = mHighMark.getChapterId();
        int lastDoneLesson = mHighMark.getLessonId();
        mReadyChapterIndex = -1;
        mReadyLessonIndex = -1;

        List<Chapter> chapters = result.getChapters();
        for (int i = 0; i < chapters.size(); ++i) {
            Chapter chapter = chapters.get(i);
            List<Lesson> lessons = chapter.getLessons();

            if (lessons == null || lessons.size() <= 0) {
                chapter.setStatus(STATUS_DONE);
                continue;
            }

            int cmpChapter = chapter.getId() - lastDoneChapter;
            int chapterStatus;
            if (cmpChapter < 0) {
                chapterStatus = STATUS_DONE;
            } else if (cmpChapter == 0) {
                if (lessons.get(lessons.size() - 1).getId() > lastDoneLesson) {
                    chapterStatus = STATUS_INPROGRESS;
                    mReadyChapterIndex = i;
                } else {
                    chapterStatus = STATUS_DONE;
                    lastDoneLesson = 0;
                }
            } else if (mReadyChapterIndex < 0) {
                chapterStatus = STATUS_INPROGRESS;
                mReadyChapterIndex = i;
            } else {
                chapterStatus = STATUS_LOCKED;
            }
            chapter.setStatus(chapterStatus);

            for (int j = 0; j < lessons.size(); ++j) {
                Lesson lesson = lessons.get(j);
                if (chapterStatus != STATUS_INPROGRESS) {
                    lesson.setStatus(chapterStatus);
                } else {
                    int cmpLesson = lesson.getId() - lastDoneLesson;
                    if (cmpLesson <= 0) {
                        lesson.setStatus(STATUS_DONE);
                    } else if (mReadyLessonIndex < 0){
                        lesson.setStatus(STATUS_INPROGRESS);
                        mReadyLessonIndex = j;
                    } else {
                        lesson.setStatus(STATUS_LOCKED);
                    }
                }
            }
        }
    }

    public Lesson getCurrentLesson() { return mCurrentLesson; }
    public void setCurrentLesson(Lesson lesson) { mCurrentLesson = lesson; }

    public String getUserToken() { return mUserToken; }
    public void setUserToken(String userToken) {
        if (mUserToken == null || !mUserToken.equals(userToken)) {
            mUserToken = userToken;
            PersistenceManager.setString(mPersistenceStore, KEY_USER_TOKEN, userToken);
        }
    }

    public long getUserTokenExpirationTime() { return mUserTokenExpirationTime; }
    public void setUserTokenExpirationTime(long time) {
        if (mUserTokenExpirationTime != time) {
            mUserTokenExpirationTime = time;
            PersistenceManager.setLong(mPersistenceStore, KEY_USER_TOKEN_EXPIRATION_TIME, time);
        }
    }

    public ProgressMark getHighMark() { return mHighMark; }
    public void setHighMark(ProgressMark mark) {
        mHighMark = mark;
        PersistenceManager.setInt(mPersistenceStore, KEY_PROGRESS_HIGHMARK_CID, mHighMark.getChapterId());
        PersistenceManager.setInt(mPersistenceStore, KEY_PROGRESS_HIGHMARK_LID, mHighMark.getLessonId());
    }
    public void doneWithCurrentLesson() {
        // We are here only when we have valid chapter and lesson
        List<Chapter> chapters = mListChaptersResult.getChapters();
        Chapter chapter = chapters.get(mReadyChapterIndex);
        List<Lesson> lessons = chapter.getLessons();
        Lesson lesson = lessons.get(mReadyLessonIndex);

        if (lesson != mCurrentLesson) {
            return;
        }

        mHighMark.setChapterId(chapter.getId());
        mHighMark.setLessonId(lesson.getId());

        lesson.setStatus(STATUS_DONE);
        if (mReadyLessonIndex < lessons.size() - 1) {
            lessons.get(++mReadyLessonIndex).setStatus(STATUS_INPROGRESS);
        } else {
            chapter.setStatus(STATUS_DONE);
            if (mReadyChapterIndex < chapters.size() - 1) {
                chapters.get(++mReadyChapterIndex).setStatus(STATUS_INPROGRESS);
                chapters.get(mReadyChapterIndex).getLessons().get(0).setStatus(STATUS_INPROGRESS);
                mReadyLessonIndex = 0;
            } else {
                mReadyChapterIndex = -1;
                mReadyLessonIndex = -1;
            }
        }

        //TODO: update server side
    }

    public long getNextLoadTime() { return mNextLoadTime; }
    public void setNextLoadTime(long time) {
        mNextLoadTime = time;
        PersistenceManager.setLong(mPersistenceStore, KEY_NEXT_LOAD_TIME, mNextLoadTime);
    }

    public int getReadyChapterIndex() { return mReadyChapterIndex; }
    public int getReadyLessonIndex() { return mReadyLessonIndex; }

    private void fromPersistenceStore() {
        mUserToken = mPersistenceStore.getString(KEY_USER_TOKEN, "");
        mUserTokenExpirationTime = mPersistenceStore.getLong(KEY_USER_TOKEN_EXPIRATION_TIME, 0);
        mHighMark = new ProgressMark();
        mHighMark.setChapterId(mPersistenceStore.getInt(KEY_PROGRESS_HIGHMARK_CID, 0));
        mHighMark.setLessonId(mPersistenceStore.getInt(KEY_PROGRESS_HIGHMARK_LID, 0));
        mNextLoadTime = mPersistenceStore.getLong(KEY_NEXT_LOAD_TIME, System.currentTimeMillis() + HOUR_TO_MILLIS);
    }

    private boolean validChapters() {
        if (mListChaptersResult == null || mListChaptersResult.getChapters() == null || mListChaptersResult.getChapters().size() == 0) {
            mReadyChapterIndex = -1;
            mReadyLessonIndex = -1;
            mCurrentLesson = null;
            mHasValidChapters = false;
            return false;
        }
        return true;
    }
}
