package com.example.a10_upload_image;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // Arbitrary numbers, we use them to check for when the response from the correct startActivityForResult is received.
    // In this case it technically doesn't matter as the app only handles two such activities and identically too.
    private int CAMERA_REQUEST_CODE = 10000;
    private int GALLERY_REQUEST_CODE = 10001;
    public Uri uri;
    public Repo repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        repo = new Repo();
    }

    public void buttonHandler(View v) {

        Log.d("all", "buttonHandler running");

        switch(v.getId()) {

            case R.id.gallery:
                Intent intent = new Intent(Intent.ACTION_PICK);
                // Set the type of files to be picked to be a type of image
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
                break;
            case R.id.camera:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, CAMERA_REQUEST_CODE);
                break;
            case R.id.upload:
                repo.uploadImage(uri, "images/file.jpg");
            case R.id.download:
                repo.downloadImage("images/file.jpg", (ImageView) findViewById(R.id.myImg));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // Android-studio seemed to want this?
        if (requestCode == CAMERA_REQUEST_CODE || requestCode == GALLERY_REQUEST_CODE) {
            Bitmap image = null;
            if (requestCode == CAMERA_REQUEST_CODE) {
                // This next line only works for the camera - the image must be obtainable in some other way from the gallery
                uri = data.getData(); // For image uploading
                image = data.getExtras().getParcelable("data"); // For displaying
            }
            else { // Handle getting image from gallery
                uri = data.getData();
                try{
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    image = BitmapFactory.decodeStream(inputStream);
                } catch (Exception e){}
            }
            // Forced error handling in case we return from the gallery and the exception occurs so image is empty
            if (image != null) {
                ImageView imageview = (ImageView) findViewById(R.id.myImg); //sets imageview as the bitmap
                imageview.setImageBitmap(image);
            }
        }
    }
}