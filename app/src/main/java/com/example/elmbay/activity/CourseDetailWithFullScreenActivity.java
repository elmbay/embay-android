package com.example.elmbay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;

import com.example.elmbay.R;
import com.example.elmbay.fragment.AudioFragment;
import com.example.elmbay.fragment.RecorderFragment;
import com.example.elmbay.fragment.TranscriptFragment;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CourseListActivity}.
 */
public class CourseDetailWithFullScreenActivity extends BaseDetailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_detail);
        setupActionBar();

        ImageButton videoView = findViewById(R.id.video_container);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });

        if (savedInstanceState == null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment f;

            f = new AudioFragment();
            ft.add(R.id.audio_container, f);

            f = new TranscriptFragment();
            ft.add(R.id.transcript_container, f);

            f = new RecorderFragment();
            ft.add(R.id.recorder_container, f);

            ft.commit();
        }
    }

    public void playVideo() {
        startActivity(new Intent(this, FullScreenVideoActivity.class));
    }

    public void viewTranscript() {
        startActivity(new Intent(this, FullScreenImageActivity.class));
    }
}
