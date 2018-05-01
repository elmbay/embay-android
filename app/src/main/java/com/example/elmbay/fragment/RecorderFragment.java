package com.example.elmbay.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.Lesson;
import com.example.elmbay.widget.RecorderBar;

/**
 *
 * Created by kgu on 3/21/18.
 */

public class RecorderFragment extends Fragment {
    private static final String LOG_TAG = RecorderFragment.class.getName();
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = { Manifest.permission.RECORD_AUDIO};
    private Lesson mLesson;
    private RecorderBar mRecorderBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View top = inflater.inflate(R.layout.fragment_recorder, container, false);

        Activity activity = getActivity();
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        mLesson = AppManager.getInstance().getSessionData().getCurrentLesson();

        mRecorderBar = new RecorderBar(getContext(), mLesson.getChapterId() + "_" + mLesson.getId());
        mRecorderBar.setRecordButtons((ImageButton) top.findViewById(R.id.record_start), (ImageButton) top.findViewById(R.id.record_stop));
        mRecorderBar.setReplayButtons((ImageButton)top.findViewById(R.id.replay), (ImageButton)top.findViewById(R.id.replay_stop));
        mRecorderBar.setShareButton((ImageButton)top.findViewById(R.id.share), mLesson.getKeyword());
        mRecorderBar.setDeleteButton((ImageButton)top.findViewById(R.id.delete));
        mRecorderBar.setObservers();

        return top;
    }

    @Override
    public void onStop() {
        super.onStop();

        mRecorderBar.onStop();
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
}
