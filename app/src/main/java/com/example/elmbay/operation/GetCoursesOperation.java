package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.NetworkManager;

import org.greenrobot.eventbus.EventBus;

/**
 *
 * Created by kgu on 5/18/18.
 */

public class GetCoursesOperation extends BaseOperation {

    public GetCoursesOperation(@NonNull GetCoursesRequest request) {
        super(request);
    }

    Response.Listener<String> onResult() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GetCoursesResult result = NetworkManager.getInstance().fromJson(response, GetCoursesResult.class);
                logResult(result);
                AppManager.getInstance().getSessionData().getCourseManager().setCoursesResult(result);
                EventBus.getDefault().post(new GetCoursesResponseEvent(null));
            }
        };
    }

    Response.ErrorListener onError() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logError(error);
                EventBus.getDefault().post(new GetCoursesResponseEvent(new OperationError(error)));
            }
        };
    }
}
