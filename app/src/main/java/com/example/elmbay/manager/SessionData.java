package com.example.elmbay.manager;

import com.example.elmbay.model.Lesson;
import com.example.elmbay.model.SignInResult;

/**
 * Created by kgu on 4/12/18.
 */

public class SessionData {
    private SignInResult mSignInResult;
    private Lesson mCurrentLesson;

    public void setSignInResult(SignInResult signInResult) { mSignInResult = signInResult; }
    public SignInResult getSignInResult() { return mSignInResult; }

    public void setCurrentLesson(Lesson lesson) { mCurrentLesson = lesson; }
    public Lesson getCurrentLesson() { return mCurrentLesson; }
}
