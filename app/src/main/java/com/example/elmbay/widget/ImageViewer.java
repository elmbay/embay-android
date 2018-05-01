package com.example.elmbay.widget;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.ContentDescriptor;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by kgu on 4/30/18.
 */

public class ImageViewer {
    private static final String LOG_TAG = ImageViewer.class.getName();
    private ImageView mImageView;
    private ContentDescriptor mImageContent;

    public ImageViewer(ImageView imageView, ContentDescriptor imageContent) {
        mImageView = imageView;
        mImageContent = imageContent;
    }

    public void display() {
        if (!mImageContent.isRemote()) {
            mImageView.setImageURI(mImageContent.getUri());
        } else {
            try {
                InputStream in = new URL(mImageContent.getUri().toString()).openStream();
                mImageView.setImageBitmap(BitmapFactory.decodeStream(in));
            } catch (Exception e) {
                if (AppManager.DEBUG) {
                    Log.e(LOG_TAG, "Exception when open image content:");
                    e.printStackTrace();
                }
            }
        }
    }
}
