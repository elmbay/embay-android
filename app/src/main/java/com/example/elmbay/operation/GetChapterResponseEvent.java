package com.example.elmbay.operation;

/**
 *
 * Created by kgu on 5/23/18.
 */

public class GetChapterResponseEvent extends BaseEvent {
    GetChapterResponseEvent(OperationError error) {
        super(error);
    }
}
