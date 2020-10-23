package com.example.photolab2.MVP;

import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;

import com.example.photolab2.CreationalPatterns.Photo;
import com.example.photolab2.CreationalPatterns.PhotoBuilder;
import com.example.photolab2.StructuralPatterns.PhotoInfoAdapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.function.Predicate;

public class Model {

    private PhotoBuilder photoBuilder = new PhotoBuilder();

    ArrayList<Photo> photos;
    int index = 0;
    Context context;

    public Model(Context c){
        context = c;
    }

    public void Search(final Date startTimestamp, final Date endTimestamp, final String keywords, final float latitude, final float longitude){
        LoadPhotos();
        photos.removeIf(new Predicate<Photo>() {
            @Override
            public boolean test(Photo n) {
                return (((startTimestamp != null && endTimestamp != null) && (n.getDate() < startTimestamp.getTime() || n.getDate() > endTimestamp.getTime())
                ) || (keywords != "" || !n.getFilePath().contains(keywords)
                ) || (latitude != 0 || longitude != 0) && (latitude != latitude || longitude == longitude));
            }
        });
        index = photos.size()-1;
    }

    public Photo GetCurrPhoto(){
        if(photos == null || photos.size() == 0)
            LoadPhotos();

        if(photos.size() == 0)
            return  null;
        return photos.get(index);
    }

    public Photo NextPhoto(){
        if(photos == null)
            LoadPhotos();
        index = (index + 1) % photos.size();
        return GetCurrPhoto();
    }

    public Photo PrevPhoto(){
        if(photos == null)
            LoadPhotos();

        index = (index - 1) % photos.size();
        if(index < 0)
            index = photos.size()-1;
        if(photos.size() == 0)
            index = 0;
        return GetCurrPhoto();
    }



    public void UpdateCurrPhoto(String newCaption){
        String path = photos.get(index).getFilePath();
        String[] attr = path.split("_");
        if (attr.length >= 3) {
            File to = new File(attr[0] + "_" + newCaption + "_" + attr[2] + "_" + attr[3]);
            File from = new File(path);
            from.renameTo(to);
        }
    }

//    public void CreateNewPhoto() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(imageFileName, ".jpg",storageDir);
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }


    void LoadPhotos(){
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photolab2/files/Pictures");

        photos = new ArrayList<Photo>();
        File[] fList = file.listFiles();

        // Check if a list of files was fetched
        if (fList != null) {

            // Iterate through each file
            for (File f : fList) {
                System.out.println("FILE : " + f.getPath());
                InputStream in;
                Uri imgUri = Uri.fromFile(f);
                PhotoInfoAdapter pAdapter = new PhotoInfoAdapter(imgUri, context, f);
                photos.add(photoBuilder.setFilePath(pAdapter.getFilePath()).setDate(pAdapter.getDate()).setLocation(pAdapter.getLat(), pAdapter.getLong()).build());
            }
        }
        index = photos.size()-1;
    }
}
