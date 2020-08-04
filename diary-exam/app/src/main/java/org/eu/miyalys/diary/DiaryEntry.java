package org.eu.miyalys.diary;

import java.io.File;
import java.time.Instant;
import android.graphics.Bitmap;

// The model for what DiaryEntry looks like
public class DiaryEntry {

    public String id;
    public String text;
    // TODO: Consider a preview image on ViewAddDiaryEntry, while storing the full size image to be shown if the user clicks it
    // Technically I don't need to track this if it's always in the same path based off array position:
    public File image;
    public Instant creation_time;
    public Instant updated_time;

//public DiaryEntry(){} // empty constructor needed?

    public DiaryEntry() {} // Used by ViewAddDiaryEntry to create diary_entry_tmp

    public DiaryEntry(String text, File image) {
        this.image = image;
        this.text = text;
        set_creation_time();
    }

    public DiaryEntry(String text) {
        this.text = text;
        set_creation_time();
    }

    public void set_creation_time() {
        this.creation_time = Instant.now();
    }

    public void set_updated_time() {
        this.updated_time = Instant.now();
    }
}
