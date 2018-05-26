package com.example.elmbay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.ContentDescriptor;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.widget.AudioPlayerButtonSet;

/**
 * The class manages replay/pause of slowed-down version of the audio
 *
 * Created by kgu on 3/21/18.
 */

public class AudioFragment extends Fragment {
    private ContentDescriptor mAudio;
    private AudioPlayerButtonSet mPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_audio, container, false);

        Lesson lesson = AppManager.getInstance().getSessionData().getCourseManager().getLessonSelected();
        if (lesson != null) {
            mAudio = lesson.getAudio();
        }

        mPlayer = new AudioPlayerButtonSet(getContext(), mAudio,
                        (ImageButton) top.findViewById(R.id.replay),
                        (ImageButton) top.findViewById(R.id.record_stop), mAudio != null);

        return top;
    }

    @Override
    public void onPause() {
        super.onPause();

        mPlayer.onStop();
    }
}
