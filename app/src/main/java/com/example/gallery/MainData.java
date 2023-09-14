package com.example.gallery;

import android.net.Uri;

public class MainData {
    Uri path;
    String title, date;

    public MainData() {

    }

    public MainData(Uri path, String title, String date) {
        this.path = path;
        this.title = title;
        this.date = date;
    }

    public Uri getPath() {
        return path;
    }

    public void setPath(Uri path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

