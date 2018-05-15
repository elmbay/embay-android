package com.example.elmbay.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kgu on 4/6/18.
 */

public class Lesson {

    @SerializedName("id")
    private int mId;

    @SerializedName("chapterId")
    private int mChapterId;

    @SerializedName("keyword")
    private String mKeyword;

    @SerializedName("video")
    private ContentDescriptor mVideo;

    @SerializedName("audio")
    private ContentDescriptor mAudio;

    @SerializedName("transcript")
    private ContentDescriptor mTranscript;

    private int mStatus;

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public int getChapterId() { return mChapterId; }
    public void setChapterId(int id) { mChapterId = id; }

    public String getKeyword() { return mKeyword; }
    public void setKeyword(String keyword) { mKeyword = keyword; }

    public ContentDescriptor getVideo() { return mVideo; }
    public void setVideo(ContentDescriptor video) { mVideo = video; }
    public void setVideo(String videoString) {
        if (videoString == null) {
            mVideo = null;
        } else {
            mVideo = new ContentDescriptor();
            mVideo.setType(ContentDescriptor.CONTENT_TYPE_VIDEO);
            mVideo.setUriString(videoString);
        }
    }

    public ContentDescriptor getAudio() { return mAudio; }
    public void setAudio(ContentDescriptor audio) { mAudio = audio; }
    public void setAudio(String audioString) {
        if (audioString == null) {
            mAudio = null;
        } else {
            mAudio = new ContentDescriptor();
            mAudio.setType(ContentDescriptor.CONTENT_TYPE_AUDIO);
            mAudio.setUriString(audioString);
        }
    }

    public ContentDescriptor getTranscript() { return mTranscript; }
    public void setTranscript(ContentDescriptor transcript) { mTranscript = transcript; }
    public void setTranscript(String transcriptString) {
        if (transcriptString == null) {
            mTranscript = null;
        } else {
            mTranscript = new ContentDescriptor();
            mTranscript.setType(ContentDescriptor.CONTENT_TYPE_IMAGE);
            mTranscript.setUriString(transcriptString);
        }
    }

    public int getStatus() { return mStatus; }
    public void setStatus(int status) { mStatus = status; }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{id=").append(mId).append(",chapterId=").append(mChapterId).append(",keyword=").append(mKeyword);
        if (mVideo!= null) {
            builder.append(",video=").append(mVideo.toString());
        }
        if (mAudio!= null) {
            builder.append(",ausio=").append(mAudio.toString());
        }
        if (mTranscript!= null) {
            builder.append(",transcript=").append(mTranscript.toString());
        }
        builder.append("}");
        return builder.toString();
    }
}
