package com.example.elmbay.widget;

import android.content.Context;
import android.widget.ImageButton;

import com.example.elmbay.model.ContentDescriptor;

import static android.view.View.VISIBLE;

/**
 * The class defines a bar of buttons (including share and delete) to manage voice recording
 *
 * Created by kgu on 4/26/18.
 */

public class RecorderBar {
    private static final String RECORDING_FILE_SUFFIX = ".3gp";

    private Context mContext;
    private ContentDescriptor mOutputFile;
    private boolean mOutputFileExists;
    private RecorderButtonSet mRecorderButtonSet;
    private AudioPlayerButtonSet mReplayButtonSet;
    private ShareButton mShareButton;
    private DeleteButton mDeleteButton;


    public RecorderBar(Context context, String outputFileName) {
        mContext = context;

        // Record to the external cache directory for visibility
        mOutputFile = new ContentDescriptor();
        mOutputFile.setMimeType(ContentDescriptor.CONTENT_TYPE_AUDIO_RECORDING);
        mOutputFile.setUriString(outputFileName + RECORDING_FILE_SUFFIX);
        mOutputFile.initUri();

        mOutputFileExists = mOutputFile.exists();
    }

    public void setRecordButtons(ImageButton startButton, ImageButton stopButton) {
        mRecorderButtonSet = new RecorderButtonSet(mContext, mOutputFile.getAbsolutePath(), startButton, stopButton);
    }

    public void setReplayButtons(ImageButton startButton, ImageButton stopButton) {
        mReplayButtonSet = new AudioPlayerButtonSet(mContext, mOutputFile, startButton, stopButton, mOutputFileExists);
    }

    public void setShareButton(ImageButton shareButton, String subject) {
        if (shareButton != null) {
            mShareButton = new ShareButton(mContext, subject, mOutputFile, shareButton, VISIBLE, mOutputFileExists);
        }
    }

    public void setDeleteButton(ImageButton deleteButton) {
        if (deleteButton != null) {
            mDeleteButton = new DeleteButton(deleteButton, VISIBLE, mOutputFileExists);
        }
    }

    /**
     * Call this after all buttons are created to coordinate the buttons responsivness
     */
    public void setObservers() {
        if (mDeleteButton != null) {
            // recording may enable/disable other action buttons
            StateButton record = mRecorderButtonSet.getStartButton();
            record.addObserver(mReplayButtonSet.getPlayButton());
            record.addObserver(mShareButton);
            record.addObserver(mDeleteButton);

            // delete recording file may disable other action buttons
            mDeleteButton.addObserver(mReplayButtonSet.getPlayButton());
            mDeleteButton.addObserver(mShareButton);
        }
    }

    public void onStop() {
        mRecorderButtonSet.onStop();
        mReplayButtonSet.onStop();
    }

    class DeleteButton extends StateButton {
        DeleteButton(ImageButton button, int visibility, boolean enabled) {
            super(button, visibility, enabled);
        }

        public void onAction() {
            if (mOutputFile.deleteFile()) {
                notifyStateChange();
                enableButton(false);
            }
        }

        public void onStateChange() {
            enableButton(mOutputFile.exists());
        }
    }
}
