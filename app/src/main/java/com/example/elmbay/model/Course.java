package com.example.elmbay.model;

import com.example.elmbay.manager.Helper;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by kgu on 5/18/18.
 */

public class Course {
    @SerializedName("id")
    private int mId;

    @SerializedName("name")
    private String mName;

    // For comparison chapters is a list of chapters in ascend order
    @SerializedName("chapters")
    private List<Chapter> mChapters;

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public String getName() { return mName; }
    public void setName(String name) { mName = name; }

    public List<Chapter> getChapters() { return mChapters; }
    public void setChapters(List<Chapter> chapters) { mChapters = chapters; }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{id=").append(mId);
        if (mName != null) {
            builder.append(",name=").append(mName);
        }
        if (mChapters != null) {
            builder.append(",chapters=").append(Helper.listToString(mChapters));
        }
        return builder.toString();
    }
}
