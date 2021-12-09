package com.example.artdict_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity implements NetworkingService.networkingListener{

    JsonService jsonService;
    NetworkingService networkingService;
    TextView detail_text;
    ImageView imageView;
    Artwork artwork;
    ImageButton save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        artwork = getIntent().getParcelableExtra("selectedArtwork");
        detail_text = findViewById(R.id.detail);
        imageView = findViewById(R.id.myImage);
//        save_btn = findViewById(R.id.save_btn);
        jsonService = ((myApp)getApplication()).getJsonService();
        networkingService = ((myApp)getApplication()).getNetworkingService();
        networkingService.listener = this;
        networkingService.fetchDetailData(129884);
        //above is for testing, which works, below is the real code
//        networkingService.fetchArtDetailData(artwork.id);
    }

    @Override
    public void APIlistener(String jsonString) {
        Artwork artDetailData = jsonService.parseArtDetailJsonChi(jsonString);
        detail_text.setText("Title: "+artDetailData.title + "\n" + "Artist: " + artDetailData.artist_display +"\n" +"Medium: " + artDetailData.medium_display);
        networkingService.getImageDatafromChi(artDetailData.image_id);
    }

    @Override
    public void APIImgListener(Bitmap image) {
        imageView.setImageBitmap(image);
    }
//NOT YET THERE: a function to save the artwork into database
//    public void save_item(View view) {
//        save_btn.setImageDrawable(R.drawable.btn_star_big_on);

//    }
}