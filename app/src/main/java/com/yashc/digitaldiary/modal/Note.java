package com.yashc.digitaldiary.modal;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Note implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String mId = "";

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "content")
    private String mContent;

    @ColumnInfo(name = "last_modified")
    private String mLastModified;

    public Note(@NonNull String mId, String mTitle, String mContent, String mLastModified) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mLastModified = mLastModified;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getLastModified() {
        return mLastModified;
    }

    public void setLastModified(String mLastModified) {
        this.mLastModified = mLastModified;
    }
}
