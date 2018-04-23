package com.example.elmbay.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.elmbay.R;
import com.example.elmbay.activity.CourseDetailActivity;
import com.example.elmbay.activity.CourseListActivity;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.NetworkManager;
import com.example.elmbay.model.ContentDescriptor;
import com.example.elmbay.model.Lesson;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link CourseListActivity}
 * in two-pane mode (on tablets) or a {@link CourseDetailActivity}
 * on handsets.
 */
public class VideoFragment extends Fragment {
    private static final String LOG_TAG = VideoFragment.class.getName();
    private ContentDescriptor mVideo;
    private VideoView mSimpleVideoView;
    private MediaPlayer mMediaPlayer;
    private MediaController mMediaController;
    ImageButton mStartButton;
    ImageButton mStopButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_video, container, false);

        Lesson lesson = AppManager.getInstance().getSessionData().getCurrentLesson();
        if (lesson != null) {
            mVideo = lesson.getVideo();
        }

        final Context context = getContext();
        setVideoView(context, top);
        setStartButton(context, top);
        setStopButton(context, top);

        // initiate a video view
        if (mVideo != null) {
            mSimpleVideoView.setVideoURI(mVideo.getUri());
        }

        return top;
    }

    @Override
    public void onPause() {
        super.onPause();
        mSimpleVideoView.stopPlayback();
    }

    @Override
    public void onStop() {
        mSimpleVideoView.stopPlayback();
        super.onStop();
    }

    private void setVideoView(final Context context, View top) {
        mSimpleVideoView = top.findViewById(R.id.video_view);
        if (mSimpleVideoView != null) {

            mSimpleVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playVideo(false);
                }
            });

            mSimpleVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                if (!NetworkManager.getInstance().hasNetworkConnection()) {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show();
                }
                return false;
                }
            });

            mSimpleVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                }
            });
        }
    }

    private void setMediaController(Context context, View top) {
        // create an object of media controller: http://abhiandroid.com/ui/videoview
        View videoFrame = top.findViewById(R.id.video_view);
        if (videoFrame != null) {
            mMediaController = new MediaController(context) {
            };
            mMediaController.setAnchorView(videoFrame);
            mSimpleVideoView.setMediaController(mMediaController);
//            mMediaController.show();
        }
    }

    private void setStartButton(Context context, View top) {
        mStartButton = top.findViewById(R.id.video_start_button);
        if (mStartButton != null) {
            if (mVideo != null) {
                mStartButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playVideo(true);
                    }
                });
            } else {
                mStartButton.setAlpha((float) 0.25);
            }
        }
    }

    private void setStopButton(Context context, View top) {
        mStopButton = top.findViewById(R.id.video_stop_button);
        if (mStopButton != null) {
            mStopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playVideo(false);
                }
            });
        }
    }

    private void playVideo(boolean isResume) {
//        try {
            if (mMediaPlayer != null) {
                if (isResume) {
                    // use MediaPlyer.start() instead of VideoView.start() to handle pause/resume properly
                    mMediaPlayer.start();
                    mStartButton.setVisibility(View.GONE);
                    mStopButton.setVisibility(View.VISIBLE);
                } else {
                    // use mMediaPlayer.pause() instead of VideoView.stopPlayback() to handle pause/resume properly
                    mMediaPlayer.pause();
                    mStopButton.setVisibility(View.GONE);
                    mStartButton.setVisibility(View.VISIBLE);
                }
            }
//        } catch (IllegalStateException e) {
//            if (AppManager.DEBUG) {
//                Log.e(LOG_TAG, e.getMessage());
//            }
//        }
    }
}
