package com.example.photolab2.StructuralPatterns;

import android.content.ContentResolver;
import android.media.ExifInterface;
import android.net.Uri;
import android.content.Context;

import com.example.photolab2.MainActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.Objects;



// Adapter implements the target interface of PhotoInfo which holds the necessary fields needed to
// build photoObjects that are served to the User in the view. These fields are populated using a given URIs data after
// some conversion or manipulation to get it in the right format.
public class UriAdapter implements PhotoInfo{
    private String filePath;
    private String caption;
    private float latitude;
    private float longitude;
    private long date;

    private Context context;
    private Uri uri;

    public UriAdapter(Uri u, Context c, File f, float lat, float longy) {
        // File path
        this.filePath = filePath;
        this.caption = caption;

        // Location
        this.latitude = lat;
        this.longitude = longy;

        // Date
        this.date = f.lastModified();

        // Context and URI
        this.context = c;
        this.uri = u;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String newCaption) {
        caption = newCaption;
    }

    @Override
    public float getLat() {
        return latitude;
    }


    @Override
    public float getLong() {
        return longitude;
    }

    @Override
    public long  getDate() {
        return date;
    }
}
