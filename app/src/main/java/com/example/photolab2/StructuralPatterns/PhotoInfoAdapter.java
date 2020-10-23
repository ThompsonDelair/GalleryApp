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
public class PhotoInfoAdapter implements PhotoInfo{
    private String filePath;
    private String caption;
    private float latitude;
    private float longitude;
    private long date;
    private Context context;
    private Uri uri;

    //public PhotoInfoAdapter(Uri u, Context c, File f, float lat, float longy) {
    public PhotoInfoAdapter(Uri u, Context c, File f) {
        // File path
        this.filePath = f.getPath();
        this.caption = caption;

        // Date
        this.date = f.lastModified();

        // Context and URI
        this.context = c;
        this.uri = u;

        // Location
        adaptLatLong();
    }

    private void adaptLatLong(){
        InputStream in;

        // Parse geocoding from photo file usin EXIF Interface.
        ExifInterface exif;
        float[] latlon = new float[2];

        try {
            in = context.getContentResolver().openInputStream(uri);
            exif = new ExifInterface(Objects.requireNonNull(in));

            // Ensure that LAT & LONG Values can be parsed. Else, set to 0
            exif.getLatLong(latlon);
            //System.out.println("Photo" + f.getPath() + " taken at pos lat : " + latlon[0] + ", lon : " + latlon[1])

            System.out.println("LAT IN ADAPT: " + latlon[0]);
            System.out.println("LONG IN ADAPT: " + latlon[1]);
            latitude = latlon[0];
            longitude = latlon[1];

        } catch(IOException e) {
            System.out.println(e.toString());
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
