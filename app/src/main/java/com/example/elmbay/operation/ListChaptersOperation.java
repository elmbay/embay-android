package com.example.elmbay.operation;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.elmbay.manager.AppManager;
import com.example.elmbay.manager.NetworkManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import static com.example.elmbay.manager.NetworkManager.ENDPOINT_CHAPTERS;

/**
 *
 * Created by kgu on 5/10/18.
 */

public class ListChaptersOperation {
    private static final String LOG_TAG = ListChaptersOperation.class.getName();
    private ListChaptersRequest mRequest;

    public ListChaptersOperation(ListChaptersRequest request) {
        mRequest = request;
    }

    public void submit() {
        int method = Request.Method.GET;

        Uri.Builder builder = Uri.parse(NetworkManager.BASE_URL_MOCK + ENDPOINT_CHAPTERS).buildUpon();
        String url = builder.build().toString();

        JSONObject jsonObject = NetworkManager.toJSONObject(mRequest);

        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonObject, onResult(), onError());
        NetworkManager.getInstance().submit(request);
    }

    private Response.Listener<JSONObject> onResult() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ListChaptersResult result = NetworkManager.fromJSONObject(response, ListChaptersResult.class);
                if (AppManager.DEBUG) {
                    Log.i(LOG_TAG, "ListChaptersResult=" + (result == null ? "null" : result.toString()));
                }
                AppManager.getInstance().getSessionData().setListChaptersResult(result);
                EventBus.getDefault().post(new ListChaptersResponseEvent(null));
            }
        };
    }

    private Response.ErrorListener onError() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (AppManager.DEBUG) {
                    Log.w(LOG_TAG, "ListChaptersResult error" + error.getMessage());
                }
                EventBus.getDefault().post(new ListChaptersResponseEvent(error)
                );
            }
        };
    }
}
