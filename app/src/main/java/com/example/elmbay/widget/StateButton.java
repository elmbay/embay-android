package com.example.elmbay.widget;

import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by kgu on 4/25/18.
 */

public abstract class StateButton {
    static final int STATE_READY = 0;
    static final int STATE_PLAYING = 1;
    static final int STATE_RECORDING = 2;

    ImageButton mButton;
    boolean mIsEnabled = true;
    List<StateButton> mObservers;

    abstract public void onAction();
    abstract public void onStateChange();

    public StateButton(ImageButton button, int visibility, boolean enabled) {
        mButton = button;
        if (mButton != null) {
            mButton.setVisibility(visibility);
            enableButton(enabled);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAction();
                }
            });
        }
    }

    public void setObservers(List<StateButton> observers) { mObservers = observers; }
    public void addObserver(StateButton observer) {
        if (mObservers == null) {
            mObservers = new ArrayList<>();
        }
        mObservers.add(observer);
    }

    public void notifyStateChange() {
        if (mObservers != null) {
            for (StateButton button : mObservers) {
                button.onStateChange();
            }
        }
    }

    public void enableButton(boolean enabled) {
        if (mIsEnabled != enabled) {
            mIsEnabled = enabled;
            mButton.setEnabled(enabled);
            mButton.setAlpha(enabled ? (float) 1 : (float) 0.2);
        }
    }
}
