package com.example.hongseonggi.chatting_client;

import android.net.Uri;

public class Useritem {
    private String name;
    private String contents;
    private int resId;
    private Uri uri;

    public Useritem(String name ,Uri uri ,String contents){
        this.name = name;
        this.uri = uri;
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
