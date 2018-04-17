package com.example.elmbay.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by kgu on 4/13/18.
 */

public class BubbleView extends FrameLayout implements Target {
    private static final String LOG_TAG = BubbleView.class.getName();

    protected ImageView mPhotoThumbnail;
    protected TextView mAbbreviation;
    protected View mPhotoThumbnailBackground;
    protected View mBubbleShadow;
    protected int mBubbleColor;

    public BubbleView(Context context) {
        super(context);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
