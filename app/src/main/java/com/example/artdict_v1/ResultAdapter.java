package com.example.artdict_v1;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.myViewHolder> implements NetworkingService.networkingListener{

    public ResultAdapter(Context c, List<Artwork> artList) {
        this.c = c;
        this.artList = artList;

    }

    private Context c;
    public List<Artwork> artList;
    NetworkingService networkingService;

    @Override
    public void APIlistener(String jsonString) {

    }

    @Override
    public void APIImgListener(Bitmap image) {

    }


    interface artClickListener {
        void artSelected(Artwork selectedArt);
    }

    artClickListener listener;

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView imgView;
        public final TextView txtView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.result_img);
            txtView = itemView.findViewById(R.id.result_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Artwork artwork = artList.get(getAdapterPosition());
            listener.artSelected(artwork
            );

        }
    }

    @NonNull
    @Override
    public ResultAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.recycler_cell,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Artwork art = artList.get(position);
        holder.txtView.setText(art.title);
        networkingService.getThumbnailImageData(art.image_id);
//        holder.imgView.setImageBitmap(networkingService.getThumbnailImageData(art.image_id));
    }


    @Override
    public int getItemCount() {
        return 0;
    }
}
