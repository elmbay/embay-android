package com.example.elmbay.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.model.SignInResult;
import com.example.elmbay.model.UserProgress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgu on 4/17/18.
 */

public class DataProvider {

    public static SignInResult getSignInResult(Context context) {
        SignInResult result = new SignInResult();

        result.setUserProgress(getUserProgress());

        List<Chapter> chapters = new ArrayList<Chapter>();

        List<Lesson> lessons = new ArrayList<Lesson>();
        lessons.add(getLesson(context, "01"));
        lessons.add(getLesson(context, "02"));
        lessons.add(getLesson(context, "03"));
        chapters.add(createChapter("01", "English 101", "Kids", lessons));

        lessons = new ArrayList<Lesson>();
        lessons.add(getLesson(context, "04"));
        chapters.add(createChapter("02", "English 101", "Nature", lessons));

        lessons = new ArrayList<Lesson>();
        lessons.add(getLesson(context, "05"));
        chapters.add(createChapter("03", "English 101", "Business", lessons));

        result.setChapters(chapters);

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
                lesson = createLesson(context, id, "01", "Change Channel","change_channel_01_vd", "change_channel_01_ad", "change_channel_01_tx");
                break;
            case "02":
                lesson = createLesson(context, id, "01", "Juicy","juicy_02_vd", "juicy_02_ad", "juicy_02_tx");
                break;
            case "03":
                lesson = createLesson(context, id, "01", "literally","literally_03_vd", "literally_03_ad", "literally_03_tx");
                break;
            case "04":
                lesson = createLesson(context, id, "02", "Blue Sea","http://abhiandroid-8fb4.kxcdn.com/ui/wp-content/uploads/2016/04/videoviewtestingvideo.mp4", null, null);
                break;
            case "05":
                lesson = createLesson(context, id, "03", "Venice","ppcash", null, null);
                break;
        }
        return lesson;
    }

    private static Chapter createChapter( @NonNull String id, @NonNull String course, @NonNull String topic, List<Lesson> lessons) {
        Chapter chapter = new Chapter();
        chapter.setId(id);
        chapter.setCourse(course);
        chapter.setTopic(topic);
        chapter.setLessons(lessons);
        return chapter;
    }

    private static Lesson createLesson(Context context, @NonNull String id, @NonNull String chapterId, @NonNull String keyword, String videoString, String audioString, String transcriptString) {
        Lesson lesson = new Lesson();

        lesson.setId(id);
        lesson.setChapterId(chapterId);
        lesson.setKeyword(keyword);
        lesson.setVideo(context, videoString);
        lesson.setAudio(context, audioString);
        lesson.setTranscript(context, transcriptString);

        return lesson;
    }
}
