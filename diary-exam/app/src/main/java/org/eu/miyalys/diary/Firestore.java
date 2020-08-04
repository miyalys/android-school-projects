package org.eu.miyalys.diary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

// For handling remote storage of images - and also audio in the future
public class Firestore {

    static FirebaseStorage storage;

    public Firestore() {
        storage = FirebaseStorage.getInstance();
    }

    // File to save, name to save it under on Firebase
    public void upload(File file, String name) {
        Log.d("all","upload running");
        // Convert file to Uri for StorageReference to upload it
        Uri uri_file = Uri.fromFile(file);
        // Get a reference to the containing directory
        StorageReference storageRef = storage.getReference();
        // Get a reference to a file by "name" relative to the above directory
        StorageReference sub = storageRef.child(name);
        UploadTask uploadTask = sub.putFile(uri_file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("all","Upload Unsuccessful");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {} // Success - don't do anything
        });
    }

    // Downloads images specifically - create other methods for other file types
    public void downloadImage(String name, final ImageView iv){
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
