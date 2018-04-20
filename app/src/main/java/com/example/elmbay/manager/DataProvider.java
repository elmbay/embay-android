package com.example.elmbay.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.elmbay.model.Lesson;
import com.example.elmbay.model.SignInResult;
import com.example.elmbay.model.UserProgress;

/**
 * Created by kgu on 4/17/18.
 */

public class DataProvider {
    public static SignInResult getSignInResult(Context context) {
        SignInResult result = new SignInResult();

        result.setUserProgress(getUserProgress());

        Lesson lessons[] = new Lesson[5];
        lessons[0] = getLesson(context, "01");
        lessons[1] = getLesson(context, "02");
        lessons[2] = getLesson(context, "03");
        lessons[3] = getLesson(context, "04");
        lessons[4] = getLesson(context, "05");
        result.setLessons(lessons);

        return result;
    }

    public static UserProgress getUserProgress() {
        UserProgress progress = new UserProgress();
        return progress;
    }

    public static Lesson getLesson(Context context, String id) {
        Lesson lesson = null;
        switch (id) {
            case "01":
                lesson = createLesson(context, id, "English 101", "Change Channel","change_channel_01_vd", "change_channel_01_ad", "change_channel_01_tx");
                break;
            case "02":
                lesson = createLesson(context, id, "English 101", "Juicy","juicy_02_vd", "juicy_02_ad", "juicy_02_tx");
                break;
            case "03":
                lesson = createLesson(context, id, "English 101", "literally","literally_03_vd", "literally_03_ad", "literally_03_tx");
                break;
            case "04":
                lesson = createLesson(context, id, "English 101", "Blue Sea","http://abhiandroid-8fb4.kxcdn.com/ui/wp-content/uploads/2016/04/videoviewtestingvideo.mp4", null, null);
                break;
            case "05":
                lesson = createLesson(context, id, "English 101", "Venice","ppcash", null, null);
                break;
        }
        return lesson;
    }

    private static Lesson createLesson(Context context, @NonNull String id, @NonNull String course, @NonNull String subject, String videoString, String audioString, String transcriptString) {
        Lesson lesson = new Lesson();

        lesson.setId(id);
        lesson.setCourse(course);
        lesson.setSubject(subject);
        lesson.setVideo(context, videoString);
        lesson.setAudio(context, audioString);
        lesson.setTranscript(context, transcriptString);

        return lesson;
    }
}
