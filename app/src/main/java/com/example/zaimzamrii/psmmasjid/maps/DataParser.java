package com.example.zaimzamrii.psmmasjid.maps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser
{
    private HashMap<String, String > getSingleNearbyPlace(JSONObject googlePlaceJSON)
    {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String NameOfPlace = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longtitude = "";
        String reference = "";
        String rating ="";

        try
        {
            if(!googlePlaceJSON.isNull("name"))
            {
                NameOfPlace = googlePlaceJSON.getString("name");
            }
            if(!googlePlaceJSON.isNull("vicinity"))
            {
                vicinity = googlePlaceJSON.getString("vicinity");
            }
            if(!googlePlaceJSON.isNull("rating"))
            {
                rating = googlePlaceJSON.getString("rating");
            }


            latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longtitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJSON.getString("reference");

            googlePlaceMap.put("place_name", NameOfPlace);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longtitude);
            googlePlaceMap.put("reference", reference);
            googlePlaceMap.put("rating", rating);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return googlePlaceMap;
    }

    private List<HashMap<String,String>> getAllNearbyPlaces (JSONArray jsonArray)
    {
        int counter = jsonArray.length();

        List<HashMap<String,String>> NearbyPlacesList = new ArrayList<>();

        HashMap<String, String> NearbyplaceMap = null;

        for (int i=0; i<counter; i++)
        {
            try
            {
                NearbyplaceMap = getSingleNearbyPlace((JSONObject) jsonArray.get(i));
                NearbyPlacesList.add(NearbyplaceMap);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return NearbyPlacesList;
    }

    public List<HashMap<String, String>> parse(String jSONdata)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(jSONdata);
            jsonArray = jsonObject.getJSONArray("results");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return getAllNearbyPlaces(jsonArray);
    }
}
