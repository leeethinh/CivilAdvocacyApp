package com.thinhle.civiladvocacyapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewholder extends RecyclerView.ViewHolder{
    TextView name;
    TextView office;
    ImageView image;
    public OfficialViewholder(@NonNull View itemView) {
        super(itemView);
        image =itemView.findViewById(R.id.entry_image);
        name =itemView.findViewById(R.id.entry_name);
        office =itemView.findViewById(R.id.entry_office);
    }



}
