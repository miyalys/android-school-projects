package org.eu.miyalys.diary;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.content.FileProvider;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.util.Log;

        import com.google.firebase.storage.FirebaseStorage;

        import java.io.ByteArrayOutputStream;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;

// setContentView, getExternalFilesDir, startActivityForResult, onActivityResult are all part of AppCompatActivity, hence why this is an Activity
// Good tips here: // https://developer.android.com/training/camera/photobasics
public class ImageHandlerActivity extends AppCompatActivity {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private final int CAMERA_REQUEST_CODE = 100;  // Arbitrary number that we pass and check for the response to in onActivityResult below
    private final int GALLERY_REQUEST_CODE = 101; // Arbitrary number that we pass and check for the response to in onActivityResult below
    private String currentPhotoPath;
    private String diary_entry_id_filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_handler);

        diary_entry_id_filename = String.valueOf( getIntent().getIntExtra("position", -1) );
        takePhoto();
    }

    public void takePhoto() {
        Log.d("all", "takePhoto running");
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
                Uri photoURI = FileProvider.getUriForFile(this, "org.eu.miyalys.diary.fileprovider", photoFile);
                Log.d("all", photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                "temp",  /* prefix. "photo" is added in front because it crashes if it's just the ID */
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

            Log.d("all", "imageResized: " + String.valueOf(imageResized));

            // 3. SAVE THE IMAGE TO THE OBJECT. TODO: Make this a temporary place until 'save' has been clicked.
            //DiaryStorage.diary.get(Integer.valueOf(diary_entry_id_filename)).image = imageResized;
            ViewAddDiaryEntryActivity.diaryEntryTmp.image = imageResized;

            // 4: Return to ViewAddDiaryEntry after the photo has been taken and saved
            Intent i = new Intent(this, ViewAddDiaryEntryActivity.class);
            // Pass information to MainActivity that an entry was added/saved, so it can call notifyDataSetChanged or otherwise ensure that the display of the list is updated
            i.putExtra("status", 1); // status 1 = updated -> notifyDataSetChanged
            i.putExtra("position", Integer.valueOf(diary_entry_id_filename));
            //i.putExtra("imgpath", imageResized.getAbsolutePath());
            startActivity(i);
        }
        else {
            Log.e("all", "Error in onActivityResult");
            Log.e("all", "Request Code: " + requestCode);
            // RESULT_OK = -1, anything else is an error
            Log.e("all", "Result Code: " + resultCode);
            //if (data == null) Log.e("all", "data was null");
        }
    }


    // For resizing the photo
    private File resizePic() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

        Bitmap scaledBm = bitmap.createScaledBitmap(bitmap, 400, 400, true);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        scaledBm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        // Getting a File from the bitmap
        //File ifile = new File(Environment.getExternalStorageDirectory() + File.separator + diary_entry_id_filename + "_temp.jpg");
        File ifile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "temp_resized.jpg");
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
}
