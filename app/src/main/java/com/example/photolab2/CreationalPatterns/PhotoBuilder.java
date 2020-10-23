package com.example.photolab2.CreationalPatterns;

import android.net.Uri;

import java.util.Date;

// Used to construct and
public class PhotoBuilder {

    private String filePath;
    private String caption;
    private float[] location;
    private Date date;

    public PhotoBuilder() {
        // Location array
        this.location = new float[2];
    }

    public PhotoBuilder setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public PhotoBuilder setCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public PhotoBuilder setLocation(float laty, float longy) {
        this.location[0] = laty;
        this.location[1] = longy;
        return this;
    }

    public PhotoBuilder setDate(Date date) {
        this.date = date;
        return this;
    }

    public Photo build() {
        if (validateParameters()) {
            return new Photo(filePath, caption, location[0], location[1], date);
        } else {
            throw new IllegalStateException();
        }
    }

    // When building a new Photo, we can check the validity of parameters here before building.
    private boolean validateParameters() {
        return true;
    }


}
