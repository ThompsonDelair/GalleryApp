package com.example.photolab2.CreationalPatterns;

import java.util.Date;

public class Photo {

    private String filePath;
    private String caption;
    private float[] location;
    private long date;

    public Photo(String filePath, String caption, float laty, float longy, long date) {
        // File path
        this.filePath = filePath;
        this.caption = caption;

        // Location
        this.location = new float[2];
        this.location[0] = laty;
        this.location[1] = longy;

        // Date
        this.date = date;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String newCaption) {
        this.caption = newCaption;
    }

    public float[] getLocation() {
        return location;
    }

    public long getDate() {
        return date;
    }

}
