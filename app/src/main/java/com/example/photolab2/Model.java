package com.example.photolab2;

import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

import com.example.photolab2.CreationalPatterns.Photo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class Model {

    ArrayList<Photo> photos;
    int index = 0;
    Context context;

    public Model(Context c){
        context = c;
    }

    public void Search(){

    }

    public Photo GetCurrPhoto(){
        return photos.get(index);
    }

    public Photo GetNextPhoto(){
        index = (index + 1) % photos.size();
        return photos.get(index);
    }

    public Photo GetPrevPhoto(){
        index = (index - 1) % photos.size();
        return photos.get(index);
    }



    public void UpdatePhoto(){

    }

    public void CreateNewPhoto(){

    }

    void LoadPhotos(){
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photolab2/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();

        // Check if a list of files was fetched
        if (fList != null) {

            // Iterate through each file
            for (File f : fList) {
                System.out.println("FILE : " + f.getPath());

                InputStream in;
                Uri imgUri = Uri.fromFile(f);

                // Parse geocoding from photo file usin EXIF Interface.
                ExifInterface exif;
                float laty = 0, longy = 0;
                float[] latlon = new float[2];

                try {
                    in = context.getContentResolver().openInputStream(imgUri);
                    exif = new ExifInterface(Objects.requireNonNull(in));

                    // Ensure that LAT & LONG Values can be parsed. Else, set to 0
                    exif.getLatLong(latlon);
                    System.out.println("Photo" + f.getPath() + " taken at pos lat : " + latlon[0] + ", lon : " + latlon[1]);

                    laty = latlon[0];
                    longy = latlon[1];

                } catch(IOException e) {
                    System.out.println("Absolute file path "+ f.getPath() + " not found.");
                }
            }
        }
    }
}
