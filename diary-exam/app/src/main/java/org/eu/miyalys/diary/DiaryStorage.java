package org.eu.miyalys.diary;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Stores the in-memory list of DiaryEntry's, though permanent storage is on Firestore
public class DiaryStorage {

    public static List<DiaryEntry> diary;
    public static String diaryPathOnFirebase = "diary";
    private static boolean initialized;
    private static DiaryStorage ds;

    private DiaryStorage() {
        diary = new ArrayList<>();
        diary.add(new DiaryEntry("Surfing"));
        // Entry with dummy empty bitmap
        //diary.add(new DiaryEntry( "Do dishes", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)));
        //diary.add(new DiaryEntry( "Do dishes", new File("random_name")));
        diary.add(new DiaryEntry( "Do dishes"));
        diary.add(new DiaryEntry("Thoughts"));
    }

    // Singleton
    public static DiaryStorage init() {
        Log.d("all", "DiaryStorage initialized?: " + String.valueOf(initialized));
        if (!initialized) {
            initialized = true;
            return new DiaryStorage();
        }
        else return ds;
    }
}
