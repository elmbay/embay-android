package com.example.elmbay.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.elmbay.R;
import com.example.elmbay.fragment.AudioFragment;
import com.example.elmbay.fragment.RecorderFragment;
import com.example.elmbay.fragment.TranscriptFragment;
import com.example.elmbay.fragment.VideoFragment;
import com.example.elmbay.manager.TouchImageView;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CourseListActivity}.
 */
public class CourseDetailActivity extends BaseDetailActivity {
    TouchImageView mTranscriptView;
    boolean mTranscriptViewVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_detail);
        setupActionBar();

        mTranscriptView = findViewById(R.id.transcript);
        if (mLesson != null && mLesson.getTranscript() != null) {
            mTranscriptView.setVisibility(View.GONE);
            mTranscriptView.setImageURI(mLesson.getTranscript().getUri());
            mTranscriptView.setZoom((float) 1.8);
        }

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment f;

            f = new VideoFragment();
            ft.add(R.id.video_container, f);

            f = new AudioFragment();
            ft.add(R.id.audio_container, f);

            f = new TranscriptFragment();
            ft.add(R.id.transcript_container, f);

            f = new RecorderFragment();
            ft.add(R.id.recorder_container, f);

            ft.commit();
        }
    }

    public void toggleTranscript() {
        mTranscriptView.setVisibility(mTranscriptViewVisible ? View.GONE : View.VISIBLE);
        mTranscriptViewVisible = !mTranscriptViewVisible;
    }
}
