package com.example.elmbay.adapter;

import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;

/**
 * Created by kgu on 4/19/18.
 */

public interface IViewHolderClickListener {
    void onLessonClick(Chapter chapter, Lesson lesson);
}
