package com.example.artdict_v1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements ResultAdapter.artClickListener,NetworkingService.networkingListener {
    ResultAdapter adapter;
    NetworkingService networkingService;
    ArrayList<Artwork> artListC;
    ArrayList<Artwork> artListR;
    ArrayList<Artwork> fullList;
    JsonService jsonService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        networkingService = ((myApp)getApplication()).getNetworkingService();
        jsonService = ((myApp)getApplication()).getJsonService();
        //combine two arraylist for display
        fullList.addAll(artListC);
        fullList.addAll(artListR);

        RecyclerView recyclerView = findViewById(R.id.resultGrid);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this,numberOfColumns));
        adapter = new ResultAdapter(this,fullList);
        recyclerView.setAdapter(adapter);
        networkingService.listener = this;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem searchViewMenuItem = menu.findItem(R.id.search);

        //search bar setup and define query
        SearchView searchView = (SearchView) searchViewMenuItem.getActionView();
        String keyword = searchView.getQuery().toString();
        if(!keyword.isEmpty()) {
            searchView.setIconified(false);
            searchView.setQuery(keyword,false);
        }
        searchView.setQueryHint("Search for specific subjects");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>=3) {
                    networkingService.fetchArtListData(newText);
                }
                else {
                    adapter.artList = new ArrayList<>(0);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public void artSelected(Artwork selectedArt) {
        Intent toDetail = new Intent(this,DetailActivity.class);
        toDetail.putExtra("selectedArtwork",selectedArt);
        startActivity(toDetail);
        //should I put the object in or just id???
    }

    @Override
    public void APIlistener(String jsonString) {
        artListC = jsonService.parseArtListJsonChi(jsonString);
        artListR = jsonService.parseArtListJsonRijks(jsonString);
        //BIG PROBLEM: use this list of id to make a image id call?????
        for(int i = 0; i < artListC.size(); i++) {
        networkingService.getImgID(artListC.get(i).id);
        }

        adapter.artList = fullList;
        adapter.notifyDataSetChanged();
        for (int i = 0; i < artListR.size(); i++) {
            networkingService.getImageDatafromRijks(artListR.get(i).image_id);
        }
    }

    @Override
    public void APIImgListener(Bitmap image) {adapter.imgView.setImageBitmap(image); }
}