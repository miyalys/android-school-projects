package org.eu.miyalys.diary;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

/* Firebase: For handling remote storage of DiaryEntry objects */
public class Firebase {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    //public static RecyclerView.Adapter adapter;

    static { // make sure the listener starts as soon as possible
        startDiaryListener();
    }

    private static void startDiaryListener() {
        Firebase.db.collection(DiaryStorage.diaryPathOnFirebase).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException e) {
                // Clear the arrayList before filling it from data fetched from Firebase
                DiaryStorage.diary.clear();
                for (DocumentSnapshot snap: values.getDocuments()) {
                    Log.i("all", "read from Firebase " + snap.getId() + " " + snap.get("text").toString());
                }
                // TODO
                //DiaryStorage.diary.add(new DiaryEntry(snap.getId(), snap.get("text").toString()));
                // TODO
                //MainActivity.arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public static void editDiaryEntry(int index, String title, String text) {
        // Get a Firebase ref. to the current note object
        String id = DiaryStorage.diary.get(index).id;
        DocumentReference docRef = Firebase.db.collection(DiaryStorage.diaryPathOnFirebase).document(id);
        // Make a new map to store all the data (including previous data)
        Map<String,String> map = new HashMap<>();
        map.put("text", text);
        // overwrites the previous map with the current one.
        docRef.set(map);
    }

    public static void deleteDiaryEntry(int index) {
        String key = DiaryStorage.diary.get(index).id;
        DocumentReference docRef = Firebase.db.collection(DiaryStorage.diaryPathOnFirebase).document(key);
        docRef.delete();
    }

}
