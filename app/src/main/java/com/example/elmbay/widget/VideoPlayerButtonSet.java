package com.example.elmbay.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.elmbay.manager.NetworkManager;
import com.example.elmbay.model.ContentDescriptor;

import java.util.ArrayList;
import java.util.List;

import static com.example.elmbay.widget.StateButton.STATE_READY;

/**
 *
 * Created by kgu on 4/26/18.
 */

public class VideoPlayerButtonSet {
    private static final String LOG_TAG = VideoPlayerButtonSet.class.getName();

    private int mState = STATE_READY;
    private Context mContext;
    private ContentDescriptor mContentDescriptor;
    private VideoView mVideoView;
    private MediaPlayer mMediaPlayer;
    private MediaController mMediaController;
    private StateButton mPlayButton;
    private StateButton mStopButton;
    private List<StateButton> mObservers = new ArrayList<>();

    public VideoPlayerButtonSet(Context context, ContentDescriptor contentDescriptor, VideoView videoView, ImageButton playButton, ImageButton stopButton) {
        mContext = context;
        mContentDescriptor = contentDescriptor;
        mVideoView = videoView;

        mPlayButton = new VideoPlayerButtonSet.VideoPlayerPlayButton(playButton, View.VISIBLE, mContentDescriptor != null);
        mStopButton = new VideoPlayerButtonSet.VideoPlayerStopButton(stopButton, View.GONE, mContentDescriptor != null);

        mObservers.add(mPlayButton);
        mObservers.add(mStopButton);
        mPlayButton.setObservers(mObservers);
        mStopButton.setObservers(mObservers);

        setVideoView();
    }

    public void onStop() {
        mVideoView.stopPlayback();
    }

    private void setVideoView() {
        if (mVideoView != null) {
            if (mContentDescriptor != null) {
                mVideoView.setVideoURI(mContentDescriptor.getUri());
            }

            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mStopButton.onAction();
                }
            });

            mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (!NetworkManager.getInstance().hasNetworkConnection()) {
                        Toast.makeText(mContext, "No internet connection", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, "An Error Occur While Playing Video " + mContentDescriptor.getUri(), Toast.LENGTH_LONG).show();
                    }
                    return false;
                }
            });

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mMediaPlayer = mediaPlayer;
                }
            });
        }
    }


    private void setMediaController(Context context, View top) {
        // create an object of media controller: http://abhiandroid.com/ui/videoview
        if (mVideoView != null) {
            mMediaController = new MediaController(context) {
            };
            mMediaController.setAnchorView(mVideoView);
            mVideoView.setMediaController(mMediaController);
            mMediaController.show();
        }
    }

    class VideoPlayerPlayButton extends StateButton {
        VideoPlayerPlayButton(ImageButton button, int visibility, boolean enabled) {
            super(button, visibility, enabled);
        }

        public void onAction() {
            if (mState != STATE_PLAYING && mMediaPlayer != null) {
                try {
                    mMediaPlayer.start();
                    mState = STATE_PLAYING;
                    notifyStateChange();
                } catch (IllegalStateException e) {
                    // ignore
                }
            }
        }

        public void onStateChange() {
            mButton.setVisibility(mState == STATE_READY ? View.VISIBLE : View.GONE);
        }
    }

    class VideoPlayerStopButton extends StateButton {
        VideoPlayerStopButton(ImageButton button, int visibility, boolean enabled) {
            super(button, visibility, enabled);
        }

        public void onAction() {
            if (mState == STATE_PLAYING && mMediaPlayer != null) {
                try {
                    mMediaPlayer.pause();
                    mState = STATE_READY;
                    notifyStateChange();
                } catch (IllegalStateException e) {
                    // ignore
                }
            }
        }

        public void onStateChange() {
            mButton.setVisibility(mState == STATE_READY ? View.GONE : View.VISIBLE);
        }
    }


}
