package com.example.elmbay.manager;

import java.util.List;

/**
 * Created by kgu on 4/23/18.
 */

public class Helper {
    public static <T> String listToString(List<T> items) {
        StringBuilder builder = new StringBuilder();
        String comma = "";
        for (T item : items) {
            builder.append(comma).append(item.toString());
            comma = ",";
        }
        return builder.toString();
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }
}
