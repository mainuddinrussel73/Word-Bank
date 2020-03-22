package com.example.czgame.wordbank.ui.media;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.czgame.wordbank.R;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class adapter_recent_songs extends RecyclerView.Adapter<adapter_recent_songs.GroceryViewHolder>{
    Context context;
    private List<AlbumModel> horizontalGrocderyList;

    public adapter_recent_songs(List<AlbumModel> horizontalGrocderyList, Context context){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mostartist, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        try{
            Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf((int)horizontalGrocderyList.get(position).getID()));


            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image);
            Glide.with(context).load(uri).apply(options).into(holder.imageView);
        } catch(NumberFormatException ex){ // handle your exception

        }

        holder.txtview.setText(horizontalGrocderyList.get(position).getAlbumName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = horizontalGrocderyList.get(position).getAlbumName();
                Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
            }
        });



        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        if (isDark) {

            //System.out.println("klklkl");
           // holder.relativeLayout.setBackgroundColor(Color.BLACK);


            holder.txtview.setTextColor(Color.parseColor("#10bcc9"));


            holder.relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.base_rounded));

        } else {

            //listitem.setBackgroundColor(Color.WHITE);
            // tt.setTextColor(Color.BLACK);
            holder.txtview.setTextColor(Color.parseColor("#10bcc9"));

            holder.relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.base_rounded_white));
        }
    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        TextView txtview;
        RelativeLayout relativeLayout;
        public GroceryViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.idProductImage);
            txtview=view.findViewById(R.id.idProductName);
            relativeLayout = view.findViewById(R.id.base);
        }
    }
}