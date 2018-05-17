package com.example.elmbay.operation;

import com.example.elmbay.manager.Helper;
import com.example.elmbay.model.Chapter;
import com.example.elmbay.model.ProgressMark;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by kgu on 4/10/18.
 */

public class ListChaptersResult {
    // For comparison chapters is a list of chapters in ascend order
    @SerializedName("chapters")
    private List<Chapter> mChapters;

    @SerializedName("mark")
    private ProgressMark mHighMark;

    @SerializedName("hour")
    private int mNextLoadInHours = 1;

    public void setChapters(List<Chapter> lessons) { mChapters = lessons; }
    public List<Chapter> getChapters() { return mChapters; }

    public void setHighMark(ProgressMark mark) { mHighMark = mark; }
    public ProgressMark getHighMark() { return mHighMark; }

    public void setNextLoadInHours(int hours) { mNextLoadInHours = hours; }
    public int getNextLoadInHours() { return mNextLoadInHours; }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        String comma = "";
        if (mChapters != null) {
            builder.append(comma).append("chapters=").append(Helper.listToString(mChapters));
            comma = ",";
        }
        if (mHighMark != null) {
            builder.append(comma).append("highMark=").append(mHighMark.toString());
            comma = ",";
        }
        builder.append(comma).append("nextLoadInHours=").append(mNextLoadInHours);
        builder.append("}");
        return builder.toString();
    }
}
