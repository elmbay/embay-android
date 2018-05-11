package com.example.elmbay.model;

import com.example.elmbay.operation.ListChaptersResult;
import com.example.elmbay.operation.SignInResult;

/**
 * Created by kgu on 4/12/18.
 */

public class SessionData {
    private SignInResult mSignInResult;
    private ListChaptersResult mListChaptersResult;
    private Lesson mCurrentLesson;
    private String mUserToken;
    private Long mUserTokenExpirationTime;
    private ProgressMark mLowMark;
    private ProgressMark mHighMark;
    private Long mChaptersExpirationTime;

    public void setSignInResult(SignInResult result) { mSignInResult = result; }
    public SignInResult getSignInResult() { return mSignInResult; }

    public void setListChaptersResult(ListChaptersResult result) { mListChaptersResult = result; }
    public ListChaptersResult getListChaptersResult() { return mListChaptersResult; }

    public void setCurrentLesson(Lesson lesson) { mCurrentLesson = lesson; }
    public Lesson getCurrentLesson() { return mCurrentLesson; }
}
