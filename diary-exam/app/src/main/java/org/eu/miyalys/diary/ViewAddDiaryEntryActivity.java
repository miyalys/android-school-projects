package org.eu.miyalys.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import static android.media.MediaRecorder.AudioSource.MIC;

// The page where an individual DiaryEntry is added or an existing one is viewed
public class ViewAddDiaryEntryActivity extends AppCompatActivity {

    //private File image;
    private DiaryEntry diaryEntry; // For tracking whether this activity represents an existing DiaryEntry or a new one, relevant to saving/deletion.
    public static DiaryEntry diaryEntryTmp; // For keeping the state of the new entry so far when adding a photo
    private int position;
    Firestore fs;

    // For audio recording
    private boolean recording = false;
    private MediaRecorder mr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_add_diary_entry);

        fs = new Firestore();

        // TODO: Make some way to create a temporary object to return to when having added a photo to a new entry?
        position = getIntent().getIntExtra("position", -1);
        //String imgpath = getIntent().getStringExtra("imgpath");
        File img;
        String entry_text;

        // Only create a new diaryEntryTmp if one doesn't already exist
        if (diaryEntryTmp == null) diaryEntryTmp = new DiaryEntry();

        // Case 1: An existing entry
        if (position != -1) {
            // Does it really make sense for me to save this object?
            diaryEntry = DiaryStorage.diary.get(position);

            entry_text = (diaryEntryTmp.text == null)? diaryEntry.text : diaryEntryTmp.text;
            img = (diaryEntryTmp.image == null)? diaryEntry.image : diaryEntryTmp.image;
        }
        else { // Case 2: A new entry
            img = diaryEntryTmp.image;
            entry_text = diaryEntryTmp.text;
        }
        // Set the textview if both aren't null
        if (entry_text != null) {
            EditText t = (EditText) findViewById(R.id.diary_text);
            t.setText(entry_text);
        }
        // Set the imageview if both aren't null
        if (img != null) { // If it isn't the case that both are null
            Log.d("all", "image wasn't null");
            ImageView i = (ImageView) findViewById(R.id.diary_image);
            // If a file is present at the location it takes precedence over any image currently associated with the diaryEntry (because it's the image you'll overwrite with if you press "save")

            Bitmap imgb = BitmapFactory.decodeFile(img.getAbsolutePath());
            i.setImageBitmap(imgb);
        }

    }

    // TODO: Visually indicate if currently recording or not
    // TODO: Add the audio file to the current DiaryEntry object (easy to do once it works)
    // TODO: Add functionality to play it back and add it to the interface
    public void toggleRecordAudio() {
        // If the MediaRecorder has already been set, just toggle start/stop. Otherwise create it and set it up.
        if (mr == null) {
            mr = new MediaRecorder();
            mr.setAudioSource(MIC);
            mr.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); // Don't really care which format for now
            File outfile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC) + File.separator + position + ".3gp");
            //File outfile = new File(getExternalCacheDir().getAbsolutePath() + File.separator + position + ".3gp");
            mr.setOutputFile(outfile);
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT); // OPUS resulted in "invalid audio encoder" error
            try { mr.prepare(); }
            catch(IOException e) { e.printStackTrace(); }
            // Add a small delay before recording starts?
            //try {
            //    Thread.sleep(2000);
            //}
            //catch (InterruptedException e) { e.printStackTrace(); }
        }
        // Use this same button to both start and stop.
        if (!recording) { mr.start(); recording = true; }
        else { mr.stop(); mr.release(); recording = false; }
    }

    public void buttonHandler(View v) {

        Log.d("all", "buttonHandler running");

        switch(v.getId()) {

            case R.id.save_diary_entry_button:
                String text = ((EditText) findViewById(R.id.diary_text)).getText().toString();
                // TODO: Ensure the image is fetched/saved here

                boolean new_entry = false;

                // If this is a new entry
                if (diaryEntry == null) {
                    // If this is an image for a new entry set the position to be the next one in the arraylist
                    if (position == -1) position = DiaryStorage.diary.size();
                    // Create a new diaryEntry. Setting the image below if there is one.
                    diaryEntry = new DiaryEntry(text);
                    // Used to determine whether to add the diaryEntry to the list or modify the existing one
                    new_entry = true;
                }

                // When saving move the photo from the temp location to the regular location
                if (diaryEntryTmp.image != null) {
                    // Find new name, rename the file to that name and save that name to diaryEntry
                    File newname = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + position + ".jpg");
                    diaryEntryTmp.image.renameTo(newname);
                    diaryEntry.image = newname; // Maybe not necessary, just ensuring we've stored the right path
                    // Save to Firestore
                    fs.upload(diaryEntry.image, position + ".jpg");
                }

                // If this is a new entry
                if (new_entry == true) {
                    // This will be a text only entry if image is null, should work for either case
                    DiaryStorage.diary.add(diaryEntry);
                }
                // If this is an existing entry
                else {
                    // Update the edited object with its current information
                    diaryEntry.text = text;
                    // Ideally it should also be possible to simple delete the associated photo without replacing with something else
                    // Update: This isn't necessary since we always store a file path, and that file path never changes
                    //if (diaryEntryTmp.image != null) diaryEntry.image = diaryEntryTmp.image;
                }

                // Note: Reset what's in the static diaryEntryTmp, after it's been saved is done in MainActivity, since then it'll work after saving, pressing "back" or using the hardware back button.

                Intent i = new Intent(this, MainActivity.class);
                // Pass information to MainActivity that an entry was added/saved, so it can call notifyDataSetChanged or otherwise ensure that the display of the list is updated
                i.putExtra("status", 1); // status 1 = updated -> notifyDataSetChanged
                i.putExtra("position", position);
                startActivity(i);
                break;
            case R.id.take_photo_button:

                // Obtain a copy of what the entry currently looks like
                String tmptxt = ((EditText) findViewById(R.id.diary_text)).getText().toString();
                diaryEntryTmp.text = tmptxt;

                // Used to just call a method, but inside it does several things which Android requires an Activity for, such as startActivityForResult, so turning it into an Activity instead.
                Intent i4 = new Intent(this, ImageHandlerActivity.class);
                i4.putExtra("position", position);
                startActivity(i4);
                break;
            // TODO: Maybe Gallery photo or just pick a file from the file system?
            //case R.id.gallery_photo_button:
            //    ImageHandler.pickGalleryPhoto();
            //    break;
            case R.id.delete_diary_entry_button:
                // Delete the diaryEntry from the list
                DiaryStorage.diary.remove(diaryEntry);

                Intent i2 = new Intent(this, MainActivity.class);
                // Pass information to MainActivity that an entry was deleted, so it can call notifyItemRemoved or otherwise ensure that the display of the list is updated
                i2.putExtra("status", 2); // status 2 = deleted -> notifyItemRemoved
                i2.putExtra("position", position);
                startActivity(i2);
                break;
            case R.id.cancel_diary_entry_button:
                Intent i3 = new Intent(this, MainActivity.class);
                startActivity(i3);
                break;
            // Code repetition here for now, as it's also in MainActivity. booo
            case R.id.record_audio_button:
                toggleRecordAudio();
                break;
        }
    }

}