package com.example.listview;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {

    // Create an inner class for some testing data
    public class MyData {
       String text;
       int imageResourceId; // AKA a UriString

       MyData(String text, int imageResourceId) {
          this.text = text;
          this.imageResourceId = imageResourceId;
       }
    }

    List<MyData> myData;

    // Add some dummy data when the Adapter is created
    MyAdapter() {
       myData = new ArrayList<>();
       myData.add(new MyData("Test1", R.drawable.a));
       myData.add(new MyData("Test2", R.drawable.b));
       myData.add(new MyData("Test3", R.drawable.c));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout ll = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_layout, parent, false);
        return new MyViewHolder(ll);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Cast it to MyViewHolder so android-studio knows setMyViews is an available method
        MyViewHolder mvh = (MyViewHolder) holder;
        mvh.setMyViews( myData.get(position).text, myData.get(position).imageResourceId );
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }
}
