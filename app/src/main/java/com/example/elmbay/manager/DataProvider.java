package com.example.elmbay.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.operation.ListChaptersResult;
import com.example.elmbay.model.ProgressMark;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgu on 4/17/18.
 */

public class DataProvider {

    public static ListChaptersResult getSignInResult(Context context) {
        ListChaptersResult result = new ListChaptersResult();

        result.setNextLoadInHours(2);

        List<Chapter> chapters = new ArrayList<Chapter>();

        List<Lesson> lessons = new ArrayList<Lesson>();
        lessons.add(getLesson("01"));
        lessons.add(getLesson("02"));
        lessons.add(getLesson("03"));
        chapters.add(createChapter("01", "English 101", "Kids", lessons));

        lessons = new ArrayList<Lesson>();
        lessons.add(getLesson("04"));
        chapters.add(createChapter("02", "English 101", "Nature", lessons));

        lessons = new ArrayList<Lesson>();
        lessons.add(getLesson("05"));
        chapters.add(createChapter("03", "English 101", "Business", lessons));

        result.setChapters(chapters);

        Gson gson = new Gson();
        String json = gson.toJson(chapters);

        Log.w("DataProvider", json);

        return result;
    }

    public static ProgressMark getUserProgress() {
        ProgressMark progress = new ProgressMark();
        return progress;
    }

    public static Lesson getLesson(String id) {
        Lesson lesson = null;
        switch (id) {
            case "01":
                lesson = createLesson(id, "01", "Change Channel","change_channel_01_vd", "change_channel_01_ad", "change_channel_01_tx");
                break;
            case "02":
                lesson = createLesson(id, "01", "Juicy","juicy_02_vd", "juicy_02_ad", "juicy_02_tx");
                break;
            case "03":
                lesson = createLesson(id, "01", "literally","literally_03_vd", "literally_03_ad", "literally_03_tx");
                break;
            case "04":
                lesson = createLesson(id, "02", "Blue Sea","http://abhiandroid-8fb4.kxcdn.com/ui/wp-content/uploads/2016/04/videoviewtestingvideo.mp4", null, null);
                break;
            case "05":
                lesson = createLesson(id, "03", "Venice","ppcash", null, null);
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

    private static Lesson createLesson(@NonNull String id, @NonNull String chapterId, @NonNull String keyword, String videoString, String audioString, String transcriptString) {
        Lesson lesson = new Lesson();

        lesson.setId(id);
        lesson.setChapterId(chapterId);
        lesson.setKeyword(keyword);
        lesson.setVideo(videoString);
        lesson.setAudio(audioString);
        lesson.setTranscript(transcriptString);

        return lesson;
    }
}
