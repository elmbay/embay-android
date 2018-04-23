package com.example.elmbay.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.Lesson;

import java.io.File;
import java.io.IOException;

/**
 * Created by kgu on 3/21/18.
 */

public class RecorderFragment extends Fragment {
    private static final String LOG_TAG = "ParrotAudioRecorder";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int STATE_READY = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_RECORDING = 2;
    private int mState = STATE_READY;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private RecordButton mRecordButton;
    private StopButton mStopButton;
    private PlayButton mPlayButton;
    private ImageButton mShareButton;
    private ImageButton mDeleteButton;
    private Lesson mLesson;
    private static String mFileName;
    private File mFile;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = { Manifest.permission.RECORD_AUDIO};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_recorder, container, false);

        Activity activity = getActivity();
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        // Record to the external cache directory for visibility
        mLesson = AppManager.getInstance().getSessionData().getCurrentLesson();
        mFileName = activity.getExternalCacheDir().getAbsolutePath() + "/" + mLesson.getId() + ".3gp";
        mFile = new File(mFileName);

        ImageButton button = top.findViewById(R.id.record_start);
        mRecordButton = new RecordButton(button);

        button = top.findViewById(R.id.record_stop);
        mStopButton = new StopButton(button);

        button = top.findViewById(R.id.replay);
        mPlayButton = new PlayButton(button);

        mShareButton = top.findViewById(R.id.share);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAudio();
            }
        });

        mDeleteButton = top.findViewById(R.id.delete);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile();
            }
        });

        checkFileExists();

        return top;
    }

    @Override
    public void onStop() {
        super.onStop();

        stopRecording();
        stopPlaying();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) {
            getActivity().finish();
        }

    }

    class PlayButton {
        ImageButton mButton;

        View.OnClickListener clicker = new View.OnClickListener() {
            public void onClick(View v) {
                switch (mState) {
                    case STATE_RECORDING:
                        onRecord(false);
                        break;
                    case STATE_PLAYING:
                        onPlay(false);
                        break;
                }
                onPlay(true);
                mState = STATE_PLAYING;
            }
        };

        public PlayButton(ImageButton button) {
            mButton = button;
            button.setOnClickListener(clicker);
        }

        public ImageButton getImage() { return mButton; }
    }

    class RecordButton {
        ImageButton mButton;

        View.OnClickListener clicker = new View.OnClickListener() {
            public void onClick(View v) {
                switch (mState) {
                    case STATE_RECORDING:
                        onRecord(false);
                        break;
                    case STATE_PLAYING:
                        onPlay(false);
                        break;
                }
                onRecord(true);
                mState = STATE_RECORDING;
            }
        };

        public RecordButton(ImageButton button) {
            mButton = button;
            button.setOnClickListener(clicker);
        }

        public ImageButton getImage() { return mButton; }
    }

    class StopButton {
        ImageButton mButton;

        View.OnClickListener clicker = new View.OnClickListener() {
            public void onClick(View v) {
                switch (mState) {
                    case STATE_RECORDING:
                        onRecord(false);
                        break;
                    case STATE_PLAYING:
                        onPlay(false);
                        break;
                }
                mState = STATE_READY;
            }
        };

        public StopButton(ImageButton button) {
            mButton = button;
            button.setOnClickListener(clicker);
        }

        public ImageButton getImage() { return mButton; }
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        mRecordButton.getImage().setVisibility(View.GONE);
        mStopButton.getImage().setVisibility(View.VISIBLE);

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        try {
            mRecorder.start();
            Toast.makeText(getContext(), "Start recording...", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            Log.e(LOG_TAG, "start() failed");
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        mStopButton.getImage().setVisibility(View.GONE);
        mRecordButton.getImage().setVisibility(View.VISIBLE);

        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }

        checkFileExists();
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            // mPlayer.start() won't throw IOException
            Log.e(LOG_TAG, "prepare() failed");
        }

    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void shareAudio() {
        try {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);

            // Grant temporary read permission to the content URI
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.setType("audio/3gpp");
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(mFileName));

            // Add data to the intent, the receiving app will decide what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, mLesson.getKeyword());
//            share.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com");

            startActivity(Intent.createChooser(share, mLesson.getKeyword()));
        } catch (IllegalArgumentException e) {
            Log.e("File Selector", "The selected file can't be shared: " + mFileName);
        }
    }

    private void deleteFile() {
        if (mFile.exists()) {
            mFile.delete();
            checkFileExists();
        }
    }

    private void checkFileExists() {
        boolean fileExists = mFile.exists();

        float alpha = (float) (fileExists ? 1.0 : 0.2);
        mPlayButton.getImage().setAlpha(alpha);
        mShareButton.setAlpha(alpha);
        mDeleteButton.setAlpha(alpha);

        mPlayButton.mButton.setEnabled(fileExists);
        mShareButton.setEnabled(fileExists);
        mDeleteButton.setEnabled(fileExists);
    }
}
