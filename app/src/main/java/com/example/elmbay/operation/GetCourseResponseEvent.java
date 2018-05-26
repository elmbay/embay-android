package com.example.elmbay.operation;

/**
 *
 * Created by kaininggu on 4/22/18.
 */

public class GetCourseResponseEvent extends BaseEvent{
    GetCourseResponseEvent(OperationError error) {
        super((error));
    }
}
