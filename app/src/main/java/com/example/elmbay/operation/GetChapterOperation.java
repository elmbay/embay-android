package com.example.elmbay.operation;

import android.support.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.elmbay.R;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.NetworkManager;
import com.example.elmbay.model.Chapter;
import com.google.gson.JsonParseException;

import org.greenrobot.eventbus.EventBus;

/**
 *
 * Created by kgu on 5/23/18.
 */

public class GetChapterOperation extends BaseOperation {

    public GetChapterOperation(@NonNull GetChapterRequest request) {
        super(request);
    }

    Response.Listener<String> onResult() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                Chapter result = NetworkManager.getInstance().fromJson(response, Chapter.class);
                    if (result != null) {
                        logResult(result);
                        AppManager.getInstance().getSessionData().getCourseManager().setLessonsResult(result);
                        EventBus.getDefault().post(new GetChapterResponseEvent(null));
                    } else {
                        logError(null, "Empty response");
                        EventBus.getDefault().post(new GetChapterResponseEvent(new OperationError(R.string.empty_response)));
                    }
                } catch (JsonParseException ex) {
                    logError(null, "Empty response");
                    EventBus.getDefault().post(new GetChapterResponseEvent(new OperationError(R.string.json_exception)));
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
                EventBus.getDefault().post(new GetChapterResponseEvent(oe));
            }
        };
    }
}
