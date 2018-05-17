package com.example.elmbay.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.example.elmbay.R;
import com.example.elmbay.fragment.AudioFragment;
import com.example.elmbay.fragment.RecorderFragment;
import com.example.elmbay.fragment.VideoFragment;
import com.example.elmbay.model.ContentDescriptor;
import com.example.elmbay.widget.ImageLoader;
import com.example.elmbay.widget.TouchImageView;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CourseListActivity}.
 */
public class CourseDetailActivity extends BaseCourseActivity {
    TouchImageView mTranscriptView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_detail);
        setupLessonActionBar();

        mTranscriptView = findViewById(R.id.transcript);
        displayTranscript();

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment f;

            f = new VideoFragment();
            ft.add(R.id.video_container, f);

            f = new AudioFragment();
            ft.add(R.id.audio_container, f);

            f = new RecorderFragment();
            ft.add(R.id.recorder_container, f);

            ft.commit();
        }
    }

    protected void setupLessonActionBar() {
        if (mLesson != null && mLesson.getKeyword() != null) {
            setTitle(mLesson.getKeyword());
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void displayTranscript() {
        if (mLesson != null) {
            ContentDescriptor transCript = mLesson.getNote();
            if (transCript != null) {
                ImageLoader imageLoader = new ImageLoader(mTranscriptView, transCript);
                imageLoader.display();
                mTranscriptView.setZoom((float) 1.8);
            }
        }
    }
}
