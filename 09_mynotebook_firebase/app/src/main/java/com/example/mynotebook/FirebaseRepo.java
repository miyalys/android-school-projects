package com.example.mynotebook;

        import android.util.Log;
        import android.widget.ArrayAdapter;

        import androidx.annotation.Nullable;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.EventListener;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.FirebaseFirestoreException;
        import com.google.firebase.firestore.QuerySnapshot;

        import java.util.HashMap;
        import java.util.Map;

public class FirebaseRepo {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    //public static RecyclerView.Adapter adapter;


    public static Note getNote(int index){
        if(index >= MainActivity.notes.size()) return new Note("New note", "Test");
        return MainActivity.notes.get(index);
    }

    static { // make sure the listener starts as soon as possible
        startNoteListener();
    }

    private static void startNoteListener() {
        FirebaseRepo.db.collection(MainActivity.notesPath).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot values, @Nullable FirebaseFirestoreException e) {
                // Clear the arrayList before filling it from data fetched from Firebase
                MainActivity.notes.clear();
                for (DocumentSnapshot snap: values.getDocuments()) {
                    Log.i("all", "read from Firebase " + snap.getId() + " " + snap.get("title").toString());
                    MainActivity.notes.add(new Note(snap.getId(), snap.get("title").toString(), snap.get("text").toString()));
                }
                MainActivity.arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public static void editNote(int index, String title, String text) {
        // Get a Firebase ref. to the current note object
        String id = MainActivity.notes.get(index).getId();
        DocumentReference docRef = FirebaseRepo.db.collection(MainActivity.notesPath).document(id);
        // Make a new map to store all the data (including previous data)
        Map<String,String> map = new HashMap<>();
        map.put("title", title);
        map.put("text", text);
        // overwrites the previous map with the current one.
        docRef.set(map);
    }

    public static void deleteNote(int index) {
        String key = MainActivity.notes.get(index).getId();
        DocumentReference docRef = FirebaseRepo.db.collection(MainActivity.notesPath).document(key);
        docRef.delete();
    }
}
