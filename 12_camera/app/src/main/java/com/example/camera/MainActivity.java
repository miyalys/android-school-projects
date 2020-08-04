package com.example.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    final int CAMERA_REQUEST_CODE = 100; // Arbitrary number that we pass and check for the response to in onActivityResult below
    String currentPhotoPath;

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("all","Permission is granted");
                return true;
            } else {

                Log.v("all","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("all","Permission is granted");
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isStoragePermissionGranted()) System.exit(-1);


        // 1: GET A HOLD OF THE IMAGE
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                Log.d("all", photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // Two options:
        // Internal storage - shouldn't require special permissions
        //File storageDir = getApplicationContext().getFilesDir();
        // 2: External storage - should require special permissions
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
        // Returning null is expected: https://stackoverflow.com/questions/9890757/android-camera-data-intent-returns-null
        // ...instead the photo is in currentPhotoPath and photoURI
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
        // 2. RESIZE IT
                File imageResized = resizePic();

                // 3. SAVE IT TO THE GALLERY
                galleryAddPic(imageResized);

            Toast.makeText(this, "Check the gallery!", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.e("all", "Error in onActivityResult");
                Log.e("all", "Request Code: " + requestCode);
                // RESULT_OK = -1, anything else is an error
                Log.e("all", "Result Code: " + resultCode);
                if (data == null) Log.e("all", "data was null");
            }
    }


    // For resizing the photo
    // https://developer.android.com/training/camera/photobasics
    private File resizePic() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

        // Getting a File from the bitmap, which we need to save it to Gallery
        Bitmap scaledBm = bitmap.createScaledBitmap(bitmap, 400, 400, true);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        scaledBm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        // Two options:
        // Internal storage - shouldn't require special permissions
        //File ifile = new File(getApplicationContext().getFilesDir() + File.separator + "temporary_file.jpg");
        // 2: External storage - should require special permissions
        //File ifile = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
        File ifile = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "temporary_file.jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(ifile);
            fos.write(bytes.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ifile;

    }
    // For adding the photo to the Gallery
    // https://developer.android.com/training/camera/photobasics
    private void galleryAddPic(File f) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        //File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
