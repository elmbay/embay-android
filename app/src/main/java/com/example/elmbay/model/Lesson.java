package com.example.elmbay.model;

import com.example.elmbay.manager.NetworkManager;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by kgu on 4/6/18.
 */

public class Lesson {

    @SerializedName("id")
    private int mId;

    @SerializedName("key")
    private String mKeyword;

    @SerializedName("video")
    private ContentDescriptor mVideo;

    @SerializedName("audio")
    private ContentDescriptor mAudio;

    @SerializedName("note")
    private ContentDescriptor mNote;

    @SerializedName("st")
    private int mStatus;

    public int getId() { return mId; }
    public void setId(int id) { mId = id; }

    public String getKeyword() { return mKeyword; }
    public void setKeyword(String keyword) { mKeyword = keyword; }

    public ContentDescriptor getVideo() { return mVideo; }
    public void setVideo(ContentDescriptor video) { mVideo = video; }
    public void setVideo(String videoString) {
        if (videoString == null) {
            mVideo = null;
        } else {
            mVideo = new ContentDescriptor();
            mVideo.setMimeType(ContentDescriptor.CONTENT_TYPE_VIDEO);
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
            mAudio.setMimeType(ContentDescriptor.CONTENT_TYPE_AUDIO);
            mAudio.setUriString(audioString);
        }
    }

    public ContentDescriptor getNote() { return mNote; }
    public void setNote(ContentDescriptor note) { mNote = note; }
    public void setNote(String noteString) {
        if (noteString == null) {
            mNote = null;
        } else {
            mNote = new ContentDescriptor();
            mNote.setMimeType(ContentDescriptor.CONTENT_TYPE_IMAGE);
            mNote.setUriString(noteString);
        }
    }

    public int getStatus() { return mStatus; }
    public void setStatus(int status) { mStatus = status; }

    @Override
    public String toString() {
        return NetworkManager.getInstance().toJson(this);
    }
}
