package com.example.elmbay.adapter;

import android.support.annotation.NonNull;

import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;

/**
 * Created by kgu on 4/19/18.
 */

public interface IViewHolderClickListener {
    void onChapterClick(@NonNull Chapter chapter);
    void onLessonClick(@NonNull Chapter chapter, @NonNull Lesson lesson);
}
