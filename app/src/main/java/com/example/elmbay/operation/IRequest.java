package com.example.elmbay.operation;

import java.util.Map;

/**
 * Created by kgu on 5/21/18.
 */

public interface IRequest {
    int getMethod();
    String getEndpoint();
    Map<String, String> getParams();
}
