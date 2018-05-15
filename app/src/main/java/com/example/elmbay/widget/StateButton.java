package com.example.elmbay.widget;

import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A base class for a state button which responses to state changes
 *
 * Created by kgu on 4/25/18.
 */

public abstract class StateButton {
    static final int MEDIA_STATE_READY = 0;
    static final int MEDIA_STATE_PLAYING = 1;
    static final int MEDIA_STATE_RECORDING = 2;

    ImageButton mButton;
    private boolean mIsEnabled = true;
    private List<StateButton> mObservers;

    abstract public void onAction();
    abstract public void onStateChange();

    StateButton(ImageButton button, int visibility, boolean enabled) {
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

    void setObservers(List<StateButton> observers) { mObservers = observers; }
    void addObserver(StateButton observer) {
        if (mObservers == null) {
            mObservers = new ArrayList<>();
        }
        mObservers.add(observer);
    }

    void notifyStateChange() {
        if (mObservers != null) {
            for (StateButton button : mObservers) {
                button.onStateChange();
            }
        }
    }

    void enableButton(boolean enabled) {
        if (mIsEnabled != enabled) {
            mIsEnabled = enabled;
            mButton.setEnabled(enabled);
            mButton.setAlpha(enabled ? (float) 1 : (float) 0.2);
        }
    }
}
