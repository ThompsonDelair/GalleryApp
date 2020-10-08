package com.example.photolab2;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import retrofit2.Call;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

//import com.twitter.sdk.android.core.Twitter;
//import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SEARCH_ACTIVITY_REQUEST_CODE = 2;

    String mCurrentPhotoPath;

    private ArrayList<String> photos = null;
    private int index = 0;

    //Twitter twitter;
    boolean twitterLoggedIn;

    private TwitterAuthClient twitterAuthClient;
    public static String consumerKey = "rCP747BkZVEQoO8BnWoUcrem5";
    public static String consumerSecret = "1Uqom6JrPg7kzvDeDVgpxnqhvgpjI69xCOxpRVoDD6rv9Tr8Ib";
    public static String callbackURL = "com.example.photolab2.ywitter_oauth";
    public static String accessToken = "1313991235279831040-qdpMens0q05DfMIl3isqODQtsgBFOs";
    public static String tokenSecret = "Mr8FcSR4vbvPwgm7tfcA4EwsEAzTHpmQU2adPkCLgyCU0";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "");
        if (photos.size() == 0) {
            displayPhoto(null);
        } else {
            displayPhoto(photos.get(index));
        }
        //TwitterSetup();
    }

//    private void TwitterSetup(){
//        ConfigurationBuilder cb = new ConfigurationBuilder();
//        cb.setDebugEnabled(true)
//                .setOAuthConsumerKey(consumerKey)
//                .setOAuthConsumerSecret(consumerSecret)
//                .setOAuthAccessToken(accessToken)
//                .setOAuthAccessTokenSecret(tokenSecret);
//        TwitterFactory tf = new TwitterFactory(cb.build());
//        twitter = tf.getInstance();
//    }


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

    private ArrayList<String> findPhotos(Date startTimestamp, Date endTimestamp, String keywords) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.photolab2/files/Pictures");
        ArrayList<String> photos = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            for (File f : fList) {
                if (((startTimestamp == null && endTimestamp == null) || (f.lastModified() >= startTimestamp.getTime()
                        && f.lastModified() <= endTimestamp.getTime())
                ) && (keywords == "" || f.getPath().contains(keywords)))
                    photos.add(f.getPath());
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

        if (twitterAuthClient != null) {
            twitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            DateFormat format = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
            Date startTimestamp , endTimestamp;
            try {
                String from = (String) data.getStringExtra("STARTTIMESTAMP");
                String to = (String) data.getStringExtra("ENDTIMESTAMP");
                startTimestamp = format.parse(from);
                endTimestamp = format.parse(to);
            } catch (Exception ex) {
                startTimestamp = null;
                endTimestamp = null;
            }
            String keywords = (String) data.getStringExtra("KEYWORDS");
            index = 0;
            photos = findPhotos(startTimestamp, endTimestamp, keywords);
            if (photos.size() == 0) {
                displayPhoto(null);
            } else {
                displayPhoto(photos.get(index));
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView mImageView = (ImageView) findViewById(R.id.imageView_gallery);
            mImageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            photos = findPhotos(new Date(Long.MIN_VALUE), new Date(), "");
        }
    }

    public void PostWithIntent(View v){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "this is a test");
        startActivity(Intent.createChooser(shareIntent, "Share your thoughts"));
    }

//    public void PostToTwitter() throws TwitterException {
//        String latestStatus = "this is a test";
////        Twitter twitter = TwitterFactory.getSingleton();
//        Status status = twitter.updateStatus(latestStatus);
//        System.out.println("Successfully updated the status to [" + status.getText() + "].");
//    }

//    public void PostTwitterButton(View v)  {

//        new AsyncTask<Void,Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                try {
//                    PostToTwitter();
//                } catch (TwitterException e) {
//                    e.printStackTrace();
//                }
//                return  null;
//            }
//        }.execute();
//    }



//    public void LoginTwitterButton(View v){
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        if (!twitterLoggedIn)
//        {
//            new TwitterAuthenticateTask().execute();
//        }
//        else
//        {
//            Intent intent = new Intent(MainActivity.this, MainActivity.class);
//            startActivity(intent);
//        }
//    }
//
//    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {
//
//        @Override
//        protected void onPostExecute(RequestToken requestToken) {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
//            startActivity(intent);
//        }
//
//        @Override
//        protected RequestToken doInBackground(String... params) {
//            return TwitterUtil.getInstance().getRequestToken();
//        }
//    }

//    public void LoginToTwitter(){
//        if(!twitterLoggedIn){
//            ConfigurationBuilder builder = new ConfigurationBuilder();
//            builder.setOAuthConsumerKey(consumerKey);
//            builder.setOAuthConsumerSecret(consumerSecret);
//            Configuration configuration = builder.build();
//        }
//    }

//    private void initControl() {
//        Uri uri = getIntent().getData();
//        if (uri != null && uri.toString().startsWith(callbackURL)) {
//            String verifier = uri.getQueryParameter("oauth_verifier");
//            new TwitterGetAccessTokenTask().execute(verifier);
//        } else
//            new TwitterGetAccessTokenTask().execute("");
//    }

//    class TwitterGetAccessTokenTask extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPostExecute(String s) {
//            // string s is user name
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String verifier = params[0];
//
//            Twitter twitter = TwitterUtil.getInstance().getTwitter();
//            RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
//
//            try {
//                AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
//                long userId = accessToken.getUserId();
//                return twitter.showUser(userId).getName();
//            } catch (TwitterException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//    }

//    public void TwitterPost(){
//        try {
//
//            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, accessToken.getToken());
//            editor.putString(ConstantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
//            editor.putBoolean(ConstantValues.PREFERENCE_TWITTER_IS_LOGGED_IN, true);
//            editor.commit();
//            return twitter.showUser(accessToken.getUserId()).getName();
//        } catch (TwitterException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
//    }
}