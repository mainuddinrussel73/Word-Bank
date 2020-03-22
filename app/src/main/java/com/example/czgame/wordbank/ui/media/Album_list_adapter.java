package com.example.czgame.wordbank.ui.media;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.czgame.wordbank.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class Album_list_adapter extends RecyclerView.Adapter<Album_list_adapter.ViewHolder> {

    private final Activity context;
    List<Audio> contactList;
    ImageView titleText;
    TextView tt, tt1, duration;

    // private final Integer[] imgid;

    public Album_list_adapter(Activity context,ArrayList<Audio> songs) {

        this.context = context;
        this.contactList = new ArrayList<Audio>();
        this.contactList.addAll(songs);


    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.media_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Audio myListData = contactList.get(position);
        holder.tt.setText(myListData.getTitle());
        holder.tt1.setText(myListData.getArtist());

        long num = Long.parseLong(myListData.getDuration());
        String s = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(num),
                TimeUnit.MILLISECONDS.toSeconds(num) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(num))
        );
        holder.duration.setText(s);

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, Integer.valueOf(myListData.getImagepath()));

        //in.close();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.image)
                .error(R.drawable.image);
        Glide.with(context).load(uri).apply(options).into(holder.imageView);

        holder.listitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
            }
        });



        SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDark", false);

        if (isDark) {

            //System.out.println("klklkl");
            holder.listitem.setBackgroundColor(Color.BLACK);


            holder.tt.setTextColor(context.getResources().getColor(R.color.per50white));

            holder.tt1.setTextColor(context.getResources().getColor(R.color.material_white));

            holder.listitem.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card_dark));

        } else {

            holder.listitem.setBackgroundColor(Color.WHITE);
            // tt.setTextColor(Color.BLACK);
            holder.tt.setTextColor(context.getResources().getColor(R.color.per54black));

            holder.tt1.setTextColor(context.getResources().getColor(R.color.darkgray));
            holder.listitem.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_card));
        }

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tt, tt1, duration;
        RelativeLayout listitem;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.play_pause);
            this.duration = itemView.findViewById(R.id.duration);
            this.tt = itemView.findViewById(R.id.title5);
            this.tt1 = itemView.findViewById(R.id.title6);
            listitem = itemView.findViewById(R.id.list_item);

        }
    }

    public int getComplimentColor(int color) {
        // get existing colors
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);

        // find compliments
        red = (~red) & 0xff;
        blue = (~blue) & 0xff;
        green = (~green) & 0xff;

        return Color.argb(alpha, red, green, blue);
    }




}