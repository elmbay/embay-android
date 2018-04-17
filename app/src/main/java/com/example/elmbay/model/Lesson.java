package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kgu on 4/6/18.
 */

public class Lesson {

    @SerializedName("id")
    private String mId;

    @SerializedName("course")
    private String mCourse;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("video")
    private ContentDescriptor mVideo;

    @SerializedName("audio")
    private ContentDescriptor mAudio;

    @SerializedName("text")
    private ContentDescriptor mText;

    public void setId(String id) { mId = id; }
    public String getId() { return mId; }

    public void setCourse(String course) { mCourse = course; }
    public String getCourse() { return mCourse; }

    public void setTitle(String title) { mTitle = title; }
    public String getTitle() { return mTitle; }

    public void setVideo(ContentDescriptor video) { mVideo = video; }
    public ContentDescriptor getVideo() { return mVideo; }

    public void setAudio(ContentDescriptor audio) { mAudio = audio; }
    public ContentDescriptor getAudio() { return mAudio; }

    public void setText(ContentDescriptor text) { mText = text; }
    public ContentDescriptor getText() { return mText; }
}
