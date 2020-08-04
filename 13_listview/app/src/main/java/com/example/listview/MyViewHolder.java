package com.example.listview;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    private TextView tv;
    private ImageView iv;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        // Get references to the Views for setting from the Adapter via the method below
        tv = itemView.findViewById(R.id.myTextView);
        iv = itemView.findViewById(R.id.myImageView);
    }

    // My own method for setting the values of the views with the data from the Adapter
    public void setMyViews(String text, int imageResourceId) {
        // This if else is just an example way of dynamically changing what's displyed based on what's received
      if (text.equals("Test1")) tv.setText("dynamic" + text);
      else tv.setText(text);
      //iv.setImageResource(R.drawable.a);
      iv.setImageResource(imageResourceId);
    }
}
