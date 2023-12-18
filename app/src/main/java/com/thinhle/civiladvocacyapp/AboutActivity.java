package com.thinhle.civiladvocacyapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.thinhle.civiladvocacyapp.databinding.ActivityAboutBinding;
import com.thinhle.civiladvocacyapp.databinding.ActivityImageBinding;

public class AboutActivity  extends AppCompatActivity {
    private ActivityAboutBinding binding;
    Toolbar myToolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myToolbar = binding.toolbar5;
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);
        binding.textView3.setPaintFlags(
                binding.textView3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


    }
    public void goAPI(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://developers.google.com/civic-information/"));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

}