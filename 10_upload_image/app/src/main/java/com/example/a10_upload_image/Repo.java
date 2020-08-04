package com.example.a10_upload_image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Repo {
    static FirebaseStorage storage;

    public Repo() {
         storage = FirebaseStorage.getInstance();
    }

    public static void uploadImage(Uri file, String name) {
        Log.d("all","uploadImage running");
        // Get a reference to the containing directory
        StorageReference storageRef = storage.getReference();
        // Get a reference to a file by "name" relative to the above directory
        StorageReference sub = storageRef.child(name);
        //Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        //StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = sub.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("all","Upload Unsuccessful :(");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Log.d("all","Upload Successful :)");
            }
        });
    }

    public static void downloadImage(String name, final ImageView iv){
        // get a reference from the file storage, given a file name
        StorageReference ref = storage.getReference(name);
        int max = 1024 * 1024 * 2; // 2 megabytes maximum
        ref.getBytes(max).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv.setImageBitmap(bm); // set imagedata to imageView
            }
        });

    }
}
