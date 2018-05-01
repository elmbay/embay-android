package com.example.elmbay.widget;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.elmbay.manager.AppManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.elmbay.widget.StateButton.STATE_READY;

/**
 * Created by kgu on 4/27/18.
 */

public class RecorderButtonSet {
    private static final String LOG_TAG = RecorderButtonSet.class.getName();

    private int mState = STATE_READY;
    private Context mContext;
    private String mOutputFilePath;
    private MediaRecorder mRecorder;
    private RecordStartButton mStartButton;
    private RecordStopButton mStopButton;
    List<StateButton> mObservers = new ArrayList<>();

    public RecorderButtonSet(Context context, String outputFilePath, ImageButton startButton, ImageButton stopButton) {
        mContext = context;
        mOutputFilePath = outputFilePath;

        mStartButton = new RecordStartButton(startButton, VISIBLE, true);
        mStopButton = new RecordStopButton(stopButton, GONE, true);

        mObservers.add(mStartButton);
        mObservers.add(mStopButton);
        mStartButton.setObservers(mObservers);
        mStopButton.setObservers(mObservers);
    }

    public StateButton getStartButton() { return mStartButton; }
    public StateButton getStopButton() { return mStopButton; }

    public void onStop() {
        mStopButton.onAction();
    }

    class RecordStartButton extends StateButton {
        RecordStartButton(ImageButton button, int visibility, boolean enabled) {
            super(button, visibility, enabled);
        }

        public void onAction() {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mOutputFilePath);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                if (AppManager.DEBUG) {
                    Log.e(LOG_TAG, "prepare() failed");
                    e.printStackTrace();
                }
            }

            try {
                mRecorder.start();
                Toast.makeText(mContext, "Start recording...", Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                if (AppManager.DEBUG) {
                    Log.e(LOG_TAG, "start() failed");
                    e.printStackTrace();
                }
            }

            mState = STATE_RECORDING;
            mButton.setVisibility(GONE);
            notifyStateChange();
        };

        public void onStateChange() {
            mButton.setVisibility(mState == STATE_READY ? View.VISIBLE : GONE);
        }
    }

    class RecordStopButton extends StateButton {
        RecordStopButton(ImageButton button, int visibility, boolean enabled) {
            super(button, visibility, enabled);
        }

        public void onAction() {
            if (mRecorder != null) {
                mRecorder.stop();       // stop recording
                mRecorder.reset();      // set state to idle
                mRecorder.release();    // release resource back to system
                mRecorder = null;
            }

            mState = STATE_READY;
            mButton.setVisibility(GONE);
            notifyStateChange();
        }

        public void onStateChange() {
            mButton.setVisibility(mState == STATE_RECORDING ? View.VISIBLE : GONE);
        }
    }
}
