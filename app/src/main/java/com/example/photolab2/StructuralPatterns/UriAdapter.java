package com.example.photolab2.StructuralPatterns;

import android.media.ExifInterface;
import android.net.Uri;
import android.content.Context;

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

    public UriAdapter(Uri u, Context c, File f) {
        // File path
        this.filePath = filePath;
        this.caption = caption;

        // Location
        getLatLong();

        // Date
        this.date = f.lastModified();

        // Context and URI
        this.context = c;
        this.uri = u;
    }

    private void getLatLong(){
        InputStream in;

        // Parse geocoding from photo file usin EXIF Interface.
        ExifInterface exif;
        float laty = 0, longy = 0;
        float[] latlon = new float[2];

        try {
            in = context.getContentResolver().openInputStream(uri);
            exif = new ExifInterface(Objects.requireNonNull(in));

            // Ensure that LAT & LONG Values can be parsed. Else, set to 0
            exif.getLatLong(latlon);
            //System.out.println("Photo" + f.getPath() + " taken at pos lat : " + latlon[0] + ", lon : " + latlon[1]);

            latitude = latlon[0];
            longitude = latlon[1];

        } catch(IOException e) {
            //System.out.println("Absolute file path "+ f.getPath() + " not found.");
        }
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
