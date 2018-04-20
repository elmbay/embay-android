package com.example.elmbay.manager;

import com.example.elmbay.model.Lesson;
import com.example.elmbay.model.SignInResult;

/**
 * Created by kgu on 4/12/18.
 */

public class SessionData {
    private SignInResult mSignInResult;
    private int mCurrentLessonIndex;

    public void setSignInResult(SignInResult signInResult) { mSignInResult = signInResult; }
    public SignInResult getSignInResult() { return mSignInResult; }

    public void setCurrentLessonIndex(int position) { mCurrentLessonIndex = position; }

    public Lesson getCurrentLesson() {
        if (mSignInResult != null) {
            Lesson lessons[] = mSignInResult.getLessons();
            if (lessons != null && mCurrentLessonIndex >= 0 && mCurrentLessonIndex < lessons.length) {
                return lessons[mCurrentLessonIndex];
            }
        }
        return null;
    }
}
