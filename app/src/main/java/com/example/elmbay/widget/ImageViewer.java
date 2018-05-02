package com.example.elmbay.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.elmbay.manager.AppManager;
import com.example.elmbay.model.ContentDescriptor;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * View local or remote image files
 *
 * Created by kgu on 4/30/18.
 */

public class ImageViewer {
    private static AsyncTask mAsyncTask;
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
            if (mAsyncTask != null) {
                mAsyncTask.cancel(true);
                mAsyncTask = null;
            }

            mAsyncTask = new ImageLoaderTask(mImageContent.getUri().toString(), mImageView).execute();
        }
    }

    /**
     * Network can't be run on MainThread.
     *
     * This AsyncTask class should be static or leaks might occur (anonymous android.os.AsyncTask):
     * Reason: Original task references ImageLoader's member data. When ImageLoader is garvage collected
     * some of its member data can't and eventually leak
     */
    static class ImageLoaderTask extends AsyncTask<String, ImageView, Bitmap> {
        private String mUrl;
        private WeakReference<ImageView> mImageViewWeakReference;

        ImageLoaderTask(String url, ImageView imageView) {
            mUrl = url;
            mImageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                InputStream in = new URL(mUrl).openStream();
                // Only the original thread that created a view hierarchy can touch its views
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                if (AppManager.DEBUG) {
                    Log.e(LOG_TAG, "Exception when open image content: " + mUrl);
                    e.printStackTrace();
                }
                return null;
            }
        }

        /**
         * Anything you change in views must be performed on UI thread or onPostExecute of AsyncTask
         * @param result
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                ImageView targetView = mImageViewWeakReference.get();
                if (targetView != null) {
                    // If image view is not shown, try to set a drawable (e.g. image_loader_enabler.xml) prior to setImageBitmap.
                    targetView.setImageBitmap(result);
                } else {
                    if (AppManager.DEBUG) {
                        Log.w(LOG_TAG, "Target view is null for image content: " + mUrl);
                    }
                }
            } else {
                if (AppManager.DEBUG) {
                    Log.w(LOG_TAG, "Bitmap is null for image content: " + mUrl);
                }
            }
            mAsyncTask = null;
        }
    }
}
