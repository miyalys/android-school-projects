package com.example.mynotebook;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Firebase {

        // TODO: Should this be static?
        FirebaseAuth auth;  // current user object.
        private static MainActivity mainActivity;

        // This constructor is only called from MainActivity, to save a reference to the list of notes. May be called multiple times, but technically only needs to be called once.
        public Firebase(MainActivity activity) {
            //this(); // Avoid code duplication, so calling the no arg constructor
            FirebaseApp.initializeApp(activity); // Android-studio apparently insisted on calling this before constructing a Firebase object
            mainActivity = activity;
            auth = FirebaseAuth.getInstance();
            setupAuthStateListener();
        }

        public Firebase() {
            FirebaseApp.initializeApp(mainActivity); // Android-studio apparently insisted on calling this before constructing a Firebase object
            auth = FirebaseAuth.getInstance();
            setupAuthStateListener();
        }

        private void setupAuthStateListener(){
            Log.d("all", "setup running");
            auth.addIdTokenListener(new FirebaseAuth.IdTokenListener() {
                @Override
                public void onIdTokenChanged(@NonNull FirebaseAuth firebaseAuth) {
                    Log.d("all", "onIdTokenChanged running");
                    if(firebaseAuth.getCurrentUser() == null){
                        Log.d("all", "Signed out from Firebase");
                        //mainActivity.arrayAdapter.clear();
                        //mainActivity.arrayAdapter.addAll(mainActivity.notes.getNotes());
                        //mainActivity.arrayAdapter.notifyDataSetChanged();
                    }else {
                        Log.d("all", "Signed in to Firebase");
                    }
                }
            });
        }

        // sign in
        // will require email and password (min. 6 chars)
        public void signIn(String email, String pwd){
            auth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                mainActivity.arrayAdapter.clear();
                                mainActivity.arrayAdapter.addAll(mainActivity.notes.getNotesSignedIn(email));
                                mainActivity.arrayAdapter.notifyDataSetChanged();
                            }else {
                                Log.d("all", "Sign in failed " + task.getException());
                            }
                        }
                    });
        }

        public void signUp(String email, String pwd){
            auth.createUserWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Log.d("all", "Sign up success " +
                                        task.getResult().getUser().getEmail());
                            }else {
                                Log.d("all", "Sign up failed " + task.getException());
                            }
                        }
                    });
        }

        public void signOut(){
            // Only show the notes available to everyone, ie. when signed out
            mainActivity.arrayAdapter.clear();
            mainActivity.arrayAdapter.addAll(mainActivity.notes.getNotes());
            mainActivity.arrayAdapter.notifyDataSetChanged();
            auth.signOut(); // signs out the current user
        }
}
