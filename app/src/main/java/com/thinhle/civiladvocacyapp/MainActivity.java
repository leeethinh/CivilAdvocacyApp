package com.thinhle.civiladvocacyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.thinhle.civiladvocacyapp.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener{
    private FusedLocationProviderClient mFusedLocationClient;
    public ActivityMainBinding binding;
    Toolbar myToolbar;
    private RecyclerView recyclerView;
    private OfficialAdapter officialAdapter;
    private static final int LOCATION_REQUEST = 111;
    private String normalizedInputAddress;

    private static String locationString = "Unspecified Location";
    private final ArrayList<Official> officialList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myToolbar =  binding.toolbar;
        myToolbar.setTitle(R.string.app_name);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);


        recyclerView = findViewById(R.id.recyclerView);
        officialAdapter = new OfficialAdapter(officialList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        determineLocation();



    }
    public void updateAddress(String s){
        binding.addressTextView.setText(s);
    }
    public void noResultsFound(){ //if search returns 0 results
    }
    public void onClick(View v) { //clicking object on recycler view

        int pos = recyclerView.getChildLayoutPosition(v);
        String querySearch = binding.addressTextView.getText().toString();
        Official selectedOfficial = officialList.get(pos);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("OFFICIAL", selectedOfficial);
        intent.putExtra("LOCATION", querySearch);
        startActivity(intent);
        return;
    }

    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
    //    Artwork m = artList.get(pos);
        Toast.makeText(v.getContext(), "You long clicked this ", Toast.LENGTH_SHORT).show();
        return true;

    }
    public void updateData(ArrayList<Official> cList) {
        officialList.addAll(cList);
      //  Collections.sort(officialList);
        officialAdapter.notifyItemRangeChanged(0, cList.size());
    }
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.menuB) { //copyright Activity
            Toast.makeText(this, "You want to View more Info", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

            return true;
        } else if (item.getItemId() == R.id.menuA) { //save button
            checkNet(item);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }
    private void determineLocation() {
        // Check perm - if not then start the  request and return
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some situations this can be null.
                    if (location != null) {
                        locationString = getPlace(location);
                       binding.addressTextView.setText(locationString);
                        OfficialLoaderVolley.downloadOfficial(this,locationString);
                        Toast.makeText(this, locationString, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    binding.addressTextView.setText("Denied");
                }
            }
        }
    }

    private String getPlace(Location loc) {
        StringBuilder sb = new StringBuilder();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                String addr = addresses.get(0).getAddressLine(0);
                sb.append(String.format(
                        Locale.getDefault(),
                        "%s%n%nProvider: %s%n%n%.5f, %.5f",
                        addr, loc.getProvider(),
                        loc.getLatitude(), loc.getLongitude()));
            } else {
                sb.append("Cannot determine location");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void checkNet(MenuItem v) {
        if (hasNetworkConnection()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(et);
            // lambda can be used here (as is below)
            builder.setPositiveButton("OK", (dialog, id) -> {
                String querySearch = et.getText().toString();
                officialList.clear();
                officialAdapter.notifyDataSetChanged();
                OfficialLoaderVolley.downloadOfficial(this,querySearch);



            });

            // lambda can be used here (as is below)
            builder.setNegativeButton("Cancel", (dialog, id) -> {
                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            });
            builder.setMessage("Location Search: ");
            //  builder.setTitle("Single Input");
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            officialList.clear();
            officialAdapter.notifyDataSetChanged();
            binding.addressTextView.setText("No Data For Location");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Network Connection");
            builder.setMessage("Data cannot be accessed/loaded without an internet connection.");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;

        }
    }

    private boolean hasNetworkConnection(){
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return(networkInfo!=null && networkInfo.isConnectedOrConnecting());

    }
    public void goCopyright(View view){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}