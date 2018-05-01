package com.example.elmbay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.example.elmbay.R;
import com.example.elmbay.activity.CourseDetailActivity;
import com.example.elmbay.activity.CourseListActivity;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.ContentDescriptor;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.widget.VideoPlayerButtonSet;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link CourseListActivity}
 * in two-pane mode (on tablets) or a {@link CourseDetailActivity}
 * on handsets.
 */
public class VideoFragment extends Fragment {
    private ContentDescriptor mVideo;
    private VideoPlayerButtonSet mVideoPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_video, container, false);

        Lesson lesson = AppManager.getInstance().getSessionData().getCurrentLesson();
        if (lesson != null) {
            mVideo = lesson.getVideo();
        }

        mVideoPlayer = new VideoPlayerButtonSet(getContext(), mVideo,
                (VideoView) top.findViewById(R.id.video_view),
                (ImageButton) top.findViewById(R.id.video_start_button),
                (ImageButton) top.findViewById(R.id.video_stop_button));

        return top;
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoPlayer.onStop();
    }

    @Override
    public void onStop() {
        mVideoPlayer.onStop();
        super.onStop();
    }
}
