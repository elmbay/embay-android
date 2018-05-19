package com.example.elmbay.manager;

import android.util.Log;

import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Course;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.operation.GetCoursesResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * The class is to provide mock data
 *
 * Created by kgu on 4/17/18.
 */

class MockDataProvider {

    static GetCoursesResult getSignInResult() {
        GetCoursesResult result = new GetCoursesResult();

        result.setNextLoadInHours(2);

        List<Chapter> chapters = new ArrayList<>();

        Course course = new Course();
        course.setId(1);
        course.setName("English 101");
        course.setChapters(chapters);

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(getLesson(1));
        lessons.add(getLesson(2));
        lessons.add(getLesson(3));
        chapters.add(createChapter(1, "Kids", lessons));

        lessons = new ArrayList<>();
        lessons.add(getLesson(4));
        chapters.add(createChapter(2, "Nature", lessons));

        lessons = new ArrayList<>();
        lessons.add(getLesson(5));
        chapters.add(createChapter(3, "Business", lessons));

        result.setCourse(course);

        Gson gson = new Gson();
        String json = gson.toJson(chapters);

        Log.w("MockDataProvider", json);

        return result;
    }

    private static Lesson getLesson(int id) {
        Lesson lesson = null;
        switch (id) {
            case 1:
                lesson = createLesson(id,"Change Channel","change_channel_01_vd", "change_channel_01_ad", "change_channel_01_tx");
                break;
            case 2:
                lesson = createLesson(id,"Juicy","juicy_02_vd", "juicy_02_ad", "juicy_02_tx");
                break;
            case 3:
                lesson = createLesson(id, "literally","literally_03_vd", "literally_03_ad", "literally_03_tx");
                break;
            case 4:
                lesson = createLesson(id, "Blue Sea","http://abhiandroid-8fb4.kxcdn.com/ui/wp-content/uploads/2016/04/videoviewtestingvideo.mp4", null, null);
                break;
            case 5:
                lesson = createLesson(id, "Venice","ppcash", null, null);
                break;
        }
        return lesson;
    }

    private static Chapter createChapter(int id, String topic, List<Lesson> lessons) {
        Chapter chapter = new Chapter();
        chapter.setId(id);
        chapter.setTopic(topic);
        chapter.setLessons(lessons);
        return chapter;
    }

    private static Lesson createLesson(int id, String keyword, String videoString, String audioString, String transcriptString) {
        Lesson lesson = new Lesson();

        lesson.setId(id);
        lesson.setKeyword(keyword);
        lesson.setVideo(videoString);
        lesson.setAudio(audioString);
        lesson.setNote(transcriptString);

        return lesson;
    }
}
