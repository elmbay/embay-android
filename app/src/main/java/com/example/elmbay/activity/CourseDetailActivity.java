package com.example.elmbay.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elmbay.R;
import com.example.elmbay.fragment.AudioFragment;
import com.example.elmbay.fragment.RecorderFragment;
import com.example.elmbay.fragment.TranscriptFragment;
import com.example.elmbay.fragment.VideoFragment;

public class CourseDetailActivity extends AppCompatActivity {
    public static final String LOG_TAG = CourseDetailActivity.class.getName();
    public static final String ARG_ITEM_ID = "item_id";
    private String mTitle = "Lesson 01: Change Channel";
    private ImageView mTranscript;
    private boolean mViewingTranscript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            mTranscript = findViewById(R.id.transcript);

            String itemId = getIntent().getStringExtra(ARG_ITEM_ID);
            Bundle arguments = new Bundle();
            arguments.putString(ARG_ITEM_ID, itemId);
            TextView title = findViewById(R.id.title);
            if (title != null) {
                title.setText("Item " + itemId);
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            Fragment f = new VideoFragment();
            f.setArguments(arguments);
            ft.add(R.id.video_container, f);

            f = new AudioFragment();
            f.setArguments(arguments);
            ft.add(R.id.audio_container, f);

            f = new TranscriptFragment();
            f.setArguments(arguments);
            ft.add(R.id.transcript_container, f);

            f = new RecorderFragment();
            f.setArguments(arguments);
            ft.add(R.id.recorder_container, f);

            ft.commit();
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        Log.i(LOG_TAG, "onConfigurationChanged mViewingTranscript=" + mViewingTranscript);
//        super.onConfigurationChanged(newConfig);
//
//        if (mViewingTranscript) {
//            // refresh the instructions image
//            mTranscript = (ImageView) findViewById(R.id.transcript);
//            mTranscript.setImageDrawable(getResources().getDrawable(R.drawable.change_channel_01_tx));
//        }
//    }

    public void viewTranscript() {
        mViewingTranscript = true;
        mTranscript.setVisibility(View.VISIBLE);
    }
}
