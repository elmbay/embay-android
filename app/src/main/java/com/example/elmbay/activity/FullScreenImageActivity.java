package com.example.elmbay.activity;

import android.os.Bundle;

import com.example.elmbay.R;
import com.example.elmbay.manager.TouchImageView;
import com.example.elmbay.model.ContentDescriptor;

/**
 * Created by kgu on 4/18/18.
 */

public class FullScreenImageActivity extends BaseDetailActivity {
    ContentDescriptor mTranscript;
    private TouchImageView mTranscriptView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_screen_image);

        if (mLesson != null) {
            mTranscript = mLesson.getTranscript();
            if (mTranscript != null && mTranscript.getType() == ContentDescriptor.CONTENT_TYPE_IMAGE) {
                mTranscriptView = findViewById(R.id.transcript);
                mTranscriptView.setImageURI(mTranscript.getUri());
                mTranscriptView.setZoom((float) 1.0);
            }
        }
    }
}
