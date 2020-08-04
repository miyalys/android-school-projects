package com.example.mynotebook;

import java.util.ArrayList;
import java.util.List;

public class Notes {

        public static List<Note> noteList;
        private static Notes instance;

        private Notes() {

                noteList = new ArrayList<>();
                noteList.add(new Note("Test1", "Expanded text1"));
                noteList.add(new Note("Test2", "Expanded text2"));
                noteList.add(new Note("Test3", "Expanded text3"));
        }

        public static Notes init() {
               if (instance == null) instance = new Notes();
               return instance;
        }

        public List<Note> getNoteList() {
               return noteList;
        }
}
