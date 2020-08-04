package org.eu.miyalys.diary;

// For detecting which DiaryEntry was clicked on in the RecyclerList, to determine which entry should be shown/edited.
public interface OnDiaryEntryListener {
    void onDiaryEntryClick(int position);
}
