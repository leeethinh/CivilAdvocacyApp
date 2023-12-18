package com.thinhle.civiladvocacyapp;

import android.net.Uri;
import android.util.Pair;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Optional;

public class OfficialLoaderVolley {
    private static MainActivity mainActivity;
    private static RequestQueue queue;
    private static Official officialObj;
    private OfficialAdapter officialAdapter;
    public static final String KEY = "AIzaSyAfFyfouJp7DVEIrOJODmyJUwe9X1h345I";
    private static final String BASE_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key=";
    public String addy;

    public static void downloadOfficial(MainActivity mainActivityIn,
                                   String query) {

        mainActivity = mainActivityIn;
        String limit = "15";

        queue = Volley.newRequestQueue(mainActivity);
        String urlToUse = BASE_URL + KEY + "&address=" + query;

        Response.Listener<JSONObject> listener =
                response -> handleResults(mainActivity, response.toString());

        Response.ErrorListener error =
                error1 ->  Toast.makeText(mainActivityIn, "Location not found", Toast.LENGTH_SHORT).show(); //mainActivity.updateData(null);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
    private static void handleResults(MainActivity mainActivity, String s) {

        if (s == null) {
            return;
        }

        final Pair<String,ArrayList<Official>> officialList = parseJSON(s);
        ArrayList<Official> ofList= officialList.second;
        if (ofList != null){
            mainActivity.updateData(ofList);
            mainActivity.updateAddress(officialList.first);}
        if(ofList.size()==0){ //if search query is invalid/return 0 results, call noResultsFound
            mainActivity.noResultsFound();
        }
    }

    private static Pair<String,ArrayList<Official>> parseJSON(String s) {
        ArrayList<Official> officialList = new ArrayList<>();
        try {
            JSONObject response = new JSONObject(s);

            // Extract data from normalizedInput
            JSONObject normalizedInput = response.optJSONObject("normalizedInput");
            String line= normalizedInput.optString("line1");
            String city = normalizedInput.optString("city");
            String state = normalizedInput.optString("state");
            String zip = normalizedInput.optString("zip");
            String normalizedInputAddress="";
            if(city.isEmpty()&&zip.isEmpty()){
                 normalizedInputAddress = state;
            }
            else if(zip.isEmpty()){
                 normalizedInputAddress = city + ", "+state;
            }
            else if (line.isEmpty()){
                normalizedInputAddress =city + ", " + state + ", " + zip;
            }
            else {
                 normalizedInputAddress = line + ", "+city + ", " + state + ", " + zip;
            }
            // Extract data from offices and officials
            JSONArray offices = response.optJSONArray("offices");
            JSONArray officials = response.optJSONArray("officials");

            // Loop through offices and officials
            for (int i = 0; i < offices.length(); i++) {
                JSONObject office = offices.getJSONObject(i);
                String officeName = office.optString("name");
                JSONArray officialIndices = office.optJSONArray("officialIndices");


                if (officialIndices != null) {
                    for (int j = 0; j < officialIndices.length(); j++) {
                        int officialIndex = officialIndices.getInt(j);
                        JSONObject official = officials.getJSONObject(officialIndex);

                        // Extract individual official information
                        String officialName = official.optString("name");

                        JSONArray officialAddressArray = official.optJSONArray("address");
                        if (officialAddressArray != null && officialAddressArray.length() > 0) {
                            JSONObject officialAddress = officialAddressArray.optJSONObject(0);
                            String line1 = officialAddress.optString("line1");
                            String line2= officialAddress.optString(("line2"));
                            String line3= officialAddress.optString(("line3"));
                            String mcity = officialAddress.optString("city");
                            String mstate = officialAddress.optString("state");
                            String mzip = officialAddress.optString("zip");
                            String addressString="";
                            if(line2.isEmpty()&&line3.isEmpty()){
                                 addressString = line1 +" " + mcity + ", " + mstate + " " + mzip;
                            }
                            else if(line3.isEmpty()) {
                                 addressString = line1 + " " + line2 + " " + mcity + ", " + mstate + " " + mzip;
                            }
                            else{
                                 addressString = line1 + " " + line2 + " " + line3 + " "+ mcity + ", " + mstate + " " + mzip;
                            }


                            String party = official.optString("party");

                            JSONArray phones = official.optJSONArray("phones");
                            String phone = "";
                            if (phones != null && phones.length() > 0) {
                                phone = phones.getString(0);
                            }

                            JSONArray urls = official.optJSONArray("urls");
                            String url = "";
                            if (urls != null && urls.length() > 0) {
                                url = urls.getString(0);
                            }

                            JSONArray emails = official.optJSONArray("emails");
                            String email = "";
                            if (emails != null && emails.length() > 0) {
                                email = emails.getString(0);
                            }

                            String photoUrl = official.optString("photoUrl");

                            JSONArray channels = official.optJSONArray("channels");
                            String mediaFacebook="";
                            String mediaTwitter="";
                            String mediaYoutube="";

                            if (channels != null) {
                                for (int k = 0; k < channels.length(); k++) {
                                    JSONObject channel = channels.getJSONObject(k);
                                    String channelType = channel.optString("type");
                                    String channelID = channel.optString("id");

                                    if (channelType.equalsIgnoreCase("Twitter")) {
                                        mediaTwitter = channelID;
                                    } else if (channelType.equalsIgnoreCase("Facebook")) {
                                        mediaFacebook = channelID;
                                    } else if (channelType.equalsIgnoreCase("Youtube"))
                                        mediaYoutube = channelID;
                                }
                            }
                                officialList.add(new Official(officialName, officeName, party, mediaFacebook, mediaTwitter, mediaYoutube, addressString, phone, email, url, photoUrl));

                        }
                    }
                }
            }

            return Pair.create(normalizedInputAddress,officialList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }










}
