package com.example.elmbay.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.elmbay.R;
import com.example.elmbay.fragment.VideoFragment;

/**
 * Created by kgu on 4/18/18.
 */

public class FullScreenVideoActivity extends BaseDetailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_screen_video);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment f = new VideoFragment();
            ft.add(R.id.video_container, f);
            ft.commit();
        }
    }
}

