package com.thinhle.civiladvocacyapp;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;


import com.squareup.picasso.Picasso;
import com.thinhle.civiladvocacyapp.databinding.ActivityOfficialBinding;
public class OfficialActivity  extends AppCompatActivity {
    private Picasso picasso;
    private ActivityOfficialBinding binding;
    Toolbar myToolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOfficialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myToolbar =  binding.toolbar2;
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        picasso = Picasso.get();
        Intent intent = getIntent();
        if(intent.hasExtra("LOCATION")){ //set address bar to query search
            String search = intent.getStringExtra("LOCATION");
            binding.addressTextView2.setText(search);
        }

        if (intent.hasExtra("OFFICIAL")) { //If Note object is passed from MA (onclick edit), setText fields to note values
            Official selectedOfficial= (Official) intent.getSerializableExtra("OFFICIAL");
            if (selectedOfficial != null){
                binding.nameTV.setText(selectedOfficial.getName());
                binding.officeTV.setText(selectedOfficial.getOffice());
                binding.partyTV.setText("("+selectedOfficial.getParty()+")");
                binding.addressTV.setText(selectedOfficial.getOfficeAddress());
                binding.phoneTV.setText(selectedOfficial.getPhoneNumber());
                binding.emailTV.setText(selectedOfficial.getEmailAddress());
               binding.websiteTV.setText(selectedOfficial.getWebsite());
                String imageUrl = selectedOfficial.getImageID();
                String facebook = selectedOfficial.getFacebook();
                String twitter = selectedOfficial.getTwitter();
                String youtube = selectedOfficial.getYoutube();
                if(!selectedOfficial.getPhoneNumber().isEmpty()){
                    Linkify.addLinks(binding.phoneTV,Linkify.ALL);
                    binding.phoneTV.setLinkTextColor(Color.parseColor("#FFFFFF"));
                  //  binding.phoneTV.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
                }
                if(!selectedOfficial.getOfficeAddress().isEmpty()){
                    Linkify.addLinks(binding.addressTV,Linkify.ALL);
                    binding.addressTV.setLinkTextColor(Color.parseColor("#FFFFFF"));
                }
                if(!selectedOfficial.getEmailAddress().isEmpty()){
                    Linkify.addLinks(binding.emailTV,Linkify.ALL);
                    binding.emailTV.setLinkTextColor(Color.parseColor("#FFFFFF"));
                }
                if(!selectedOfficial.getWebsite().isEmpty()){
                    Linkify.addLinks(binding.websiteTV,Linkify.ALL);
                    binding.websiteTV.setLinkTextColor(Color.parseColor("#FFFFFF"));
                }

                if(facebook!=null&&!facebook.isEmpty()){
                    binding.facebookImage.setVisibility(View.VISIBLE);
                }
                if(twitter!=null&&!twitter.isEmpty()){
                    binding.twitterImage.setVisibility(View.VISIBLE);
                }
                if(youtube!=null&&!youtube.isEmpty()){
                    binding.youtubeImage.setVisibility(View.VISIBLE);
                }
                if(selectedOfficial.getEmailAddress().isEmpty()){
                    binding.textView8.setVisibility(View.GONE);
                }
                if(selectedOfficial.getWebsite().isEmpty()){
                    binding.textView10.setVisibility(View.GONE);
                }
                if(selectedOfficial.getPhoneNumber().isEmpty()){
                    binding.textView6.setVisibility(View.GONE);
                }


               if (imageUrl != null && !imageUrl.isEmpty()) {
                    picasso.load(imageUrl).error(R.drawable.brokenimage).into(binding.imageView);
                } else {
                    // Handle the case when the image URL is empty or null
                    binding.imageView.setImageResource(R.drawable.missing);
                }
               if(selectedOfficial.getParty().equals("Republican Party")){
                   binding.constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                   binding.partyLogo.setImageResource(R.drawable.rep_logo);
               }
               else if(selectedOfficial.getParty().equals("Democratic Party")){
                   binding.constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
                   binding.partyLogo.setImageResource(R.drawable.dem_logo);
               }else{
                   binding.constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
                   binding.partyLogo.setVisibility(View.GONE);
               }

            } else {
                return;
            }
        }

    }
    public void facebookClicked(View v){
        Intent intent = getIntent();
        if (intent.hasExtra("OFFICIAL")) { //If Note object is passed from MA (onclick edit), setText fields to note values
            Official selectedOfficial= (Official) intent.getSerializableExtra("OFFICIAL");
            if (selectedOfficial != null) {
                String name = selectedOfficial.getFacebook();
                String FACEBOOK_URL = "https://www.facebook.com/"+name;

                // Check if FB is installed, if not we'll use the browser
                if (isPackageInstalled("com.facebook.katana")) {
                    String urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_URL));
                }

                // Check if there is an app that can handle fb or https intents
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    makeErrorAlert("No Application found that handles ACTION_VIEW (fb/https) intents");
                }
            }

        }
    }
    public void partyLogoClicked(View v){
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


    public void twitterClicked(View v) {
        Intent intent = getIntent();
        if (intent.hasExtra("OFFICIAL")) { //If Note object is passed from MA (onclick edit), setText fields to note values
            Official selectedOfficial= (Official) intent.getSerializableExtra("OFFICIAL");
            if (selectedOfficial != null) {
                String name = selectedOfficial.getTwitter();
                String twitterAppUrl = "twitter://user?screen_name=" + name;
                String twitterWebUrl = "https://twitter.com/" + name;
                if (isPackageInstalled("com.twitter.android")) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
                } else {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
                }

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    makeErrorAlert("No Application found that handles ACTION_VIEW (twitter/https) intents");
                }
            }
        }
    }
    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public boolean isPackageInstalled(String packageName) {
        try {
            return getPackageManager().getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void goImage(View view){
        Intent newIntent = new Intent(this, ImageActivity.class);
        Intent getObject = getIntent();
        Official selectedOfficial= (Official) getObject.getSerializableExtra("OFFICIAL");
        if(selectedOfficial.getImageID()!=null&&!selectedOfficial.getImageID().isEmpty()) {
            String querySearch = binding.addressTextView2.getText().toString();
            newIntent.putExtra("OFFICIAL", selectedOfficial);
            newIntent.putExtra("LOCATION", querySearch);
            startActivity(newIntent);
            return;
        }
    }
}