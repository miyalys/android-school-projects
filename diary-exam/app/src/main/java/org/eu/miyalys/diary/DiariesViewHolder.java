package org.eu.miyalys.diary;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DiariesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Note: On the list we only show a generic symbol to indicate a photo is stored. The photo, if there is one, is only actually shown on ViewAddDiaryEntry.
    private TextView dtext;
    private TextView dcreation_time;
    private OnDiaryEntryListener onDiaryEntryListener;

    public DiariesViewHolder(@NonNull View itemView, OnDiaryEntryListener onDiaryEntryListener) {
        super(itemView);

        // - For detecting which DiaryEntry was clicked on in the RecyclerList, to determine which entry should be shown/edited.
        this.onDiaryEntryListener = onDiaryEntryListener;

        // Get references to the Views for setting from the Adapter via the method below
        dtext = itemView.findViewById(R.id.diary_entry_textview_text);
        dcreation_time = itemView.findViewById(R.id.diary_entry_textview_time);

        // - For detecting which DiaryEntry was clicked on in the RecyclerList, to determine which entry should be shown/edited.
        itemView.setOnClickListener(this);
    }

    // TODO: Handle images?
    // My own method for setting the values of the views with the data from the Adapter
    public void setMyViews(String text, Instant creation_time) {
        // This if else is just an example way of dynamically changing what's displyed based on what's received
        dtext.setText(text);
        // Converting a unix timestamp to a date representation requires a locale
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                        .withLocale( Locale.GERMANY ) // Same timezone as DK?
                        .withZone( ZoneId.systemDefault() );
        // With substring remove the time so only the date is shown, for now
        dcreation_time.setText(formatter.format(creation_time).substring(0, 8));
    }

    // - For detecting which DiaryEntry was clicked on in the RecyclerList, to determine which entry should be shown/edited.
    @Override
    public void onClick(View view) {
        onDiaryEntryListener.onDiaryEntryClick(getAdapterPosition());
    }
}
