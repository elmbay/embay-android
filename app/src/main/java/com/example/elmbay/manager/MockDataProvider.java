package com.example.elmbay.manager;

import android.util.Log;

import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.operation.ListChaptersResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is to provide mock data
 *
 * Created by kgu on 4/17/18.
 */

class MockDataProvider {

    static ListChaptersResult getSignInResult() {
        ListChaptersResult result = new ListChaptersResult();

        result.setNextLoadInHours(2);

        List<Chapter> chapters = new ArrayList<>();

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(getLesson(1));
        lessons.add(getLesson(2));
        lessons.add(getLesson(3));
        chapters.add(createChapter(1, "English 101", "Kids", lessons));

        lessons = new ArrayList<>();
        lessons.add(getLesson(4));
        chapters.add(createChapter(2, "English 101", "Nature", lessons));

        lessons = new ArrayList<>();
        lessons.add(getLesson(5));
        chapters.add(createChapter(3, "English 101", "Business", lessons));

        result.setChapters(chapters);

        Gson gson = new Gson();
        String json = gson.toJson(chapters);

        Log.w("MockDataProvider", json);

        return result;
    }

    private static Lesson getLesson(int id) {
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

    private static Chapter createChapter(int id, String course, String topic, List<Lesson> lessons) {
        Chapter chapter = new Chapter();
        chapter.setId(id);
        chapter.setCourse(course);
        chapter.setTopic(topic);
        chapter.setLessons(lessons);
        return chapter;
    }

    private static Lesson createLesson(int id, int chapterId, String keyword, String videoString, String audioString, String transcriptString) {
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
