package com.example.czgame.wordbank.ui.media;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.czgame.wordbank.R;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Genera_adapter extends RecyclerView.Adapter<Genera_adapter.GroceryViewHolder>{
    Context context;
    private List<Genre> horizontalGrocderyList;
    private DatabaseHelper_artist_image databaseHelper;

    public Genera_adapter(List<Genre> horizontalGrocderyList, Context context){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        databaseHelper = new DatabaseHelper_artist_image(context);

    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grenes, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {

        if(horizontalGrocderyList.get(position).getName().trim().equals("Dance")){
            try {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.image)
                        .error(R.drawable.image);
                Glide.with(context).load("https://e-cdns-images.dzcdn.net/images/misc/e19942660ebf4f095dd8daaa84282a98/134x264-000000-80-0-0.jpg").apply(options).into(holder.imageView);


            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }else if(horizontalGrocderyList.get(position).getName().trim().equals("Pop")){
            try {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.image)
                        .error(R.drawable.image);
                Glide.with(context).load("https://e-cdns-images.dzcdn.net/images/misc/2b15578cf12902c0c7d2ae07e8071460/134x264-000000-80-0-0.jpg").apply(options).into(holder.imageView);


            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }else if(horizontalGrocderyList.get(position).getName().trim().equals("Alternative")){
            try {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.image)
                        .error(R.drawable.image);
                Glide.with(context).load("https://e-cdns-images.dzcdn.net/images/misc/a896075854903176bf47ff48a1213a09/164x264-000000-80-0-0.jpg").apply(options).into(holder.imageView);


            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if(horizontalGrocderyList.get(position).getName().trim().equals("Country")){
            try {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.image)
                        .error(R.drawable.image);
                Glide.with(context).load("https://e-cdns-images.dzcdn.net/images/misc/d9a30050b3c6772e607da52c99acb791/164x264-000000-80-0-0.jpg").apply(options).into(holder.imageView);


            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if(horizontalGrocderyList.get(position).getName().trim().equals("Hip Hop/Rap")){
            try {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.image)
                        .error(R.drawable.image);
                Glide.with(context).load("https://e-cdns-images.dzcdn.net/images/misc/b8ae8f0791f5e9e6c7a11bab94b0cbad/134x264-000000-80-0-0.jpg").apply(options).into(holder.imageView);


            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if(horizontalGrocderyList.get(position).getName().trim().equals("K-Pop")){
            try {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.image)
                        .error(R.drawable.image);
                Glide.with(context).load("https://e-cdns-images.dzcdn.net/images/misc/49ff19261ef3fb1ad85da7fb344c60ce/134x264-000000-80-0-0.jpg").apply(options).into(holder.imageView);


            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if(horizontalGrocderyList.get(position).getName().trim().equals("Bollywood")){
            try {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.image)
                        .error(R.drawable.image);
                Glide.with(context).load("https://e-cdns-images.dzcdn.net/images/misc/dfb00b6be960c363e07f503e7a1b40ee/164x264-000000-80-0-0.jpg").apply(options).into(holder.imageView);


            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if(horizontalGrocderyList.get(position).getName().trim().equals("Rock")){
            try {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.image)
                        .error(R.drawable.image);
                Glide.with(context).load("https://e-cdns-images.dzcdn.net/images/misc/8ad3050b34360d0573d9d7b8bf38997d/134x264-000000-80-0-0.jpg").apply(options).into(holder.imageView);


            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else if(horizontalGrocderyList.get(position).getName().trim().equals("Latin Urban")){
            try {

                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.image)
                        .error(R.drawable.image);
                Glide.with(context).load("https://e-cdns-images.dzcdn.net/images/misc/d1f20c3f8aac2f94c87c5f64e0d14c99/134x264-000000-80-0-0.jpg").apply(options).into(holder.imageView);


            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }



        holder.txtview.setText(horizontalGrocderyList.get(position).getName());
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




           // holder.relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card_dark));

        } else {

            //listitem.setBackgroundColor(Color.WHITE);
            // tt.setTextColor(Color.BLACK);

           // holder.relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card));
        }
    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        CardView relativeLayout;
        TextView txtview;
        public GroceryViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.idProductImage);
            txtview=view.findViewById(R.id.idProductName);
            relativeLayout = view.findViewById(R.id.base);
        }
    }
}