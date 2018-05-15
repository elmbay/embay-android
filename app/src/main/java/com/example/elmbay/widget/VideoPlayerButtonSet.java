package com.example.elmbay.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.NetworkManager;
import com.example.elmbay.model.ContentDescriptor;

import java.util.ArrayList;
import java.util.List;

import static com.example.elmbay.widget.StateButton.MEDIA_STATE_READY;

/**
 *
 * Created by kgu on 4/26/18.
 */

public class VideoPlayerButtonSet {
    private int mState = MEDIA_STATE_READY;
    private Context mContext;
    private ContentDescriptor mContentDescriptor;
    private VideoView mVideoView;
    private MediaPlayer mMediaPlayer;
    private StateButton mStopButton;
    private boolean mHasPlayedVideo;

    public VideoPlayerButtonSet(Context context, ContentDescriptor contentDescriptor, VideoView videoView, ImageButton playButtonView, ImageButton stopButtonView) {
        mContext = context;
        mContentDescriptor = contentDescriptor;
        mVideoView = videoView;

        StateButton playButton = new VideoPlayerButtonSet.VideoPlayerPlayButton(playButtonView, View.VISIBLE, mContentDescriptor != null);
        mStopButton = new VideoPlayerButtonSet.VideoPlayerStopButton(stopButtonView, View.GONE, mContentDescriptor != null);

        List<StateButton> observers = new ArrayList<>();
        observers.add(playButton);
        observers.add(mStopButton);
        playButton.setObservers(observers);
        mStopButton.setObservers(observers);

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
            MediaController controller = new MediaController(context) {
            };
            controller.setAnchorView(mVideoView);
            mVideoView.setMediaController(controller);
            controller.show();
        }
    }

    class VideoPlayerPlayButton extends StateButton {
        VideoPlayerPlayButton(ImageButton button, int visibility, boolean enabled) {
            super(button, visibility, enabled);
        }

        public void onAction() {
            if (mState != MEDIA_STATE_PLAYING && mMediaPlayer != null) {
                try {
                    if (!mHasPlayedVideo) {
                        mHasPlayedVideo = true;
                        AppManager.getInstance().getSessionData().finishedCurrentLesson();
                    }
                    mMediaPlayer.start();
                    mState = MEDIA_STATE_PLAYING;
                    notifyStateChange();
                } catch (IllegalStateException e) {
                    // ignore
                }
            }
        }

        public void onStateChange() {
            mButton.setVisibility(mState == MEDIA_STATE_READY ? View.VISIBLE : View.GONE);
        }
    }

    class VideoPlayerStopButton extends StateButton {
        VideoPlayerStopButton(ImageButton button, int visibility, boolean enabled) {
            super(button, visibility, enabled);
        }

        public void onAction() {
            if (mState == MEDIA_STATE_PLAYING && mMediaPlayer != null) {
                try {
                    mMediaPlayer.pause();
                    mState = MEDIA_STATE_READY;
                    notifyStateChange();
                } catch (IllegalStateException e) {
                    // ignore
                }
            }
        }

        public void onStateChange() {
            mButton.setVisibility(mState == MEDIA_STATE_READY ? View.GONE : View.VISIBLE);
        }
    }


}
