package com.example.mynotebook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Notes implements Serializable {

        private static List<Note> noteList;
        private static Notes instance;

        public Notes() {

            noteList = new ArrayList<>();
            noteList.add(new Note("My note 1", "Expanded text1"));
            noteList.add(new Note("My note 2", "Expanded text2"));
            noteList.add(new Note("My note 3", "Expanded text3"));

            noteList.add(new Note("My secret note 1", "Expanded text1", "m@m.me"));
            noteList.add(new Note("My secret note 2", "Expanded text2", "m@m.me"));
            noteList.add(new Note("My secret note 3", "Expanded text3", "m@m.me"));
        }

        // The getter to use while signed out
        public static List<Note> getNotes() {
            return noteList.stream().filter(note -> note.user == null).collect(Collectors.toList());
        }

        public static List<Note> getNotesSignedIn(String email) {
            return noteList.stream().filter(note -> note.user == null || note.user.equals(email)).collect(Collectors.toList());
        }

        public static void add(Note note) {
           noteList.add(note);
        }
}
