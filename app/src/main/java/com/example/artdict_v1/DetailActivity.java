package com.example.artdict_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity implements NetworkingService.networkingListener{

    JsonService jsonService;
    NetworkingService networkingService;
    TextView detail_text;
    ImageView imageView;
    Artwork artwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        artwork = getIntent().getParcelableExtra("selectedArtwork");
        detail_text = findViewById(R.id.detail);
        imageView = findViewById(R.id.myImage);
        jsonService = ((myApp)getApplication()).getJsonService();
        networkingService = ((myApp)getApplication()).getNetworkingService();
        networkingService.listener = this;
        networkingService.fetchArtDetailData(129884);
//        networkingService.fetchArtDetailData(artwork.id);
    }

    @Override
    public void APIlistener(String jsonString) {
        Artwork artDetailData = jsonService.parseArtDetailJson(jsonString);
        detail_text.setText("Title: "+artDetailData.title + "\n" + "Artist: " + artDetailData.artist_display +"\n" +"Medium: " + artDetailData.medium_display);
        networkingService.getImageData(artDetailData.image_id);



    }

    @Override
    public void APIImgListener(Bitmap image) {

        imageView.setImageBitmap(image);


    }
}