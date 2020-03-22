package com.example.czgame.wordbank.ui.media;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.czgame.wordbank.R;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class adapter_new_artist extends RecyclerView.Adapter<adapter_new_artist.GroceryViewHolder>{
    Context context;
    private List<ArtistModel> horizontalGrocderyList;
    private DatabaseHelper_artist_image databaseHelper;

    public adapter_new_artist(List<ArtistModel> horizontalGrocderyList, Context context){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        databaseHelper = new DatabaseHelper_artist_image(context);

    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mostplayed, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        try {

            ImageHelper imageHelper = databaseHelper.getImage(Long.toString(horizontalGrocderyList.get(position).getID()));
            Bitmap myBitmap = BitmapFactory.decodeByteArray(imageHelper.getImageByteArray(), 0, imageHelper.getImageByteArray().length);
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.image)
                    .error(R.drawable.image);
            Glide.with(context).load(myBitmap).apply(options).into(holder.imageView);


        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        holder.txtview.setText(horizontalGrocderyList.get(position).getArtistName());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String productName = horizontalGrocderyList.get(position).getAlbum().toString();
               // Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
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
        RelativeLayout relativeLayout;
        TextView txtview;
        public GroceryViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.idProductImage);
            txtview=view.findViewById(R.id.idProductName);
            relativeLayout = view.findViewById(R.id.base);
        }
    }
}
