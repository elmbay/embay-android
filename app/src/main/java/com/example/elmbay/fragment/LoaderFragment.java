package com.example.elmbay.fragment;

import android.support.v4.app.Fragment;

import com.example.elmbay.event.SignInRequestEvent;
import com.example.elmbay.manager.SignInOperation;
import com.example.elmbay.model.SignInRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by kaininggu on 4/22/18.
 */

public class LoaderFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);

        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SignInRequestEvent event) {
        SignInRequest request = new SignInRequest("xueshengjia", "bar", null);
        SignInOperation op = new SignInOperation(request, false);
        op.submit();
    }
}
