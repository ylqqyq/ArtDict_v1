package com.example.artdict_v1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonService {

    public ArrayList<Artwork> parseArtListJson(String jsonThumbStr) {

        ArrayList<Artwork> fullArtList = new ArrayList<>(0);

        try {
            JSONArray jsonArray = new JSONArray(jsonThumbStr);
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = new JSONObject(jsonThumbStr);
                JSONArray thumbArr = jsonObject.getJSONArray("data");
                JSONObject thumbObj = thumbArr.getJSONObject(i);
                int id = thumbObj.getInt("id");
                String title = thumbObj.getString("title");
                String imgStr = thumbObj.getString("img_id");

                fullArtList.add(new Artwork(id,title,imgStr));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fullArtList;
    }

    public Artwork parseArtDetailJson(String jsonArtStr) {
        Artwork artDetailData = new Artwork();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonArtStr);
            JSONObject detailObj = jsonObject.getJSONObject("data");
            int id = detailObj.getInt("id");
            String title = detailObj.getString("title");
            String artist = detailObj.getString("artist_display");
            String dimensions = detailObj.getString("dimensions");
            String medium = detailObj.getString("medium_display");
            boolean zoomable = detailObj.getBoolean("is_zoomable");
            String image = detailObj.getString("image_id");
            artDetailData = new Artwork(id,title,artist,medium,dimensions,image,zoomable);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return artDetailData;




    }
}
