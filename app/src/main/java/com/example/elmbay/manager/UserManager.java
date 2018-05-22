package com.example.elmbay.manager;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.elmbay.model.ProgressMark;
import com.example.elmbay.operation.SignInResult;

import static com.example.elmbay.manager.PersistenceManager.KEY_PASSWORD;
import static com.example.elmbay.manager.PersistenceManager.KEY_PROGRESS_HIGHMARK_CID;
import static com.example.elmbay.manager.PersistenceManager.KEY_PROGRESS_HIGHMARK_CSID;
import static com.example.elmbay.manager.PersistenceManager.KEY_PROGRESS_HIGHMARK_LID;
import static com.example.elmbay.manager.PersistenceManager.KEY_UID;
import static com.example.elmbay.manager.PersistenceManager.KEY_UID_TYPE;
import static com.example.elmbay.manager.PersistenceManager.KEY_USER_TOKEN;
import static com.example.elmbay.manager.PersistenceManager.KEY_USER_TOKEN_EXPIRATION_TIME;

/**
 *
 * Created by kgu on 5/21/18.
 */

public class UserManager {

    public static final String UID_TYPE_UNKNOWN = "U";
    public static final String UID_TYPE_EMAIL = "E";
    public static final String UID_TYPE_PHONE = "P";

    private SharedPreferences mPersistenceStore;
    private String mUserToken;
    private long mUserTokenExpirationTime;
    private ProgressMark mHighMark;

    UserManager(SharedPreferences persistenceStore) {
        mPersistenceStore = persistenceStore;
        fromPersistenceStore();
    }

    private void fromPersistenceStore() {
        mUserToken = mPersistenceStore.getString(KEY_USER_TOKEN, "");
        mUserTokenExpirationTime = mPersistenceStore.getLong(KEY_USER_TOKEN_EXPIRATION_TIME, 0);
        mHighMark = new ProgressMark();
        mHighMark.setCourseId(mPersistenceStore.getInt(KEY_PROGRESS_HIGHMARK_CSID, 0));
        mHighMark.setChapterId(mPersistenceStore.getInt(KEY_PROGRESS_HIGHMARK_CID, 0));
        mHighMark.setLessonId(mPersistenceStore.getInt(KEY_PROGRESS_HIGHMARK_LID, 0));
    }

    // PII data stays in persistence store only
    public @NonNull
    String getUid() { return mPersistenceStore.getString(KEY_UID, ""); }
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
    void advanceHighMark(@Nullable ProgressMark mark) {
        if (mark != null) {
            if (mHighMark.getCourseId() != mark.getCourseId()) {
                // first time use or user has changed the course, change highMark to force update
                mHighMark.setCourseId(mark.getCourseId());
                mHighMark.setChapterId(mark.getChapterId() - 1);
            }
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
        PersistenceManager.setInt(mPersistenceStore, KEY_PROGRESS_HIGHMARK_CSID, mHighMark.getCourseId());
        PersistenceManager.setInt(mPersistenceStore, KEY_PROGRESS_HIGHMARK_CID, mHighMark.getChapterId());
        PersistenceManager.setInt(mPersistenceStore, KEY_PROGRESS_HIGHMARK_LID, mHighMark.getLessonId());
        //TODO: update server side - this may not needed if server can record highmark upon receiving a load request
    }

    void logout() {
        mUserToken = "";
    }
}
