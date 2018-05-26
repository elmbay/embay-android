package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.NetworkManager;
import com.google.gson.JsonParseException;

import org.greenrobot.eventbus.EventBus;

/**
 *
 * Created by kgu on 5/18/18.
 */

public class GetCourseOperation extends BaseOperation {

    public GetCourseOperation(@NonNull GetCourseRequest request) {
        super(request);
    }

    Response.Listener<String> onResult() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    GetCourseResult result = NetworkManager.getInstance().fromJson(response, GetCourseResult.class);
                    if (result != null) {
                        logResult(result);
                        AppManager.getInstance().getSessionData().getCourseManager().setCourseResult(result);
                        EventBus.getDefault().post(new GetCourseResponseEvent(null));
                    } else {
                        logError(null, "Empty response");
                        EventBus.getDefault().post(new GetCourseResponseEvent(new OperationError(R.string.empty_response)));
                    }
                } catch (JsonParseException ex) {
                    logError(null, "Empty response");
                    EventBus.getDefault().post(new GetCourseResponseEvent(new OperationError(R.string.json_exception)));
                }
            }
        };
    }

    Response.ErrorListener onError() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                OperationError oe = new OperationError(error);
                logError(oe, "Network error");
                EventBus.getDefault().post(new GetCourseResponseEvent(oe));
            }
        };
    }
}
