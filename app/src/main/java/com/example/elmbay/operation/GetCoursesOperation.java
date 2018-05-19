package com.example.elmbay.operation;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.NetworkManager;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import static com.example.elmbay.manager.NetworkManager.ENDPOINT_CHAPTERS;

/**
 * Created by kgu on 5/18/18.
 */

public class GetCoursesOperation {
    private static final String LOG_TAG = GetCoursesOperation.class.getName();
    private GetCoursesRequest mRequest;

    public GetCoursesOperation(@NonNull GetCoursesRequest request) {
        mRequest = request;
    }

    public void submit() {
        int method = Request.Method.GET;

        Uri.Builder builder = Uri.parse(NetworkManager.BASE_URL_MOCK + ENDPOINT_CHAPTERS).buildUpon();
        String url = builder.build().toString();

        StringRequest request = new StringRequest(method, url, onResult(), onError()) {
            @Override
            protected Map<String, String> getParams() {
                return mRequest.getParams();
            }
        };
        NetworkManager.getInstance().submit(request);
    }

    private Response.Listener<String> onResult() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GetCoursesResult result = NetworkManager.getInstance().fromJson(response, GetCoursesResult.class);
                if (AppManager.DEBUG) {
                    Log.i(LOG_TAG, "GetCoursesResult=" + (result == null ? "null" : result.toString()));
                }
                AppManager.getInstance().getSessionData().setListChaptersResult(result);
                EventBus.getDefault().post(new GetCoursesResponseEvent(null));
            }
        };
    }

    private Response.ErrorListener onError() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (AppManager.DEBUG) {
                    Log.w(LOG_TAG, "GetCoursesResult error" + error.getMessage());
                }
                EventBus.getDefault().post(new GetCoursesResponseEvent(error)
                );
            }
        };
    }
}
