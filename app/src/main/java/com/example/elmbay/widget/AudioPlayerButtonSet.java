package com.example.elmbay.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.elmbay.model.ContentDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.elmbay.model.ContentDescriptor.CONTENT_TYPE_OUTPUT_FILE;
import static com.example.elmbay.widget.StateButton.MEDIA_STATE_READY;

/**
 * The class defines a set of audio play buttons
 *
 * Created by kgu on 4/26/18.
 */
public class AudioPlayerButtonSet {
    private static final String LOG_TAG = AudioPlayerButtonSet.class.getName();

    private int mState = MEDIA_STATE_READY;
    private Context mContext;
    private ContentDescriptor mContentDescriptor;
    private MediaPlayer mMediaPlayer;
    private StateButton mPlayButton;
    private StateButton mStopButton;

    public AudioPlayerButtonSet(Context context, ContentDescriptor contentDescriptor, ImageButton playButton, ImageButton stopButton, boolean enabled) {
        mContext = context;
        mContentDescriptor = contentDescriptor;

        mPlayButton = new AudioPlayerPlayButton(playButton, View.VISIBLE, enabled);
        mStopButton = new AudioPlayerStopButton(stopButton, View.GONE, enabled);

        List<StateButton> observers = new ArrayList<>();
        observers.add(mPlayButton);
        observers.add(mStopButton);
        mPlayButton.setObservers(observers);
        mStopButton.setObservers(observers);
    }

    public void onStop() {
        mStopButton.onAction();
    }

    StateButton getPlayButton() { return mPlayButton; }

    class AudioPlayerPlayButton extends StateButton {
        AudioPlayerPlayButton(ImageButton button, int visibility, boolean enabled) {
            super(button, visibility, enabled);
        }

        public void onAction() {
            if (mState != MEDIA_STATE_PLAYING) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mState = MEDIA_STATE_READY;
                        notifyStateChange();
                    }
                });
                try {
                    if (mContentDescriptor.getType() == CONTENT_TYPE_OUTPUT_FILE) {
                        mMediaPlayer.setDataSource(mContentDescriptor.getAbsolutePath());
                    } else {
                        mMediaPlayer.setDataSource(mContext, mContentDescriptor.getUri());
                    }
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                    mState = MEDIA_STATE_PLAYING;
                    notifyStateChange();
                } catch (IOException e) {
                    // mMediaPlayer.start() won't throw IOException
                    Log.e(LOG_TAG, "prepare() failed");
                }
            }
        }

        public void onStateChange() {
            mButton.setVisibility(mState == MEDIA_STATE_READY ? View.VISIBLE : View.GONE);
            if (mContentDescriptor.getType() == CONTENT_TYPE_OUTPUT_FILE) {
                enableButton(mContentDescriptor.exists());
            }
        }
    }

    class AudioPlayerStopButton extends StateButton {
        AudioPlayerStopButton(ImageButton button, int visibility, boolean enabled) {
            super(button, visibility, enabled);
        }

        public void onAction() {
            if (mState == MEDIA_STATE_PLAYING && mMediaPlayer != null) {
                mMediaPlayer.release();
                mMediaPlayer = null;
                mState = MEDIA_STATE_READY;
                notifyStateChange();
            }
        }

        public void onStateChange() {
            mButton.setVisibility(mState == MEDIA_STATE_READY ? View.GONE : View.VISIBLE);
            if (mContentDescriptor.getType() == CONTENT_TYPE_OUTPUT_FILE) {
                enableButton(mContentDescriptor.exists());
            }
        }
    }

}
