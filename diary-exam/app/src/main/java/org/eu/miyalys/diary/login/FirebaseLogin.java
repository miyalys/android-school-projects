package org.eu.miyalys.diary.login;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.eu.miyalys.diary.LandingActivity;
import org.eu.miyalys.diary.MainActivity;

public class FirebaseLogin {

        // TODO: Should this be static?
        public FirebaseAuth auth;  // current user object.
        private static LandingActivity landingActivity;

        // This constructor is only called from LandingActivity, to save a reference to the list of diary entries. May be called multiple times, but technically only needs to be called once.
        public FirebaseLogin(LandingActivity a) {
            this(); // Avoid code duplication, so calling the no arg constructor
            landingActivity = a;
        }

        public FirebaseLogin() {
            auth = FirebaseAuth.getInstance();
            setupAuthStateListener();
        }

        private void setupAuthStateListener(){
            Log.d("all", "setup running");
            auth.addIdTokenListener(new FirebaseAuth.IdTokenListener() {
                @Override
                public void onIdTokenChanged(@NonNull FirebaseAuth firebaseAuth) {
                    Log.d("all", "onIdTokenChanged running");
                    // Ideally this should redirect to LandingActivity, but unfortunately non activities can't easily start activities
                    if(firebaseAuth.getCurrentUser() == null){
                        Log.d("all", "Signed out from Firebase");
                        // Should redirect to LandingActivity, but since this is no Activity it can't create other activities...
                        // DiaryStorage.diary.clear();
                    }else {
                        Log.d("all", "Signed in to Firebase");
                    }
                }
            });
        }

        // will require email and password (min. 6 chars)
        public void signIn(String email, String pwd){
            auth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(landingActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Log.d("all", "Sign in success? " + task.getException());
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
            auth.signOut(); // signs out the current user
        }
}
