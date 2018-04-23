package com.example.elmbay.model;

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
