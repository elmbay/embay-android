package com.example.elmbay.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.elmbay.R;
import com.example.elmbay.activity.CourseDetailActivity;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.CourseManager;
import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.widget.RecorderBar;

import java.util.Locale;

/**
 * The class manage buttons for recording user voice
 *
 * Created by kgu on 3/21/18.
 */

public class RecorderFragment extends Fragment {
    private String [] mPermissions = { Manifest.permission.RECORD_AUDIO };
    private RecorderBar mRecorderBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_recorder, container, false);

        ActivityCompat.requestPermissions( getActivity(), mPermissions, CourseDetailActivity.REQUEST_RECORD_AUDIO_PERMISSION);

        CourseManager courseManager = AppManager.getInstance().getSessionData().getCourseManager();
        Chapter chapter = courseManager.getChapterSelected();
        Lesson lesson = courseManager.getLessonSelected();
        String fileBaseName = String.format(Locale.getDefault(), "%d_%d", chapter == null ? 0 : chapter.getId(), lesson == null ? 0 : lesson.getId());
        mRecorderBar = new RecorderBar(getContext(), fileBaseName);
        mRecorderBar.setRecordButtons((ImageButton) top.findViewById(R.id.record_start), (ImageButton) top.findViewById(R.id.record_stop));
        mRecorderBar.setReplayButtons((ImageButton) top.findViewById(R.id.replay), (ImageButton) top.findViewById(R.id.replay_stop));
        mRecorderBar.setShareButton((ImageButton) top.findViewById(R.id.share), lesson == null ? "None" : lesson.getKeyword());
        mRecorderBar.setDeleteButton((ImageButton) top.findViewById(R.id.delete));
        mRecorderBar.setObservers();

        return top;
    }

    @Override
    public void onStop() {
        super.onStop();

        mRecorderBar.onStop();
    }
}
