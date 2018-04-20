package com.example.elmbay.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.ContentDescriptor;
import com.example.elmbay.model.Lesson;

import java.io.IOException;

/**
 * Created by kgu on 3/21/18.
 */

public class AudioFragment extends Fragment {
    private static final String LOG_TAG = AudioFragment.class.getName();
    private static final int STATE_READY = 0;
    private static final int STATE_PLAYING = 1;
    private ContentDescriptor mAudio;
    private int mState = STATE_READY;
    private MediaPlayer mPlayer;
    private ImageButton mPlayButton;
    private ImageButton mStopButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_audio, container, false);

        Lesson lesson = AppManager.getInstance().getSessionData().getCurrentLesson();
        if (lesson != null) {
            mAudio = lesson.getAudio();
        }

        mPlayButton = top.findViewById(R.id.replay);
        if (mPlayButton != null) {
            if (mAudio != null) {
                mPlayButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        startPlaying();
                    }
                });
            } else {
                mPlayButton.setAlpha((float)0.20);
            }
        }

        mStopButton = top.findViewById(R.id.record_stop);
        if (mStopButton != null) {
            mStopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopPlaying();
                }
            });
        }
        return top;
    }

    @Override
    public void onStop() {
        super.onStop();

        stopPlaying();
    }

    private void startPlaying() {
        if (mState != STATE_PLAYING) {
            mState = STATE_PLAYING;
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(getContext(), mAudio.getUri());
                mPlayer.prepare();
                mPlayer.start();
                mPlayButton.setVisibility(View.GONE);
                mStopButton.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                // mPlayer.start() won't throw IOException
                Log.e(LOG_TAG, "prepare() failed");
            }
        }
    }

    private void stopPlaying() {
        if (mState == STATE_PLAYING) {
            mState = STATE_READY;
            if (mPlayer != null) {
                mPlayer.release();
                mPlayer = null;
                mPlayButton.setVisibility(View.VISIBLE);
                mStopButton.setVisibility(View.GONE);
            }
        }
    }
}
