package com.example.elmbay.model;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kgu on 4/6/18.
 */

public class Lesson {
    @SerializedName("id")
    private String mId;

    @SerializedName("chapterId")
    private String mChapterId;

    @SerializedName("keyword")
    private String mKeyword;

    @SerializedName("video")
    private ContentDescriptor mVideo;

    @SerializedName("audio")
    private ContentDescriptor mAudio;

    @SerializedName("transcript")
    private ContentDescriptor mTranscript;

    public String getId() { return mId; }
    public void setId(String id) { mId = id; }

    public String getChapterId() { return mChapterId; }
    public void setChapterId(String id) { mChapterId = id; }

    public String getKeyword() { return mKeyword; }
    public void setKeyword(String keyword) { mKeyword = keyword; }

    public ContentDescriptor getVideo() { return mVideo; }
    public void setVideo(ContentDescriptor video) { mVideo = video; }
    public void setVideo(Context context, String videoString) {
        if (videoString == null) {
            mVideo = null;
        } else {
            mVideo = new ContentDescriptor();
            mVideo.setType(ContentDescriptor.CONTENT_TYPE_VIDEO);
            mVideo.setUri(context, videoString);
        }
    }

    public ContentDescriptor getAudio() { return mAudio; }
    public void setAudio(ContentDescriptor audio) { mAudio = audio; }
    public void setAudio(Context context, String audioString) {
        if (audioString == null) {
            mAudio = null;
        } else {
            mAudio = new ContentDescriptor();
            mAudio.setType(ContentDescriptor.CONTENT_TYPE_AUDIO);
            mAudio.setUri(context, audioString);
        }
    }

    public ContentDescriptor getTranscript() { return mTranscript; }
    public void setTranscript(ContentDescriptor transcript) { mTranscript = transcript; }
    public void setTranscript(Context context, String transcriptString) {
        if (transcriptString == null) {
            mTranscript = null;
        } else {
            mTranscript = new ContentDescriptor();
            mTranscript.setType(ContentDescriptor.CONTENT_TYPE_IMAGE);
            mTranscript.setUri(context, transcriptString);
        }
    }
}
