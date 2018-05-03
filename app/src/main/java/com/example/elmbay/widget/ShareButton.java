package com.example.elmbay.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageButton;

import com.example.elmbay.model.ContentDescriptor;

/**
 *
 * Created by kgu on 4/27/18.
 */

public class ShareButton extends StateButton {
    private Context mContext;
    private String mSubject;
    private ContentDescriptor mContent;

    ShareButton(Context context, String subject, ContentDescriptor content, ImageButton button, int visibility, boolean enabled) {
        super(button, visibility, enabled);
        mContext = context;
        mSubject = subject;
        mContent = content;
    }

    public void onAction() {
        try {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);

            // Grant temporary read permission to the content URI
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            share.setType(mContent.getMimeType());
            share.putExtra(Intent.EXTRA_SUBJECT, mSubject);
//            share.putExtra(Intent.EXTRA_TEXT, mSubject);

            // Two places to set uri: This is to fix "Unsupported audio format"
            share.setData(mContent.getUri());
            // This is to really attach the file
            share.putExtra(Intent.EXTRA_STREAM, mContent.getUri());

            mContext.startActivity(Intent.createChooser(share, mSubject));
        } catch (IllegalArgumentException e) {
            Log.e("File Selector", "The selected file can't be shared: " + mContent.getUri());
        }
    }

    public void onStateChange() {
        enableButton(mContent.exists());
    }
}
