package com.example.photolab2;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;

    String mCurrentPhotoPath;

    private ArrayList<String> photos = null;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "", 0, 0);
        if (photos.size() == 0) {
            displayPhoto(null);
        } else {
            displayPhoto(photos.get(index));
        }
    }

    public void search(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
    }

    public void takePhoto(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            // Error occurred while creating the File
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.photolab2.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private ArrayList<String> findPhotos(Date startTimestamp, Date endTimestamp, String keywords, float latitude, float longitude) {
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
                    in = getContentResolver().openInputStream(imgUri);
                    exif = new ExifInterface(Objects.requireNonNull(in));

                    // Ensure that LAT & LONG Values can be parsed. Else, set to 0
                    System.out.println("EXIF : " + exif.toString());
                    exif.getLatLong(latlon);
                    System.out.println("lat : " + latlon[0] + ", lon : " + latlon[1]);

                    laty = latlon[0];
                    longy = latlon[1];

                } catch(IOException e) {
                    System.out.println("Absolute file path "+ f.getPath() + " not found.");
                }

//                System.out.println("Lat : " + laty + ", Long : " + longy);

                // If this returns true, a file will be added to the photos list.
                if ( ( (startTimestamp == null && endTimestamp == null) || ( f.lastModified() >= startTimestamp.getTime() && f.lastModified() <= endTimestamp.getTime() )
                ) && ( keywords == "" || f.getPath().contains(keywords)
                ) && ( latitude == 0 && longitude == 0) || (latitude == laty && longitude == longy))  {
                    photos.add(f.getPath());
                }
            }
        }

        return photos;
    }

    public void scrollPhotos(View v) {
        updatePhoto(photos.get(index), ((EditText) findViewById(R.id.editText_caption)).getText().toString());

        switch (v.getId()) {
            case R.id.button_prev:
                if (index > 0) {
                    index--;
                }
                break;
            case R.id.button_next:
                if (index < (photos.size() - 1)) {
                index++;
            }
            break;
            default:
                break;
        }
        displayPhoto(photos.get(index));
    }

    private void displayPhoto(String path) {
        ImageView iv = (ImageView) findViewById(R.id.imageView_gallery);
        TextView tv = (TextView) findViewById(R.id.textView_timestamp);
        EditText et = (EditText) findViewById(R.id.editText_caption);
        if (path == null || path =="") {
            iv.setImageResource(R.mipmap.ic_launcher);
            et.setText("");
            tv.setText("");
        } else {
            iv.setImageBitmap(BitmapFactory.decodeFile(path));
            String[] attr = path.split("_");
            et.setText(attr[1]);
            tv.setText(attr[2]);
        }
    }

    private void updatePhoto(String path, String caption) {
        String[] attr = path.split("_");
        if (attr.length >= 3) {
            File to = new File(attr[0] + "_" + caption + "_" + attr[2] + "_" + attr[3]);
            File from = new File(path);
            from.renameTo(to);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
            Date startTimestamp , endTimestamp;
            float laty, longy;

            try {
                String from = (String) data.getStringExtra("STARTTIMESTAMP");
                String to = (String) data.getStringExtra("ENDTIMESTAMP");
                startTimestamp = format.parse(from);
                endTimestamp = format.parse(to);

                laty = data.getFloatExtra("LATITUDE", 0);
                longy = data.getFloatExtra("LONGITUDE", 0);

            } catch (Exception ex) {
                startTimestamp = null;
                endTimestamp = null;
                laty = 0;
                longy = 0;
            }

            String keywords = (String) data.getStringExtra("KEYWORDS");

            index = 0;
            photos = findPhotos(startTimestamp, endTimestamp, keywords, laty, longy);
            if (photos.size() == 0) {
                displayPhoto(null);
            } else {
                displayPhoto(photos.get(index));
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView mImageView = (ImageView) findViewById(R.id.imageView_gallery);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "", 0, 0);
        }
    }
}