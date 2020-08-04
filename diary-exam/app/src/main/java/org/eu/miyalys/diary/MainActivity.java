package org.eu.miyalys.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.eu.miyalys.diary.login.FirebaseLogin;

public class MainActivity extends AppCompatActivity implements OnDiaryEntryListener {

    public static DiariesAdapter diariesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("all", "mainActivity onCreate running");

        // Note: Reset what's in the static diaryEntryTmp, after it's been saved is done in MainActivity, since then it'll work after saving, pressing "back" or using the hardware back button.
        // Also delete the photo temp file
        ViewAddDiaryEntryActivity.diaryEntryTmp = null;

        // Get a reference to the RecyclerView from the XML file
        RecyclerView rv = findViewById(R.id.diaries_recyclerview);
        // Create and set its LayoutManager
        rv.setLayoutManager(new LinearLayoutManager(this));
        // Create and set its Adapter
        diariesAdapter = new DiariesAdapter(this);
        rv.setAdapter( diariesAdapter );

        // Updated should be set to true when returning from ViewAddDiaryEntry having clicked "save"
        int status = getIntent().getIntExtra("status", -1);
        int position = getIntent().getIntExtra("position", -1);
        if (status != -1) Log.d("all", "status was set!");
        // status 1: updated, status 2: deleted
        if (status == 0) diariesAdapter.notifyItemChanged(position);
        if (status == 1) diariesAdapter.notifyItemRemoved(position);

        // TODO: Maybe just temp while testing
        DiaryStorage.init();

    }

    public void viewDiaryEntry(View v) {
        Intent i = new Intent(this, ViewAddDiaryEntryActivity.class);
        LinearLayout r = (LinearLayout) v;

        // Pass in which one was clicked on so the clicked entry can be shown/edited
        //i.putExtra
        startActivity(i);
    }

    public void buttonHandler(View v) {

        Log.d("all", "buttonHandler running");

        switch(v.getId()) {

            case R.id.add_diary_entry_button:
                Intent i = new Intent(this, ViewAddDiaryEntryActivity.class);
                startActivity(i);
                break;
            case R.id.exit_button:
                new FirebaseLogin().signOut();
                this.finishAffinity();
                break;
        }
    }

    // Required by my OnDiaryEntryListener - For detecting which DiaryEntry was clicked on in the RecyclerList, to determine which entry should be shown/edited.
    @Override
    public void onDiaryEntryClick(int position) {
        Intent i = new Intent(this, ViewAddDiaryEntryActivity.class);
        // Pass into the activity which one was clicked
        i.putExtra("position", position);
        startActivity(i);
    }
}