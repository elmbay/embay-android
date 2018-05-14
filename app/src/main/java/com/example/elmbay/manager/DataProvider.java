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
        lessons.add(getLesson(1));
        lessons.add(getLesson(2));
        lessons.add(getLesson(3));
        chapters.add(createChapter(1, "English 101", "Kids", lessons));

        lessons = new ArrayList<Lesson>();
        lessons.add(getLesson(4));
        chapters.add(createChapter(2, "English 101", "Nature", lessons));

        lessons = new ArrayList<Lesson>();
        lessons.add(getLesson(5));
        chapters.add(createChapter(3, "English 101", "Business", lessons));

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

    public static Lesson getLesson(int id) {
        Lesson lesson = null;
        switch (id) {
            case 1:
                lesson = createLesson(id, 1, "Change Channel","change_channel_01_vd", "change_channel_01_ad", "change_channel_01_tx");
                break;
            case 2:
                lesson = createLesson(id, 1, "Juicy","juicy_02_vd", "juicy_02_ad", "juicy_02_tx");
                break;
            case 3:
                lesson = createLesson(id, 1, "literally","literally_03_vd", "literally_03_ad", "literally_03_tx");
                break;
            case 4:
                lesson = createLesson(id, 2, "Blue Sea","http://abhiandroid-8fb4.kxcdn.com/ui/wp-content/uploads/2016/04/videoviewtestingvideo.mp4", null, null);
                break;
            case 5:
                lesson = createLesson(id, 3, "Venice","ppcash", null, null);
                break;
        }
        return lesson;
    }

    private static Chapter createChapter( @NonNull int id, @NonNull String course, @NonNull String topic, List<Lesson> lessons) {
        Chapter chapter = new Chapter();
        chapter.setId(id);
        chapter.setCourse(course);
        chapter.setTopic(topic);
        chapter.setLessons(lessons);
        return chapter;
    }

    private static Lesson createLesson(@NonNull int id, @NonNull int chapterId, @NonNull String keyword, String videoString, String audioString, String transcriptString) {
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
