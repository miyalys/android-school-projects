package org.eu.miyalys.diary;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DiariesAdapter extends RecyclerView.Adapter {

    // Not needed by the Adapter, but the ViewHolder needs it, and this Adapter constructs the Viewholder
    private OnDiaryEntryListener onDiaryEntryListener;

    DiariesAdapter(OnDiaryEntryListener o) {
        onDiaryEntryListener = o;
    }

    // Sets the viewType in onCreateViewHolder, here specifically based off whether an image is set or not
    @Override
    public int getItemViewType(int position) {
        if ( DiaryStorage.diary.get(position).image == null) {
            return 0;
        }
        else return 1;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout ll;
        Log.d("all", "ViewType was: " + viewType);
        if (viewType == 0) ll = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_entry_text_only, parent, false);
        else ll = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_entry_image, parent, false);

        return new DiariesViewHolder(ll, onDiaryEntryListener);
    }

    // Sets the content of the ViewHolder, and thus the layout, after creation.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Cast it to DiariesViewHolder so android-studio knows setMyViews is an available method
        DiariesViewHolder dvh = (DiariesViewHolder) holder;
        DiaryEntry d = DiaryStorage.diary.get(position);
        dvh.setMyViews( d.text, d.creation_time );
    }

    @Override
    public int getItemCount() {
        return DiaryStorage.diary.size();
    }


}
