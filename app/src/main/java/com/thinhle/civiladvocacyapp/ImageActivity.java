package com.thinhle.civiladvocacyapp;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.thinhle.civiladvocacyapp.databinding.ActivityImageBinding;

public class ImageActivity extends AppCompatActivity {
    private ActivityImageBinding binding;
    Toolbar myToolbar;
    private Picasso picasso;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myToolbar = binding.toolbar3;
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        picasso = Picasso.get();
        Intent intent = getIntent();
        if (intent.hasExtra("LOCATION")) { //set address bar to query search
            String search = intent.getStringExtra("LOCATION");
            binding.addressTV2.setText(search);
        }

        if (intent.hasExtra("OFFICIAL")) { //
            Official selectedOfficial = (Official) intent.getSerializableExtra("OFFICIAL");
            if (selectedOfficial != null) {
                binding.imageName.setText(selectedOfficial.getName());
                binding.imageOffice.setText(selectedOfficial.getOffice());
                String imageUrl = selectedOfficial.getImageID();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    picasso.load(imageUrl).error(R.drawable.brokenimage).into(binding.imageView4);
                } else {
                    // Handle the case when the image URL is empty or null
                    binding.imageView4.setImageResource(R.drawable.missing);
                }
                if (selectedOfficial.getParty().equals("Republican Party")) {
                    binding.constraintLayout2.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                    binding.partyLogo2.setImageResource(R.drawable.rep_logo);
                } else if (selectedOfficial.getParty().equals("Democratic Party")) {
                    binding.constraintLayout2.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
                    binding.partyLogo2.setImageResource(R.drawable.dem_logo);
                } else {
                    binding.constraintLayout2.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
                    binding.partyLogo2.setVisibility(View.GONE);
                }
            }
        }
    }
    public void goWebsite(View v){
        Intent intent = getIntent();
        if (intent.hasExtra("OFFICIAL")) { //
            Official selectedOfficial = (Official) intent.getSerializableExtra("OFFICIAL");
            if(selectedOfficial.getParty().equals("Democratic Party")){
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://democrats.org/"));
            }
            else if(selectedOfficial.getParty().equals("Republican Party")) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gop.com/"));
            }
        }
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
