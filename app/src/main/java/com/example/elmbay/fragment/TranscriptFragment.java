package com.example.elmbay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.elmbay.R;
import com.example.elmbay.activity.CourseDetailActivity;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.Lesson;

/**
 * Created by kgu on 4/15/18.
 */

public class TranscriptFragment extends Fragment {
    private static final String LOG_TAG = TranscriptFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_transcript, container, false);

        final ImageButton button = top.findViewById(R.id.transcript_icon);
        if (button != null) {
            Lesson lesson = AppManager.getInstance().getSessionData().getCurrentLesson();
            if (lesson != null && lesson.getTranscript() != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((CourseDetailActivity) getActivity()).toggleTranscript();
                    }
                });
            } else {
                button.setAlpha((float) 0.20);
            }
        }
        return top;
    }

}
